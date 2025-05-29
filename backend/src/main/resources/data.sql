INSERT INTO users (email, password, name, enabled) VALUES
('afonso@gmail.com', 'pass', 'Afonso Ferreira', true),
('ricardo.antunes2002@gmail.com', 'banana', 'Ricardo Antunes', true);

INSERT INTO driver (id) VALUES
(1),
(2);

INSERT INTO car (id, name, make, model, year, license_plate, battery_capacity, battery_level, kilometers, consumption, enabled) VALUES
(1, 'Tesla Model 3', 'Tesla', 'Model 3', 2022, 'AA-12-BB', 75.0, 40.0, 25000, 15.0, true),
(2, 'Nissan Leaf', 'Nissan', 'Leaf', 2021, 'CC-34-DD', 40.0, 20.0, 32000, 14.5, true),

(3, 'Hyundai Kona Electric', 'Hyundai', 'Kona Electric', 2020, 'EE-56-FF', 64.0, 55.0, 15000, 16.2, true),
(4, 'Renault Zoe', 'Renault', 'Zoe', 2023, 'II-90-JJ', 52.0, 48.0, 9000, 12.8, true);

INSERT INTO driver_cars (driver_id, cars_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4);


-- CHARGING STATIONS (IDs 1–20)
INSERT INTO charging_stations (
    id, municipality, address, latitude, longitude,
    streetName, countryCode, country, vehicleType
) VALUES
(1, 'Aveiro', 'Rua do Batalhão de Caçadores 10 10, 3810-064 Aveiro', 40.641029, -8.652739,
 'Rua do Batalhão de Caçadores 10', 'PT', 'Portugal', 'Car,Truck'),

(2, 'Aveiro', 'Rua Batalhão Caçadores Dez -, 3810-064 Aveiro', 40.641029, -8.652738,
 'Rua Batalhão Caçadores Dez -', 'PT', 'Portugal', 'Car,Truck'),

(3, 'Aveiro', 'Rua Batalhýo Caýadores Dez -, 3810-064 Aveiro', 40.641029, -8.652738,
 'Rua Batalhýo Caýadores Dez -', 'PT', 'Portugal', 'Car,Truck'),

(4, 'Aveiro', 'Rua Príncipe Perfeito, 3810-151 Aveiro', 40.639324, -8.651682,
 'Rua Príncipe Perfeito', 'PT', 'Portugal', 'Car,Truck'),

(5, 'Aveiro', 'Praça Marquês de Pombal, 3810-133 Aveiro', 40.638799, -8.652208,
 'Praça Marquês de Pombal', 'PT', 'Portugal', 'Car,Truck'),

(6, 'Aveiro', 'Largo do Rossio, 3800-246 Aveiro', 40.642059, -8.656503,
 'Largo do Rossio', 'PT', 'Portugal', 'Car,Truck'),

(7, 'Aveiro', 'Rua Doutor Alberto Souto -, 3800-148 Aveiro', 40.64375, -8.64861,
 'Rua Doutor Alberto Souto -', 'PT', 'Portugal', 'Car,Truck'),

(8, 'Aveiro', 'Rua Doutor Alberto Soares Machado, 3800-146 Aveiro', 40.643751, -8.648611,
 'Rua Doutor Alberto Soares Machado', 'PT', 'Portugal', 'Car,Truck'),

(9, 'Aveiro', 'Cais da Fonte Nova, 3810-200 Aveiro', 40.638509, -8.645023,
 'Cais da Fonte Nova', 'PT', 'Portugal', 'Car,Truck'),

(10, 'Aveiro', 'Avenida da Universidade, 3810-489 Aveiro', 40.633825, -8.656514,
 'Avenida da Universidade', 'PT', 'Portugal', 'Car,Truck');


-- Station 1: Atlante
INSERT INTO charging_points (id, station_id, available, brand) VALUES (1, 1, true, 'Atlante');
INSERT INTO charging_points (id, station_id, available, brand) VALUES (2, 1, true, 'Atlante');

-- Station 2: Mobi.E
INSERT INTO charging_points (id, station_id, available, brand) VALUES (3, 2, true, 'Mobi.E');

-- Station 3: Mobi.E
INSERT INTO charging_points (id, station_id, available, brand) VALUES (4, 3, false, 'Mobi.E');
INSERT INTO charging_points (id, station_id, available, brand) VALUES (5, 3, true, 'Mobi.E');

-- Station 4: EDP Comercial
INSERT INTO charging_points (id, station_id, available, brand) VALUES (6, 4, true, 'EDP');

-- Station 5: EDP Comercial
INSERT INTO charging_points (id, station_id, available, brand) VALUES (7, 5, true, 'EDP');
INSERT INTO charging_points (id, station_id, available, brand) VALUES (8, 5, true, 'EDP');

-- Station 6: AVR-90002
INSERT INTO charging_points (id, station_id, available, brand) VALUES (9, 6, true, 'AVR-90002');
INSERT INTO charging_points (id, station_id, available, brand) VALUES (10, 6, false, 'AVR-90002');
INSERT INTO charging_points (id, station_id, available, brand) VALUES (11, 6, true, 'AVR-90002');

-- Station 7: Mobi.E
INSERT INTO charging_points (id, station_id, available, brand) VALUES (12, 7, true, 'Mobi.E');

-- Station 8: Galp Power
INSERT INTO charging_points (id, station_id, available, brand) VALUES (13, 8, true, 'Galp electric');
INSERT INTO charging_points (id, station_id, available, brand) VALUES (14, 8, false, 'Galp electric');

-- Station 9: Powerdot
INSERT INTO charging_points (id, station_id, available, brand) VALUES (15, 9, true, 'Powerdot');

-- Station 10: Atlante
INSERT INTO charging_points (id, station_id, available, brand) VALUES (16, 10, true, 'Atlante');
INSERT INTO charging_points (id, station_id, available, brand) VALUES (17, 10, true, 'Atlante');

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
