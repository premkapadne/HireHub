CREATE TABLE companies
(
    id            BIGINT AUTO_INCREMENT   NOT NULL,
    name          VARCHAR(255)            NOT NULL,
    logo          VARCHAR(500) NULL,
    industry      VARCHAR(100)            NOT NULL,
    size          VARCHAR(50)             NOT NULL,
    rating        DECIMAL(3, 2)           NOT NULL,
    locations     VARCHAR(1000) NULL,
    founded       INT                     NOT NULL,
    `description` LONGTEXT NULL,
    employees     INT NULL,
    website       VARCHAR(500) NULL,
    created_at    timestamp DEFAULT NOW() NOT NULL,
    created_by    VARCHAR(20)             NOT NULL,
    updated_at    timestamp NULL,
    updated_by    VARCHAR(20) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE contacts
(
    id         BIGINT AUTO_INCREMENT     NOT NULL,
    created_at datetime                  NOT NULL,
    created_by VARCHAR(20)               NOT NULL,
    email      VARCHAR(255)              NOT NULL,
    message    LONGTEXT                  NOT NULL,
    name       VARCHAR(255)              NOT NULL,
    status     VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    subject    VARCHAR(255)              NOT NULL,
    updated_at datetime NULL,
    updated_by VARCHAR(20) NULL,
    user_type  VARCHAR(50)               NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE job_applications
(
    id           BIGINT AUTO_INCREMENT         NOT NULL,
    user_id      BIGINT                        NOT NULL,
    job_id       BIGINT                        NOT NULL,
    applied_at   timestamp   DEFAULT NOW()     NOT NULL,
    status       VARCHAR(50) DEFAULT 'PENDING' NOT NULL,
    cover_letter LONGTEXT NULL,
    notes        LONGTEXT NULL,
    created_at   timestamp   DEFAULT NOW()     NOT NULL,
    created_by   VARCHAR(20)                   NOT NULL,
    updated_at   timestamp NULL,
    updated_by   VARCHAR(20) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE jobs
(
    id                   BIGINT AUTO_INCREMENT        NOT NULL,
    title                VARCHAR(255)                 NOT NULL,
    company_id           BIGINT                       NOT NULL,
    location             VARCHAR(255)                 NOT NULL,
    work_type            VARCHAR(50)                  NOT NULL,
    job_type             VARCHAR(50)                  NOT NULL,
    category             VARCHAR(100)                 NOT NULL,
    experience_level     VARCHAR(50)                  NOT NULL,
    salary_min           DECIMAL(12, 2)               NOT NULL,
    salary_max           DECIMAL(12, 2)               NOT NULL,
    salary_currency      VARCHAR(10) DEFAULT 'USD'    NOT NULL,
    salary_period        VARCHAR(20) DEFAULT 'year'   NOT NULL,
    `description`        LONGTEXT                     NOT NULL,
    requirements         LONGTEXT NULL,
    benefits             LONGTEXT NULL,
    posted_date          timestamp   DEFAULT NOW()    NOT NULL,
    application_deadline timestamp NULL,
    applications_count   INT         DEFAULT 0 NULL,
    featured             TINYINT(1)  DEFAULT 0        NULL,
    urgent               TINYINT(1)  DEFAULT 0        NULL,
    `remote`             TINYINT(1)  DEFAULT 0        NULL,
    status               VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL,
    created_at           timestamp   DEFAULT NOW()    NOT NULL,
    created_by           VARCHAR(20)                  NOT NULL,
    updated_at           timestamp NULL,
    updated_by           VARCHAR(20) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE profiles
(
    id                   BIGINT AUTO_INCREMENT   NOT NULL,
    user_id              BIGINT                  NOT NULL,
    job_title            VARCHAR(255)            NOT NULL,
    location             VARCHAR(255)            NOT NULL,
    experience_level     VARCHAR(50)             NOT NULL,
    professional_bio     LONGTEXT                NOT NULL,
    portfolio_website    VARCHAR(500) NULL,
    profile_picture      BLOB NULL,
    profile_picture_name VARCHAR(255) NULL,
    profile_picture_type VARCHAR(100) NULL,
    resume               BLOB NULL,
    resume_name          VARCHAR(255) NULL,
    resume_type          VARCHAR(100) NULL,
    created_at           timestamp DEFAULT NOW() NOT NULL,
    created_by           VARCHAR(20)             NOT NULL,
    updated_at           timestamp NULL,
    updated_by           VARCHAR(20) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id         BIGINT AUTO_INCREMENT   NOT NULL,
    name       VARCHAR(50)             NOT NULL,
    created_at timestamp DEFAULT NOW() NOT NULL,
    created_by VARCHAR(20)             NOT NULL,
    updated_at timestamp NULL,
    updated_by VARCHAR(20) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE saved_jobs
(
    user_id BIGINT NOT NULL,
    job_id  BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (user_id, job_id)
);

CREATE TABLE users
(
    id            BIGINT AUTO_INCREMENT   NOT NULL,
    name          VARCHAR(255)            NOT NULL,
    email         VARCHAR(255)            NOT NULL,
    password_hash VARCHAR(500)            NOT NULL,
    mobile_number VARCHAR(20) NULL,
    role_id       BIGINT                  NOT NULL,
    company_id    BIGINT NULL,
    created_at    timestamp DEFAULT NOW() NOT NULL,
    created_by    VARCHAR(20)             NOT NULL,
    updated_at    timestamp NULL,
    updated_by    VARCHAR(20) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT mobile_number UNIQUE (mobile_number);

ALTER TABLE companies
    ADD CONSTRAINT name UNIQUE (name);

ALTER TABLE roles
    ADD CONSTRAINT name UNIQUE (name);

ALTER TABLE job_applications
    ADD CONSTRAINT unique_user_job_application UNIQUE (user_id, job_id);

ALTER TABLE profiles
    ADD CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

ALTER TABLE saved_jobs
    ADD CONSTRAINT fk_saved_jobs_job FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE;

ALTER TABLE saved_jobs
    ADD CONSTRAINT fk_saved_jobs_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE users
    ADD CONSTRAINT fk_users_company FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE SET NULL;

ALTER TABLE users
    ADD CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE NO ACTION;

ALTER TABLE job_applications
    ADD CONSTRAINT job_applications_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE job_applications
    ADD CONSTRAINT job_applications_ibfk_2 FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE;

CREATE INDEX job_id ON job_applications (job_id);

ALTER TABLE jobs
    ADD CONSTRAINT jobs_ibfk_1 FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE CASCADE;

CREATE INDEX company_id ON jobs (company_id);
