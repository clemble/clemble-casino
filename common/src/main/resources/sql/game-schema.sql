
    drop table if exists GAME_SESSION;

    create table GAME_SESSION (
        SESSION_ID bigint not null auto_increment,
        BET_TYPE varchar(255),
        BET_MIN_PRICE integer,
        BET_MAX_PRICE integer,
        BET_CASH_TYPE varchar(255),
        LOOSE_TYPE varchar(255),
        LOOSE_MIN_PART integer,
        PARTICIPANT_TYPE varchar(255),
        PARTICIPANT_MATCH_TYPE varchar(255),
        PARTICIPANT_PRIVACY_TYPE varchar(255),
        PARTICIPANT_MIN integer,
        PARTICIPANT_MAX integer,
        TIME_TYPE varchar(255),
        TIME_BREACH_TYPE integer,
        TIME_LIMIT integer,
        SESSION_STATE integer,
        primary key (SESSION_ID)
    ) ENGINE=InnoDB;
