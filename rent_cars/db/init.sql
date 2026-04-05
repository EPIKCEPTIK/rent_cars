
CREATE TABLE car_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    base_rate NUMERIC(10, 2) NOT NULL CHECK (base_rate > 0),
    description TEXT
);

CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    category_id INTEGER NOT NULL REFERENCES car_categories(id),
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    plate_no VARCHAR(15) UNIQUE NOT NULL,
    year INTEGER NOT NULL CHECK (year > 1900),
    status VARCHAR(20) DEFAULT 'available'
);

CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    license_no VARCHAR(20) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(50),
    address TEXT
);

CREATE TABLE rentals (
    id SERIAL PRIMARY KEY,
    car_id INTEGER NOT NULL REFERENCES cars(id) ON DELETE CASCADE,
    client_id INTEGER NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL CHECK (end_date > start_date),
    total_price NUMERIC(10, 2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'active'
);

CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    rental_id INTEGER NOT NULL REFERENCES rentals(id) ON DELETE CASCADE,
    amount NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    pay_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    method VARCHAR(20) NOT NULL
);

CREATE TABLE maintenance (
    id SERIAL PRIMARY KEY,
    car_id INTEGER NOT NULL REFERENCES cars(id) ON DELETE CASCADE,
    serv_date DATE NOT NULL,
    type VARCHAR(50) NOT NULL,
    cost NUMERIC(10, 2) DEFAULT 0 CHECK (cost >= 0),
    description TEXT
);



INSERT INTO car_categories (name, base_rate, description) VALUES
('Економ', 800.00, 'Малолітражні авто для міста'),
('Бізнес', 2200.00, 'Комфортні седани'),
('Позашляховик', 3500.00, 'Повнопривідні авто для бездоріжжя');

INSERT INTO cars (category_id, brand, model, plate_no, year, status) VALUES
(1, 'Skoda', 'Fabia', 'AA1111BB', 2021, 'available'),
(2, 'Toyota', 'Camry', 'AA2222BB', 2022, 'available'),
(3, 'Mitsubishi', 'L200', 'AA3333BB', 2020, 'available');

INSERT INTO clients (full_name, license_no, phone, email) VALUES
('Петренко Олександр', 'BXX000111', '+380501234567', 'petrenko@gmail.com'),
('Сидоренко Марія', 'BXX000222', '+380679876543', 'mariya.s@ukr.net');

INSERT INTO rentals (car_id, client_id, start_date, end_date, total_price, status) VALUES
(1, 1, '2023-12-01 10:00:00', '2023-12-05 10:00:00', 3200.00, 'closed');

INSERT INTO payments (rental_id, amount, method) VALUES
(1, 3200.00, 'card');

INSERT INTO maintenance (car_id, serv_date, type, cost, description) VALUES
(2, '2023-11-20', 'ТО-1', 4500.00, 'Заміна мастила та фільтрів');