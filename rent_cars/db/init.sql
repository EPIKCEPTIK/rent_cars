
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
     email VARCHAR(50) UNIQUE,
     address TEXT,
     password VARCHAR(255) NOT NULL,
     role VARCHAR(20) DEFAULT 'USER'
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

 -- Заповнення даними:

 INSERT INTO car_categories (name, base_rate, description) VALUES
 ('Економ', 800.00, 'Малолітражні авто для міста'),
 ('Бізнес', 2200.00, 'Комфортні седани'),
 ('Позашляховик', 3500.00, 'Повнопривідні авто для бездоріжжя'),
 ('Premium', 5000.00, 'Люксові седани для VIP клієнтів'),
 ('Electro', 1500.00, 'Екологічні авто для міста');

 INSERT INTO cars (category_id, brand, model, plate_no, year, status) VALUES
 (1, 'Skoda', 'Fabia', 'KA1111AA', 2021, 'available'),
 (1, 'Volkswagen', 'Polo', 'KA2222BB', 2020, 'available'),
 (2, 'Toyota', 'Corolla', 'KA3333CC', 2022, 'available'),
 (2, 'Hyundai', 'Elantra', 'KA4444DD', 2023, 'available'),
 (3, 'Toyota', 'Camry', 'KA5555EE', 2023, 'available'),
 (3, 'BMW', '530i', 'KA6666FF', 2022, 'available'),
 (4, 'Mitsubishi', 'Pajero Sport', 'KA7777GG', 2021, 'available'),
 (4, 'Toyota', 'Land Cruiser Prado', 'KA8888HH', 2022, 'available'),
 (5, 'Tesla', 'Model 3', 'KA9999II', 2023, 'rented');

 INSERT INTO clients (full_name, license_no, phone, email, password, role) VALUES
 ('Адмінчик Віталік', 'ADMIN2005', '+380671111111', 'admin@rent.com', '$2a$10$kypbnGGCpJ7UQlysnqzJG.6H.dUewn7UPVWA3Ip.E.8U4jlVnFNnu', 'ADMIN'),
 ('Степан Бандера', 'STEPAN2345', '+380501234567', 'stepan@gmail.com', '$2a$10$kypbnGGCpJ7UQlysnqzJG.6H.dUewn7UPVWA3Ip.E.8U4jlVnFNnu', 'USER'),
 ('Володимир Зеленьский', 'POTUZHNO', '+380639876543', 'maria@gmail.com', '$2a$10$kypbnGGCpJ7UQlysnqzJG.6H.dUewn7UPVWA3Ip.E.8U4jlVnFNnu', 'USER');

 INSERT INTO rentals (car_id, client_id, start_date, end_date, total_price, status) VALUES
 (9, 3, '2023-12-01 09:00:00', '2023-12-10 09:00:00', 25000.00, 'paid');

 INSERT INTO payments (rental_id, amount, method) VALUES
 (1, 25000.00, 'card');

 INSERT INTO maintenance (car_id, serv_date, type, cost, description) VALUES
 (1, '2023-11-15', 'ТО-1', 4500.00, 'Заміна мастила та всіх фільтрів');