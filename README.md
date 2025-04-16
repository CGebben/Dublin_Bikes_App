# Dublin Bikes Web Application - Rebuild

## Overview

This project is a full-stack redevelopment of a previously completed academic assignment designed to complement the Dublin Bikes rental service. The goal is to rebuild the application from the ground up using modern tools and professional practices to better showcase full-stack development capabilities.

This version uses React for the frontend and Spring Boot for the backend. It includes Docker-based local development and will eventually feature a machine learning model for predictive bike availability. The project follows Agile practices including version control, changelogs, and development logs.

## Features (In Progress)

- Real-time bike availability from JCDecaux API.
- Real-time weather data from OpenWeatherMap API.
- RESTful backend using Spring Boot.
- PostgreSQL database containerized with Docker.
- React frontend connected to backend.
- Google Maps interface displaying dynamic station data.
- Weather bar UI synced with backend updates.
- Efficient update logic using `If-Modified-Since` HTTP headers.
- Predictive model using historical data and machine learning (planned).
- Sidebar tabs for expanded station and prediction data (planned).
- Deployment via AWS EC2 and RDS (planned).

## Tech Stack

- Frontend: React
- Backend: Java, Spring Boot
- Database: PostgreSQL
- DevOps: Docker, GitHub Projects (planned)
- Hosting (planned): AWS EC2, RDS
- Machine Learning (planned): Python or Java

## Current Progress

Backend and frontend are now fully connected.

- All scrapers are functional (`/stations`, `/availability`, `/weather`).
- Scheduled tasks fetch live data every 5 minutes.
- PostgreSQL database is integrated and persists correctly.
- REST endpoints are stable and tested.
- Frontend polls backend every 60 seconds using `If-Modified-Since` headers.
- Bike station markers and weather data update dynamically without redundant API calls.

Frontend development is now focused on UI refinement, marker visualization, and planning for predictive data integration.

## Original Project Summary

The original academic version of this project used:

- Flask (Python) as the backend
- MySQL database hosted on AWS RDS
- A JavaScript frontend with Google Maps integration
- Python scripts for scraping weather and bike data
- Averages-based prediction model using historical data

The original version is no longer live, and the database has been deleted. The original project can be found in the 'Original Project' folder. This version will rebuild the entire application from scratch using a different tech stack.

## Architecture (Planned)

- Scrapers (Java)
- Spring Boot REST API
- PostgreSQL database (Dockerized)
- React frontend
- Machine learning model for prediction
- Hosting with AWS (EC2, RDS)

## How to Run (To be added)

Setup instructions will be added after the backend and database layers are functional.

## Author

Colton

- Rebuilding this project to demonstrate full-stack web app development
- Focusing on Java, React, Docker, and AWS as part of ongoing professional development
- Interested in deploying working examples viewable via GitHub portfolio

## License

This project is a personal redevelopment of a previously completed academic project. It is not intended for commercial use.
