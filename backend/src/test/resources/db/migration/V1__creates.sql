CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    enabled BOOLEAN NOT NULL
);

INSERT INTO users (email, password, name, enabled) VALUES 
('john@example.com', '123456', 'John Doe', true),
('alice@example.com', 'alicepass', 'Alice', true),
('bob@example.com', 'bobpass', 'Bob', false);


