package com.hirehub.auth;

import com.hirehub.contants.ApplicationConstants;
import com.hirehub.dto.LoginRequestDto;
import com.hirehub.dto.LoginResponseDto;
import com.hirehub.dto.RegisterRequestDto;
import com.hirehub.dto.UserDto;
import com.hirehub.entity.HireHubUser;
import com.hirehub.entity.Role;
import com.hirehub.repository.HireHubUserRepository;
import com.hirehub.repository.RoleRepository;
import com.hirehub.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil ;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final HireHubUserRepository hireHubUserRepository;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @PostMapping(path = "/login/public", version = "1.0")
    public ResponseEntity<LoginResponseDto> apilogin(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            var resultAuthentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password()));
            String jwtToken = jwtUtil.generateJwtToken(resultAuthentication);
            var userDto = new UserDto();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(),
                            userDto, jwtToken));
        } catch (BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (AuthenticationException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }
    }

    @PostMapping(value = "/register/public",version = "1.0")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequestDto)
    {
        // Checking if the password is being compromised or not.
        CompromisedPasswordDecision decision = compromisedPasswordChecker
                .check(registerRequestDto.password());
        if (decision.isCompromised()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("password", "Choose a strong password"));
        }

        Optional<HireHubUser> existingUser = hireHubUserRepository.readUserByEmailOrMobileNumber
                (registerRequestDto.email(), registerRequestDto.mobileNumber());
        if (existingUser.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            HireHubUser hireHubUser = existingUser.get();
            if (hireHubUser.getEmail().equalsIgnoreCase(registerRequestDto.email())) {
                errors.put("email", "Email is already registered");
            }
            if (hireHubUser.getMobileNumber().equals(registerRequestDto.mobileNumber())) {
                errors.put("mobileNumber", "Mobile number is already registered");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        HireHubUser hireHubUser = new HireHubUser();
        BeanUtils.copyProperties(registerRequestDto, hireHubUser);
        hireHubUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        Role role = roleRepository.findRoleByName(ApplicationConstants.ROLE_JOB_SEEKER)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " +
                        ApplicationConstants.ROLE_JOB_SEEKER));
        hireHubUser.setRole(role);
        hireHubUserRepository.save(hireHubUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status, String message)
    {
        return ResponseEntity
                .status(status)
                .body(new LoginResponseDto(message, null, null));
    }
}

