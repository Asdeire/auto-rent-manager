INSERT INTO users (user_id, username, email, password, balance)
VALUES ('1be86e05-7a94-4d67-b33a-36f8f7b3d0dc', 'Userok', 'john.doe@example.com', '$2b$10$KpOVZGykfId5RuUDij6aQu9dW.m2VkKL86uKtzWsI20EYZrOreP9i', 100.00),
       ('b58f7c69-4bf4-4cb2-a50c-35b02f5c15fb', 'JaneDoe', 'jane.doe@example.com', '$2b$10$KpOVZGykfId5RuUDij6aQu9dW.m2VkKL86uKtzWsI20EYZrOreP9i', 150.00);

INSERT INTO categories (category_id, name, description)
VALUES ('a9258479-b33c-4f30-8e57-0aeb3a331ae7', 'Economy', 'Economy class cars suitable for routine city driving'),
       ('8e672a65-bb9b-4743-b40a-54b81d0ff189', 'Luxury', 'High-end cars with premium features'),
       ('ce5fb37b-70f6-4892-97b7-79837aaf7d11', 'SUV', 'Sport Utility Vehicles suitable for off-road driving and family trips');

INSERT INTO cars (car_id, brand, model, year, category_id, rating, availability, price)
VALUES
    ('4e251b14-79a3-4650-bc4c-ae14d9d78eb7', 'Toyota', 'Corolla', 2020, 'a9258479-b33c-4f30-8e57-0aeb3a331ae7', 4.5, TRUE, 100.00),
    ('6e738a3c-6d45-4d53-94b7-9f755e78b628', 'Mercedes', 'S-Class', 2022, '8e672a65-bb9b-4743-b40a-54b81d0ff189', 4.8, FALSE, 200.00),
    ('b722e4f4-bd7f-4b71-a105-10c35b137ef0', 'Jeep', 'Grand Cherokee', 2021, 'ce5fb37b-70f6-4892-97b7-79837aaf7d11', 4.3, TRUE, 150.00),
    ('0f83b89b-b0b5-4994-805a-739db6b47d80', 'Ford', 'Explorer', 2022, 'ce5fb37b-70f6-4892-97b7-79837aaf7d11', 4.1, TRUE, 180.00),
    ('e99b2202-3f0d-4dd3-a607-97d7c6e17159', 'Chevrolet', 'Tahoe', 2021, 'ce5fb37b-70f6-4892-97b7-79837aaf7d11', 4.4, TRUE, 170.00),
    ('53ac92a3-04a9-4a41-ae1f-d447e9c24c48', 'BMW', 'X5', 2023, 'ce5fb37b-70f6-4892-97b7-79837aaf7d11', 4.6, TRUE, 220.00),
    ('0dcff7fd-180f-4b8f-8909-4b614aaf07cf', 'Audi', 'Q7', 2022, 'ce5fb37b-70f6-4892-97b7-79837aaf7d11', 4.7, TRUE, 210.00),
    ('e4fd97f1-c62f-4847-bc2a-b6fd164a2a0a', 'Land Rover', 'Range Rover', 2021, '8e672a65-bb9b-4743-b40a-54b81d0ff189', 4.8, TRUE, 250.00),
    ('166cf6a2-74d0-41a3-b7a9-129d6462b15f', 'Porsche', 'Cayenne', 2023, 'ce5fb37b-70f6-4892-97b7-79837aaf7d11', 4.9, TRUE, 230.00),
    ('c11f0e7a-99f7-4e85-bf76-b93797cb924e', 'Tesla', 'Model X', 2022, 'ce5fb37b-70f6-4892-97b7-79837aaf7d11', 4.5, TRUE, 190.00),
    ('f0d8245f-b824-4e27-b53d-146bf47c1b8d', 'Hyundai', 'Elantra', 2021, 'a9258479-b33c-4f30-8e57-0aeb3a331ae7', 4.2, TRUE, 120.00),
    ('57d18cf2-7004-42d1-967b-15fb5148cc9d', 'Nissan', 'Sentra', 2022, 'a9258479-b33c-4f30-8e57-0aeb3a331ae7', 4.1, TRUE, 110.00),
    ('e0b2b2f1-0f64-4128-aa09-9d5418d06b1b', 'Honda', 'Civic', 2023, 'a9258479-b33c-4f30-8e57-0aeb3a331ae7', 4.3, TRUE, 130.00),
    ('57f5241d-850f-40ad-a1ec-df856f7792bb', 'BMW', '3 Series', 2022, '8e672a65-bb9b-4743-b40a-54b81d0ff189', 4.7, TRUE, 200.00),
    ('f60d85a9-47d9-4904-9f3f-87ae7d409ea1', 'Audi', 'A6', 2023, '8e672a65-bb9b-4743-b40a-54b81d0ff189', 4.8, TRUE, 220.00),
    ('09d22864-3c36-4c3c-820a-ba5a145672b2', 'Mercedes', 'E-Class', 2022, '8e672a65-bb9b-4743-b40a-54b81d0ff189', 4.9, TRUE, 240.00),
    ('303e3c45-92ab-45a5-abe0-61eb41d18c1d', 'Volvo', 'XC90', 2023, 'ce5fb37b-70f6-4892-97b7-79837aaf7d11', 4.3, TRUE, 200.00);


INSERT INTO rentals (rental_id, user_id, car_id, start_date, end_date, price)
VALUES ('9d847ce5-7e99-4e61-b012-3bde3f4d637b', '1be86e05-7a94-4d67-b33a-36f8f7b3d0dc',
        '4e251b14-79a3-4650-bc4c-ae14d9d78eb7', '2021-06-01', '2021-06-10', 500.00);

INSERT INTO reviews (review_id ,user_id, car_id, rating, comment, review_date)
VALUES ('57f5241d-850f-40ad-a1ec-df856f7792bb' ,'1be86e05-7a94-4d67-b33a-36f8f7b3d0dc', '4e251b14-79a3-4650-bc4c-ae14d9d78eb7', 4.5,
        'Great experience, will rent again!', '2021-06-11');
