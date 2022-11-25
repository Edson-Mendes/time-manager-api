--  insere 1 usuário (com id fornecido) que irá possuir as atividades
INSERT INTO tb_user (id, name, email, password) VALUES
    (1000, 'Lorem Ipsum', 'lorem@email.com', '$2a$10$yT3KTVbQsHJTmPQSMEzhkeWQh4mgJlPbBG.dpY9Cp3mlLyQ246F6a');

--  insere a role do usuário acima
INSERT INTO tb_user_roles (user_id, roles_id) VALUES
    (1000, 1);

--  insere 1 atividades
INSERT INTO tb_activity (name, description, created_at, status, user_id) VALUES
    ('Awesome activity 1', 'Some awesome activity 1', '2022-10-23 08:00:00', 'CONCLUDED', 1000);

--  insere 2 intervalos para atividade de id = 1
INSERT INTO tb_interval (started_at, elapsed_time, activity_id) VALUES
    ('2022-10-23 10:00:00', '00:30:00', 1);
