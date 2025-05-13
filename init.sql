CREATE TABLE subscriptions (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

INSERT INTO subscriptions(id, name)
VALUES 
(1, 'Youtube Premium'),
(2, 'VK Музыка'),
(3, 'Яндекс.Плюс'),
(4, 'Netflix');