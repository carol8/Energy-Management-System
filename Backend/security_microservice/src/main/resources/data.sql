DELETE FROM users WHERE username = 'carol8';
DELETE FROM users WHERE username = 'maria';

INSERT INTO users(username, password, role) VALUES
    ('carol8', '$2a$12$MtKaX3Oigkn6UbHewhanO.a0cwZIAgciJEanuYQq8ehz/bi4xVr/q', 'ADMIN'),
    ('maria', '$2a$12$FEuL2FVEh/PetsHIe/8QZuZ.MQ3HH1P2gIztvBlepuiWz2.pRghNm', 'USER');