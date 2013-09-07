
    drop table PLAYER_CREDENTIALS if exists;

    drop table PLAYER_IDENTITY if exists;

    create table PLAYER_CREDENTIALS (
        PLAYER_ID bigint not null,
        EMAIL varchar(128),
        PASSWORD varchar(255),
        primary key (PLAYER_ID)
    );

    create table PLAYER_IDENTITY (
        PLAYER_ID bigint not null,
        DEVICE varchar(255) not null,
        SECRET varchar(255) not null,
        primary key (PLAYER_ID)
    );

    alter table PLAYER_CREDENTIALS 
        add constraint UK_sn85xlhrj1sbru10efj3taqql unique (EMAIL);
