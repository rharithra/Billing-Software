 Billing-Software
A complete Billing solution for retail &amp; wholesale operations with role-based authentication.
Billing & Inventory Management System

A professional **desktop billing software** built using **Java (Swing + JDBC)** and **MySQL** for managing retail and wholesale billing, customer credits, stock management, and reporting.

 Features

 Billing Module
- Create and manage **retail** and **wholesale bills**
- Dynamic **product search** while billing
- Auto-calculates totals, paid amount, and balance
- **PDF bill generation** with print and preview

 Customer Management
- Maintain customer profiles with type: **Cash / Credit**
- Auto-fetch customer name by phone number
- Track outstanding balances and credit limits
- Record **collections and payments**, generate receipts

 Inventory Management
- Add, update, delete products easily
- Real-time **stock reduction after billing**
- Low stock highlighting (<5 units)
- Product and group-based search

 Reports
- **Bill-wise, Customer-wise, and Product-wise Sales Reports**
- Export reports to **Excel** (password-protected)
- View pending balances and historical transactions
- Graphical analytics (planned using JavaFX)

Email Notifications
- Sends product-wise **bill breakdown via email** to customers
  
 Tech Stack

| Layer | Technology |
|-------|-------------|
| Language | Java (JDK 17+) |
| UI | Swing (with custom buttons & layouts) |
| Backend | JDBC + DAO Pattern |
| Database | MySQL |
| PDF Generation | iText / ReportLab |
| Email Service | JavaMail API |
| Build Tool | IntelliJ IDEA |
| Deployment | Standalone `.exe` desktop application |

---

## 🏗️ Project Architecture

src/
 dao/ # Data Access Objects (DB logic)
 model/ # POJOs for Customer, Bill, Product, etc.
 service/ # Business logic
 ui/ # Swing UI Screens
 utils/ # Helpers (DBConnection, PDF, Email)
 Main.java # Application entry point
 
 Database Schema

**Tables:**
- `customers(id, name, phone, type, balance, created_at)`
- `bills(id, customer_name, bill_type, total_amount, paid_amount, balance_amount, created_at)`
- `bill_items(id, bill_id, product_id, quantity, price)`
- `products(id, name, group_name, stock, price)`
- `bill_collections(id, bill_id, amount, created_at)`


 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/BillingSystem.git
   cd BillingSystem

   Import into IntelliJ IDEA or Eclipse

Configure MySQL Database
CREATE DATABASE billing_db;
USE billing_db;
-- Import SQL schema from /resources/billing_db.sql

Update DB credentials in DBConnection.java
private static final String URL = "jdbc:mysql://localhost:3306/billing_db";
private static final String USER = "root";
private static final String PASSWORD = "your_password"

Run the Application

Open Main.java
Click Run

Building Executable (.exe)

Export project as a Runnable JAR (Build → Artifacts → JAR → From modules with dependencies)

Use Launch4j or Inno Setup to convert .jar → .exe

Distribute as a standalone desktop billing software

Future Enhancements

Migrate backend to Spring Boot

Add REST API for web or Android integration

Integrate JavaFX dashboards

Add GST / Tax management module

Add user activity logs and role-based analytics

Author

Harithra R
Backend Developer
rharithra9@gmail.com
www.linkedin.com/in/harithrar
https://github.com/rharithra

