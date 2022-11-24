--  insere 1 usuário (com id fornecido) que irá possuir as atividades
INSERT INTO tb_user (id, name, email, password) VALUES
    (1000, 'Lorem Ipsum', 'lorem@email.com', '$2a$10$yT3KTVbQsHJTmPQSMEzhkeWQh4mgJlPbBG.dpY9Cp3mlLyQ246F6a');

--  insere a role do usuário acima
INSERT INTO tb_user_roles (user_id, roles_id) VALUES
    (1000, 1);

--  insere 1 atividade ativa e 3 atividade deletadas
INSERT INTO tb_activity (name, description, created_at, status, user_id) VALUES
    ('Awesome activity 1', 'Some awesome activity 1', '2022-10-23 08:00:00', 'ACTIVE', 1000),
    ('Awesome activity 2', 'Some awesome activity 2', '2022-10-23 09:00:00', 'DELETED', 1000),
    ('Another awesome activity 1', 'Another some awesome activity 1', '2022-10-24 10:00:00', 'DELETED', 1000),
    ('Another awesome activity 2', 'Another some awesome activity 2', '2022-10-24 11:00:00', 'DELETED', 1000);