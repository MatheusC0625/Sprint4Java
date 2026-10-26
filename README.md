# Guardiões da Luz — API (NUVA)

Sprint 4 — Domain Driven Design Using Java — Challenge FIAP 2026

API RESTful em Spring Boot para o sistema de missões de sustentabilidade do Guardiões da Luz.

## Deploy

- API: https://sprint4java-api.onrender.com
- Swagger: https://sprint4java-api.onrender.com/swagger-ui/index.html


## Documentação

A documentação completa (objetivo, funcionalidades, endpoints, MER, diagrama de classes e procedimentos de execução) está em [`documentacao/GuardioesDaLuz_Documentacao_Sprint4_Java.pdf`](documentacao/GuardioesDaLuz_Documentacao_Sprint4_Java.pdf).

## Como rodar localmente

- JDK 17+
- IntelliJ IDEA (ou `mvn spring-boot:run`)
- Acesso ao Oracle da FIAP (`oracle.fiap.com.br:1521`)

A aplicação lê as credenciais do banco via variáveis de ambiente (não ficam no código):

```
git clone git@github.com:MatheusC0625/Sprint4Java.git
cd Sprint4Java
export DB_USER=<seu usuario oracle>
export DB_PASSWORD=<sua senha oracle>
mvn spring-boot:run
```

No IntelliJ: *Run → Edit Configurations → Environment variables* e adicione `DB_USER` e `DB_PASSWORD`.

A aplicação sobe em `http://localhost:8080`.

## Equipe

NUVA — Matheus Costa Rodrigues dos Santos — RM 573146 — Turma 1TDSPV

## Uso de IA

Declaro o uso de IA (Claude) neste projeto, para: revisão de código, testes da API local e no deploy do Render, orientação sobre boas práticas de segurança (remoção de credenciais do código) e apoio na configuração do GitHub. Qualquer dúvida, fico à disposição.
