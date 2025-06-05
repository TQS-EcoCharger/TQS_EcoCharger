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

-- Estações de carregamento
INSERT INTO charging_stations (municipality, address, latitude, longitude, countryCode, country) VALUES
('Aveiro', 'Rua do Batalhão de Caçadores 10 10, 3810-064 Aveiro', 40.641029, -8.652739, 'PT', 'Portugal'),
('Aveiro', 'Rua Batalhão Caçadores Dez -, 3810-064 Aveiro', 40.641029, -8.652738, 'PT', 'Portugal'),
('Aveiro', 'Rua Batalhýo Caýadores Dez -, 3810-064 Aveiro', 40.641029, -8.652738, 'PT', 'Portugal'),
('Aveiro', 'Rua Príncipe Perfeito, 3810-151 Aveiro', 40.639324, -8.651682, 'PT', 'Portugal'),
('Aveiro', 'Praça Marquês de Pombal, 3810-133 Aveiro', 40.638799, -8.652208, 'PT', 'Portugal'),
('Aveiro', 'Largo do Rossio, 3800-246 Aveiro', 40.642059, -8.656503, 'PT', 'Portugal'),
('Aveiro', 'Rua Doutor Alberto Souto -, 3800-148 Aveiro', 40.64375, -8.64861, 'PT', 'Portugal'),
('Aveiro', 'Rua Doutor Alberto Soares Machado, 3800-146 Aveiro', 40.643751, -8.648611, 'PT', 'Portugal'),
('Aveiro', 'Cais da Fonte Nova, 3810-200 Aveiro', 40.638509, -8.645023, 'PT', 'Portugal'),
('Aveiro', 'Avenida da Universidade, 3810-489 Aveiro', 40.633825, -8.656514, 'PT', 'Portugal');

SELECT setval('charging_stations_id_seq', (SELECT MAX(id) FROM charging_stations));

-- Pontos de carregamento
INSERT INTO charging_points (station_id, available, brand) VALUES
((SELECT id FROM charging_stations ORDER BY id LIMIT 1 OFFSET 0), true, 'Atlante'),
((SELECT id FROM charging_stations ORDER BY id LIMIT 1 OFFSET 0), true, 'Atlante'),
((SELECT id FROM charging_stations ORDER BY id LIMIT 1 OFFSET 1), true, 'Mobi.E'),
((SELECT id FROM charging_stations ORDER BY id LIMIT 1 OFFSET 2), false, 'Mobi.E'),
((SELECT id FROM charging_stations ORDER BY id LIMIT 1 OFFSET 2), true, 'Mobi.E');

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
