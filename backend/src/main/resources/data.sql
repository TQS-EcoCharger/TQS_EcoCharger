-- Inserir utilizadores (drivers)
INSERT INTO users (email, password, name, enabled, user_type) VALUES
('afonso@gmail.com', 'pass', 'Afonso Ferreira', true, 'drivers'),
('ricardo.antunes2002@gmail.com', 'banana', 'Ricardo Antunes', true, 'drivers');

-- Criar drivers com IDs iguais aos criados em "users"
INSERT INTO drivers (id) VALUES
(1),
(2);

-- Inserir carros
INSERT INTO car (name, make, model, manufacture_year, license_plate, battery_capacity, battery_level, kilometers, consumption, enabled) VALUES
('Tesla Model 3', 'Tesla', 'Model 3', 2022, 'AA-12-BB', 75.0, 40.0, 25000, 15.0, true),
('Nissan Leaf', 'Nissan', 'Leaf', 2021, 'CC-34-DD', 40.0, 20.0, 32000, 14.5, true),
('Hyundai Kona Electric', 'Hyundai', 'Kona Electric', 2020, 'EE-56-FF', 64.0, 55.0, 15000, 16.2, true),
('Renault Zoe', 'Renault', 'Zoe', 2023, 'II-90-JJ', 52.0, 48.0, 9000, 12.8, true);

-- Associar carros a drivers
INSERT INTO driver_cars (driver_id, cars_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4);

-- Inserir utilizadores (admin e client)
INSERT INTO users (email, password, name, enabled, user_type) VALUES
('tomas@gmail.com', 'pass1', 'Tomás Silva', true, 'administrators'),
('tomascliente@gmail.com', 'pass2', 'Tomás Cliente', true, 'clients');

-- Criar admin e cliente
INSERT INTO administrators (id) SELECT id FROM users WHERE email = 'tomas@gmail.com';
INSERT INTO clients (id) SELECT id FROM users WHERE email = 'tomascliente@gmail.com';

-- Estações de carregamento adicionais
INSERT INTO charging_stations (municipality, address, latitude, longitude, countryCode, country) VALUES
('Porto', 'Rua de Santa Catarina, 4000-447 Porto', 41.14961, -8.60747, 'PT', 'Portugal'),
('Porto', 'Rua das Flores, 4050-265 Porto', 41.14141, -8.61107, 'PT', 'Portugal'),
('Lisboa', 'Praça do Comércio, 1100-148 Lisboa', 38.70775, -9.13659, 'PT', 'Portugal'),
('Lisboa', 'Avenida da Liberdade, 1250-140 Lisboa', 38.72057, -9.14584, 'PT', 'Portugal'),
('Coimbra', 'Rua Ferreira Borges, 3000-179 Coimbra', 40.21010, -8.42920, 'PT', 'Portugal'),
('Faro', 'Rua Dom Francisco Gomes, 8000-262 Faro', 37.01530, -7.93550, 'PT', 'Portugal'),
('Braga', 'Avenida Central, 4710-229 Braga', 41.54877, -8.42761, 'PT', 'Portugal'),
('Setúbal', 'Avenida Luísa Todi, 2900-461 Setúbal', 38.52357, -8.89267, 'PT', 'Portugal'),
('Leiria', 'Rua Capitão Mouzinho de Albuquerque, 2400-193 Leiria', 39.74349, -8.80768, 'PT', 'Portugal'),
('Aveiro', 'Estrada de São Bernardo, 3810-174 Aveiro', 40.63132, -8.64325, 'PT', 'Portugal');

SELECT setval('charging_stations_id_seq', (SELECT MAX(id) FROM charging_stations));

-- Pontos de carregamento adicionais
INSERT INTO charging_points (station_id, available, brand) VALUES
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 0), true, 'EDP'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 0), false, 'EDP'),

((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 1), true, 'Mobi.E'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 1), true, 'Mobi.E'),

((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 2), true, 'Ionity'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 2), true, 'Ionity'),

((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 3), true, 'Galp'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 3), false, 'Galp'),

((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 4), true, 'PowerDot'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 4), true, 'PowerDot'),

((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 5), true, 'Mobi.E'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 5), true, 'Mobi.E'),

((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 6), true, 'Ionity'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 6), true, 'Ionity'),

((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 7), true, 'Galp'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 7), false, 'Galp'),

((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 8), true, 'EDP'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 8), true, 'EDP'),

((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 9), true, 'PowerDot'),
((SELECT id FROM charging_stations ORDER BY id DESC LIMIT 1 OFFSET 9), true, 'PowerDot');

SELECT setval('charging_points_id_seq', (SELECT MAX(id) FROM charging_points));


-- Conectores
INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type) VALUES
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 0), 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 0), 'Chademo', 50, 400, 125, 'DC'),
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 1), 'Chademo', 50, 400, 125, 'DC'),
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 2), 'IEC62196Type2CCS', 60, 400, 150, 'DC'),
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 3), 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 4), 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 5), 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 6), 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 7), 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
((SELECT id FROM charging_points ORDER BY id LIMIT 1 OFFSET 8), 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

SELECT setval('connectors_id_seq', (SELECT MAX(id) FROM connectors));
