CREATE TABLE MYBATIES_DYNAMIC_DATA_SOURCE_TEMPLATE (
	id VARCHAR (36) NOT NULL,
	create_date datetime,
	create_user_id VARCHAR (255),
	create_user_name VARCHAR (255),
	enable_flag bit NOT NULL,
	update_date datetime,
	update_user_id VARCHAR (255),
	update_user_name VARCHAR (255),
	version BIGINT,
	CODE VARCHAR (255),
	NAME VARCHAR (255),
	PRIMARY KEY (id)
) ENGINE = INNODB