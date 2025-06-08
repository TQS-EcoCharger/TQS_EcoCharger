-- USERS
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    user_type VARCHAR(50),
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    enabled BOOLEAN NOT NULL
);

-- ADMINISTRATORS, CLIENTS, DRIVERS (JPA inheritance)
CREATE TABLE IF NOT EXISTS administrators (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS drivers (
    id BIGINT PRIMARY KEY,
    balance DOUBLE PRECISION DEFAULT 0,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- CHARGING_STATIONS
CREATE TABLE IF NOT EXISTS charging_stations (
    id BIGSERIAL PRIMARY KEY,
    municipality VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    countrycode VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    administrator_id BIGINT,
    CONSTRAINT fk_station_admin FOREIGN KEY (administrator_id) REFERENCES administrators(id)
);

-- CHARGING_POINTS
CREATE TABLE IF NOT EXISTS charging_points (
    id BIGSERIAL PRIMARY KEY,
    station_id BIGINT NOT NULL,
    available BOOLEAN NOT NULL,
    brand VARCHAR(255) NOT NULL,
    price_per_kwh DOUBLE,
    price_per_minute DOUBLE,
    charging_rate_kwh_per_minute DOUBLE NOT NULL,
    CONSTRAINT fk_cp_station FOREIGN KEY (station_id) REFERENCES charging_stations(id)
);


-- CONNECTORS
CREATE TABLE IF NOT EXISTS connectors (
    id BIGSERIAL PRIMARY KEY,
    connector_type VARCHAR(255) NOT NULL,
    rated_power_kw INT NOT NULL,
    voltage_v INT,
    current_a INT,
    current_type VARCHAR(255) NOT NULL,
    charging_point_id BIGINT,
    FOREIGN KEY (charging_point_id) REFERENCES charging_points(id)
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
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    manufacture_year INT NOT NULL,
    license_plate VARCHAR(255) NOT NULL,
    battery_capacity DOUBLE PRECISION NOT NULL,
    battery_level DOUBLE PRECISION NOT NULL,
    kilometers DOUBLE PRECISION NOT NULL,
    consumption DOUBLE PRECISION NOT NULL,
    enabled BOOLEAN NOT NULL
);

-- DRIVER <-> CARS
CREATE TABLE IF NOT EXISTS driver_cars (
    driver_id BIGINT NOT NULL,
    cars_id BIGINT NOT NULL,
    PRIMARY KEY (driver_id, cars_id),
    FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE CASCADE,
    FOREIGN KEY (cars_id) REFERENCES car(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS otp_code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    reservation_id BIGINT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

CREATE TABLE IF NOT EXISTS charging_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT,
    user_id BIGINT NOT NULL,
    charging_point_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    duration_minutes BIGINT,
    total_cost DOUBLE,
    status VARCHAR(50) NOT NULL,
    initial_battery_level DOUBLE NOT NULL,
    energy_delivered DOUBLE,
    payment_intent_id VARCHAR(255),
    FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (charging_point_id) REFERENCES charging_points(id),
    FOREIGN KEY (car_id) REFERENCES car(id)
);

CREATE TABLE IF NOT EXISTS charging_operator (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS charging_operator_stations (
    operator_id BIGINT NOT NULL,
    stations_id BIGINT NOT NULL,
    PRIMARY KEY (operator_id, stations_id),
    CONSTRAINT fk_operator_user FOREIGN KEY (operator_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_station FOREIGN KEY (stations_id) REFERENCES charging_stations(id) ON DELETE CASCADE
);


-- USERS
INSERT INTO users (email, password, name, enabled, user_type)
VALUES
('john@example.com', '123456', 'John Doe', true, 'drivers'),
('alice@example.com', 'alicepass', 'Alice', true, 'drivers'),
('bob@example.com', 'bobpass', 'Bob', false, 'drivers');


INSERT INTO drivers (id)
SELECT id FROM users WHERE email IN ('john@example.com', 'alice@example.com', 'bob@example.com');
