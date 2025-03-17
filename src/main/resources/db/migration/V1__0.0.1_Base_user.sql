CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS user_account
(
    user_account_id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                   CHARACTER VARYING NOT NULL,
    email                  CHARACTER VARYING NOT NULL,
    password               CHARACTER VARYING,
    password_salt          CHARACTER VARYING,
    login_attempts         INTEGER,
    email_verified         BOOLEAN,
    verification_token     CHARACTER VARYING,
    reset_password_token   CHARACTER VARYING,
    reset_password_expires TIMESTAMP,
    created_at             TIMESTAMP,
    lockout_time           TIMESTAMP,
    last_login             TIMESTAMP,
    last_password_change   TIMESTAMP,
    oauth_id               CHARACTER VARYING,
    company_id             UUID              NOT NULL
);

CREATE TABLE IF NOT EXISTS rule
(
    rule_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        CHARACTER VARYING,
    description CHARACTER VARYING
    );

CREATE TABLE IF NOT EXISTS user_group
(
    user_group_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        CHARACTER VARYING,
    description CHARACTER VARYING,
    company_id  UUID              NOT NULL
    );

CREATE TABLE IF NOT EXISTS user_group_rule
(
    user_group_id UUID NOT NULL,
    rule_id  UUID NOT NULL,
    PRIMARY KEY (user_group_id, rule_id),
    CONSTRAINT user_group_rule_user_group_id_fk FOREIGN KEY (user_group_id) REFERENCES user_group (user_group_id),
    CONSTRAINT user_group_rule_rule_id_fk FOREIGN KEY (rule_id) REFERENCES rule (rule_id)
    );

CREATE TABLE IF NOT EXISTS user_account_user_group
(
    user_account_id UUID NOT NULL,
    user_group_id UUID NOT NULL,
    PRIMARY KEY (user_account_id, user_group_id),
    CONSTRAINT user_account_user_account_id_fk FOREIGN KEY (user_account_id) REFERENCES user_account (user_account_id),
    CONSTRAINT user_account_user_group_id_fk FOREIGN KEY (user_group_id) REFERENCES user_group (user_group_id)
    );

INSERT INTO rule (name, description) VALUES ('INVITE_USER', 'rule.invite-user.description');
INSERT INTO rule (name, description) VALUES ('LIST_COMPANY_USERS', 'rule.list-company-users.description');
INSERT INTO rule (name, description) VALUES ('CREATE_CUSTOMER', 'rule.create-customer.description');
INSERT INTO rule (name, description) VALUES ('LIST_CUSTOMERS', 'rule.list-customers.description');
INSERT INTO rule (name, description) VALUES ('GET_CUSTOMER_DETAILS', 'rule.get-customer-details.description');
INSERT INTO rule (name, description) VALUES ('UPDATE_CUSTOMER', 'rule.update-customer.description');
INSERT INTO rule (name, description) VALUES ('DELETE_CUSTOMER', 'rule.delete-customer.description');

