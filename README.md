#   Bank REST API

##   Описание
REST API для управления банковскими картами, клиентами и переводами.  
Реализован на **Spring Boot** с использованием **Spring Data JPA**, **PostgreSQL** и Docker.

---

##  ️ Технологии
- Java 17+  
- Spring Boot 3.x  
- Spring Data JPA — работа с базой через сущности и репозитории  
- PostgreSQL  
- Maven  
- Docker & Docker Compose  
- Swagger / OpenAPI — автогенерация документации API и тестирование через UI
- Liquibase

---

##   Требования
Перед запуском убедитесь, что установлены:  
- JDK 17+  
- Docker (для контейнеризации)  
- PostgreSQL (если не используете Docker)  

---

##   Запуск проекта
1. Клонируйте проект:  

git clone https://github.com/InnerVoice89/bank_rest.git

2. Соберите приложение

3. Запуск через Docker Compose:

  docker compose up --build
  
---

##   Документация API (Swagger)

Swagger UI доступен по адресу:

http://localhost:8080/swagger-ui/index.html


  
  
