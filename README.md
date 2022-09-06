# Sorting Hat's Backend

The Sorting Hat's backend is responsible for collecting service-based system data and extracting CharM metrics.

## Technologies

- Kotlin language
- Spring Boot Framework
- MongoDB
- JUnit5 and Mockito for tests
- Gradle

## Backend overview

![Backend overview](.docs/backend_overview.jpg)

Every time the backend is started, it fetches systems' data manually collected from a spreadsheet and saves those that have not yet been saved in the database.

The endpoints are as follows:

- `GET /systems`: to get all systems registered in the tool.
- `GET /systems/{name}`: to get more detailed information of a specific system with name `name`.
  - Example: `GET /systems/InterSCity`.
- `GET /systems/{name}/metrics`: to extract and get the CharM metrics of a specific system with name `name`.
  - Example: `GET /systems/TrainTicket/metrics`.
- `POST /systems`: to collect system data from a remote repository.

Example of a request body for `POST /systems`:
```json
{
  "repoUrl": "https://github.com/codelab-alexia/buscar-hackathon",
  "filename": "docker-compose.yaml"
}
```

## Modules architecture

The backend is composed by the modules as follows:

![Modules architecture](.docs/modules_architecture.jpg)

- `domain`: contains the core entities used by other modules.
- `data_collector`: responsible for collecting service-based systems' data. Currently, it collects data from docker-compose files.
- `metrics_extractor`: responsible for extracting CharM metrics from systems collected by `data_collector`.
- `persistence`: handle saving systems' data in a MongoDB and accessing them. It is shared by `data_collector` and `metrics_extractor`.
- `server`: starts an HTTP Web Server and connects the other modules. It is the entry point of the application.

## Internal Architecture

All images follow the color key below:

![Color key](.docs/color_key.jpg)

### Domain

![Domain entities](.docs/domain.jpg)

![Domain behaviors](.docs/domain_with_behaviors.jpg)

### Metrics Extractor

![Metrics Extractor](.docs/metrics_extractor.jpg)

### Data Collector

![Data Collector](.docs/data_collector.jpg)

![Data Collector with docker](.docs/data_collector_with_docker.jpg)

