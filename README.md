# Smart Canteen Token System 🍽️
**Kalasalingam Academy of Research and Education**  
*Department of Computer Science & Engineering — Cyber Security*

A production-ready full-stack web application for automated college canteen token management, built with Spring Boot 3, Spring Data JPA, Spring Security, Thymeleaf, and MySQL.

---

## 📌 Features

### 👨‍🎓 Student Portal
1. **Authentication**: Register with college email and secure login via BCrypt password hashing.
2. **Interactive Menu**: Responsive card grid with South Indian dishes, category filters (`Breakfast`, `Meals`, `Snacks`, `Beverages`), search by name, and quantity steppers.
3. **Smart Cart**: Browser `localStorage`-based cart with instant total calculation and quantity adjustments.
4. **Token Generation**: Secure order placement generates incrementing tokens (e.g., `T-0001`, `T-0002`).
5. **Real-time Order Status**:
   - `PLACED` → Order submitted.
   - `READY` → Page polls `/api/orders/{id}/status` every 5 seconds. When ready, triggers an **audio beep**, **toast notification**, and **modal alert**.
   - `COLLECTED` → Order picked up at the counter.
6. **Order History**: "My Orders" page displays previous tokens, item details, timestamps, and order status.

### 👨‍🍳 Admin Portal
1. **Live Order Dashboard**: Displays all active (`PLACED` & `READY`) orders with auto-refresh every 5 seconds.
2. **One-Click Order Workflow**:
   - PLACED → Mark **READY** (alerts the student).
   - READY → Mark **COLLECTED** (completes order).
3. **Menu Management**:
   - Add new food items with price, category, and availability.
   - File upload for dish images or custom image URLs.
   - Toggle availability (instantly disables items on student menu).
   - Edit prices, names, and delete items.
4. **Daily Analytics**: Summary of today's total orders and total revenue in ₹ (INR).

---

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.0, Spring Data JPA, Spring Security 6
- **Frontend**: Plain HTML5, CSS3, JavaScript (ES6+), Thymeleaf templates
- **Database**: MySQL 8.0+ (or Supabase PostgreSQL)
- **Build Tool**: Maven

---

## 📁 Project Structure

```
smart-canteen/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── kalasalingam/
        │           └── canteen/
        │               ├── SmartCanteenApplication.java
        │               ├── config/
        │               │   ├── DataInitializer.java
        │               │   └── SecurityConfig.java
        │               ├── controller/
        │               │   ├── AdminController.java
        │               │   ├── AuthController.java
        │               │   ├── GlobalExceptionHandler.java
        │               │   ├── OrderApiController.java
        │               │   └── StudentController.java
        │               ├── dto/
        │               │   └── CartItemDTO.java
        │               ├── entity/
        │               │   ├── FoodItem.java
        │               │   ├── Order.java
        │               │   ├── OrderItem.java
        │               │   ├── Token.java
        │               │   └── User.java
        │               ├── repository/
        │               │   ├── FoodItemRepository.java
        │               │   ├── OrderItemRepository.java
        │               │   ├── OrderRepository.java
        │               │   ├── TokenRepository.java
        │               │   └── UserRepository.java
        │               └── service/
        │                   ├── CustomUserDetailsService.java
        │                   ├── FoodItemService.java
        │                   ├── OrderService.java
        │                   └── UserService.java
        └── resources/
            ├── application.properties
            ├── data.sql
            ├── schema.sql
            ├── static/
            │   ├── css/
            │   │   └── style.css
            │   ├── images/
            │   │   └── placeholder.jpg
            │   └── js/
            │       └── app.js
            └── templates/
                ├── cart.html
                ├── login.html
                ├── menu.html
                ├── my-orders.html
                ├── order-confirmation.html
                ├── register.html
                ├── admin/
                │   ├── daily-summary.html
                │   ├── dashboard.html
                │   ├── menu-form.html
                │   └── menu-management.html
                ├── error/
                │   ├── 404.html
                │   └── 500.html
                └── fragments/
                    └── layout.html
```

---

## ⚡ Quick Start & Run Instructions

### 1. Database Setup (MySQL)
Open MySQL terminal or Workbench:
```sql
CREATE DATABASE smart_canteen;
```
*(Hibernate `ddl-auto=update` and `DataInitializer.java` will automatically create all tables and seed sample data on first launch)*.

### 2. Configure Database Credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### 3. Run the Application
Open a terminal inside `smart-canteen/`:
```bash
mvn spring-boot:run
```
*(Or open the project folder in IntelliJ IDEA / Eclipse / VS Code and run `SmartCanteenApplication.java`)*.

### 4. Access the Application
Open your browser and navigate to:
```
http://localhost:8080
```

---

## 🔑 Demo Accounts

The application automatically seeds these test accounts on startup:

| Role | Email | Password | Redirects To |
|------|-------|----------|--------------|
| **Admin** | `admin@kare.edu` | `admin123` | `/admin/dashboard` |
| **Student** | `student@kare.edu` | `student123` | `/menu` |

---

## 🖼️ Food Images Guide

Save your images in `src/main/resources/static/images/`. Download free-license South Indian food photos from [Unsplash](https://unsplash.com) or [Pexels](https://pexels.com) using these exact filenames:

| Item | Recommended Filename | Category |
|------|----------------------|----------|
| Idli | `idli.jpg` | Breakfast |
| Masala Dosa | `masala_dosa.jpg` | Breakfast |
| Pongal | `pongal.jpg` | Breakfast |
| Veg Biryani | `veg_biryani.jpg` | Meals |
| Parotta | `parotta.jpg` | Meals |
| Veg Meals | `veg_meals.jpg` | Meals |
| Fried Rice | `fried_rice.jpg` | Meals |
| Samosa | `samosa.jpg` | Snacks |
| Vada | `vada.jpg` | Snacks |
| Tea | `tea.jpg` | Beverages |
| Coffee | `coffee.jpg` | Beverages |
| Lemon Juice | `lemon_juice.jpg` | Beverages |

> **Note**: If an image is missing or not downloaded yet, the built-in `/images/placeholder.jpg` fallback automatically renders with zero broken links!

---

## ☁️ Connecting to Your Supabase Project (`wwluaakdogtsozyrecdr`)

Your project is already configured for your Supabase project **smart-canteen** (`wwluaakdogtsozyrecdr`)!

### Why you don't need `npm @supabase/supabase-js @supabase/ssr`:
`@supabase/supabase-js` is for JavaScript / React / Next.js frontends. Because this project is built with **Spring Boot 3 + Spring Data JPA (Java)**, Spring connects natively to your Supabase PostgreSQL database using JDBC!

### Quick Connection Steps:
1. Open [application.properties](file:///c:/Users/saip4/Downloads/java%20mini%20project/smart-canteen/src/main/resources/application.properties).
2. Set your Supabase database password (the password you created when setting up the project):
   ```properties
   spring.datasource.url=jdbc:postgresql://db.wwluaakdogtsozyrecdr.supabase.co:5432/postgres?sslmode=require
   spring.datasource.username=postgres
   spring.datasource.password=YOUR_SUPABASE_DB_PASSWORD
   ```
3. *(Optional)* If you want to run the table creations and seeds directly in your Supabase dashboard:
   - Go to [Supabase SQL Editor](https://supabase.com/dashboard/project/wwluaakdogtsozyrecdr/sql/new).
   - Paste the contents of `src/main/resources/schema-supabase.sql` and click **Run**.
4. Run the app (`run.bat` or `mvn spring-boot:run`)!
