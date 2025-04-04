# Changelog

All notable changes to this project will be documented in this file.

---

## [0.1.4] - 2025-04-04

### Added

- Completed and debugged `DublinWeatherScraper` to fetch and save live weather data using the OpenWeatherMap API.
- Implemented `@Scheduled` annotation to run the weather scraper every 5 minutes.
- Added custom JPA query and new endpoint `GET /weather/latest` to retrieve the most recent weather data.
- Added custom JPA query and endpoint `GET /availability/latest` to retrieve the latest availability snapshot per station.
- Confirmed correct integration of composite keys (`WeatherId`, `AvailabilityId`) across scraping and endpoint layers.
- Successfully tested all endpoints via browser.

### Fixed

- Resolved multiple casting issues in the weather scraper, including numeric type mismatches (e.g. string vs integer).
- Ensured weather scraper handles optional and missing fields gracefully.

### Notes

- Backend is now fully functional and API-complete.
- Next stage: React frontend.

---

## [0.1.3] - 2025-04-03

### Added

- Created `DublinBikesScraper` service to fetch station and availability data from the JCDecaux API.
- Implemented scheduled task using `@Scheduled` to run the `DublinBikesScraper` every 5 minutes.
- Triggered the scraper for the first time, resulting in real-time station and availability data being successfully inserted into the PostgreSQL database.
- Verified scraper functionality through logs and SQL queries.
- Began work on `DublinWeatherScraper` to fetch live weather data from the `OpenWeatherMap API`.

### Updated

- Updated `.gitignore` to exclude `application.properties` and added environment variable setup for API keys to prevent accidental commits.

### Notes

- The `DublinBikesScraper` is fully operational and stores both static and dynamic data reliably.
- The `DublinWeatherScraper` is not yet functional. Further debugging or refactoring is needed.
- PostgreSQL database must be running for scrapers to function properly. The scheduled tasks will silently fail if the database or Spring Boot app is not active.

---

## [0.1.2] - 2025-04-01

### Added

- Created `StationRepository` interface extending `JpaRepository`.
- Created `StationController` with `GET /stations` and `POST /stations` endpoints.
- Verified database connection and JSON response using browser and Postman.
- Confirmed JPA-Hibernate table creation with clean terminal logs.
- Created `Availability` entity and `AvailabilityId` composite key class.
- Created `AvailabilityRepository` and `AvailabilityController` with `GET /availability` and `POST /availability` endpoints.
- Created `Weather` entity and `WeatherId` composite key class.
- Created `WeatherRepository` and `WeatherController` with `GET /weather` and `POST /weather` endpoints.

### Updated

- Renamed main application class to `DublinBikesApp`.
- Moved `DublinBikesApp.java` to correct package (`com.dublinbikes`) to simplify structure.
- Cleaned up project folders to follow standard Spring Boot conventions.
- Changed `spring.jpa.hibernate.ddl-auto` from `create` to `update` to persist data across restarts.

### Notes

- Endpoint `GET /stations` now reflects added data and is ready for scraping integration.
- PostgreSQL container must be running for Spring Boot to connect successfully.
- All three entity types (Station, Availability, Weather) have been tested for full insert-retrieve flow.
- Added dummy station for local dev testing to enable Availability insertions. Test station removed from database after successful testing.

---

## [0.1.1] - 2025-03-31

### Added

- Initialized Spring Boot backend using Spring Initializr.
- Included dependencies: `Spring Web`, `Spring Data JPA`, `PostgreSQL Driver`, `Lombok`, and `DevTools`.
- Added `.gitignore` files to the root and backend directories to ignore environment files, IDE metadata, Maven output, and Docker volumes.
- Created initial `Station` entity with JPA annotations.

### Configured

- Docker Compose setup for PostgreSQL with persistent volume and `.env` file for credentials.
- Spring Boot `application.properties` for PostgreSQL connection.
- `Lombok` integration for reducing boilerplate in backend entities.

### Notes

- Docker container must be restarted manually with `docker-compose up -d` after reboot.

---

## [0.1.0] - 2025-03-29

### Added

- Created `Dev` branch for active development.
- Cleaned repository to prepare for full-stack rebuild.
- Wrote new README to reflect updated project goals.
- Planned initial tech stack and development roadmap.
