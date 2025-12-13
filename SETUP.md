# Lilies & Latte - Quick Start Guide

## first way

- **step 1** Go to src\main\java\LiliesApplication.java
- **step 2** press run java 
-            Wait until it's finished and appears (Sample menu items created in terminal)
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
  - JDBC URL: `jdbc:h2:mem:liliesandlatte`
  - Username: `sa`
  - Password: (leave empty)
  - press connect then from here we can to mange database

## second way (This method varies from one device to another)

### Step 1: Prerequisites Check
Make sure you have:
- Java 17 or higher installed
- Maven installed (or use the included Maven wrapper)

Check your Java version:
```bash
java -version
```

### Step 2: Navigate to Project
```bash
cd "c:\Users\Fatimah\LiliesAndLatte"
```

### Step 3: Build the Project
```bash
mvn clean install
```

### Step 4: Run the Application
```bash
mvn spring-boot:run
```

### Step 5: Access the Application
Open your browser and go to:
```
http://localhost:8080
```

## Test Accounts

### Admin Account
- **Email**: `admin@liliesandlatte.com`
- **Password**: `admin123`
- **Access**: Admin dashboard at `/admin`

### Customer Account
- **Email**: `customer@example.com`
- **Password**: `customer123`
- **Access**: Full shopping experience

## What to Try

### As a Customer:
1. **Browse Menu** → Go to `/menu` to see all items
2. **Add to Cart** → Click "Add to Cart" on any item
3. **View Cart** → Click the cart icon in navigation
4. **View Orders** → Go to `/orders` to see order history

### As an Admin:
1. **Login** with admin credentials
2. **Go to Admin Dashboard** → `/admin`
3. **Manage Orders** → View and update order status
4. **Manage Menu** → Add, edit, or delete menu items

## Database Access

### H2 Console (Development)
URL: `http://localhost:8080/h2-console`

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:liliesandlatte`
- Username: `sa`
- Password: (leave blank)

## Change Active Profile

### Run with Development Profile (Default)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Run with Production Profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Troubleshooting

### Port 8080 Already in Use
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Build Fails
```bash
mvn clean install -U -DskipTests
```

### Database Issues
Delete the H2 database and restart:
```bash
# The database is in-memory, just restart the application
mvn spring-boot:run
```

## Key Features to Explore

### Customer Features
- [x] User registration and login
- [x] Browse menu with category filters
- [x] Add items to cart
- [x] View and modify cart
- [x] Place orders
- [x] Track order status

### Admin Features  
- [x] View all orders
- [x] Update order status
- [x] Add/Edit/Delete menu items
- [x] View dashboard statistics

## Customization

### Change Application Name
Edit `src/main/resources/templates/*.html` files and replace "Lilies & Latte"

### Modify Menu Items
Edit `src/main/java/com/liliesandlatte/app/config/DataInitializer.java`

### Change Colors
Search for color codes in HTML templates:
- Primary color: `#a1887f`
- Secondary color: `#8d6e63`
- Background: `#fdfaf6`

## Security Notes

- Passwords are encrypted using BCrypt
- CSRF protection enabled for web forms
- Session-based authentication
- Role-based access control (ADMIN/CUSTOMER)

## API Testing

### Using curl:

**Get all menu items:**
```bash
curl http://localhost:8080/api/menu
```

**Login and get cookie:**
```bash
curl -c cookies.txt -X POST http://localhost:8080/login \
  -d "username=customer@example.com&password=customer123"
```

**Get cart (authenticated):**
```bash
curl -b cookies.txt http://localhost:8080/api/cart
```

## Production Deployment

### 1. Build Production JAR
```bash
mvn clean package -Pprod
```

### 2. Run Production JAR
```bash
java -jar target/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 3. Set Environment Variables
```bash
export DB_USERNAME=your_mysql_user
export DB_PASSWORD=your_mysql_password
export PORT=8080
```

## Tips

1. **Hot Reload**: Spring DevTools enables automatic restart when you make changes
2. **SQL Logging**: Set `spring.jpa.show-sql=true` to see SQL queries
3. **Debug Mode**: Run with `--debug` flag for detailed logs
4. **Clean Build**: Use `mvn clean` before building to avoid caching issues

## Verification Checklist

After setup, verify:
- [ ] Application starts without errors
- [ ] Home page loads at http://localhost:8080
- [ ] Can login with test accounts
- [ ] Menu displays with images
- [ ] Can add items to cart
- [ ] Admin dashboard is accessible
- [ ] H2 console works (dev mode)

## Getting Help

If you encounter issues:
1. Check the console logs for error messages
2. Verify all prerequisites are installed
3. Ensure port 8080 is available
4. Try `mvn clean install -U`
5. Check the Troubleshooting section

---

**Ready to Start? Run these commands:**
```bash
cd LiliesAndLatte
mvn spring-boot:run
```

Then open: **http://localhost:8080**
