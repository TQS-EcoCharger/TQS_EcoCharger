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
