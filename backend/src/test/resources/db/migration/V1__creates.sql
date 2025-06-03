CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    user_type VARCHAR(50),
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS administrators (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS clients (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES users(id)
);





INSERT INTO users (email, password, name, enabled, user_type)
VALUES
('john@example.com', '123456', 'John Doe', true, 'User'),
('alice@example.com', 'alicepass', 'Alice', true, 'User'),
('bob@example.com', 'bobpass', 'Bob', false, 'User');

INSERT INTO clients (id)
SELECT id FROM users WHERE email IN ('john@example.com', 'alice@example.com');