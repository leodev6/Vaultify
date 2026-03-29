# Vaultify - Secure Password Manager

A production-grade, security-first full-stack application for managing sensitive personal data.

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        FRONTEND (React + Vite)                  │
│                                                                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────────┐   │
│  │  Login   │  │Register  │  │Dashboard │  │  AuthContext │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────────┘   │
│                           │                                      │
│                    Axios + JWT Interceptor                       │
└───────────────────────────┼─────────────────────────────────────┘
                            │ HTTPS
┌───────────────────────────┼─────────────────────────────────────┐
│                        BACKEND                                    │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────────────────┐  │
│  │ AuthController│  │   JwtFilter  │  │  GlobalExceptionHandler│  │
│  └──────────────┘  └──────────────┘  └───────────────────────┘  │
│           │                   │                                   │
│  ┌────────▼─────────┐  ┌──────▼───────┐  ┌──────────────┐       │
│  │  UserService     │  │ CredentialSVC│  │ EncryptionUtil│       │
│  └────────┬─────────┘  └──────┬───────┘  └──────────────┘       │
│           │                   │                                   │
│  ┌────────▼───────────────────▼──────────────────────────────┐  │
│  │                    Spring Security                         │  │
│  │    BCrypt (12 rounds) + AES-256-GCM + JWT                 │  │
│  └────────┬───────────────────────────────┬──────────────────┘  │
│           │                               │                       │
│  ┌────────▼──────────┐         ┌──────────▼──────────────────┐  │
│  │   User Entity     │<───────>|    Credential Entity         │  │
│  │   (PostgreSQL)    │         │    (PostgreSQL)              │  │
│  └───────────────────┘         └──────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────┘
```

## Security Features

### Backend
- **Password Hashing**: BCrypt with cost factor 12
- **Credential Encryption**: AES-256-GCM with unique IV per credential
- **Key Management**: Encryption key stored in environment variables
- **Authentication**: JWT with 15-minute expiration
- **Authorization**: Stateless, user can only access their own credentials
- **Input Validation**: Bean Validation on all DTOs
- **CORS**: Configured to allow only frontend origin
- **CSRF**: Disabled (JWT-based stateless API)
- **SQL Injection**: JPA with parameterized queries
- **Rate Limiting**: Basic protection via Spring Security

### Frontend
- Token stored in localStorage (with auto-logout on expiration)
- No encryption keys stored in frontend
- Protected routes with authentication check
- HTTPS-only communication (production)

## API Endpoints

### Auth
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login, returns JWT token

### Credentials (Protected)
- `GET /credentials` - List all user's credentials
- `POST /credentials` - Create new credential
- `PUT /credentials/{id}` - Update credential
- `DELETE /credentials/{id}` - Delete credential
- `GET /credentials/{id}/decrypt` - Get decrypted password

## Setup Instructions

### Prerequisites
- Java 17
- Node.js 18+
- PostgreSQL 14+
- Maven 3.8+

### Local Development

1. **Database Setup**
   ```sql
   CREATE DATABASE vaultify;
   ```

2. **Backend Configuration**
   Create `.env` file in project root:
   ```env
   DB_URL=jdbc:postgresql://localhost:5432/vaultify
   DB_USERNAME=postgres
   DB_PASSWORD=your_password
   JWT_SECRET=your-256-bit-secret-minimum-32-characters
   ENCRYPTION_KEY=your-32-byte-hex-key-for-aes-256
   APP_HOST=http://localhost:5173
   ```

3. **Run Backend**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Run Frontend**
   ```bash
   cd frontend
   npm run dev
   ```

### Deployment

#### Backend (Render)
1. Create new Web Service on Render
2. Connect GitHub repository
3. Set environment variables:
   - `DB_URL`: PostgreSQL connection string
   - `DB_USERNAME`: Database username
   - `DB_PASSWORD`: Database password
   - `JWT_SECRET`: Generate secure random string (32+ chars)
   - `ENCRYPTION_KEY`: Generate 32-byte hex key
   - `APP_HOST`: Your Netlify frontend URL
4. Build command: `./mvnw package -DskipTests`
5. Start command: `java -jar target/vaultify-0.0.1-SNAPSHOT.jar`

#### Frontend (Netlify)
1. Connect GitHub repository
2. Set build command: `npm run build`
3. Set publish directory: `dist`
4. Add environment variable:
   - `VITE_API_URL`: Your Render backend URL (e.g., https://vaultify-api.onrender.com)

## Data Flow

1. User registers/login via frontend
2. Backend validates credentials, returns JWT token
3. Frontend stores JWT (localStorage)
4. All subsequent requests include JWT in Authorization header
5. JWT filter validates token, loads user data into SecurityContext
6. Credentials service encrypts passwords with AES-256-GCM before storage
7. Decryption requires valid JWT token and ownership of credential

## Key Rotation Strategy

For key rotation (basic implementation):
1. Add `keyVersion` field to credentials table
2. Store multiple encryption keys in environment (comma-separated)
3. When decrypting, use key version to select correct key
4. When updating password, re-encrypt with new key version

## OWASP Considerations

This implementation addresses:
- A01:2021 Broken Access Control - User can only access own credentials
- A02:2021 Cryptographic Failures - AES-256-GCM with unique IV
- A03:2021 Injection - JPA prevents SQL injection
- A04:2021 Insecure Design - Stateless JWT, no session fixation
- A05:2021 Security Misconfiguration - Strict CORS, secure headers
- A07:2021 Identification and Authentication Failures - BCrypt, rate limited

## Tech Stack

- **Frontend**: React 19, Vite, Tailwind CSS, Axios
- **Backend**: Spring Boot 3.4.5, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Authentication**: JWT (JJWT library)
- **Encryption**: AES-256-GCM (Java built-in)