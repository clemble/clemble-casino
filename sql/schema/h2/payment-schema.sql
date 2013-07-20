
    create table PAYMENT_TRANSACTION (
        MONEY_SOURCE varchar(255) not null,
        TRANSACTION_ID bigint not null,
        TRANSACTION_DATE timestamp,
        primary key (MONEY_SOURCE, TRANSACTION_ID)
    );

    create table PAYMENT_TRANSACTION_OPERATION (
        TRANSACTION_ID varchar(255) not null,
        MONEY_SOURCE bigint not null,
        CURRENCY integer,
        AMOUNT bigint,
        OPERATION varchar(255),
        PLAYER_ID bigint
    );

    create table PLAYER_ACCOUNT (
        PLAYER_ID bigint not null,
        primary key (PLAYER_ID)
    );

    create table PLAYER_ACCOUNT_AMOUNT (
        PLAYER_ID bigint not null,
        CURRENCY integer,
        AMOUNT bigint
    );

    alter table PAYMENT_TRANSACTION_OPERATION 
        add constraint FK27595F6D8230C2AC 
        foreign key (TRANSACTION_ID, MONEY_SOURCE) 
        references PAYMENT_TRANSACTION;

    alter table PLAYER_ACCOUNT_AMOUNT 
        add constraint FKC949D3E88E9CB787 
        foreign key (PLAYER_ID) 
        references PLAYER_ACCOUNT;
