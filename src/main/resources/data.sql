-- TODO: SQL não usado
INSERT INTO tb_role (name) VALUES ('ROLE_USER');
INSERT INTO tb_user (name, email, password) VALUES ('User', 'user@email.com', '$2a$10$yT3KTVbQsHJTmPQSMEzhkeWQh4mgJlPbBG.dpY9Cp3mlLyQ246F6a');
INSERT INTO tb_user_roles (user_id, roles_id) VALUES (1, 1);
