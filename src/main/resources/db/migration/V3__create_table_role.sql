CREATE TABLE tb_role (
	id serial NOT NULL,
	name varchar(50) NOT NULL,
	CONSTRAINT tb_role_pkey PRIMARY KEY (id),
	CONSTRAINT uk_1ncmoedv5ta7r19y9d4oidn0y UNIQUE (name)
);