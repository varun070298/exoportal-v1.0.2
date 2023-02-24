create table PORTLET_CATEGORY(
  id                   VARCHAR(128) NOT NULL,
  createdDate          TIMESTAMP NOT NULL,
  modifiedDate         TIMESTAMP NOT NULL,
  portletCategoryName  VARCHAR(255),
  description          TEXT ,
  PRIMARY KEY (id));

create table PORTLET (
  id                     VARCHAR(128) NOT NULL,
  portletCategoryId      VARCHAR(128),
  createdDate            TIMESTAMP NOT NULL,
  modifiedDate           TIMESTAMP NOT NULL,
  displayName            VARCHAR(255),
  portletApplicationName VARCHAR(255),
  portletName            VARCHAR(255),
  description            TEXT ,
  PRIMARY KEY (id));

create table PORTLET_ROLE (
  id              VARCHAR(128) NOT NULL,
  portletId       VARCHAR(128),
  portletRoleName VARCHAR(255),
  PRIMARY KEY (id));