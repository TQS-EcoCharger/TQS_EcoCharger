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

-- Station 1: Atlante
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(1, 1, true, 'Atlante', 0.25, 0.05, 0.3667),
(2, 1, true, 'Atlante', 0.25, 0.05, 0.8333);

-- Station 2: Mobi.E
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(3, 2, true, 'Mobi.E', 0.20, 0.03, 1.0000);

-- Station 3: Mobi.E
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(4, 3, false, 'Mobi.E', 0.20, 0.03, 1.0000),
(5, 3, true, 'Mobi.E', 0.20, 0.03, 0.3667);

-- Station 4: EDP Comercial
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(6, 4, true, 'EDP', 0.23, 0.04, 0.3667);

-- Station 5: EDP Comercial
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(7, 5, true, 'EDP', 0.23, 0.04, 0.3667),
(8, 5, true, 'EDP', 0.23, 0.04, 0.3667);

-- Station 6: AVR-90002
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(9, 6, true, 'AVR-90002', 0.30, 0.06, 0.3667),
(10, 6, false, 'AVR-90002', 0.30, 0.06, 0.3667),
(11, 6, true, 'AVR-90002', 0.30, 0.06, 0.3667);

-- Station 7: Mobi.E
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(12, 7, true, 'Mobi.E', 0.21, 0.04, 0.3667);

-- Station 8: Galp Power
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(13, 8, true, 'Galp electric', 0.28, 0.07, 0.3667),
(14, 8, false, 'Galp electric', 0.28, 0.07, 0.3667);

-- Station 9: Powerdot
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(15, 9, true, 'Powerdot', 0.26, 0.06, 0.3667);

-- Station 10: Atlante
INSERT INTO charging_points (id, station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(16, 10, true, 'Atlante', 0.25, 0.05, 0.8333),
(17, 10, true, 'Atlante', 0.25, 0.05, 0.8333);


INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (1, 1, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (2, 1, 'Chademo', 50, 400, 125, 'DC');

INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (3, 2, 'Chademo', 50, 400, 125, 'DC');

INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (4, 3, 'IEC62196Type2CCS', 60, 400, 150, 'DC');

INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (5, 4, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (6, 5, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (7, 6, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (8, 7, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (9, 8, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (id, charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (10, 9, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');
