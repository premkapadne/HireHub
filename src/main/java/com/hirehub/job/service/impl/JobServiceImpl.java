package com.hirehub.job.service.impl;

import com.hirehub.dto.JobApplicationDto;
import com.hirehub.dto.JobDto;
import com.hirehub.dto.UpdateJobApplicationDto;
import com.hirehub.entity.HireHubUser;
import com.hirehub.entity.Job;
import com.hirehub.entity.JobApplication;
import com.hirehub.exception.ResourceNotFoundException;
import com.hirehub.job.service.JobService;
import com.hirehub.repository.HireHubUserRepository;
import com.hirehub.repository.JobApplicationRepository;
import com.hirehub.repository.JobRepository;
import com.hirehub.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final HireHubUserRepository userRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Override
    public List<JobDto> getEmployerJobs(String employerEmail) {
        HireHubUser employer = userRepository.findHireHubUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }

        List<Job> jobs = employer.getCompany().getJobs();
        return jobs.stream()
                .map(job -> ApplicationUtility.transformJobToDto(job))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public JobDto updateJobStatus(Long jobId, String status, String employerEmail) {
        // Validate status
        if (!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT")) {
            throw new RuntimeException("Invalid status. Must be ACTIVE, CLOSED, or DRAFT");
        }
        HireHubUser employer = userRepository.findHireHubUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }
        Job job = employer.getCompany().getJobs().stream().filter(j -> j.getId().equals(jobId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        return ApplicationUtility.transformJobToDto(job);
    }

    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {
        // Validate employer and get their company
        HireHubUser employer = userRepository.findHireHubUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned. Please contact admin.");
        }
        Job job = tranformDtoToEntity(jobDto);
        job.setPostedDate(Instant.now());
        job.setApplicationsCount(0);
        job.setStatus("DRAFT");
        job.setCompany(employer.getCompany());
        Job savedJob = jobRepository.save(job);
        return ApplicationUtility.transformJobToDto(savedJob);
    }

    @Override
    public List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId, String employerEmail) {
        Long companyId = getEmployerCompanyId(employerEmail);
        if (!jobRepository.existsByIdAndCompanyId(jobId, companyId)) {
            throw new ResourceNotFoundException("Job not found");
        }
        List<JobApplication> applications = jobApplicationRepository
                .findByJobIdAndJobCompanyIdOrderByAppliedAtAsc(jobId, companyId);
        return applications.stream()
                .map(jobApplication -> ApplicationUtility.mapToJobApplicationDto(jobApplication))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateJobApplication(UpdateJobApplicationDto dto, String employerEmail) {
        Long companyId = getEmployerCompanyId(employerEmail);
        int updatedRows = jobApplicationRepository.updateStatusAndNotesById(
                dto.status().name(), dto.notes(), dto.applicationId(),
                ApplicationUtility.getLoggedInUser(), companyId);
        if (updatedRows == 0) {
            throw new ResourceNotFoundException("Application not found");
        }
    }

    private Long getEmployerCompanyId(String employerEmail) {
        HireHubUser employer = userRepository.findHireHubUserByEmail(employerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));
        if (employer.getCompany() == null) {
            throw new ResourceNotFoundException("Employer does not have a company assigned");
        }
        return employer.getCompany().getId();
    }

    private Job tranformDtoToEntity(JobDto jobDto) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDto, job);
        return job;
    }
}
