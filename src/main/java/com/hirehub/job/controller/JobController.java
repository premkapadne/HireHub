package com.hirehub.job.controller;

import com.hirehub.dto.JobApplicationDto;
import com.hirehub.dto.JobDto;
import com.hirehub.dto.UpdateJobApplicationDto;
import com.hirehub.job.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.hirehub.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/jobs")
@PreAuthorize("hasRole('EMPLOYER')")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping(path = "/employer", version = "1.0")
    @Operation(summary = "List employer jobs", description = "Returns jobs owned by the authenticated employer.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Jobs returned"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "EMPLOYER role required")})
    public ResponseEntity<List<JobDto>> getEmployerJobs(Authentication authentication) {
        String employerEmail = authentication.getName();
        List<JobDto> jobs = jobService.getEmployerJobs(employerEmail);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping(path = "/employer", version = "1.0")
    @Operation(summary = "Create a job", description = "Creates a job for the authenticated employer using the supplied validated job details.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Job created"), @ApiResponse(responseCode = "400", description = "Invalid job request"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "EMPLOYER role required")})
    public ResponseEntity<JobDto> createJob(@RequestBody @Valid JobDto jobDto, Authentication authentication) {
        String employerEmail = authentication.getName();
        JobDto createdJob = jobService.createJob(jobDto, employerEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);

    }

    @PatchMapping("/{jobId}/status/employer")
    @Operation(summary = "Update job status", description = "Updates the status of the specified job owned by the authenticated employer. The request body must contain a non-empty status.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Job status updated"), @ApiResponse(responseCode = "400", description = "Status is missing or invalid"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "EMPLOYER role required")})
    public ResponseEntity<?> updateJobStatus(
            @PathVariable Long jobId,
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        String employerEmail = authentication.getName();
        String status = requestBody.get("status");

        if (status == null || status.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Status is required"));
        }
        JobDto updatedJob = jobService.updateJobStatus(jobId, status.toUpperCase(), employerEmail);
        return ResponseEntity.ok(updatedJob);
    }

    @GetMapping("/applications/{jobId}/employer")
    @Operation(summary = "List applications for a job", description = "Returns applications for the specified job owned by the authenticated employer.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Applications returned"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "EMPLOYER role required")})
    public ResponseEntity<List<JobApplicationDto>> getApplicationsByJobForEmployer(
            @PathVariable Long jobId, Authentication authentication) {
        List<JobApplicationDto> applications = jobService.getApplicationsByJobForEmployer(
                jobId, authentication.getName());
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/applications/employer")
    @Operation(summary = "Update a job application", description = "Updates an application using the supplied validated application details. Requires the EMPLOYER role.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Application updated"), @ApiResponse(responseCode = "400", description = "Invalid application update request"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "EMPLOYER role required")})
    public ResponseEntity<String> updateJobApplication(
            @RequestBody @Valid UpdateJobApplicationDto updateJobApplicationDto,
            Authentication authentication) {
        jobService.updateJobApplication(updateJobApplicationDto, authentication.getName());
        return ResponseEntity.ok("Application updated successfully");
    }

}
