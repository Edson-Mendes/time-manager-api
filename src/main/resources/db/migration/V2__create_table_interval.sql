CREATE TABLE tb_interval (
	id bigserial NOT NULL,
	elapsed_time time NOT NULL,
	started_at timestamp NOT NULL,
	activity_id int8 NOT NULL,
	CONSTRAINT tb_interval_pkey PRIMARY KEY (id),
	CONSTRAINT fkte2msxsci9kym392a96rpp8v5 FOREIGN KEY (activity_id) REFERENCES tb_activity(id)
);