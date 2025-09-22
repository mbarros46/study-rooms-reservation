# Sistema de Reserva de Salas de Estudo (Spring MVC + Thymeleaf)

## Stack
- Spring Boot 3.3 (Web, Thymeleaf, Data JPA, OAuth2 Client, Validation)
- PostgreSQL 16 (Docker Compose)
- Flyway (migrações)
- Java 17

## Configuração OAuth2

### 1. Configurar GitHub OAuth App
1. Vá em GitHub → Settings → Developer settings → OAuth Apps
2. Clique em "New OAuth App"
3. Configure:
   - **Application name:** `projeto_diamante` (ou outro nome)
   - **Homepage URL:** `http://localhost:8082`
   - **Authorization callback URL:** `http://localhost:8082/login/oauth2/code/github`
4. Após criar, copie o **Client ID** e **Client Secret**

### 2. Configurar Variáveis de Ambiente
1. Crie um arquivo `.env` na raiz do projeto
2. Adicione suas credenciais OAuth2:
```bash
# GitHub OAuth Credentials
GITHUB_ID=your_github_client_id_here
GITHUB_SECRET=your_github_client_secret_here

# Google OAuth Credentials (opcional)
GOOGLE_ID=your_google_client_id_here
GOOGLE_SECRET=your_google_client_secret_here
```

## Como executar
1. Subir banco:
```bash
docker compose up -d
```
2. Executar aplicação:
```bash
./gradlew bootRun
```
3. Login:
- `/oauth2/authorization/google`
- `/oauth2/authorization/github`

Defina `GITHUB_ID`, `GITHUB_SECRET`, `GOOGLE_ID`, `GOOGLE_SECRET` no ambiente.

## Perfis
Utiliza `application.properties` padrão apontando para Postgres local do Compose.

## Papéis
- `STUDENT`: padrão após primeiro login.
- `LIBRARIAN`: promover no banco:
```sql
UPDATE users SET role='LIBRARIAN' WHERE email='seu.email@dominio.com';
```

## Principais rotas
- `/` (público)
- `/reservations/mine`, `/reservations/new`, `POST /reservations` (aluno autenticado)
- `/rooms/**`, `/reservations/admin`, `POST /reservations/approve/{id}`, `POST /reservations/cancel/{id}` (bibliotecário)



# Gera os arquivos do wrapper (gradlew, gradlew.bat, gradle/wrapper/*)
docker run --rm -v ${PWD}:/app -w /app gradle:8.10.0-jdk17 gradle wrapper

# Agora o wrapper existe, rode a app localmente:
.\gradlew.bat bootRun