# CodeShelf

CodeShelf is a full-stack e-commerce web application for selling programming and technology books.
The project was built using Java EE (Jakarta EE) with a classic MVC architecture and focuses heavily on backend development, security, and clean project structure.

The application allows users to browse books, create accounts, place orders, and write reviews, while admins can manage products, users, and customer orders through a dedicated dashboard.

## Tech Stack

Backend:

* Java EE / Jakarta EE
* Servlets
* JSP + JSTL
* Apache Tomcat
* Maven

Database & Security:

* MySQL
* HikariCP
* Redis
* JWT Authentication
* BCrypt Password Hashing

Libraries:

* Jackson / Gson
* SLF4J
* Jedis

---

## Features

### Authentication & Authorization

* User signup and login
* BCrypt password hashing
* Session-based authentication
* JWT authentication for REST APIs
* Role-based access control (USER / ADMIN)

### Product Management

* Browse all books
* Product search and filtering
* Product details page
* Admin CRUD operations for books
* Stock management

### Orders

* Place orders with delivery details
* Order confirmation page
* User order history
* Admin order management

### Reviews

* Add reviews and ratings
* Product rating summaries
* Recent reviews section

### Performance & Security

* Redis caching for products
* Rate limiting using Redis
* PreparedStatement for SQL injection protection
* HTTP-only cookies
* XSS protection using JSTL escaping

### REST APIs

* Authentication API
* Product API
* Review API

---

## Project Structure

The project follows a layered MVC architecture:

* View Layer (JSP)
* Controller Layer (Servlets)
* Service Layer
* DAO Layer
* Model Layer

Request Flow:

Browser
→ Filters
→ Servlet Controller
→ Service Layer
→ DAO Layer
→ MySQL Database

---

## Main Components

### Controllers

Servlets handle incoming HTTP requests and forward responses to JSP pages.

Examples:

* LoginServlet
* SignupServlet
* ProductServlet
* OrderServlet
* AdminServlet

### Services

Contains business logic and validation.

Examples:

* AuthService
* ProductService
* ReviewService
* RateLimiter

### DAO Layer

Responsible for all SQL operations.

Examples:

* UserDAO
* ProductDAO
* OrderDAO
* ReviewDAO

### Utilities

Helper classes used across the application.

Examples:

* DBUtil
* RedisUtil
* JwtUtil
* PasswordUtil
* JsonUtil

---

## Redis Usage

Redis is used for:

* Product caching
* Rate limiting

Product lists are cached for 120 seconds to reduce database load and improve response time.

The rate limiter uses a sliding window algorithm with a default limit of 60 requests per minute per IP.

---

## Security

The project includes several security layers:

* BCrypt password hashing
* JWT token validation
* Session authentication
* Rate limiting
* Role-based authorization
* SQL injection prevention
* XSS protection
* HTTP-only cookies

---

## Database

Main entities:

* Users
* Products
* Orders
* Reviews

MySQL is used as the primary relational database.

HikariCP is used for efficient database connection pooling.

---

## Setup

### Requirements

* Java 17+
* Maven
* MySQL 8
* Redis
* Apache Tomcat 10

### Configuration

Edit `application.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/ecommerce
db.user=root
db.password=1234

redis.host=localhost
redis.port=6379

jwt.secret=your-secret
jwt.expiry.minutes=60
```

### Run the Project

```bash
mvn clean package
```

Deploy the generated WAR file to Tomcat.

---

## What I Focused On

The main focus of this project was:

* Building a clean backend architecture
* Applying enterprise Java concepts
* Improving scalability using caching and pooling
* Implementing real security practices
* Separating responsibilities between layers

---

## Future Improvements

Possible future upgrades:

* Payment gateway integration
* Docker deployment
* Elasticsearch search
* Email notifications
* Recommendation system
* React frontend

