INSERT INTO users (user_id, username, email, password, balance)
VALUES
    ('1be86e05-7a94-4d67-b33a-36f8f7b3d0dc', 'JohnDoe', 'john.doe@example.com', 'password123', 100.00),
    ('b58f7c69-4bf4-4cb2-a50c-35b02f5c15fb', 'JaneDoe', 'jane.doe@example.com', 'anothersecurepassword456', 150.00);

INSERT INTO categories (category_id, name, description)
VALUES
    ('a9258479-b33c-4f30-8e57-0aeb3a331ae7', 'Economy', 'Economy class cars suitable for routine city driving'),
    ('8e672a65-bb9b-4743-b40a-54b81d0ff189', 'Luxury', 'High-end cars with premium features');

INSERT INTO cars (car_id, brand, model, year, category_id, rating, availability)
VALUES
    ('4e251b14-79a3-4650-bc4c-ae14d9d78eb7', 'Toyota', 'Corolla', 2020, 'a9258479-b33c-4f30-8e57-0aeb3a331ae7', 4.5, TRUE),
    ('6e738a3c-6d45-4d53-94b7-9f755e78b628', 'Mercedes', 'S-Class', 2022, '8e672a65-bb9b-4743-b40a-54b81d0ff189', 4.8, FALSE);

INSERT INTO rentals (rental_id, user_id, car_id, start_date, end_date, price)
VALUES
    ('9d847ce5-7e99-4e61-b012-3bde3f4d637b', '1be86e05-7a94-4d67-b33a-36f8f7b3d0dc', '4e251b14-79a3-4650-bc4c-ae14d9d78eb7', '2021-06-01', '2021-06-10', 500.00);

INSERT INTO reviews (user_id, car_id, rating, comment, review_date)
VALUES
    ('1be86e05-7a94-4d67-b33a-36f8f7b3d0dc', '4e251b14-79a3-4650-bc4c-ae14d9d78eb7', 4.5, 'Great experience, will rent again!', '2021-06-11');
