CREATE TABLE tb_user (
	id bigserial NOT NULL,
	email varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	password varchar(255) NOT NULL,
	CONSTRAINT tb_user_pkey PRIMARY KEY (id),
	CONSTRAINT tb_user_email_unique UNIQUE (email)
);