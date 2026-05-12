
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(64)   NOT NULL UNIQUE,
    email         VARCHAR(128)  NOT NULL UNIQUE,
    password_hash VARCHAR(255)  NOT NULL,
    role          VARCHAR(16)   NOT NULL DEFAULT 'USER',
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX ix_users_email ON users(email);

CREATE TABLE products (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(150)   NOT NULL,
    description VARCHAR(2000)  NULL,
    price       DECIMAL(12,2)  NOT NULL,
    stock       INT            NOT NULL DEFAULT 0,
    image_url   VARCHAR(500)   NULL,
    created_by  BIGINT         NULL,
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_user FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX ix_products_name ON products(name);

CREATE TABLE reviews (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT        NOT NULL,
    user_id    BIGINT        NOT NULL,
    rating     INT           NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment    VARCHAR(1000) NULL,
    created_at DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_user    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    CONSTRAINT uq_reviews_user_product UNIQUE (user_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX ix_reviews_product ON reviews(product_id);

INSERT INTO users (username, email, password_hash, role) VALUES
('admin', 'admin@example.com',
 '$2a$10$IIl40YEYyn7DNcvoDGANsOd2LYvpOPEzvopGJ0YeA31t4eAbUoHFy', 'ADMIN');

INSERT INTO products (name, description, price, stock, image_url, created_by) VALUES
('Clean Code: A Handbook of Agile Software Craftsmanship',
 'Robert C. Martin''s classic guide to writing clean, maintainable code. Essential reading for every professional developer.',
 34.99, 50, NULL, 1),
('The Pragmatic Programmer: Your Journey to Mastery',
 'Andrew Hunt and David Thomas share practical advice on becoming a better programmer. Covers topics from career development to coding techniques.',
 39.99, 40, NULL, 1),
('Designing Data-Intensive Applications',
 'Martin Kleppmann''s comprehensive guide to building reliable, scalable, and maintainable systems. Covers databases, distributed systems, and data processing.',
 44.99, 30, NULL, 1),
('Introduction to Algorithms (CLRS)',
 'The definitive textbook on algorithms and data structures. Covers sorting, graph algorithms, dynamic programming, and much more.',
 79.99, 20, NULL, 1),
('You Don''t Know JS: Scope & Closures',
 'Kyle Simpson''s deep dive into JavaScript''s core mechanisms. Part of the popular YDKJS series for serious JavaScript developers.',
 24.99, 60, NULL, 1),
('The Linux Command Line',
 'William Shotts'' comprehensive introduction to the Linux command line. Perfect for developers who want to master the terminal.',
 29.99, 45, NULL, 1);


CREATE TABLE IF NOT EXISTS orders (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    product_id   BIGINT         NOT NULL,
    product_name VARCHAR(150)   NOT NULL,
    quantity     INT            NOT NULL DEFAULT 1,
    unit_price   DECIMAL(12,2)  NOT NULL,
    total_price  DECIMAL(12,2)  NOT NULL,
    full_name    VARCHAR(128)   NOT NULL,
    phone        VARCHAR(32)    NOT NULL,
    address      VARCHAR(500)   NOT NULL,
    city         VARCHAR(100)   NOT NULL,
    notes        VARCHAR(500)   NULL,
    status       VARCHAR(32)    NOT NULL DEFAULT 'PENDING',
    created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX ix_orders_user ON orders(user_id);
