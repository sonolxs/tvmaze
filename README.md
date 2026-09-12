# TVMaze Middleware API

A Spring Boot middleware that wraps the [TVMaze public API](https://www.tvmaze.com/api), providing search, show details with MongoDB caching, and a commenting/rating system.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Requirements](#requirements)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Error Handling](#error-handling)
- [Project Structure](#project-structure)
- [Design Decisions](#design-decisions)
- [Data Sources](#data-sources)

---

## Overview

This project implements a middleware layer over the TVMaze public API. It exposes a REST API that:

1. **Searches shows** by name and returns a simplified list including user comments.
2. **Retrieves full show details** by TVMaze ID, backed by a MongoDB cache to reduce external calls.
3. **Stores user comments and ratings** (0–5) associated with each show.

---

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Maven | 3.9+ |
| MongoDB | Atlas (Free Tier M0) |
| Lombok | 1.18.30 |
| springdoc-openapi | 3.1.1 |

---

## Architecture

The application follows a classic **layered architecture**:

```
Client → Controller → Service → (Repository | RestClient) → (MongoDB | TVMaze API)
```

- **Controller layer**: exposes REST endpoints, handles input validation.
- **Service layer**: orchestrates business logic, cache lookups, and external API calls.
- **Repository layer**: MongoDB access via Spring Data.
- **Mapper layer**: converts between external DTOs, internal documents, and response DTOs.
- **Exception layer**: global error handling using `ProblemDetail` (RFC 7807).

---

## Requirements

- **JDK 21** installed
- **Maven 3.9+**
- A **MongoDB Atlas** account (free tier works)
- Internet access (to reach `api.tvmaze.com`)

---

## Configuration

The application reads the MongoDB connection string from an environment variable called `MONGO_URI`.

### 1. Create a MongoDB Atlas cluster

1. Sign up at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas).
2. Create a **free M0 cluster**.
3. Under **Database Access**, create a user with read/write privileges.
4. Under **Network Access**, add the IP `0.0.0.0/0` (allow access from anywhere).
5. Copy the connection string and replace `<password>` with your user's password.

The final URI should look like:

```
mongodb+srv://<user>:<password>@<cluster>.mongodb.net/tvmaze?retryWrites=true&w=majority
```

### 2. Set the environment variable

**Linux / macOS:**
```bash
export MONGO_URI="mongodb+srv://..."
```

**Windows (PowerShell):**
```powershell
$env:MONGO_URI="mongodb+srv://..."
```

**IntelliJ IDEA:** add `MONGO_URI` under *Run → Edit Configurations → Environment variables*.

### 3. Application properties

All non-secret configuration lives in `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: tvmaze-middleware
  mongodb:
    uri: ${MONGO_URI}
    database: tvmaze
  jackson:
    default-property-inclusion: non_null

server:
  port: 8080

springdoc:
  swagger-ui:
    path: /swagger-ui.html
  api-docs:
    path: /v3/api-docs

tvmaze:
  api:
    base-url: https://api.tvmaze.com
```

---

## Running the Application

```bash
# Compile
mvn clean compile

# Run
mvn spring-boot:run
```

Once started:

- API base URL: `http://localhost:8080`
- Swagger UI: [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## API Endpoints

### 1. Search shows

```http
GET /api/v1/shows/search?q={query}
```

Searches shows by name using the TVMaze fuzzy search. Each result includes the show's comments.

**Example:**
```bash
curl "http://localhost:8080/api/v1/shows/search?q=girls"
```

**Response (200 OK):**
```json
[
  {
    "id": 139,
    "name": "Girls",
    "channel": "HBO",
    "summary": "<p>This Emmy winning series is a comic look...</p>",
    "genres": ["Drama", "Romance"],
    "comments": [
      { "comment": "Great show!", "rating": 5 }
    ]
  }
]
```

---

### 2. Get show by ID

```http
GET /api/v1/shows/{id}
```

Returns the full show object from TVMaze. Uses MongoDB as a cache:

- **Cache HIT** → returns the stored document.
- **Cache MISS** → fetches from TVMaze, stores it, and returns it.

Comments are always fetched fresh (not cached) and attached to the response.

**Example:**
```bash
curl "http://localhost:8080/api/v1/shows/139"
```

**Response (200 OK):**
```json
{
  "id": 139,
  "url": "https://www.tvmaze.com/shows/139/girls",
  "name": "Girls",
  "type": "Scripted",
  "language": "English",
  "genres": ["Drama", "Romance"],
  "status": "Ended",
  "runtime": 30,
  "premiered": "2012-04-15",
  "ended": "2017-04-16",
  "network": { "id": 8, "name": "HBO", "country": { "code": "US", "name": "United States", "timezone": "America/New_York" } },
  "image": { "medium": "...", "original": "..." },
  "summary": "<p>This Emmy winning series...</p>",
  "comments": [
    { "comment": "Great show!", "rating": 5 }
  ]
}
```

---

### 3. Add a comment

```http
POST /api/v1/shows/{showId}/comments
Content-Type: application/json
```

**Request body:**
```json
{
  "comment": "Great show, highly recommended",
  "rating": 5
}
```

**Validation rules:**
- `comment`: required, non-blank, max 1000 characters.
- `rating`: required, integer between 0 and 5.

**Response (201 Created):**
```json
{
  "status": 201,
  "message": "Comment added successfully for show 139"
}
```

**Response (400 Bad Request):**
```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "rating": "Rating must be at most 5"
  }
}
```

---

## Error Handling

All errors follow the [RFC 7807 Problem Details](https://datatracker.ietf.org/doc/html/rfc7807) format.

| Exception | HTTP Status | Description |
|-----------|-------------|-------------|
| `IllegalArgumentException` | 400 | Invalid query parameters |
| `MethodArgumentNotValidException` | 400 | Bean validation failed |
| `ResourceNotFoundException` | 404 | Resource not found |
| `HttpClientErrorException.NotFound` | 404 | TVMaze returned 404 |
| `ExternalApiException` | 502 | TVMaze call failed |

---

## Project Structure

```
src/main/java/com/example/tvmaze/
├── TvMazeApplication.java
├── config/
│   ├── OpenApiConfig.java
│   └── RestClientConfig.java
├── controller/
│   ├── ShowController.java
│   └── CommentController.java
├── dto/
│   ├── request/
│   │   └── CommentRequest.java
│   ├── response/
│   │   ├── ApiResponse.java
│   │   ├── CommentResponse.java
│   │   ├── ShowResponse.java
│   │   └── ShowSearchResponse.java
│   └── tvmaze/
│       ├── TvMazeCountry.java
│       ├── TvMazeExternals.java
│       ├── TvMazeImage.java
│       ├── TvMazeLink.java
│       ├── TvMazeLinks.java
│       ├── TvMazeNetwork.java
│       ├── TvMazeRating.java
│       ├── TvMazeSchedule.java
│       ├── TvMazeSearchResult.java
│       └── TvMazeShow.java
├── exception/
│   ├── ExternalApiException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── mapper/
│   └── ShowMapper.java
├── model/
│   ├── CommentDocument.java
│   └── ShowDocument.java
├── repository/
│   ├── CommentRepository.java
│   └── ShowRepository.java
└── service/
    ├── CommentService.java
    ├── ShowService.java
    └── impl/
        ├── CommentServiceImpl.java
        └── ShowServiceImpl.java
```

---

## Design Decisions

### Why `RestClient` instead of `RestTemplate` or `WebClient`?

`RestClient` is the modern synchronous HTTP client introduced in Spring 6.1. It's simpler than `WebClient` (which requires Reactor) and more idiomatic than `RestTemplate` (which is in maintenance mode).

### Why separate DTOs for TVMaze, response, and documents?

To avoid coupling external contracts to our own. If TVMaze changes a field, we only adjust the `TvMaze*` DTOs and the mapper — the response contract stays stable.

### Why cache `ShowResponse` and not `TvMazeShow`?

Because the cache is a read model for the client. Storing the mapped response means a cache hit can be returned without any mapping work.

### Why are comments not stored in the show document?

Comments are a separate aggregate. If they were embedded in the show document, every new comment would invalidate the cache. Instead, comments are always fetched live from their own collection and attached at response time.

### How is the N+1 problem avoided in the search endpoint?

When searching, all show IDs are collected first, and a single Mongo query `findByShowIdIn(showIds)` fetches all related comments in one round trip. Results are grouped in memory by `showId`.

---

## Data Sources

All show data is provided by the [TVMaze API](https://www.tvmaze.com/api). Usage is licensed under [CC BY-SA](https://creativecommons.org/licenses/by-sa/4.0/). TVMaze is credited as the source of the data.