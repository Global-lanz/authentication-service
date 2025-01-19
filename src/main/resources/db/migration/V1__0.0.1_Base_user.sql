CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS currency
(
    currency_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        CHARACTER VARYING,
    symbol      CHARACTER VARYING(3),
    code        CHARACTER VARYING(3)
);

CREATE TABLE IF NOT EXISTS company
(
    company_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        CHARACTER VARYING NOT NULL,
    country     CHARACTER VARYING,
    currency_id UUID,
    CONSTRAINT company_currency_fk FOREIGN KEY (currency_id) REFERENCES currency (currency_id)
);

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
    company_id             UUID              NOT NULL,
    CONSTRAINT company_fk FOREIGN KEY (company_id) REFERENCES company (company_id)
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
    description CHARACTER VARYING
    );

CREATE TABLE IF NOT EXISTS user_group_rule
(
    user_group_id UUID NOT NULL,
    rule_id  UUID NOT NULL,
    PRIMARY KEY (user_group_id, rule_id),
    CONSTRAINT user_group_rule_user_group_id_fk FOREIGN KEY (user_group_id) REFERENCES user_group (user_group_id),
    CONSTRAINT user_group_rule_rule_id_fk FOREIGN KEY (rule_id) REFERENCES rule (rule_id)
    );

CREATE TABLE IF NOT EXISTS user_account_group
(
    user_account_id UUID NOT NULL,
    user_group_id UUID NOT NULL,
    PRIMARY KEY (user_account_id, user_group_id),
    CONSTRAINT user_account_group_user_account_id_fk FOREIGN KEY (user_account_id) REFERENCES user_account (user_account_id),
    CONSTRAINT user_account_group_user_group_id_fk FOREIGN KEY (user_group_id) REFERENCES user_group (user_group_id)
    );

INSERT INTO rule (name, description) VALUES ('CREATE_USER', 'The userAccount can create new users.');
INSERT INTO user_group (name, description) VALUES ('SALES', 'The sales team that will prospect customers.');

INSERT INTO currency (name, symbol, code)
VALUES ('US Dollar', '$', 'USD');
INSERT INTO currency (name, symbol, code)
VALUES ('Euro', '€', 'EUR');
INSERT INTO currency (name, symbol, code)
VALUES ('Japanese Yen', '¥', 'JPY');
INSERT INTO currency (name, symbol, code)
VALUES ('British Pound', '£', 'GBP');
INSERT INTO currency (name, symbol, code)
VALUES ('Australian Dollar', 'A$', 'AUD');
INSERT INTO currency (name, symbol, code)
VALUES ('Canadian Dollar', 'C$', 'CAD');
INSERT INTO currency (name, symbol, code)
VALUES ('Swiss Franc', 'CHF', 'CHF');
INSERT INTO currency (name, symbol, code)
VALUES ('Chinese Yuan', '¥', 'CNY');
INSERT INTO currency (name, symbol, code)
VALUES ('Hong Kong Dollar', 'HK$', 'HKD');
INSERT INTO currency (name, symbol, code)
VALUES ('New Zealand Dollar', 'NZ$', 'NZD');
INSERT INTO currency (name, symbol, code)
VALUES ('Swedish Krona', 'kr', 'SEK');
INSERT INTO currency (name, symbol, code)
VALUES ('Norwegian Krone', 'kr', 'NOK');
INSERT INTO currency (name, symbol, code)
VALUES ('Russian Ruble', '₽', 'RUB');
INSERT INTO currency (name, symbol, code)
VALUES ('Indian Rupee', '₹', 'INR');
INSERT INTO currency (name, symbol, code)
VALUES ('Brazilian Real', 'R$', 'BRL');
INSERT INTO currency (name, symbol, code)
VALUES ('South African Rand', 'R', 'ZAR');
INSERT INTO currency (name, symbol, code)
VALUES ('Mexican Peso', '$', 'MXN');
INSERT INTO currency (name, symbol, code)
VALUES ('Singapore Dollar', 'S$', 'SGD');
INSERT INTO currency (name, symbol, code)
VALUES ('South Korean Won', '₩', 'KRW');
INSERT INTO currency (name, symbol, code)
VALUES ('Turkish Lira', '₺', 'TRY');
INSERT INTO currency (name, symbol, code)
VALUES ('Israeli New Shekel', '₪', 'ILS');
