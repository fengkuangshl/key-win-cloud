create table mybaties_template (
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
 primary key (id)
 ) engine=InnoDB