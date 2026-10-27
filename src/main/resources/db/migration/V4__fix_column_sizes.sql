-- Fix 1: created_by / updated_by were VARCHAR(20) but the app stores the user's email there
--        (an email longer than 20 characters makes the INSERT fail).
ALTER TABLE companies        MODIFY created_by VARCHAR(255) NOT NULL, MODIFY updated_by VARCHAR(255) NULL;
ALTER TABLE contacts         MODIFY created_by VARCHAR(255) NOT NULL, MODIFY updated_by VARCHAR(255) NULL;
ALTER TABLE jobs             MODIFY created_by VARCHAR(255) NOT NULL, MODIFY updated_by VARCHAR(255) NULL;
ALTER TABLE roles            MODIFY created_by VARCHAR(255) NOT NULL, MODIFY updated_by VARCHAR(255) NULL;
ALTER TABLE users            MODIFY created_by VARCHAR(255) NOT NULL, MODIFY updated_by VARCHAR(255) NULL;
ALTER TABLE profiles         MODIFY created_by VARCHAR(255) NOT NULL, MODIFY updated_by VARCHAR(255) NULL;
ALTER TABLE job_applications MODIFY created_by VARCHAR(255) NOT NULL, MODIFY updated_by VARCHAR(255) NULL;

-- Fix 2: BLOB holds only 64 KB, so a resume or picture larger than that fails to save.
--        MEDIUMBLOB holds 16 MB.
ALTER TABLE profiles MODIFY profile_picture MEDIUMBLOB NULL, MODIFY resume MEDIUMBLOB NULL;