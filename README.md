# Todo Service Application

This repository contains the source code for a Todo Service Application built with Spring Boot. The application allows users to create and manage boards and tasks. It also includes webhook functionality to handle user deletions.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Installation](#installation)
- [API Endpoints](#api-endpoints)

## Features

- Create, read, and delete boards
- Create, update, patch, and delete tasks
- Handle user deletions via webhooks
- Uses RESTful API principles

## Architecture

The application follows a layered architecture:
- **Controller Layer**: Handles HTTP requests and responses.
- **Service Layer**: Contains business logic.
- **Repository Layer**: Handles database interactions.
- **Utility Layer**: Provides additional utilities, such as fetching user details from an external API.

## Installation

1. Clone this repository.
2. Ensure you have Java 17, Maven installed, Docker is installed and running.
3. Configure the application properties in src/main/resources/application.properties if needed (e.g., database settings).
4. Build the project using Maven: ```mvn clean install```
5. Build docker image: ```docker build -t todo-service .```
6. Run docker compose: ```docker compose up -d ```
7. The application will start on http://localhost:8092. You can now use any API testing tool (e.g., Postman) to interact with the endpoints.
8. Example.postman_collection.json contains example for all endpoints.

## API Endpoints

### Get All Boards
```
GET /todo-service/api/boards/
Response: 
[
    {
        "id": "e7b1a3c5-8c0d-4a56-9f77-497e47d2c4e5",
        "name": "Project Alpha",
        "description": "Initial Board setup",
        "tasks": []
    }
]

```

### Create Board
```
POST /todo-service/api/boards/
Request: 
{
    "name": "Board Beta",
    "description": "Secondary project"
}

Response:
{
    "id": "d2a16b60-5dca-4a0b-8e7f-03b2bdb7a03b",
    "name": "Board Beta",
    "description": "Secondary project",
    "tasks": []
}
```

### Get Board By ID
```
GET /todo-service/api/boards/{id}
Response: 
{
    "id": "d2a16b60-5dca-4a0b-8e7f-03b2bdb7a03b",
    "name": "Project Beta",
    "description": "Secondary project",
    "tasks": []
}

```

### Delete Board
``` 
DELETE /todo-service/api/boards/{id}
Status: 204 No Content
```

### Create Task 
``` 
POST /todo-service/api/boards/{id}/tasks
Request: 
{
    "name": "New Task",
    "description": "A new Task"
}

Response: 
{
    "id": "a4f04c20-847b-4a60-85e1-d5fbbd4e3e5c",
    "name": "New Task",
    "description": "A new Task",
    "user": {
        "id": "b1c9d830-0334-4e8d-830d-c2c4c781d9f1",
        "name": "John Doe"
    },
    "status": "CREATED"
}
``` 

### Update Task
``` 
PUT /todo-service/api/tasks/{id}
Request: 
{
    "name": "Update Task",
    "description": "Updated Task description",
    "user": "b1c9d830-0334-4e8d-830d-c2c4c781d9f1",
    "status": "STARTED"
}

Response: 
{
    "id": "a4f04c20-847b-4a60-85e1-d5fbbd4e3e5c",
    "name": "Update Task",
    "description": "Updated Task description",
    "user": {
        "id": "b1c9d830-0334-4e8d-830d-c2c4c781d9f1",
        "name": "John Doe"
    },
    "status": "STARTED"
}
``` 

### Patch Task
``` 
PATCH /todo-service/api/tasks/{id}
Request: 
{
    "description": "Updated Task description"
}

Response: 
{
    "id": "a4f04c20-847b-4a60-85e1-d5fbbd4e3e5c",
    "name": "Update Task",
    "description": "Updated Task description",
    "user": {
        "id": "b1c9d830-0334-4e8d-830d-c2c4c781d9f1",
        "name": "John Doe"
    },
    "status": "CREATED"
}
```

### Delete Task
``` 
DELETE /todo-service/api/tasks/{id}
Response: Status: 204 No Content
``` 

### Handle User Deleted
``` 
POST /todo-service/api/webhooks/user-deleted
Request: 
{
    "user": "b1c9d830-0334-4e8d-830d-c2c4c781d9f1"
}
``` 