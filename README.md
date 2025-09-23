# 📚 Study Rooms Reservation System

Sistema de reserva de salas de estudo desenvolvido com **Spring Boot**, **PostgreSQL**, **Thymeleaf** e **Docker**.

## 👥 **Integrantes**
- **Miguel Barros RM556652**
- **Pedro Valentim RM556826**

## 🚀 **Instalação Completa**

### **📋 Pré-requisitos**
- Docker e Docker Compose instalados
- Conta no GitHub

### **⚠️ IMPORTANTE: Configuração OAuth2 Obrigatória**

**Sem configurar o GitHub OAuth2, o projeto NÃO funcionará!**

### **1. Clonar o projeto**
```bash
git clone https://github.com/mbarros46/study-rooms-reservation.git
cd study-rooms-reservation
```

### **2. Configurar GitHub OAuth2**

#### **2.1. Criar OAuth App no GitHub**
1. Acesse: https://github.com/settings/developers
2. Clique em **"OAuth Apps"** → **"New OAuth App"**
3. Preencha:
   - **Application name**: `Study Rooms Reservation`
   - **Homepage URL**: `http://localhost:8080`
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
4. Clique em **"Register application"**
5. **Copie o Client ID**
6. Clique em **"Generate a new client secret"** e **copie o Client Secret**

#### **2.2. Criar Arquivo de Configuração**
Crie um arquivo `.env` na raiz do projeto:

```bash
# .env
GITHUB_ID=seu-client-id-do-github
GITHUB_SECRET=seu-client-secret-do-github
```

⚠️ **IMPORTANTE**: Substitua pelos valores reais do seu GitHub OAuth App!

### **3. Executar a Aplicação**
```bash
docker compose up --build
```

### **4. Acessar o Sistema**
1. Abra: http://localhost:8080
2. Clique em **"Entrar com GitHub"**
3. Autorize a aplicação
4. Pronto! Sistema funcionando

## ✅ **Checklist de Verificação**

- [ ] Docker e Docker Compose instalados
- [ ] Repositório clonado
- [ ] OAuth App criado no GitHub
- [ ] Arquivo `.env` criado com credenciais corretas
- [ ] `docker compose up --build` executado
- [ ] Login com GitHub funcionando

## �️ **Tecnologias Utilizadas**

- **Backend**: Spring Boot 3.x, Spring Security, JPA/Hibernate
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Database**: PostgreSQL + Flyway (migrações)
- **Containerização**: Docker + Docker Compose
- **Autenticação**: GitHub OAuth2

## ✨ **Funcionalidades**

### **👤 STUDENT** (Padrão)
- 🔍 Visualizar salas disponíveis
- 📅 Criar reservas
- 📋 Gerenciar suas próprias reservas

### **👨‍💼 LIBRARIAN** (Admin)
- ✅ Todas as permissões de STUDENT
- ⚡ Aprovar/cancelar qualquer reserva
- 🏢 Adicionar/editar/remover salas
- 📊 Acesso ao painel administrativo

## 📁 **Estrutura do Projeto**

```
src/main/
├── java/br/com/fiap/epictaskg/
│   ├── config/          # Configurações (Security, OAuth2)
│   ├── user/            # Entidades e lógica de usuários
│   ├── room/            # Entidades e lógica de salas
│   ├── reservation/     # Entidades e lógica de reservas
│   └── Application.java # Classe principal
├── resources/
│   ├── templates/       # Templates Thymeleaf
│   ├── static/          # CSS, JS, imagens
│   ├── db/migration/    # Scripts Flyway
│   └── application.properties
└── docker-compose.yml   # Configuração Docker
```

## 🗄️ **Banco de Dados**

O sistema usa **PostgreSQL** com **Flyway** para versionamento:

- **V1**: Estrutura inicial (users_app, rooms, reservations)
- **V2**: Trigger para prevenir sobreposição de horários
- **V3**: Dados de teste (usuários, salas, reservas)

### **🔒 Dados de Teste Incluídos**

O sistema já vem com dados de teste:
- **Usuários**: Alguns usuários já cadastrados
- **Salas**: 5 salas de estudo disponíveis
- **Reservas**: Algumas reservas de exemplo

## 🐛 **Solução de Problemas**

### ❌ **Erro: "redirect_uri_mismatch"**
- **Causa**: URL de callback incorreta
- **Solução**: Verifique se no GitHub está exatamente: `http://localhost:8080/login/oauth2/code/github`

### ❌ **Erro: "invalid_client"**
- **Causa**: Credenciais incorretas no `.env`
- **Solução**: Verifique se `GITHUB_ID` e `GITHUB_SECRET` estão corretos

### ❌ **Erro: "Application error"**
- **Causa**: Arquivo `.env` não foi criado
- **Solução**: Crie o arquivo `.env` com as credenciais do GitHub

### ❌ **Erro de Banco**
- Aguarde o PostgreSQL inicializar completamente
- Verifique os logs: `docker compose logs db`

### ❌ **Erro de Build**
- Limpe o cache: `docker compose down -v`
- Rebuild: `docker compose up --build`

## 🎯 **Comandos Essenciais**

```bash
# 1. Clonar
git clone https://github.com/mbarros46/study-rooms-reservation.git
cd study-rooms-reservation

# 2. Criar .env (com suas credenciais do GitHub)
echo "GITHUB_ID=seu-client-id" > .env
echo "GITHUB_SECRET=seu-client-secret" >> .env

# 3. Executar
docker compose up --build

# 4. Acessar: http://localhost:8080
```

## 📄 **Estrutura Esperada**

```
study-rooms-reservation/
├── .env                    # ← DEVE ser criado por você
├── docker-compose.yml
├── Dockerfile
├── README.md
└── src/
    └── ...
```

---

**⚠️ ATENÇÃO**: Sem o arquivo `.env` com credenciais válidas do GitHub, o login não funcionará!
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


docker compose up --build