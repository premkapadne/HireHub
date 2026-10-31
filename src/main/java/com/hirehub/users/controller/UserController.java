package com.hirehub.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hirehub.dto.*;
import com.hirehub.entity.HireHubUser;
import com.hirehub.users.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/search/admin")
    @Operation(summary = "Search for a user by email", description = "Finds a user by email address. Requires the ADMIN role.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User found"), @ApiResponse(responseCode = "404", description = "No user found for the supplied email"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> searchUserByEmail(@RequestParam String email) {
        Optional<UserDto> userOptional = userService.searchUserByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with email: " + email));
        }
        return ResponseEntity.ok(userOptional.get());
    }

    @GetMapping("/admin")
    @Operation(summary = "List users", description = "Returns all users. Requires the ADMIN role.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Users returned"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        List<UserDto> userDto = userService.getAllUsers();
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/{userId}/role/employer/admin")
    @Operation(summary = "Elevate user to employer", description = "Grants the employer role to the specified user. Requires the ADMIN role.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User role updated"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> elevateToEmployer(@PathVariable Long userId) {
        UserDto updatedUser = userService.elevateToEmployer(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{userId}/company/{companyId}/admin")
    @Operation(summary = "Assign company to employer", description = "Associates the specified employer user with the specified company. Requires the ADMIN role.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Company assigned to user"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignCompanyToEmployer(
            @PathVariable Long userId, @PathVariable Long companyId) {
        UserDto updatedUser = userService.assignCompanyToEmployer(userId, companyId);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping(value = "/profile/jobseeker", version = "1.0",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create or update job seeker profile", description = "Creates or updates the authenticated job seeker's profile. Submit profile as JSON and optionally include profilePicture and resume files as multipart parts.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Profile saved"), @ApiResponse(responseCode = "400", description = "Invalid profile or multipart request"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ProfileDto> createOrUpdateProfile(
            @RequestPart(value = "profile") String profileJson,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart(value = "resume", required = false) MultipartFile resume,
            Authentication authentication) throws JsonProcessingException {
        String userEmail = authentication.getName();
        ProfileDto savedProfile = userService.createOrUpdateProfile(
                userEmail, profileJson, profilePicture, resume);
        return ResponseEntity.ok(savedProfile);
    }

    @GetMapping(value = "/profile/jobseeker", version = "1.0")
    @Operation(summary = "Get job seeker profile", description = "Returns the authenticated job seeker's profile.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Profile returned"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ProfileDto> getProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        ProfileDto profileDto = userService.getProfile(userEmail);
        return ResponseEntity.ok(profileDto);
    }

    @GetMapping(value = "/profile/picture/jobseeker", version = "1.0")
    @Operation(summary = "Get profile picture", description = "Returns the authenticated job seeker's profile picture as binary content.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Profile picture returned"), @ApiResponse(responseCode = "404", description = "No profile picture is available"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<byte[]> getProfilePicture(Authentication authentication) {
        String userEmail = authentication.getName();
        ProfileDto profileDto = userService.getProfilePicture(userEmail);
        byte[] picture = profileDto.profilePicture();
        if (picture == null || picture.length == 0) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.profilePictureType()));
        headers.setContentLength(picture.length);
        return new ResponseEntity<>(picture, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/profile/resume/jobseeker", version = "1.0")
    @Operation(summary = "Download resume", description = "Downloads the authenticated job seeker's resume as an attachment.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Resume returned"), @ApiResponse(responseCode = "404", description = "No resume is available"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<byte[]> getResume(Authentication authentication) {
        String userEmail = authentication.getName();
        ProfileDto profileDto = userService.getResume(userEmail);
        byte[] resume = profileDto.resume();
        if (resume == null || resume.length == 0) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.resumeType()));
        headers.setContentLength(resume.length);
        headers.setContentDispositionFormData("attachment", profileDto.resumeName());
        return new ResponseEntity<>(resume, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/saved-jobs/{jobId}/jobseeker", version = "1.0")
    @Operation(summary = "Save a job", description = "Saves the specified job for the authenticated job seeker.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Job saved"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<JobDto> saveJob(@PathVariable Long jobId,
                                          Authentication authentication) {
        String userEmail = authentication.getName();
        JobDto savedJob = userService.saveJob(userEmail, jobId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
    }

    @DeleteMapping(value = "/saved-jobs/{jobId}/jobseeker", version = "1.0")
    @Operation(summary = "Unsave a job", description = "Removes the specified job from the authenticated job seeker's saved jobs.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Job unsaved"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<String> unsaveJob(@PathVariable Long jobId,
                                            Authentication authentication) {
        String userEmail = authentication.getName();
        userService.unsaveJob(userEmail, jobId);
        return ResponseEntity.status(HttpStatus.OK).body("Job unsaved successfully");
    }

    @GetMapping(value = "/saved-jobs/jobseeker", version = "1.0")
    @Operation(summary = "List saved jobs", description = "Returns jobs saved by the authenticated job seeker.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Saved jobs returned"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<List<JobDto>> getSavedJobs(Authentication authentication) {
        String userEmail = authentication.getName();
        List<JobDto> savedJobDtos = userService.getSavedJobs(userEmail);
        return ResponseEntity.ok(savedJobDtos);
    }

    @PostMapping(value = "/job-applications/jobseeker", version = "1.0")
    @Operation(summary = "Apply for a job", description = "Submits a job application for the authenticated job seeker using the supplied application details.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Application submitted"), @ApiResponse(responseCode = "400", description = "Invalid application request"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<JobApplicationDto> applyForJob(
            @RequestBody @Valid ApplyJobRequestDto applyJobRequestDto, Authentication authentication) {
        String userEmail = authentication.getName();
        JobApplicationDto application = userService.applyForJob(userEmail, applyJobRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @DeleteMapping(value = "/job-applications/{jobId}/jobseeker", version = "1.0")
    @Operation(summary = "Withdraw a job application", description = "Withdraws the authenticated job seeker's application for the specified job.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Application withdrawn"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<String> withdrawApplication(@PathVariable Long jobId,
                                                      Authentication authentication) {
        String userEmail = authentication.getName();
        userService.withdrawApplication(userEmail, jobId);
        return ResponseEntity.status(HttpStatus.OK).body("Application withdrawn successfully");
    }

    @GetMapping(value = "/job-applications/jobseeker", version = "1.0")
    @Operation(summary = "List job seeker applications", description = "Returns job applications submitted by the authenticated job seeker.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Applications returned"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "JOB_SEEKER role required")})
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<List<JobApplicationDto>> getJobSeekerApplications(Authentication authentication) {
        String userEmail = authentication.getName();
        List<JobApplicationDto> applications = userService.getJobSeekerApplications(userEmail);
        return ResponseEntity.ok(applications);
    }

}
