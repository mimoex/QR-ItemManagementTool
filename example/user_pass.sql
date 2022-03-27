CREATE TABLE user_account_pass (
    user_name CHAR(7) NOT NULL,
    password VARCHAR(10) NOT NULL,
    PRIMARY KEY (user_name)
);

BEGIN TRANSACTION;
INSERT INTO user_account_pass VALUES ('1929040', 'Pw1929040');
INSERT INTO user_account_pass VALUES ('1929041', 'Pw1929041');
INSERT INTO user_account_pass VALUES ('1929042', 'Pw1929042');
INSERT INTO user_account_pass VALUES ('1929043', 'Pw1929043');
COMMIT;
