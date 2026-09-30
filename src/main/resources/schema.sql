-- Create companies table
CREATE TABLE IF NOT EXISTS companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    logo VARCHAR(500),
    industry VARCHAR(100) NOT NULL,
    size VARCHAR(50) NOT NULL,
    rating DECIMAL(3,2) NOT NULL,
    locations VARCHAR(1000),
    founded INT NOT NULL,
    description TEXT,
    employees INT,
    website VARCHAR(500),
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by  VARCHAR(20)  NOT NULL,
    updated_at  TIMESTAMP   DEFAULT NULL,
    updated_by  VARCHAR(20) DEFAULT NULL
);

-- Create contacts table
CREATE TABLE IF NOT EXISTS contacts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    message TINYTEXT NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    subject VARCHAR(255) NOT NULL,
    updated_at DATETIME(6),
    updated_by VARCHAR(20),
    user_type VARCHAR(50) NOT NULL
);

-- Create jobs table
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    company_id BIGINT NOT NULL,
    location VARCHAR(255) NOT NULL,
    work_type VARCHAR(50) NOT NULL, -- On-site, Remote, Hybrid
    job_type VARCHAR(50) NOT NULL, -- Full-time, Part-time, Contract, Freelance
    category VARCHAR(100) NOT NULL, -- Technology, Design, Marketing, Sales, Finance, Healthcare, Education, Operations
    experience_level VARCHAR(50) NOT NULL, -- Entry Level, Mid Level, Senior Level, Executive Level
    salary_min DECIMAL(12,2) NOT NULL,
    salary_max DECIMAL(12,2) NOT NULL,
    salary_currency VARCHAR(10) DEFAULT 'USD' NOT NULL,
    salary_period VARCHAR(20) DEFAULT 'year' NOT NULL,
    description TEXT NOT NULL,
    requirements TEXT, -- JSON array stored as text
    benefits TEXT, -- JSON array stored as text
    posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    application_deadline TIMESTAMP,
    applications_count INT DEFAULT 0,
    featured BOOLEAN DEFAULT FALSE,
    urgent BOOLEAN DEFAULT FALSE,
    remote BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL, -- ACTIVE, CLOSED, DRAFT
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
);

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP   DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(500) NOT NULL,
    mobile_number VARCHAR(20) UNIQUE,
    role_id BIGINT NOT NULL,
    company_id BIGINT NULL,
    created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by    VARCHAR(20)  NOT NULL,
    updated_at    TIMESTAMP   DEFAULT NULL,
    updated_by    VARCHAR(20) DEFAULT NULL,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_users_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL
);

TRUNCATE TABLE contacts;

-- =========================================================
-- INSERT CONSISTENT TEST DATA
-- =========================================================

-- =========================================================
-- RESET CONTACTS TABLE
-- =========================================================

TRUNCATE TABLE contacts;


-- =========================================================
-- INSERT CONTACT DATA
-- updated_at and updated_by are NULL initially
-- =========================================================

INSERT INTO contacts
(created_at, created_by, email, message, name, status, subject,
 updated_at, updated_by, user_type)
