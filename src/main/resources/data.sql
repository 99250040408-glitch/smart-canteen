-- ================================================
-- Smart Canteen Token System - Seed Data
-- ================================================
-- This file is for reference / manual seeding.
-- The application seeds data automatically on first run
-- via DataInitializer.java (with proper BCrypt hashes).
--
-- Demo Logins:
--   Admin:   admin@kare.edu   / admin123
--   Student: student@kare.edu / student123

USE smart_canteen;

-- Users (passwords are BCrypt hashed by the application)
-- INSERT INTO users (name, email, password_hash, role) VALUES
-- ('Admin', 'admin@kare.edu', '<bcrypt_hash_of_admin123>', 'ADMIN'),
-- ('Rahul Kumar', 'student@kare.edu', '<bcrypt_hash_of_student123>', 'STUDENT');

-- Food Items
INSERT IGNORE INTO food_items (id, name, price, category, available, image_url) VALUES
(1,  'Idli',          30.00,  'Breakfast',  TRUE, '/images/idli.jpg'),
(2,  'Masala Dosa',    50.00,  'Breakfast',  TRUE, '/images/masala_dosa.jpg'),
(3,  'Pongal',         40.00,  'Breakfast',  TRUE, '/images/pongal.jpg'),
(4,  'Veg Biryani',    90.00,  'Meals',      TRUE, '/images/veg_biryani.jpg'),
(5,  'Parotta',        35.00,  'Meals',      TRUE, '/images/parotta.jpg'),
(6,  'Veg Meals',     100.00,  'Meals',      TRUE, '/images/veg_meals.jpg'),
(7,  'Fried Rice',     80.00,  'Meals',      TRUE, '/images/fried_rice.jpg'),
(8,  'Samosa',         20.00,  'Snacks',     TRUE, '/images/samosa.jpg'),
(9,  'Vada',           25.00,  'Snacks',     TRUE, '/images/vada.jpg'),
(10, 'Tea',            15.00,  'Beverages',  TRUE, '/images/tea.jpg'),
(11, 'Coffee',         20.00,  'Beverages',  TRUE, '/images/coffee.jpg'),
(12, 'Lemon Juice',    25.00,  'Beverages',  TRUE, '/images/lemon_juice.jpg');
