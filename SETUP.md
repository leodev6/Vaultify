# Vaultify - Secure Password Manager

A production-grade, security-first full-stack application for managing sensitive personal data.

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│              FRONTEND (Netlify)                                  │
│         https://gestionliens.netlify.app                         │
└─────────────────────────┬───────────────────────────────────────┘
                          │ HTTPS
┌─────────────────────────┴───────────────────────────────────────┐
│                    DOCKER COMPOSE                                 │
│                                                                   │
│  ┌─────────────────────┐     ┌────────────────────────────────┐  │
│  │  Backend (Port 8080)│     │  PostgreSQL (Port 5432)        │  │
│  │  Spring Boot 3.4    │     │  Container: vaultify-db        │  │
│  └─────────────────────┘     └────────────────────────────────┘  │
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
- **CORS**: Configured for Netlify frontend
- **CSRF**: Disabled (JWT-based stateless API)
- **SQL Injection**: JPA with parameterized queries

### Frontend
- Token stored in localStorage
- No encryption keys stored in frontend
- Protected routes with authentication check
- HTTPS via Netlify

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

## Configuration Docker

### Commandes

**Démarrer:**
```bash
cd Vaultify
docker-compose up -d
```

**Arrêter:**
```bash
docker-compose down
```

**Logs:**
```bash
docker-compose logs -f backend
docker-compose logs -f postgres
```

### URLs
| Service | URL |
|---------|-----|
| Backend | http://localhost:8080 |
| PostgreSQL | localhost:5432 |

### Variables d'environnement (docker-compose.yml)
```yaml
POSTGRES_DB: vaultify
POSTGRES_USER: vaultify
POSTGRES_PASSWORD: vaultify_password_2024
JWT_SECRET: vaultify-jwt-secret-key-minimum-32-characters
ENCRYPTION_KEY: a89343b4f7c2d1e5a6b7c8d9e0f1a2b3
```

## Configuration Frontend Netlify

Dans Netlify Dashboard, ajouter variable:
```
VITE_API_URL=http://localhost:8080
```

Pour accès depuis Internet, utiliser ngrok:
```bash
ngrok http 8080
# Puis configurer VITE_API_URL avec l'URL ngrok
```

## Tech Stack

- **Frontend**: React 19, Vite, Tailwind CSS, Axios, Netlify
- **Backend**: Spring Boot 3.4.5, Spring Security, Spring Data JPA
- **Database**: PostgreSQL (Docker)
- **Containerization**: Docker + Docker Compose
- **Authentication**: JWT (JJWT library)
- **Encryption**: AES-256-GCM (Java built-in)