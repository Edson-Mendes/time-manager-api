INSERT
    INTO tb_activity (name, description, created_at, enabled)
    VALUES('Finances API', 'A simple project for financial control', '2022-06-19 11:06:32', true),
        ('Time Manager API', 'A simple project for manage your time', '2022-06-22 08:12:47', true);

INSERT INTO tb_interval (elapsed_time, started_at, activity_id) VALUES ('01:32:50', '2022-06-19 11:06:32', 1),
    ('00:53:12', '2022-06-20 09:17:20', 1),
    ('00:30:00', '2022-06-22 10:27:04', 2);