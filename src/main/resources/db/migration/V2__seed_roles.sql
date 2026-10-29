-- Roles the application depends on. INSERT IGNORE keeps this safe to re-run (roles.name is UNIQUE).
INSERT IGNORE INTO roles (name, created_at, created_by) VALUES ('ROLE_JOB_SEEKER', CURRENT_TIMESTAMP, 'FLYWAY');
INSERT IGNORE INTO roles (name, created_at, created_by) VALUES ('ROLE_EMPLOYER',   CURRENT_TIMESTAMP, 'FLYWAY');
INSERT IGNORE INTO roles (name, created_at, created_by) VALUES ('ROLE_ADMIN',      CURRENT_TIMESTAMP, 'FLYWAY');
