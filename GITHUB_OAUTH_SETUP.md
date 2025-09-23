# 🔧 Configuração do GitHub OAuth2

## 📋 **Passo a Passo**

### **1. Criar OAuth App no GitHub**

1. **Acesse**: https://github.com/settings/developers
2. **Clique em**: "OAuth Apps" > "New OAuth App"
3. **Preencha os dados**:
   - **Application name**: `Study Rooms Reservation`
   - **Homepage URL**: `http://localhost:8080`
   - **Application description**: `Sistema de reservas de salas de estudo`
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
4. **Clique em**: "Register application"
5. **Copie as credenciais**:
   - **Client ID**: (será exibido na página)
   - **Client Secret**: clique em "Generate a new client secret"

### **2. Configurar Variáveis de Ambiente**

Crie um arquivo `.env` na raiz do projeto:

```bash
# Suas credenciais reais do GitHub
GITHUB_ID=seu-client-id-aqui
GITHUB_SECRET=seu-client-secret-aqui
```

### **3. Executar a Aplicação**

```bash
# No diretório do projeto:
docker compose up --build
```

### **4. Testar o Login**

1. Acesse: http://localhost:8080
2. Clique em "Entrar com GitHub"
3. Autorize a aplicação no GitHub
4. Deve redirecionar para a página de reservas

## ✅ **Verificações**

- [ ] OAuth App criado no GitHub
- [ ] Callback URL configurado corretamente
- [ ] Arquivo `.env` criado com credenciais reais
- [ ] Docker reiniciado com novas variáveis
- [ ] Login com GitHub funcionando

## 🐛 **Solução de Problemas**

### Erro "redirect_uri_mismatch":
- Verifique se o callback URL no GitHub é exatamente:
  `http://localhost:8080/login/oauth2/code/github`

### Erro "invalid_client":
- Verifique se GITHUB_ID e GITHUB_SECRET estão corretos no .env

### Erro "access_denied":
- Usuário negou autorização ou app não está configurado corretamente

## 📝 **Notas**

- Apenas GitHub OAuth2 está configurado (Google foi removido)
- Novos usuários são criados automaticamente com role STUDENT
- Para criar usuários LIBRARIAN, use a migração de dados de teste