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

## 🚀 Como Executar a Aplicação

### Pré-requisitos
- Java 17 instalado
- Docker Desktop rodando
- Git instalado

### Comandos para Executar (Windows PowerShell)

1. **Clone o repositório** (se ainda não fez):
```powershell
git clone https://github.com/mbarros46/study-rooms-reservation.git
cd study-rooms-reservation
```

2. **Pare qualquer processo Java conflitante**:
```powershell
taskkill /F /IM java.exe
```

3. **Remova containers antigos** (se existirem):
```powershell
docker rm -f rooms_pg
```

4. **Inicie apenas o banco PostgreSQL**:
```powershell
docker compose up -d db
```

5. **Configure as variáveis de ambiente e execute a aplicação**:
```powershell
$env:GITHUB_ID="Ov23liLtdUza7rewjQ9r"
$env:GITHUB_SECRET="789d41d8d0d5dc7cdf74f1c44630160b4ba852e5"
$env:SPRING_DOCKER_COMPOSE_ENABLED="false"
.\gradlew.bat bootRun
```

**OU use o script automatizado**:
```powershell
.\run-with-env.bat
```

6. **Acesse a aplicação**:
   - Abra seu navegador em: `http://localhost:8082`

### Comandos de Limpeza (se necessário)

**Parar tudo**:
```powershell
# Parar aplicação Spring Boot (Ctrl+C no terminal)
# Parar containers Docker
docker compose down
```

**Resetar banco de dados**:
```powershell
docker compose down -v
docker compose up -d db
```

### ⚠️ Troubleshooting

**Erro "Port already in use"**:
```powershell
# Verificar o que está usando a porta 8082
netstat -ano | findstr :8082
# Matar o processo (substitua PID pelo número retornado)
taskkill /F /PID <PID>
```

**Erro "Gradle lock file"**:
```powershell
# Limpar cache do Gradle
.\gradlew --stop
rm -rf .gradle
```

**Erro OAuth 404**:
- Verifique se está acessando `http://localhost:8082` (não 8080)
- Confirme que o GitHub OAuth App está configurado com callback: `http://localhost:8082/login/oauth2/code/github`

**Container PostgreSQL não inicia**:
```powershell
docker ps -a
docker logs rooms_pg
```

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