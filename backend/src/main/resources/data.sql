-- Inserir utilizadores (drivers)
INSERT INTO users (email, password, name, enabled, user_type) VALUES
('afonso@gmail.com', 'pass', 'Afonso Ferreira', true, 'drivers'),
('ricardo.antunes2002@gmail.com', 'banana', 'Ricardo Antunes', true, 'drivers'),
('tomascliente@gmail.com', 'pass2', 'Tomás Cliente', true, 'drivers'),
('tomas@gmail.com', 'pass1', 'Tomás Silva', true, 'administrators'),
('chargingoperator@example.com', 'op1234', 'Charging Operator', true, 'chargingOperators'),
('driver@example.com', 'driver', 'Driver User', true, 'drivers'),
('admin@example.com', 'admin1', 'Admin User', true, 'administrators');

-- Criar drivers com IDs iguais aos criados em "users"
INSERT INTO drivers (id) VALUES
(1),
(2),
(6);

INSERT INTO administrators (id) VALUES
(4);

INSERT INTO charging_operator (id) VALUES
(3);

-- Inserir carros
INSERT INTO car (name, make, model, manufacture_year, license_plate, battery_capacity, battery_level, kilometers, consumption, enabled) VALUES
('Tesla Model 3', 'Tesla', 'Model 3', 2022, 'AA-12-BB', 75.0, 40.0, 25000, 15.0, true),
('Nissan Leaf', 'Nissan', 'Leaf', 2021, 'CC-34-DD', 40.0, 20.0, 32000, 14.5, true),
('Hyundai Kona Electric', 'Hyundai', 'Kona Electric', 2020, 'EE-56-FF', 64.0, 55.0, 15000, 16.2, true),
('Renault Zoe', 'Renault', 'Zoe', 2023, 'II-90-JJ', 52.0, 48.0, 9000, 12.8, true),
('BMW i3', 'BMW', 'i3', 2019, 'KK-11-LL', 42.0, 30.0, 20000, 13.5, true),
('Audi e-tron', 'Audi', 'e-tron', 2022, 'MM-22-NN', 95.0, 70.0, 5000, 20.0, true),
('Volkswagen ID.4', 'Volkswagen', 'ID.4', 2021, 'OO-33-PP', 82.0, 60.0, 12000, 18.0, true);

-- Associar carros a drivers
INSERT INTO driver_cars (driver_id, cars_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(6, 5),
(6, 6);



-- Estações de carregamento
INSERT INTO charging_stations (municipality, address, latitude, longitude, countryCode, country) VALUES
('Coimbra', 'Rua do Batalhão de Caçadores 10 10, 3810-064 Aveiro', 40.641029, -8.652739, 'PT', 'Portugal'),
('Aveiro', 'Rua Batalhão Caçadores Dez -, 3810-064 Aveiro', 40.641029, -8.652738, 'PT', 'Portugal'),
('Aveiro', 'Rua Príncipe Perfeito, 3810-151 Aveiro', 40.639324, -8.651682, 'PT', 'Portugal'),
('Aveiro', 'Praça Marquês de Pombal, 3810-133 Aveiro', 40.638799, -8.652208, 'PT', 'Portugal'),
('Aveiro', 'Largo do Rossio, 3800-246 Aveiro', 40.642059, -8.656503, 'PT', 'Portugal'),
('Aveiro', 'Rua Doutor Alberto Souto -, 3800-148 Aveiro', 40.64375, -8.64861, 'PT', 'Portugal'),
('Aveiro', 'Rua Doutor Alberto Soares Machado, 3800-146 Aveiro', 40.643751, -8.648611, 'PT', 'Portugal'),
('Aveiro', 'Cais da Fonte Nova, 3810-200 Aveiro', 40.638509, -8.645023, 'PT', 'Portugal'),
('Aveiro', 'Avenida da Universidade, 3810-489 Aveiro', 40.633825, -8.656514, 'PT', 'Portugal');

INSERT INTO charging_operator_stations (operator_id, stations_id) VALUES
(3, 1),
(3, 2),
(3, 7);


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
-- Station 1: Atlante
INSERT INTO charging_points (station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(1, true, 'Atlante', 0.25, 0.05, 0.3667),
(1, true, 'Atlante', 0.25, 0.05, 0.8333);

-- Station 2: Mobi.E
INSERT INTO charging_points (station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(2, true, 'Mobi.E', 0.20, 0.03, 1.0000);

-- Station 3: Mobi.E
INSERT INTO charging_points (station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(3, false, 'Mobi.E', 0.20, 0.03, 1.0000),
(3, true, 'Mobi.E', 0.20, 0.03, 0.3667);

-- Station 4: EDP Comercial
INSERT INTO charging_points (station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(4, true, 'EDP', 0.23, 0.04, 0.3667);

-- Station 5: EDP Comercial
INSERT INTO charging_points (station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(5, true, 'EDP', 0.23, 0.04, 0.3667),
(5, true, 'EDP', 0.23, 0.04, 0.3667);

-- Station 6: AVR-90002
INSERT INTO charging_points (station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(6, true, 'AVR-90002', 0.30, 0.06, 0.3667),
(6, false, 'AVR-90002', 0.30, 0.06, 0.3667),
(6, true, 'AVR-90002', 0.30, 0.06, 0.3667);

-- Station 7: Mobi.E
INSERT INTO charging_points (station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(7, true, 'Mobi.E', 0.21, 0.04, 0.3667);

-- Station 8: Galp Power
INSERT INTO charging_points (station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(8, true, 'Galp electric', 0.28, 0.07, 0.3667),
(8, false, 'Galp electric', 0.28, 0.07, 0.3667);

-- Station 9: Powerdot
INSERT INTO charging_points (station_id, available, brand, price_per_kwh, price_per_minute, charging_rate_kwh_per_minute) 
VALUES 
(9, true, 'Powerdot', 0.26, 0.06, 0.3667);


INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (1, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (1, 'Chademo', 50, 400, 125, 'DC');

INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (2, 'Chademo', 50, 400, 125, 'DC');

INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (3, 'IEC62196Type2CCS', 60, 400, 150, 'DC');

INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (4, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (5, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (6, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (7, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (8, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type)
VALUES (9, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

-- Add connectors for charging points 10 to 18
INSERT INTO connectors (charging_point_id, connector_type, rated_power_kw, voltage_v, current_a, current_type) VALUES
(10, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
(11, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
(12, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
(13, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
(14, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3'),
(15, 'IEC62196Type2Outlet', 22, 230, 32, 'AC3');

UPDATE drivers SET balance = 50.0 WHERE id = 1; 
UPDATE drivers SET balance = 35.5 WHERE id = 2; 
UPDATE drivers SET balance = 20.0 WHERE id = 6;
