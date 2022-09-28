create table sys_param (
 id varchar(36) not null,
 create_date datetime,
 create_user_id varchar(255),
 create_user_name varchar(255),
 enable_flag bit not null,
 update_date datetime,
 update_user_id varchar(255),
 update_user_name varchar(255),
 version bigint,
 code varchar(255),
 name varchar(255),
 attr1 varchar(255),
 attr2 varchar(255),
 attr3 varchar(255),
 attr4 varchar(255),
 attr5 varchar(255),
 primary key (id)
 ) engine=InnoDB

 create table sys_param_tree (
 id varchar(36) not null,
 create_date datetime,
 create_user_id varchar(255),
 create_user_name varchar(255),
 enable_flag bit not null,
 update_date datetime,
 update_user_id varchar(255),
 update_user_name varchar(255),
 version bigint,
 parent_id varchar(255),
 code varchar(255),
 name varchar(255),
 attr1 varchar(255),
 attr2 varchar(255),
 attr3 varchar(255),
 attr4 varchar(255),
 attr5 varchar(255),
 primary key (id)
 ) engine=InnoDB