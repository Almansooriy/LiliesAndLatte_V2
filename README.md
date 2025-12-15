# Lilies & Latte

## Features

### Customer Features
- **User Authentication & Registration**: Secure sign-up and login system with encrypted passwords
- **Interactive Menu Browsing**: View menu items with images, descriptions, prices, and calorie information
- **Category Filtering**: Filter menu items by Coffee, Tea, Pastries, and Sandwiches
- **Shopping Cart**: Add items to cart, update quantities, and view cart total
- **Order Placement**: Convert cart to orders with automatic total calculation
- **Order History**: Track all past orders with real-time status updates
- **Order Status Tracking**: Monitor orders through stages (Pending → Preparing → Ready for Pickup → Completed)

### Admin Features
- **Order Management**: View all customer orders and update their status
- **Menu Management**: Add, update, and delete menu items
- **Availability Control**: Toggle item availability
- **Dashboard Statistics**: View total orders, revenue, and order status breakdown

### Technical Features
- **RESTful API**: Well-structured REST endpoints for all operations
- **Security**: Spring Security with role-based access control (ADMIN/CUSTOMER)
- **Data Persistence**: JPA/Hibernate with H2 in-memory database (easily switchable to MySQL)
- **Exception Handling**: Global exception handler with proper error responses
- **Responsive Design**: Mobile-friendly UI with modern CSS
- **AJAX Operations**: Asynchronous cart and order operations
- **Data Initialization**: Auto-populated sample data on first run

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.1.5**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring DevTools
- **Lombok**: Reduces boilerplate code
- **H2 Database**: In-memory database for development
- **MySQL Connector**: For production deployment ()

### Frontend
- **Thymeleaf**: Server-side templating
- **HTML5/CSS3**: Modern responsive design
- **Thymeleaf Security**: Integration with Spring Security

## Prerequisites

- **Java JDK 17** or higher
- **Maven 3.6+**
- **Git** (optional, for cloning)

## Getting Started

## first way
- **step 1** Go to src\main\java\LiliesApplication.java
- **step 2** press run java 
-            Wait until it's finished
- **step 3** copy http://localhost:8080    
- **step 4** go to chrome and paste in URL to open website
-            you can registr or login

- **step 5** if login as admin 
### Admin Account
  - **Email**: admin@liliesandlatte.com
  - **Password**: admin123
  - **Access**: Full admin dashboard, order management, menu management

- **setp 6** if login as Customer (we defind to show and test you can registr by any name and login)
### Customer Account
  - **Email**: customer@example.com
  - **Password**: customer123
  - **Access**: Browse menu, place orders, view order history

-            to open h2 database 
- **step 7** copy http://localhost:8080/h2-console
  - refill information
  - JDBC URL: `jdbc:h2:file:./data/testdb`
  - Username: `sa`
  - Password: (leave empty)
  - press connect then from here we can to mange database

## second way (This method varies from one device to another)
### 1. Clone the Repository
```bash
git clone <repository-url>
cd LiliesAndLatte
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/app-0.0.1-SNAPSHOT.jar
```

### 4. Access the Application
- **Main Application**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/testdb`
  - Username: `sa`
  - Password: (leave empty)

## Default Users

The application comes with pre-configured users:

### Admin Account
- **Email**: admin@liliesandlatte.com
- **Password**: admin123
- **Access**: Full admin dashboard, order management, menu management

### Customer Account
- **Email**: customer@example.com
- **Password**: customer123
- **Access**: Browse menu, place orders, view order history

## API Documentation

### Public Endpoints
```
GET  /                    - Home page
GET  /menu                - Menu page
GET  /login               - Login page
POST /login               - Process login
GET  /register            - Registration page
POST /register            - Process registration
POST /logout              - Logout
```

### Authenticated Endpoints
```
GET  /orders              - Customer orders page
GET  /cart                - Shopping cart page
```

### REST API Endpoints

#### Menu API
```
GET  /api/menu            - Get all menu items
```

#### Cart API (Authenticated)
```
GET    /api/cart          - Get user's cart
POST   /api/cart/items    - Add item to cart
PUT    /api/cart/items/{id} - Update cart item quantity
DELETE /api/cart/items/{id} - Remove item from cart
DELETE /api/cart          - Clear cart
```

#### Order API (Authenticated)
```
POST   /api/orders        - Place order
GET    /api/orders/user/{id} - Get user's orders
GET    /api/orders/{id}   - Get order details
PUT    /api/orders/{id}/cancel - Cancel order
```

#### Admin API (Admin Only)
```
GET    /api/admin/orders  - Get all orders
PUT    /api/admin/orders/{id}/status - Update order status
POST   /api/admin/menu    - Add menu item
PUT    /api/admin/menu/{id} - Update menu item
DELETE /api/admin/menu/{id} - Delete menu item
GET    /api/admin/stats   - Get dashboard statistics
```

## Project Structure

```
LiliesAndLatte/
├── src/
│   ├── main/
│   │   ├── java/com/liliesandlatte/app/
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/
│   │   │   │   ├── WebController.java
│   │   │   │   └── api/
│   │   │   │       ├── AdminRestController.java
│   │   │   │       ├── CartRestController.java
│   │   │   │       ├── MenuRestController.java
│   │   │   │       └── OrderRestController.java
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── MenuItem.java
│   │   │   │   ├── Cart.java
│   │   │   │   ├── CartItem.java
│   │   │   │   ├── Orders.java
│   │   │   │   └── OrderDetail.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── MenuItemRepository.java
│   │   │   │   ├── CartRepository.java
│   │   │   │   ├── CartItemRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── OrderDetailRepository.java
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── MenuService.java
│   │   │   │   ├── CartService.java
│   │   │   │   └── OrderService.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── UnauthorizedException.java
│   │   │   └── LiliesApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   │           ├── index.html
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── menu.html
│   │           ├── orders.html
│   │           └── admin.html
│   └── test/
└── pom.xml
```

## Configuration

### Database Configuration

#### Development (H2 - Default)
```properties
spring.datasource.url=jdbc:h2:mem:liliesandlatte
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

