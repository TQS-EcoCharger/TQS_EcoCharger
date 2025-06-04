CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    user_type VARCHAR(50),
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    enabled BOOLEAN NOT NULL
);


-- CHARGING_STATIONS
CREATE TABLE IF NOT EXISTS charging_stations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    municipality VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    streetname VARCHAR(255) NOT NULL,
    countrycode VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    vehicletype VARCHAR(255) NOT NULL
);


-- CHARGING_POINTS
CREATE TABLE IF NOT EXISTS charging_points (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    station_id BIGINT NOT NULL,
    available BOOLEAN NOT NULL,
    brand VARCHAR(255) NOT NULL,
    price_per_kwh DOUBLE,
    price_per_minute DOUBLE,
    CONSTRAINT fk_cp_station FOREIGN KEY (station_id) REFERENCES charging_stations(id)
);

-- CONNECTORS
CREATE TABLE IF NOT EXISTS connectors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    connector_type VARCHAR(255) NOT NULL,
    rated_power_kw INT NOT NULL,
    voltage_v INT,
    current_a INT,
    current_type VARCHAR(255) NOT NULL,
    charging_point_id BIGINT,
    CONSTRAINT fk_connector_cp FOREIGN KEY (charging_point_id) REFERENCES charging_points(id)
);

-- RESERVATIONS
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    charging_point_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_res_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_res_cp FOREIGN KEY (charging_point_id) REFERENCES charging_points(id)
);

CREATE TABLE IF NOT EXISTS driver (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS car (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    manufacture_year INT NOT NULL,
    license_plate VARCHAR(255) NOT NULL,
    battery_capacity DOUBLE NOT NULL,
    battery_level DOUBLE NOT NULL,
    kilometers DOUBLE NOT NULL,
    consumption DOUBLE NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS driver_cars (
    driver_id BIGINT NOT NULL,
    cars_id BIGINT NOT NULL,
    PRIMARY KEY (driver_id, cars_id),
    FOREIGN KEY (driver_id) REFERENCES driver(id) ON DELETE CASCADE,
    FOREIGN KEY (cars_id) REFERENCES car(id) ON DELETE CASCADE
);



-- CHARGING_STATIONS
CREATE TABLE IF NOT EXISTS charging_stations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    municipality VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    streetname VARCHAR(255) NOT NULL,
    countrycode VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    vehicletype VARCHAR(255) NOT NULL
);


-- CHARGING_POINTS
CREATE TABLE IF NOT EXISTS charging_points (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    station_id BIGINT NOT NULL,
    available BOOLEAN NOT NULL,
    brand VARCHAR(255) NOT NULL,
    price_per_kwh DOUBLE,
    price_per_minute DOUBLE,
    CONSTRAINT fk_cp_station FOREIGN KEY (station_id) REFERENCES charging_stations(id)
);

-- CONNECTORS
CREATE TABLE IF NOT EXISTS connectors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    connector_type VARCHAR(255) NOT NULL,
    rated_power_kw INT NOT NULL,
    voltage_v INT,
    current_a INT,
    current_type VARCHAR(255) NOT NULL,
    charging_point_id BIGINT,
    CONSTRAINT fk_connector_cp FOREIGN KEY (charging_point_id) REFERENCES charging_points(id)
);

-- RESERVATIONS
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    charging_point_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_res_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_res_cp FOREIGN KEY (charging_point_id) REFERENCES charging_points(id)
);

CREATE TABLE IF NOT EXISTS driver (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS car (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    manufacture_year INT NOT NULL,
    license_plate VARCHAR(255) NOT NULL,
    battery_capacity DOUBLE NOT NULL,
    battery_level DOUBLE NOT NULL,
    kilometers DOUBLE NOT NULL,
    consumption DOUBLE NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS driver_cars (
    driver_id BIGINT NOT NULL,
    cars_id BIGINT NOT NULL,
    PRIMARY KEY (driver_id, cars_id),
    FOREIGN KEY (driver_id) REFERENCES driver(id) ON DELETE CASCADE,
    FOREIGN KEY (cars_id) REFERENCES car(id) ON DELETE CASCADE
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