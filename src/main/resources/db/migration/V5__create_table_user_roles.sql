CREATE TABLE tb_user_roles (
    user_id int8 NOT NULL,
    roles_id int4 NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_id FOREIGN KEY (roles_id) REFERENCES tb_role(id)
);