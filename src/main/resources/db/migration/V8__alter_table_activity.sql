ALTER TABLE tb_activity ADD COLUMN user_id bigint NOT NULL;
ALTER TABLE tb_activity ADD CONSTRAINT fk_user_id_tb_user FOREIGN KEY (user_id) REFERENCES tb_user(id);