CREATE TABLE ACCOUNT (
                            ID INT PRIMARY KEY,
                            NAME VARCHAR(128),
                            STATUS INT,
                            OPEN_TIME TIMESTAMP,
                            CLIENT_ID VARCHAR(64)
);

CREATE TABLE BALANCE (
                      ACCOUNT_ID VARCHAR(64) PRIMARY KEY,
                      BAL DECIMAL(19, 2) NOT NULL,
                      CCY VARCHAR(10),
                      CHANGE_TIME TIMESTAMP
);

CREATE TABLE HIST (
                      TRANS_ID VARCHAR(255),
                      ACCOUNT_ID VARCHAR(64),
                      TO_ACCOUNT_ID VARCHAR(64),
                      TRANS_TYPE VARCHAR(16),
                      AMOUNT DECIMAL(19, 2) NOT NULL,
                      CCY VARCHAR(10),
                      TRANS_TIME TIMESTAMP NOT NULL,
                      CLIENT_ID VARCHAR(64) NOT NULL,
                      MARK VARCHAR(256),
                      UNIQUE (TRANS_ID, ACCOUNT_ID)

);

CREATE INDEX idx_hist_client_id ON HIST (CLIENT_ID);
CREATE INDEX idx_hist_amount ON HIST (AMOUNT);


insert into ACCOUNT values (1234,'测试账号1',0,null,'c1');
insert into ACCOUNT values (6789,'测试账号2',0,null,'c2');
insert into BALANCE values (1234,0,null,null);
insert into BALANCE values (6789,0,null,null);

