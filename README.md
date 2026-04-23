# 🛒 E-Commerce Backend System

A production-style E-commerce backend built using Spring Boot, focusing on real-world backend architecture, security, and business logic.

---

## 🚀 Features

* 🔐 JWT-based Authentication & Role-Based Authorization
* 🛍️ Product Management (Admin)
* 🛒 Cart Management (Add, Update, Remove Items)
* 📦 Order Processing (Cart → Order Lifecycle)
* 💳 Payment System with Failure Handling & Retry Mechanism
* 📄 Swagger API Documentation
* ⚠️ Global Exception Handling
* 📊 Pagination & Validation
* 📝 Logging with SLF4J

---

## 🏗️ Architecture

Controller → Service → Repository → Entity

* Clean layered architecture
* Business logic handled in service layer
* DTOs used for request/response separation

---

## 🛠️ Tech Stack

* Language: Java
* Framework: Spring Boot
* Security: Spring Security, JWT
* Database: PostgreSQL
* ORM: Hibernate (JPA)
* API Docs: Swagger (OpenAPI)
* Testing: JUnit, Mockito
* Build Tool: Maven

---

## 🔐 Authentication

* Uses JWT for stateless authentication
* Role-based access:

  * USER → Access cart, orders
  * ADMIN → Manage products, orders

---

## 🔄 Order & Payment Flow

1. User adds items to cart
2. User places order
3. System:

   * Validates stock
   * Deducts product quantity
4. Payment initiated
5. Handles:

   * ✅ Success → Order confirmed
   * ❌ Failure → Retry mechanism
6. Cart is cleared after successful order

---

## ⚙️ API Endpoints (Sample)

### Auth

POST /satoru/users/register

POST /satoru/users/login

GET  /satoru/users/profile

### Products

GET    /Gojo/products

GET    /Gojo/products/{id}

POST   /Gojo/products        (ADMIN)

PUT    /Gojo/products/{id}   (ADMIN)

DELETE /Gojo/products/{id}   (ADMIN)

### Cart

POST   /api/cart/items

PUT    /api/cart/items

DELETE /api/cart/items/{productId}

GET    /api/cart

DELETE /api/cart/clear

### Orders

POST   /Gojo/orders

GET    /Gojo/orders

GET    /Gojo/orders/{id}

POST   /Gojo/orders/{id}/cancel

GET    /Gojo/orders/admin              (ADMIN)

PUT    /Gojo/orders/admin/{id}/status  (ADMIN)

GET    /Gojo/orders/admin/user/{userId} (ADMIN)

### Payments

POST /Gojo/user/order/payments/{paymentId}/process

POST /Gojo/user/order/payments/retry/{orderId}

POST /Gojo/user/order/payments/webhook

---

## 📄 Swagger UI

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## ▶️ How to Run

### 1. Clone the repository

git clone [https://github.com/Shivnaruka01/e-commerce-](https://github.com/Shivnaruka01/e-commerce-)

### 2. Configure Database

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password

### 3. Run the application

mvn spring-boot:run

---

## 🧪 Testing

* Unit testing using JUnit & Mockito
* Focused on service-layer logic

---

## 📌 Future Improvements

* Deploy application (AWS / Render)
* Add caching (Redis)
* Add order tracking system
* Integrate real payment gateway

---

## 👨‍💻 Author

Shiv Singh Naruka

GitHub: [https://github.com/Shivnaruka01](https://github.com/Shivnaruka01)

LinkedIn: [https://linkedin.com/in/shivsinghnaruka](https://linkedin.com/in/shivsinghnaruka)

---

## ⭐ If you like this project, consider giving it a star!