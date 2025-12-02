# 🎧 REST API Automation Framework — Spotify Web API

![Java](https://img.shields.io/badge/Java-11+-red)
![Rest Assured](https://img.shields.io/badge/Rest%20Assured-Latest-green)
![TestNG](https://img.shields.io/badge/TestNG-Latest-blue)
![Maven](https://img.shields.io/badge/Maven-3.6+-orange)
![Allure](https://img.shields.io/badge/Allure%20Reporting-Integrated-purple)
![Status](https://img.shields.io/badge/Build-Stable-brightgreen)

---

## 📌 1. Overview

This repository contains a **scalable, maintainable, and enterprise-grade REST API automation framework** designed for testing the **Spotify Web API**, focusing on **Playlist & Track Management (CRUD)**.

The framework follows clean architectural principles, ensures reusability, and includes advanced reporting capabilities.

### 🌟 Key Features

- Layered Architecture (Client → Service → Test)  
- OAuth 2.0 Token Refresh Automation  
- POJO-based request/response modeling  
- Allure report integration  
- Custom assertion layer  
- Dynamic test data with Java Faker  
- High code reusability & maintainability  

---

## 🧰 2. Technical Stack

| Category | Technology | Version | Role |
|---------|------------|---------|------|
| Language | Java | 11+ | Core Language |
| Testing Framework | TestNG | Latest | Test Execution |
| HTTP Client | Rest Assured | Latest | API Requests/Assertions |
| Data Mapping | Jackson | Latest | JSON ↔️ POJOs |
| Reporting | Allure | Latest | Test Reporting |
| Build Tool | Maven | 3.6+ | Build & Dependency Management |
| Utilities | Lombok | Latest | POJO Code Reduction |
| Utilities | Java Faker | Latest | Random Test Data |

---

## 🗂️ 3. Folder Structure

C:.
├───.idea
├───.mvn
├───AI
├───allure-report
│   ├───data
│   │   ├───attachments
│   │   └───test-cases
│   ├───export
│   ├───history
│   ├───plugin
│   │   ├───behaviors
│   │   ├───packages
│   │   └───screen-diff
│   └───widgets
├───src
│   ├───main
│   │   ├───java
│   │   └───resources
│   └───test
│       ├───java
│       │   └───com
│       │       └───Spotify
│       │           └───oauth2
│       │               ├───api
│       │               │   ├───ApplicationApi
│       │               │   └───UserApi
│       │               ├───pojo
│       │               ├───tests
│       │               │   ├───NegativeTests
│       │               │   │   ├───PlayList
│       │               │   │   ├───tracks
│       │               │   │   └───User
│       │               │   ├───PositiveTests
│       │               │   │   ├───PlayList
│       │               │   │   ├───tracks
│       │               │   │   └───User
│       │               │   └───Steps
│       │               └───Utils
│       └───resources
└───target
    ├───allure-results
    ├───classes
    ├───generated-sources
    │   └───annotations
    ├───generated-test-sources
    │   └───test-annotations
    ├───maven-status
    │   └───maven-compiler-plugin
    │       ├───compile
    │       │   └───default-compile
    │       └───testCompile
    │           └───default-testCompile
    └───test-classes
        └───com
            └───Spotify
                └───oauth2
                    ├───api
                    │   ├───ApplicationApi
                    │   └───UserApi
                    ├───pojo
                    ├───tests
                    │   ├───NegativeTests
                    │   │   ├───PlayList
                    │   │   ├───tracks
                    │   │   └───User
                    │   ├───PositiveTests
                    │   │   ├───PlayList
                    │   │   ├───tracks
                    │   │   └───User
                    │   └───Steps
                    └───Utils

---

## 🏗️ 4. Framework Architecture

### 🔹 API Client Layer (`com.Spotify.oauth2.api`)
- Handles HTTP communication  
- **RestResource** → Generic GET/POST/PUT/DELETE  
- **TokenManager** → OAuth token refresh  
- **PlaylistApi / TrackApi** → High-level business actions  

### 🔹 Data Layer (`com.Spotify.oauth2.pojo`)
- POJOs representing request/response payloads  
- Uses **Jackson** and **Lombok**  

### 🔹 Test Layer (`com.Spotify.oauth2.tests`)
- `BaseTest` → Pre-test setup (token, config)  
- Test classes → Positive/Negative/CRUD test cases  

---

## ⚙️ 5. Environment Setup

### Prerequisites
- Java 11+  
- Maven 3.6+  
- Allure CLI (optional)  
- Spotify Developer credentials: `CLIENT_ID`, `CLIENT_SECRET`, `REFRESH_TOKEN`

### System Properties

| Property | Example | Description |
|----------|---------|-------------|
| BASE_URI | https://api.spotify.com | Spotify Web API |
| ACCOUNT_BASE_URI | https://accounts.spotify.com | Token API |
| CLIENT_ID | 25f5f1... | App Credential |
| CLIENT_SECRET | 36781... | App Secret |
| REFRESH_TOKEN | AQDemiR... | Refresh Token |

---

## ▶️ 6. Execution

### Run all tests
```bash
mvn clean install

---
## 📊 7. Allure Reporting
Generate results:
Allure results are stored under: /allure-results
Serve the report:
allure serve allure-results

##  🎯 9. Future Enhancements
-CI/CD Integration (GitHub Actions / Jenkins)
-Data-driven testing (JSON / Excel)
-Additional coverage: Albums, Users, Artists
-Parallel execution optimization





