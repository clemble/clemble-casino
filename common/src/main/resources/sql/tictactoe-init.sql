      drop table if exists TIC_TAC_TOE_SPECIFICATION;
    
    create table TIC_TAC_TOE_SPECIFICATION (
        SPECIFICATION_GROUP varchar(255) not null,
        NAME varchar(255) not null,
        PRICE integer,
        CURRENCY varchar(255),
        GIVE_UP varchar(255),
        MATCH varchar(255),
        MOVE_TIME_LIMIT integer,
        MOVE_TIME_PUNISHMENT varchar(255),
        NUMBER varchar(255),
        PRIVACY varchar(255),
        TOTAL_TIME_LIMIT integer,
        TOTAL_TIME_PUNISHMENT varchar(255),
        primary key (SPECIFICATION_GROUP, NAME)
    );

INSERT INTO TIC_TAC_TOE_SPECIFICATION (SPECIFICATION_GROUP, NAME, PRICE, CURRENCY, GIVE_UP, "MATCH", MOVE_TIME_LIMIT, MOVE_TIME_PUNISHMENT, NUMBER, PRIVACY, TOTAL_TIME_LIMIT, TOTAL_TIME_PUNISHMENT)
VALUES('basic', 'low', 50, 'FakeMoney', 'all', 'automatic', 0, 'loose', 'two', 'everybody', 0, 'loose');
INSERT INTO TIC_TAC_TOE_SPECIFICATION (SPECIFICATION_GROUP, NAME, PRICE, CURRENCY, GIVE_UP, "MATCH", MOVE_TIME_LIMIT, MOVE_TIME_PUNISHMENT, NUMBER, PRIVACY, TOTAL_TIME_LIMIT, TOTAL_TIME_PUNISHMENT)
VALUES('basic', 'medium', 100, 'FakeMoney', 'all', 'automatic', 0, 'loose', 'two', 'everybody', 0, 'loose');
INSERT INTO TIC_TAC_TOE_SPECIFICATION (SPECIFICATION_GROUP, NAME, PRICE, CURRENCY, GIVE_UP, "MATCH", MOVE_TIME_LIMIT, MOVE_TIME_PUNISHMENT, NUMBER, PRIVACY, TOTAL_TIME_LIMIT, TOTAL_TIME_PUNISHMENT)
VALUES('basic', 'high', 150, 'FakeMoney', 'all', 'automatic', 0, 'loose', 'two', 'everybody', 0, 'loose');

