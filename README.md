# 🛒 eCommerce Backend API

![Java 25](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![Spring Boot 3.4](https://img.shields.io/badge/Spring_Boot-3.4-brightgreen?style=for-the-badge&logo=springboot)
![AWS](https://img.shields.io/badge/Deployed-AWS-FF9900?style=for-the-badge&logo=amazon-aws)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue?style=for-the-badge&logo=docker)

A production-ready, containerized eCommerce RESTful API built with Java 25 and Spring Boot 3.4. This system serves as a scalable backbone for digital marketplaces, featuring stateless JWT authentication and a robust relational data model.

---

## 🛠 Tech Stack

* **Language:** Java 25 (Latest LTS version for modern feature support)
* **Framework:** Spring Boot 3.4 (Core web server and auto-configuration)
* **Database:** MySQL via Amazon RDS (Managed relational data storage)
* **ORM:** Hibernate / Spring Data JPA (Entity mapping and transaction management)
* **Security:** Spring Security + JWT (Stateless authentication and RBAC)
* **Documentation:** SpringDoc / Swagger UI (Interactive API documentation)
* **Infrastructure:** Docker (Containerization) and AWS EC2 (Cloud hosting)
* **Build Tool:** Maven (Dependency management)

---

## 🏗 System Architecture

The application follows a **layered architecture** pattern to ensure a clean separation of concerns:
1. **Controller Layer:** Handles HTTP requests and maps them to specific endpoints.
2. **Service Layer:** Contains the core business logic and processes data.
3. **Repository Layer:** Manages database interactions using Spring Data JPA.
4. **Security Layer:** Intercepts requests for JWT validation and role-based access control.

---

## 🔐 Security & Data Handling

### Stateless Authentication
The system implements **Spring Security 7** with a custom **JWT (JSON Web Token)** filter. This ensures that the API remains stateless and secure, requiring a valid bearer token for protected endpoints.

### Professional Data Layering
To maintain a clean separation between the database and the API response, the project utilizes:
* **DTOs (Data Transfer Objects):** Ensures only necessary data is transferred, reducing payload size and hiding sensitive entity fields.
* **Mappers:** Automated mapping logic between Entities and DTOs to keep the Service layer clean and maintainable.
* **Global Error Handling:** A centralized controller advice that returns standardized JSON error responses for validation and business logic failures.

---

## ☁️ Cloud Infrastructure (AWS)

The project is fully containerized and deployed within the Amazon Web Services ecosystem to ensure high availability and scalability:

* **Compute:** Hosted on an **AWS EC2** instance running a Dockerized environment.
* **Database:** Utilizes **Amazon RDS (MySQL)** for managed, reliable, and secure data persistence.
* **DevOps:** Employs multi-stage **Docker** builds to create lightweight, production-ready images.
* **Network:** Configured with specific Security Groups to allow secure HTTP traffic to the API gateway.

---

## 📖 Live API Documentation

The API is publicly accessible and fully documented. You can interact with the endpoints in real-time through the Swagger UI:

**Live Link:** [http://16.171.55.183/swagger-ui/index.html](http://16.171.55.183/swagger-ui/index.html)

---


## 🚦 API Endpoints

### 🔑 Authentication & User Management
* **POST** `/api/users/register` - Create a new user account
* **POST** `/api/users/login` - Authenticate and receive a JWT token
* **GET** `/api/users/profile` - Retrieve details of the logged-in user
* **PUT** `/api/users/update/{id}` - Update profile information
* **DELETE** `/api/users/delete/{id}` - Remove a user account (Admin/Owner)

### 📦 Product & Category Management
* **GET** `/api/products` - List all available products
* **GET** `/api/products/{id}` - Get detailed information for a specific product
* **POST** `/api/products` - Add a new product to the catalog (Admin Only)
* **PUT** `/api/products/{id}` - Update product details (Admin Only)
* **DELETE** `/api/products/{id}` - Remove a product (Admin Only)
* **POST** `/api/products/{id}/images` - Upload product images (Admin Only)
* **GET** `/api/categories` - List all product categories
* **POST** `/api/categories` - Create a new category (Admin Only)

### 🛒 Shopping Cart Logic
* **GET** `/api/cart` - View items in the current user's cart
* **POST** `/api/cartItem/add-item` - Add a product and quantity to the cart
* **PUT** `/api/cartItem/update-item/{id}` - Update the quantity of a cart item
* **DELETE** `/api/cartItem/remove-item/{id}` - Remove a specific item from the cart
* **DELETE** `/api/cart/clear` - Empty the entire cart

### 🧾 Order & Checkout
* **POST** `/api/order/checkout` - Process the current cart and place a permanent order
* **GET** `/api/order/{id}` - View details of a specific order
* **GET** `/api/order/user/{userId}` - Retrieve order history for a specific user
* **PUT** `/api/order/{id}/status` - Update order fulfillment status (Admin Only)

---

### Entity Relationship Diagram (ERD)

```mermaid
erDiagram
    USER ||--o{ ORDER : places
    USER ||--o| CART : owns
    CART ||--o{ CART_ITEM : contains
    PRODUCT ||--o{ CART_ITEM : "added to"
    PRODUCT ||--o{ ORDER_ITEM : "part of"
    ORDER ||--o{ ORDER_ITEM : "consists of"
    CATEGORY ||--o{ PRODUCT : categorizes

    USER {
        string email
        string role
        string password
    }
    PRODUCT {
        string name
        double price
        int quantity
    }
    ORDER {
        string status
        double total_amount
        timestamp created_at
    }
