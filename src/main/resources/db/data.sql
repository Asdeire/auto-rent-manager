/*INSERT INTO users (username, email, password, balance)
VALUES ('JohnDoe', 'johnad.doe@example.com', 'securepassword123', 100.00),
       ('JaneDoe', 'janet.doe@example.com', 'anothersecurepassword456', 150.00);

INSERT INTO categories (name, description)
VALUES ('Economy', 'Economy class cars suitable for routine city driving'),
       ('Luxury', 'High-end cars with premium features');

INSERT INTO cars (brand, model, year, category_id, rating, availability)
VALUES ('Toyota', 'Corolla', 2020, (SELECT category_id FROM categories WHERE name = 'Economy'), 4.5, TRUE),
       ('Mercedes', 'S-Class', 2022, (SELECT category_id FROM categories WHERE name = 'Luxury'), 4.8, FALSE);

INSERT INTO rentals (user_id, car_id, start_date, end_date, price)
VALUES ((SELECT user_id FROM users WHERE username = 'JohnDoe'), (SELECT car_id FROM cars WHERE model = 'Corolla'),
        '2021-06-01', '2021-06-10', 500.00);

INSERT INTO reviews (user_id, car_id, rating, comment, review_date)
VALUES ((SELECT user_id FROM users WHERE username = 'JohnDoe'), (SELECT car_id FROM cars WHERE model = 'Corolla'), 4.5,
        'Great experience, will rent again!', '2021-06-11');*/
