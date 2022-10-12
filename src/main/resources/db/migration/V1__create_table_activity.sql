CREATE TABLE tb_activity (
	id bigserial NOT NULL,
	created_at timestamp NOT NULL,
	description varchar(255) NOT NULL,
	"name" varchar(100) NOT NULL,
	status varchar(20) NOT NULL,
	CONSTRAINT tb_activity_pkey PRIMARY KEY (id)
);