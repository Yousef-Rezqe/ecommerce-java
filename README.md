# 🛒 E-Commerce Web Application

A full-featured e-commerce platform built with **Java Servlets**, **JSP**, and **MySQL**, deployed on **Apache Tomcat**. The application covers the complete shopping experience — from product browsing and user authentication to order management and product reviews — with a RESTful API layer and Redis-based caching.

---

## 📌 Features

- **User Authentication** — Signup, login, logout with session management and JWT token support
- **Product Catalog** — Browse and search products with a dedicated shop view
- **Product Reviews** — Authenticated users can submit and view product ratings and reviews
- **Order Management** — Place orders and view order confirmations
- **Admin Panel** — Manage products, orders, and users from a dedicated admin interface
- **REST API** — JSON API endpoints for products, reviews, and authentication
- **Rate Limiting** — Per-IP request throttling via Redis to prevent abuse
- **Security** — BCrypt password hashing, HTTP-only session cookies, JWT for API auth
- **Connection Pooling** — HikariCP for efficient database connection management
- **Caching** — Redis-backed product caching with configurable TTL

---

## 🏗️ Architecture

```
src/
├── controller/          # Servlet controllers (MVC pattern)
│   ├── HomeServlet
│   ├── ShopServlet
│   ├── ProductServlet
│   ├── LoginServlet / SignupServlet / LogoutServlet
│   ├── OrderServlet / OrderConfirmServlet
│   ├── ReviewServlet
│   ├── AccountServlet / DeleteAccountServlet
│   ├── AdminServlet
│   ├── ContactServlet
│   └── api/             # REST API endpoints
│       ├── AuthApiServlet
│       ├── ProductApiServlet
│       └── ReviewApiServlet
├── model/               # Domain models
│   ├── User
│   ├── Product
│   ├── Order
│   └── Review
├── dao/                 # Data Access Layer (DAO pattern)
│   ├── UserDAO
│   ├── ProductDAO
│   ├── OrderDAO
│   └── ReviewDAO
├── service/             # Business logic layer
│   ├── AuthService
│   ├── ProductService
│   ├── ReviewService
│   └── RateLimiter
├── filter/              # Servlet filters
│   ├── AuthFilter
│   └── RateLimitFilter
├── util/                # Utility classes
│   ├── DBUtil
│   ├── JwtUtil
│   ├── RedisUtil
│   ├── PasswordUtil
│   └── JsonUtil
└── config/
    └── AppConfig
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Web Framework | Jakarta Servlet 6.0 / JSP / JSTL 3.0 |
| Server | Apache Tomcat |
| Database | MySQL 8.x |
| Cache | Redis (via Jedis 5.1.2) |
| Connection Pool | HikariCP 5.1.0 |
| Password Hashing | jBCrypt 0.4 |
| Authentication | JWT (jjwt 0.12.5) |
| JSON | Jackson 2.17 / Gson 2.10 |
| Logging | SLF4J 2.0 |
| Build Tool | Maven |
| Packaging | WAR (deployed as ROOT.war) |

---

## ⚙️ Configuration

All settings are in `WEB-INF/classes/application.properties`:

```properties
# Database
db.url=jdbc:mysql://localhost:3306/ecommerce?useSSL=false&serverTimezone=UTC
db.user=root
db.password=your_password
db.pool.size=10

# Redis
redis.host=localhost
redis.port=6379
redis.password=

# JWT
jwt.secret=change-me-to-a-long-random-secret-of-at-least-32-bytes
jwt.expiry.minutes=60

# Caching
cache.products.ttl.seconds=120

# Rate Limiting
ratelimit.requests=60
ratelimit.window.seconds=60
```
## 🚀 Getting Started

### Prerequisites

- Java 17+
- Apache Tomcat 10+
- MySQL 8.x
- Redis (optional, for caching and rate limiting)
- Maven 3.x


## 🔐 Security Notes

- Passwords are hashed using **BCrypt** — plain-text passwords are never stored.
- Sessions use **HTTP-only cookies** to mitigate XSS risks.
- API endpoints are protected with **JWT Bearer tokens**.
- Rate limiting is enforced per IP using a sliding window algorithm backed by Redis.

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Authenticate and receive JWT |
| POST | `/api/auth/signup` | Register a new user |
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/reviews/{productId}` | Get reviews for a product |
| POST | `/api/reviews` | Submit a review (JWT required) |

-
