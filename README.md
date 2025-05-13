# WebRiseTask


## Microservice for managing users and users subscriptions.

### Tools:
- Java 17
- Spring Boot 3
- PostgreSQL 16
- Docker + Docker Compose
- Maven

## API Endpoints

#### Create a User
- **Endpoint**: `POST /users`  
- **Description**: Creates a new user.  
- **Request Body (JSON)**:
  ```json
  {
    "username": "User Name"
  }
  ```

#### Update a User
- **Endpoint**: `PUT /users/{id}`  
- **Description**: Updates a existing user.  
- **Request Body (JSON)**:
  ```json
  {
    "username": "new Username"
  }
  ```
#### Get info about User
- **Endpoint**: `GET /users/{id}`  
- **Description**: Getting information about user.  

##### Response (Success):
```json
{
    "username": "user123",
    "userId": "id"
}
```

#### Deleting a User
- **Endpoint**: `DELETE /users/{id}`  
- **Description**: Deleting user by id.

##### Response (Success):
  ```json
  {
    "message": "the operation was completed successfully"
  }
  ```

#### Add subscription
- **Endpoint**: `POST /users/{user_id}/subscriptions/{sub_id}`  
- **Description**: Add subscription to User.  
- **Request Body (JSON)**:
  ```json
  {
    "name": "Subscription Name"
  }
  ```

##### Response (Success and if name is exist):
```json
    {
        "userId": "{user_id}",
        "subscription": "Subscription Name"
    }
```

#### Get info about User
- **Endpoint**: `GET /users/{user_id}/subscriptions`
- **Description**: Getting information about users subscriptions.  

##### Response (Success):
```json
{
    "subscriptions": [
        {
            "name": "Subscription name 1"
        },
        {
            "name": "Subscription name 2"
        },
        {
            "name": "Subscription name N"
        }
    ]
}
```

#### Deleting a subscription
- **Endpoint**: `DELETE /users/{user_id}/subscription/{sub_id}`  
- **Description**: Deleting users subscription by id.

##### Response (Success):
```json
{
    "message": "the operation was completed successfully"
}
```

### Environments:
- `DB_HOST`: database host (default: `postgres`)
- `DB_PORT`: database port (default: `5432`)
- `DB_NAME`: database name (default: `postgres`)
- `DB_USERNAME`: the name of the user to connect to the database (default: `postgres`)
- `DB_PASSWORD`: the user's password for accessing the database (default: `postgres`)
- `SERVER_PORT`: port of working servir application (default: `8181`)
- `OUTPUT_PORT`: port of server application for mapping (default: `8181`)

### Building
#### Docker compose
- run ```docker compose up --build```
- 
#### Java Running
1) enter to java_app directory
2) run for building ```mvn package``` or ```mvn clean package```
- run for tests ```mvn test``` or ```mvn clean test```
#### Optional
You may define ```.env``` file with parameters in [Environments](#environments)