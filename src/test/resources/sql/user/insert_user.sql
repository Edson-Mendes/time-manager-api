--  insere 1 usuário (com id fornecido)
INSERT INTO tb_user (id, name, email, password) VALUES
    (1000, 'Lorem Ipsum', 'lorem@email.com', '$2a$10$yT3KTVbQsHJTmPQSMEzhkeWQh4mgJlPbBG.dpY9Cp3mlLyQ246F6a');

--  insere a role do usuário acima
INSERT INTO tb_user_roles (user_id, roles_id) VALUES
    (1000, 1);