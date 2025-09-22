@echo off
set GITHUB_ID=Ov23liLtdUza7rewjQ9r
set GITHUB_SECRET=789d41d8d0d5dc7cdf74f1c44630160b4ba852e5
set SPRING_DOCKER_COMPOSE_ENABLED=false
echo Iniciando aplicacao com variaveis de ambiente configuradas...
echo GITHUB_ID: %GITHUB_ID%
echo Docker Compose desabilitado para evitar conflitos
gradlew.bat bootRun