VALUES

    ('2026-08-30 14:23:38.971429', 'System',
     'purva.patil@gmail.com',
     'I am unable to access some features of my account.',
     'Purva Patil', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-08-30 14:24:11.844807', 'System',
     'prem.kapdane@gmail.com',
     'I am facing a problem while managing my account.',
     'Prem Kapdane', 'NEW', 'Account Problem', NULL, NULL, 'Employer'),

    ('2026-08-30 14:24:55.614880', 'System',
     'disha.bagul@gmail.com',
     'I am unable to apply for a job.',
     'Disha Bagul', 'NEW', 'Application Question', NULL, NULL, 'Job Seeker'),

    ('2026-08-31 02:27:26.383331', 'System',
     'harshit.nikam@gmail.com',
     'The website is showing an unexpected error.',
     'Harshit Nikam', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-08-31 12:06:09.379720', 'System',
     'vasundhara.indaji@gmail.com',
     'I am having trouble using the job search feature.',
     'Vasundhara Indaji', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-08-31 12:24:35.598507', 'Anonymous User',
     'neha.patil@gmail.com',
     'I need help with my account.',
     'Neha Patil', 'NEW', 'Account Problem', NULL, NULL, 'Job Seeker'),

    ('2026-09-01 09:15:22.123456', 'System',
     'rahul.shinde@gmail.com',
     'I have a question regarding a job application.',
     'Rahul Shinde', 'NEW', 'Application Question', NULL, NULL, 'Job Seeker'),

    ('2026-09-01 10:32:45.654321', 'System',
     'sneha.kadam@gmail.com',
     'I cannot update my profile information.',
     'Sneha Kadam', 'NEW', 'Account Problem', NULL, NULL, 'Job Seeker'),

    ('2026-09-01 11:48:19.345678', 'Anonymous User',
     'amit.jadhav@gmail.com',
     'I am unable to create a new job posting.',
     'Amit Jadhav', 'NEW', 'Job Posting Issue', NULL, NULL, 'Employer'),

    ('2026-09-02 08:22:51.789012', 'System',
     'pooja.patil@gmail.com',
     'I forgot my password and cannot login.',
     'Pooja Patil', 'NEW', 'Account Problem', NULL, NULL, 'Job Seeker'),

    ('2026-09-02 09:37:26.234567', 'Anonymous User',
     'rohit.more@gmail.com',
     'My application status is not updating.',
     'Rohit More', 'NEW', 'Application Question', NULL, NULL, 'Job Seeker'),

    ('2026-09-02 10:45:13.456789', 'System',
     'anjali.deshmukh@gmail.com',
     'I am unable to upload my resume.',
     'Anjali Deshmukh', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-02 11:52:38.567890', 'System',
     'sachin.kale@gmail.com',
     'I cannot edit my company information.',
     'Sachin Kale', 'NEW', 'Account Problem', NULL, NULL, 'Employer'),

    ('2026-09-03 09:26:49.678901', 'Anonymous User',
     'neha.joshi@gmail.com',
     'I am getting an error while searching for jobs.',
     'Neha Joshi', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-03 10:41:17.789012', 'System',
     'vikas.pawar@gmail.com',
     'I have a question about my job application.',
     'Vikas Pawar', 'NEW', 'Application Question', NULL, NULL, 'Job Seeker'),

    ('2026-09-03 11:05:33.890123', 'System',
     'kiran.mahajan@gmail.com',
     'I cannot publish my job posting.',
     'Kiran Mahajan', 'NEW', 'Job Posting Issue', NULL, NULL, 'Employer'),

    ('2026-09-04 08:19:44.901234', 'Anonymous User',
     'manasi.gore@gmail.com',
     'My profile picture is not uploading.',
     'Manasi Gore', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-04 09:33:28.012345', 'System',
     'akash.bhosale@gmail.com',
     'I cannot see the employer contact details.',
     'Akash Bhosale', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-04 10:47:52.123456', 'System',
     'riya.sawant@gmail.com',
     'The website is loading very slowly.',
     'Riya Sawant', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-04 12:15:36.234567', 'System',
     'sameer.chaudhari@gmail.com',
     'I cannot shortlist candidates.',
     'Sameer Chaudhari', 'NEW', 'Feature Request', NULL, NULL, 'Employer'),

    ('2026-09-05 08:28:47.345678', 'Anonymous User',
     'kajal.ingle@gmail.com',
     'My job application submission failed.',
     'Kajal Ingle', 'NEW', 'Application Question', NULL, NULL, 'Job Seeker'),

    ('2026-09-05 09:12:54.456789', 'System',
     'omkar.thorat@gmail.com',
     'I am not receiving email notifications.',
     'Omkar Thorat', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-05 10:26:31.567890', 'Anonymous User',
     'shruti.nikam@gmail.com',
     'The password reset link is not working.',
     'Shruti Nikam', 'NEW', 'Account Problem', NULL, NULL, 'Job Seeker'),

    ('2026-09-05 11:49:16.678901', 'System',
     'nilesh.kulkarni@gmail.com',
     'I cannot edit the salary information in my job post.',
     'Nilesh Kulkarni', 'NEW', 'Job Posting Issue', NULL, NULL, 'Employer'),

    ('2026-09-06 09:07:25.789012', 'System',
     'tanvi.surve@gmail.com',
     'My saved jobs are not visible.',
     'Tanvi Surve', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-06 10:36:41.890123', 'Anonymous User',
     'pratik.rao@gmail.com',
     'I am unable to verify my email address.',
     'Pratik Rao', 'NEW', 'Account Problem', NULL, NULL, 'Job Seeker'),

    ('2026-09-06 11:48:12.901234', 'System',
     'simran.khan@gmail.com',
     'The dashboard is showing incorrect information.',
     'Simran Khan', 'NEW', 'Technical Issue', NULL, NULL, 'Other'),

    ('2026-09-06 13:17:39.012345', 'System',
     'yash.patil@gmail.com',
     'I cannot view applicants for my job posting.',
     'Yash Patil', 'NEW', 'Job Posting Issue', NULL, NULL, 'Employer'),

    ('2026-09-07 08:42:53.123456', 'Anonymous User',
     'sakshi.deshpande@gmail.com',
     'I am getting an unexpected error after login.',
     'Sakshi Deshpande', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-07 09:25:17.234567', 'System',
     'tejas.wagh@gmail.com',
     'I cannot upload my company logo.',
     'Tejas Wagh', 'NEW', 'Technical Issue', NULL, NULL, 'Employer'),

    ('2026-09-07 10:39:48.345678', 'Anonymous User',
     'mrunal.patil@gmail.com',
     'The job filters are not working properly.',
     'Mrunal Patil', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-08 08:14:29.456789', 'System',
     'siddhant.jadhav@gmail.com',
     'I cannot download my resume.',
     'Siddhant Jadhav', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-08 09:36:55.567890', 'Anonymous User',
     'vaishnavi.more@gmail.com',
     'Notifications are not appearing on my dashboard.',
     'Vaishnavi More', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-08 11:05:18.678901', 'System',
     'ganesh.kamble@gmail.com',
     'I cannot update my company description.',
     'Ganesh Kamble', 'NEW', 'Account Problem', NULL, NULL, 'Employer'),

    ('2026-09-08 12:27:42.789012', 'Anonymous User',
     'aarti.bhagwat@gmail.com',
     'I have a question about withdrawing my application.',
     'Aarti Bhagwat', 'NEW', 'Application Question', NULL, NULL, 'Job Seeker'),

    ('2026-09-09 09:18:36.890123', 'System',
     'saurabh.kadam@gmail.com',
     'The search results are not displaying correctly.',
     'Saurabh Kadam', 'NEW', 'Technical Issue', NULL, NULL, 'Job Seeker'),

    ('2026-09-09 10:24:15.123456', 'System',
     'megha.joshi@gmail.com',
     'I have an idea for improving the platform.',
     'Megha Joshi', 'NEW', 'Feature Request', NULL, NULL, 'Other'),

    ('2026-09-10 11:42:31.234567', 'Anonymous User',
     'aditya.sharma@gmail.com',
     'I need help understanding the employer onboarding process.',
     'Aditya Sharma', 'NEW', 'Employer Onboarding', NULL, NULL, 'Employer'),

    ('2026-09-11 13:15:48.345678', 'System',
     'kavya.patil@gmail.com',
     'I have a general question about the job portal.',
     'Kavya Patil', 'NEW', 'General Inquiry', NULL, NULL, 'Other'),

    ('2026-09-12 14:28:59.456789', 'Anonymous User',
     'rohan.kulkarni@gmail.com',
     'I have another issue that does not fit the available categories.',
     'Rohan Kulkarni', 'NEW', 'Other', NULL, NULL, 'Other');