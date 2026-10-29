-- ========================================================
-- Smart Canteen Token System - Supabase PostgreSQL Schema
-- Kalasalingam Academy of Research and Education
-- Dept of CSE - Cyber Security
-- ========================================================
-- Run this in your Supabase SQL Editor:
-- https://supabase.com/dashboard/project/wwluaakdogtsozyrecdr/sql/new

-- 1. Drop old/conflicting tables (if any exist) to ensure clean schema
DROP TABLE IF EXISTS tokens CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS food_items CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 2. Create Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT'
);

-- 3. Create Food Items Table (with category, available, image_url)
CREATE TABLE food_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    category VARCHAR(50) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    image_url VARCHAR(255)
);

-- 4. Create Orders Table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    total DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PLACED',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Create Order Items Table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    food_item_id BIGINT NOT NULL REFERENCES food_items(id) ON DELETE CASCADE,
    quantity INT NOT NULL,
    price DOUBLE PRECISION NOT NULL
);

-- 6. Create Tokens Table
CREATE TABLE tokens (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE REFERENCES orders(id) ON DELETE CASCADE,
    token_no VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 7. Seed 12 South Indian Food Items
INSERT INTO food_items (name, price, category, available, image_url) VALUES
('Idli',          30.00,  'Breakfast',  TRUE, '/images/idli.jpg'),
('Masala Dosa',   50.00,  'Breakfast',  TRUE, '/images/masala_dosa.jpg'),
('Pongal',        40.00,  'Breakfast',  TRUE, '/images/pongal.jpg'),
('Veg Biryani',   90.00,  'Meals',      TRUE, '/images/veg_biryani.jpg'),
('Parotta',       35.00,  'Meals',      TRUE, '/images/parotta.jpg'),
('Veg Meals',    100.00,  'Meals',      TRUE, '/images/veg_meals.jpg'),
('Fried Rice',    80.00,  'Meals',      TRUE, '/images/fried_rice.jpg'),
('Samosa',        20.00,  'Snacks',     TRUE, '/images/samosa.jpg'),
('Vada',          25.00,  'Snacks',     TRUE, '/images/vada.jpg'),
('Tea',           15.00,  'Beverages',  TRUE, '/images/tea.jpg'),
('Coffee',        20.00,  'Beverages',  TRUE, '/images/coffee.jpg'),
('Lemon Juice',   25.00,  'Beverages',  TRUE, '/images/lemon_juice.jpg');
