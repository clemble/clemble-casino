
    create table PLAYER_WALLET (
        PLAYER_ID bigint not null,
        primary key (PLAYER_ID)
    );

    create table PLAYER_WALLET_MONEY (
        PLAYER_ID bigint not null,
        CURRENCY integer,
        AMOUNT bigint
    );

    create table PLAYER_WALLET_OPERATION (
        TRANSACTION_ID varchar(255) not null,
        MONEY_SOURCE bigint not null,
        CURRENCY integer,
        AMOUNT bigint,
        OPERATION varchar(255),
        PLAYER_ID bigint
    );

    create table PLAYER_WALLET_TRANSACTION (
        MONEY_SOURCE varchar(255) not null,
        TRANSACTION_ID bigint not null,
        TRANSACTION_DATE timestamp,
        primary key (MONEY_SOURCE, TRANSACTION_ID)
    );

    alter table PLAYER_WALLET_MONEY 
        add constraint FK927D05788C841CF1 
        foreign key (PLAYER_ID) 
        references PLAYER_WALLET;

    alter table PLAYER_WALLET_OPERATION 
        add constraint FK2263961FB6ED86FD 
        foreign key (TRANSACTION_ID, MONEY_SOURCE) 
        references PLAYER_WALLET_TRANSACTION;
