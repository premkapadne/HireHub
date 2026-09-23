package com.hirehub.aspect;

import com.hirehub.dto.RegisterRequestDto;
import com.hirehub.entity.HireHubUser;
import com.hirehub.exception.RegistrationValidationException;
import com.hirehub.repository.HireHubUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterValidationAspect {

    private final CompromisedPasswordChecker compromisedPasswordChecker;
    private final HireHubUserRepository hireHubUserRepository;

    @Before("""
        execution(* com.hirehub.auth.AuthController
        .registerUser(..))
        """)
    public void validateBeforeRegister(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        RegisterRequestDto request = (RegisterRequestDto) args[0];
        log.info("🔍 Validating user registration request");
        Map<String, String> errors = new HashMap<>();
        // 1️⃣ Compromised password check
        CompromisedPasswordDecision decision =
                compromisedPasswordChecker.check(request.password());
        if (decision.isCompromised()) {
            errors.put("password", "Choose a strong password");
        }
        // 2️⃣ Existing user check
        Optional<HireHubUser> existingUser =
                hireHubUserRepository.readUserByEmailOrMobileNumber(
                        request.email(), request.mobileNumber());

        if (existingUser.isPresent()) {
            HireHubUser user = existingUser.get();

            if (user.getEmail().equalsIgnoreCase(request.email())) {
                errors.put("email", "Email is already registered");
            }

            if (user.getMobileNumber().equals(request.mobileNumber())) {
                errors.put("mobileNumber", "Mobile number is already registered");
            }
        }

        // 3️⃣ Stop execution if validation fails
        if (!errors.isEmpty()) {
            log.warn("❌ Registration validation failed: {}", errors);
            throw new RegistrationValidationException(errors);
        }

        log.info("✅ Registration validation passed");
    }

}