#### Production (MySQL)
1. Uncomment MySQL configuration in `application.properties`
2. Update the following:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/liliesandlatte
spring.datasource.username=your_username
spring.datasource.password=your_password
```
3. Create MySQL database:
```sql
CREATE DATABASE liliesandlatte;
```

### Application Properties
```properties
# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Server Port (default: 8080)
server.port=8080
```

## Testing

Run tests with:
```bash
mvn test
```

## Continuous Integration (CI)

This project includes a GitHub Actions workflow (`.github/workflows/ci.yml`) that runs the Maven build and tests on push/pull requests.
The pipeline performs:
- JDK setup
- Maven build & tests
- Caching of Maven dependencies

If you enable Stripe integration, make sure to configure the `STRIPE_API_KEY` and `STRIPE_WEBHOOK_SECRET` secrets in your GitHub repository and set `stripe.enabled=true` via environment variables or application properties for integration test coverage.

In GitHub Actions, add repository secrets named `STRIPE_API_KEY` and `STRIPE_WEBHOOK_SECRET` under Settings -> Secrets. The CI pipeline will run Stripe tests when `STRIPE_API_KEY` is set.

## Building for Production

### Create JAR
```bash
mvn clean package
```

### Run Production JAR
```bash
java -jar target/app-0.0.1-SNAPSHOT.jar
```

### Build Docker Image (Optional)
Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t lilies-and-latte .
docker run -p 8080:8080 lilies-and-latte
```

## Security Features

- **Password Encryption**: BCrypt hashing
- **CSRF Protection**: Enabled for web forms, disabled for API endpoints
- **Role-Based Access**: ADMIN and CUSTOMER roles
- **Session Management**: Secure session handling
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries

## Customization

### Adding New Menu Categories
1. Add items with new category in `DataInitializer.java`
2. Update filter buttons in `menu.html`

### Changing Colors/Theme
Modify CSS variables in template files (search for color codes like `#a1887f`)

### Adding Payment Integration
Implement payment gateway in `OrderService.java` and `OrderRestController.java`.

### Stripe Test Mode
To enable Stripe Test Mode in this project, follow the steps below:

1. Create a [Stripe account](https://stripe.com) and obtain your test API key from the Developer Dashboard.
2. In `src/main/resources/application.properties`, set the following property:
```properties
stripe.enabled=true
stripe.apiKey=sk_test_...your_api_key_here...
```
3. Stop and start the application, then perform checkout as usual. The system will create Stripe PaymentIntents in test mode.

Note: The repository uses a mock payment processor by default (for local dev). Enabling `stripe.enabled=true` switches to the Stripe processor via Spring's `@ConditionalOnProperty`.

## Troubleshooting

### Port Already in Use
Change the port in `application.properties`:
```properties
server.port=8081
```

### Database Connection Issues
- Verify H2 console settings
- Check MySQL is running (if using MySQL)
- Verify credentials

### Build Errors
```bash
mvn clean install -U
```

## Future Enhancements

- [ ] Payment gateway integration (Stripe/PayPal)
- [ ] Email notifications for order updates
- [ ] Real-time order tracking with WebSocket
- [ ] Customer reviews and ratings
- [ ] Loyalty points system
- [ ] Mobile app (React Native/Flutter)
- [ ] Advanced analytics dashboard
- [ ] Multi-location support

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Author

Abu Rayan

## Acknowledgments

- Spring Boot Documentation
- Thymeleaf Documentation
- Unsplash for sample images

---

**Made with coffee and love**
