

CREATE TABLE NPP_ROLES (
ROLE_ID  NUMBER                               NOT NULL,
NAME     VARCHAR2(45 BYTE)                    NOT NULL
);

CREATE TABLE NPP_USERS_CASH (
USER_ID  NUMBER,
CASH_ID  NUMBER
);

CREATE TABLE NPP_USERS_ROLES (
USER_ID  NUMBER                               NOT NULL,
ROLE_ID  NUMBER                               NOT NULL
);