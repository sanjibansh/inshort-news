#  News API

A Spring Boot application that loads news data from JSON, stores it in the database, simulates user events, and provides intelligent news search and trending functionality using OpenAI and Redis caching.

---

##  Setup Instructions


---

##  Application Behavior on Startup

- On application start:
  - Required database tables are created automatically (via JPA/Hibernate).
  - JSON data from `news_data.json` is loaded and stored in the database.
  - Simulated user events are inserted into the relevant table for testing/trending.

---

##  User Query Endpoint

- **URL**:
  ```
  http://localhost:8081/api/v1/news/dynamic?query=user query
  ```

- **Method**: `GET`

- **Description**:
  Automatically detects **intents** and **categories** from the user query using **OpenAI**, and internally routes the query to the appropriate search or filter logic.

- **Example**:
  ```http
  GET http://localhost:8081/api/v1/news/dynamic?query=latest finance news from news18
  ```
```http
  GET http://localhost:8081/api/v1/news/dynamic?query=news near me&lat=21.334455
  ```
```http
  GET http://localhost:8081/api/v1/news/dynamic?query=something whose intent is score score&score=.7
  ```
---


##  Trending News Endpoint

- **URL**:
  ```
  http://localhost:8081/api/v1/news/trending?lat=19.697333&lon=73.867777
  ```

- **Method**: `GET`

- **Description**:
  Returns trending news articles based on the user's location (latitude and longitude) and simulated user interaction events.

---

###Total 6 api is there please test .....


##  Caching Behavior

- Before querying the database, the application checks **Redis cache** for faster response.
- If the response is not found in Redis, it fetches from the **database**, then stores it in Redis for future use.

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Redis
- OpenAI (via Spring AI integration)
- Maven

---

##  Running the Application

Use Maven:

```bash
mvn spring-boot:run
```

Or run directly via your IDE.


Use Redis
