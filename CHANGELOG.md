# Changelog

All notable changes to this project will be documented in this file.

---

## [0.1.0] - 2025-03-29

### Added

- Created `Dev` branch for active development.
- Cleaned repository to prepare for full-stack rebuild.
- Wrote new README to reflect updated project goals.
- Planned initial tech stack and development roadmap.

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
