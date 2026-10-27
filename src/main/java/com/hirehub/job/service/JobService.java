package com.hirehub.job.service;

import com.hirehub.dto.JobApplicationDto;
import com.hirehub.dto.JobDto;
import com.hirehub.dto.UpdateJobApplicationDto;

import java.util.List;

public interface JobService {

    /**
     * Get all jobs posted by the employer's company
     * @param employerEmail the email of the employer
     * @return list of jobs
     */
    List<JobDto> getEmployerJobs(String employerEmail);

    /**
     * Update the status of a job
     * @param jobId the ID of the job
     * @param status the new status (ACTIVE, CLOSED, DRAFT)
     * @param employerEmail the email of the employer making the request
     * @return updated JobDto
     */
    JobDto updateJobStatus(Long jobId, String status, String employerEmail);

    /**
     * Create a new job for the employer's company
     * @param jobDto the job data
     * @param employerEmail the email of the employer creating the job
     * @return created JobDto
     */
    JobDto createJob(JobDto jobDto, String employerEmail);

    /**
     * Retrieves a list of job applications submitted for a specific job
     *
     * @param jobId the ID of the job for which applications need to be retrieved
     * @return a list of job applications submitted for the specified job
     */
    List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId, String employerEmail);

    /**
     * Updates an existing job application with the provided details.
     *
     * @param updateJobApplicationDto the data transfer object containing
     *                                the details to update the job application,
     *                                such as the application ID, status, and notes.
     * @param employerEmail the email of the employer making the request
     */
    void updateJobApplication(UpdateJobApplicationDto updateJobApplicationDto, String employerEmail);

}
