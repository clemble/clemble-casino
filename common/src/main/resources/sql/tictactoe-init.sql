    drop table if exists TIC_TAC_TOE_SPECIFICATION;
    
    create table TIC_TAC_TOE_SPECIFICATION (
        SPECIFICATION_GROUP varchar(255) not null,
        SPECIFICATION_NAME varchar(255) not null,
        PRICE integer,
        CURRENCY varchar(255),
        GIVE_UP varchar(255),
        MATCH_RULE varchar(255),
        MOVE_TIME_LIMIT integer,
        MOVE_TIME_PUNISHMENT varchar(255),
        PLAYER_NUMBER varchar(255),
        PRIVACY_RULE varchar(255),
        TOTAL_TIME_LIMIT integer,
        TOTAL_TIME_PUNISHMENT varchar(255),
        primary key (SPECIFICATION_GROUP, SPECIFICATION_NAME)
    );

    INSERT INTO TIC_TAC_TOE_SPECIFICATION (SPECIFICATION_GROUP, SPECIFICATION_NAME, PRICE, CURRENCY, GIVE_UP, MATCH_RULE, MOVE_TIME_LIMIT, MOVE_TIME_PUNISHMENT, PLAYER_NUMBER, PRIVACY_RULE, TOTAL_TIME_LIMIT, TOTAL_TIME_PUNISHMENT)
    VALUES('basic', 'low', 50, 'FakeMoney', 'all', 'automatic', 0, 'loose', 'two', 'everybody', 0, 'loose');

    INSERT INTO TIC_TAC_TOE_SPECIFICATION (SPECIFICATION_GROUP, SPECIFICATION_NAME, PRICE, CURRENCY, GIVE_UP, MATCH_RULE, MOVE_TIME_LIMIT, MOVE_TIME_PUNISHMENT, PLAYER_NUMBER, PRIVACY_RULE, TOTAL_TIME_LIMIT, TOTAL_TIME_PUNISHMENT)
    VALUES('basic', 'medium', 100, 'FakeMoney', 'all', 'automatic', 0, 'loose', 'two', 'everybody', 0, 'loose');

    INSERT INTO TIC_TAC_TOE_SPECIFICATION (SPECIFICATION_GROUP, SPECIFICATION_NAME, PRICE, CURRENCY, GIVE_UP, MATCH_RULE, MOVE_TIME_LIMIT, MOVE_TIME_PUNISHMENT, PLAYER_NUMBER, PRIVACY_RULE, TOTAL_TIME_LIMIT, TOTAL_TIME_PUNISHMENT)
    VALUES('basic', 'high', 150, 'FakeMoney', 'all', 'automatic', 0, 'loose', 'two', 'everybody', 0, 'loose');
