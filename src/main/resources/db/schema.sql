/*DROP TABLE IF EXISTS rentals;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS categories;*/


CREATE TABLE IF NOT EXISTS users (
    user_id uuid PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) DEFAULT 0.00
    );

CREATE TABLE IF NOT EXISTS categories (
    category_id uuid PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT
    );

CREATE TABLE IF NOT EXISTS cars (
    car_id uuid PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    category_id uuid,
    rating DECIMAL(3, 2) DEFAULT NULL,
    availability BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    price DECIMAL(5, 2)
    );

CREATE TABLE IF NOT EXISTS rentals (
    rental_id uuid PRIMARY KEY,
    user_id uuid NOT NULL,
    car_id uuid NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (car_id) REFERENCES cars(car_id)
    );

CREATE TABLE IF NOT EXISTS reviews (
    review_id SERIAL PRIMARY KEY,
    user_id uuid,
    car_id uuid,
    rating DECIMAL(3, 2),
    comment TEXT,
    review_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (car_id) REFERENCES cars(car_id)
    );
