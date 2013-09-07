
    drop table if exists PLAYER_CREDENTIALS;

    drop table if exists PLAYER_IDENTITY;

    create table PLAYER_CREDENTIALS (
        PLAYER_ID bigint not null,
        EMAIL varchar(128),
        PASSWORD varchar(255),
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;

    create table PLAYER_IDENTITY (
        PLAYER_ID bigint not null,
        DEVICE varchar(255) not null,
        SECRET varchar(255) not null,
        primary key (PLAYER_ID)
    ) ENGINE=InnoDB;

    alter table PLAYER_CREDENTIALS 
        add constraint UK_sn85xlhrj1sbru10efj3taqql unique (EMAIL);
