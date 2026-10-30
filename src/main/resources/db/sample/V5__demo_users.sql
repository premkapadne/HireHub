-- DEMO USERS for local testing only - never use these passwords in a real deployment.
-- Safe to run more than once: a user is skipped if its email already exists.
--
--   admin@hirehub.com     / Admin@12345     (ROLE_ADMIN)
--   employer@hirehub.com  / Employer@12345  (ROLE_EMPLOYER, assigned to company 'TechCorp Inc.')
--   seeker@hirehub.com    / Seeker@12345    (ROLE_JOB_SEEKER)

INSERT INTO users (name, email, password_hash, mobile_number, role_id, company_id, created_by)
SELECT 'Admin User', 'admin@hirehub.com', '$2a$10$MPpYJGAlbb5Esm426yimSOqzdsVEhx14hNLDyn0ePQqhzxByAHLZm', '9000000001', r.id, NULL, 'DEMO'
FROM roles r
WHERE r.name = 'ROLE_ADMIN'
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.email = 'admin@hirehub.com');

INSERT INTO users (name, email, password_hash, mobile_number, role_id, company_id, created_by)
SELECT 'Employer User', 'employer@hirehub.com', '$2a$10$x7tu303qeUCMw8I86KmAsuBxscrbWB.8g5yP9hagZxBxqa8oe4U0e', '9000000002', r.id, (SELECT id FROM companies WHERE name = 'TechCorp Inc.'), 'DEMO'
FROM roles r
WHERE r.name = 'ROLE_EMPLOYER'
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.email = 'employer@hirehub.com');

INSERT INTO users (name, email, password_hash, mobile_number, role_id, company_id, created_by)
SELECT 'Job Seeker', 'seeker@hirehub.com', '$2a$10$/TqQmz23bf3RMFvNTGw0juW2tkNp0ZrE.APXNN0aq38e3GZz/wB76', '9000000003', r.id, NULL, 'DEMO'
FROM roles r
WHERE r.name = 'ROLE_JOB_SEEKER'
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.email = 'seeker@hirehub.com');
