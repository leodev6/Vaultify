# Vaultify - Gestionnaire de Mots de Passe Local

Application 100% locale avec Docker.

## Architecture

```
┌─────────────────────────────────────┐
│         FRONTEND (npm dev)          │
│      http://localhost:5173          │
└─────────────────┬───────────────────┘
                  │
┌─────────────────┴───────────────────┐
│           DOCKER LOCAL              │
│  ┌─────────────┐  ┌──────────────┐  │
│  │  Backend    │  │  PostgreSQL  │  │
│  │  :8080      │  │  :5432       │  │
│  └─────────────┘  └──────────────┘  │
└─────────────────────────────────────┘
```

## Prérequis

- **Docker Desktop** (Windows/Mac/Linux)
- **Node.js 18+** (pour le frontend)

## Lancement

### 1. Backend + Database
```bash
cd Vaultify
docker-compose up --build
```

### 2. Frontend
```bash
cd frontend_liens
npm install
npm run dev
```

## URLs Locales

| Service | URL |
|---------|-----|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8080 |
| PostgreSQL | localhost:5432 |

## Variables d'Environnement

### Docker Compose
- `POSTGRES_DB: vaultify`
- `POSTGRES_USER: vaultify`
- `POSTGRES_PASSWORD: vaultify_password_2024`
- `JWT_SECRET` et `ENCRYPTION_KEY` (configurés dans docker-compose.yml)

### Frontend (localhost:5173)
```env
VITE_API_URL=http://localhost:8080
```

## Commandes Utiles

```bash
# Démarrer
docker-compose up -d

# Arrêter
docker-compose down

# Voir les logs
docker-compose logs -f backend
docker-compose logs -f postgres

# Reconstruire
docker-compose up --build
```

## Base de données

Identifiants PostgreSQL:
- Database: `vaultify`
- User: `vaultify`
- Password: `vaultify_password_2024`

## Sécurité Locale

- **Authentification**: JWTtoken
- **Mot de passe**: BCrypt (cost 12)
- **Mots de passe identifiés**: AES-256-GCM avec IV unique