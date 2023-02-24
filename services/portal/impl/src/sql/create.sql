create table EXO_PORTAL_CONFIG (
    owner           VARCHAR(128) NOT NULL,
    viewPermission  VARCHAR(128) NOT NULL,
    editPermission  VARCHAR(128) NOT NULL, 
    data            TEXT NOT NULL,
    PRIMARY KEY (owner));

create table EXO_PAGE (
    id              VARCHAR(128) NOT NULL,
    owner           VARCHAR(128) NOT NULL,
    name            VARCHAR(128) NOT NULL,
    title           VARCHAR(128) NOT NULL,
    viewPermission  VARCHAR(128) NOT NULL,
    editPermission  VARCHAR(128) NOT NULL,
    data            TEXT NOT NULL,
    PRIMARY KEY (id));
