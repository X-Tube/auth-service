# Auth Service
A microservice dedicated to secure user authentication, account verification (OTP), and stateless session management via JWTs and HttpOnly cookies.

🏗️ Architecture
This service acts as the identity provider, managing user registration states, validating secure verification codes, and issuing short-lived access tokens along with long-lived refresh tokens.

* **Database**: PostgreSQL
* **Cryptography**: BCrypt (for password and verification code hashing)
* **Security**: Spring Security & JWT (JSON Web Tokens)

🚀 Getting Started
Prerequisites
* Java 17 or 21
* PostgreSQL Database
* Maven 3.x

Running Locally
1. Ensure you have a PostgreSQL database named `xtube_auth` running on port `5432`.
2. Run the application: `./mvnw spring-boot:run`

🧪 Testing
The project contains integration and context loading tests to verify security configurations and repository interactions.

Run all tests: `./mvnw test`
*Note: Make sure your local PostgreSQL instance is running or configure an in-memory database profile before running the tests.*

🛡️ Security & Session Management
This service implements secure practices to protect credentials and handle user sessions safely.

| Mechanism | Implementation | Purpose |
| :--- | :--- | :--- |
| **Passwords & OTP** | BCrypt Hashing | Hashing secrets in the database using a configurable strength factor (salt rounds). |
| **Cookies** | HttpOnly, SameSite=Lax | Delivery of tokens to the client while reducing exposure to XSS and CSRF attacks. |
| **JWT Tokens** | Cryptographically signed | Stateless user authorization containing user UUID, role, and email claims. |

⚙️ Configuration

| Variable | Default | Description |
| --- | --- | --- |
| SPRING_DATASOURCE_URL | jdbc:postgresql://localhost:5432/xtube_auth | PostgreSQL database connection URL |
| SECRET_KEY | 404E635266556A586E32... | HMAC-SHA key used to sign and verify JWTs |
| JWT_EXPIRATION | 900000 (15 minutes) | Expiration time for the access token in milliseconds |
| REFRESH_EXPIRATION | 604800000 (7 days) | Expiration time for the refresh token in milliseconds |
| BCRYPT_SALTROUNDS | 12 | Strength parameter (logarithmic rounds) for BCrypt |