INSERT INTO users (email, password, name, enabled) VALUES
('afonso@gmail.com', 'pass', 'Afonso Ferreira', true);

-- CHARGING STATIONS (IDs 1–20)

-- Coimbra
INSERT INTO charging_stations (id, city_name, address, latitude, longitude) VALUES
(1, 'Coimbra', 'Av. Sá da Bandeira 1', 40.205, -8.413),
(2, 'Coimbra', 'Rua do Brasil 12', 40.206, -8.407),
(3, 'Coimbra', 'Praça da República 5', 40.209, -8.421),
(4, 'Coimbra', 'Rua da Sofia 22', 40.204, -8.428),
(5, 'Coimbra', 'Av. Fernão de Magalhães 30', 40.213, -8.429),
(6, 'Coimbra', 'Rua Direita 44', 40.210, -8.412),
(7, 'Coimbra', 'Rua do Loreto 3', 40.208, -8.416),
(8, 'Coimbra', 'Av. Dias da Silva 100', 40.207, -8.405),
(9, 'Coimbra', 'Rua Oliveira Matos 8', 40.202, -8.418),
(10, 'Coimbra', 'Rua da Alegria 99', 40.206, -8.424);

-- Aveiro
INSERT INTO charging_stations (id, city_name, address, latitude, longitude) VALUES
(11, 'Aveiro', 'Av. Dr. Lourenço Peixinho 1', 40.640, -8.653),
(12, 'Aveiro', 'Rua dos Combatentes 14', 40.642, -8.655),
(13, 'Aveiro', 'Rua de Viseu 18', 40.643, -8.649),
(14, 'Aveiro', 'Rua da Estação 10', 40.644, -8.651),
(15, 'Aveiro', 'Rua das Pombas 23', 40.646, -8.657),
(16, 'Aveiro', 'Rua das Agras 50', 40.645, -8.654),
(17, 'Aveiro', 'Rua Nova 7', 40.647, -8.648),
(18, 'Aveiro', 'Rua dos Marnotos 35', 40.641, -8.652),
(19, 'Aveiro', 'Rua da Escola 70', 40.639, -8.650),
(20, 'Aveiro', 'Av. das Pontes 5', 40.638, -8.656);

-- CHARGING POINTS (3–5 por estação, todos indisponíveis)

-- Coimbra
INSERT INTO charging_points (station_id, available) VALUES
(1, false), (1, false), (1, false),
(2, false), (2, false), (2, false), (2, false),
(3, false), (3, false), (3, false),
(4, false), (4, false), (4, false), (4, false), (4, false),
(5, false), (5, false), (5, false),
(6, false), (6, false), (6, false), (6, false),
(7, false), (7, false), (7, false),
(8, false), (8, false), (8, false), (8, false),
(9, false), (9, false), (9, false),
(10, false), (10, false), (10, false), (10, false);

-- Aveiro
INSERT INTO charging_points (station_id, available) VALUES
(11, false), (11, false), (11, false),
(12, false), (12, false), (12, false), (12, false),
(13, false), (13, false), (13, false),
(14, false), (14, false), (14, false), (14, false), (14, false),
(15, false), (15, false), (15, false),
(16, false), (16, false), (16, false), (16, false),
(17, false), (17, false), (17, false),
(18, false), (18, false), (18, false), (18, false),
(19, false), (19, false), (19, false),
(20, false), (20, false), (20, false), (20, false);
