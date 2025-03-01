# Employee-Tracking-Backend

## Introduction

It is basically a backend project written for the project manager in the company to control, monitor and manage the employees.
There are 3 roles in the system: SUPER_USER, ADMIN and USER. SUPER_USER has authority over everything and can invite users in the ADMIN or USER role to register in the system by e-mail.
The project manager has ADMIN authority in the system. PM can create a team, add an employer here, create a project, open tasks under the project and assign these tasks to users. Employers can add comments to these tasks and change the status of the task as the process progresses. PM can update and view the performance of employees on my site.
Additionally, test classes of some important service classes were written using JUnit 5.


## TECHNOLOGIES

### Environment and Tools

- Java 21
- Spring Boot 3.3.2
- Maven
- PostgreSQL

### Library and Dependencies

- Spring Boot Data JPA
- Hibernate
- Spring Boot Security
- Lombok
- Model Mapper
- SpringDoc OpenAPI (Swagger 3)
- Spring Boot Validation
- Bearer Authentication
- JWT
- JUnit 5

## STRUCTURE
### Architecture
> **REST** architecture implemented
* REST is an architecture that works over the HTTP protocol, which enables communication between client and server. REST is a transfer method used in software built on service-oriented architecture. It enables the application to communicate by carrying XML and JSON data between the client and the server. Services that use the REST architecture are called RESTful services.</br>

### 3.2 Principle
> Efforts were made to remain faithful to **SOLID** principles.
```
1. (S)ingle Responsibility Principle (SRP: Tek Sorumluluk Prensibi )
```
```
2. (O)pen/Closed Principle (OCP: Açık Kapalı Prensibi)
```
```
3. (L)iskov ‘s Substitution Principle (LSP: Liskov’un Yerine Geçme Prensibi )
```
```
4. (I)nterface Segregation Principle (ISP: Arayüz Ayrıştırma Prensibi )
```
```
5. (D)ependency Inversion Principle (DIP: Bağımlılık Ters Çevirme Prensibi )
```


## Endpoints

context-path: http://localhost:8088/api/v1

### Auth

| HTTP Method | Endpoint                 | Description           | Authority                 |
|-------------|--------------------------|-----------------------|---------------------------|
| POST        | /auth/invite             | Invite new employee   | SUPER_USER & ADMIN        |
| POST        | /auth/complete-register  | Complete register     | ADMIN & USER              |
| POST        | /auth/authenticate       | Authenticate user     | SUPER_USER & ADMIN & USER |
| GET         | /auth/activate-account   | Activate user account | ADMIN & USER              |

### Employee

| HTTP Method | Endpoint             | Description                           | Authority    |
|-------------|----------------------|---------------------------------------|--------------|
| GET         | /employees           | Retrieves all employees               | ADMIN        |
| GET         | /employees/{id}      | Retrieves a employees by ID           | ADMIN & USER |
| GET         | /employees/me        | Retrieves logged in employees details | ADMIN & USER |
| PUT         | /employees/{id}      | Updates a employees by ID             | ADMIN & USER |
| DELETE      | /employees/{id}      | Deletes a employees  by ID            | ADMIN & USER |  

### Comments

| HTTP Method | Endpoint                | Description                      | Authority    |
|-------------|-------------------------|----------------------------------|--------------|
| POST        | /comments               | Adds a new comments              | ADMIN & USER |
| GET         | /comments               | Retrieves all comments           | ADMIN & USER |
| GET         | /comments/{id}          | Retrieves a comments by ID       | ADMIN & USER |
| PUT         | /comments/{id}          | Updates a comments by ID         | ADMIN        |
| DELETE      | /comments/{id}          | Deletes a comments by ID         | ADMIN        |

### Project

| HTTP Method | Endpoint        | Description               | Authority    |
|-------------|-----------------|---------------------------|--------------|
| POST        | /projects       | Add a new projects        | ADMIN        |
| GET         | /projects       | Retrieve all projects     | ADMIN & USER |
| GET         | /projects/{id}  | Retrieve a projects by ID | ADMIN & USER |
| PUT         | /projects/{id}  | Update a projects by ID   | ADMIN        |
| DELETE      | /projects/{id}  | Delete a projects by ID   | ADMIN        |

### Project roles

| HTTP Method | Endpoint                             | Description                                             | Authority     |
|-------------|--------------------------------------|---------------------------------------------------------|---------------|
| POST        | /projects/{projectId}/roles          | Adds a new project role under a specific project        | ADMIN         |
| GET         | /projects/{projectId}/roles          | Retrieves all roles under a specific project            | ADMIN & USER  |
| GET         | /projects/{projectId}/roles/{roleId} | Retrieves a project role by ID under a specific project | ADMIN & USER  |
| PUT         | /projects/{projectId}/roles/{roleId} | Updates a project role by ID under a specific project   | ADMIN         |
| DELETE      | /projects/{projectId}/roles/{roleId} | Deletes a project role by ID under a specific project   | ADMIN         |

### Performances

| HTTP Method | Endpoint            | Description                        | Authority    |
|-------------|---------------------|------------------------------------|--------------|
| POST        | /performances       | Creates a new performances         | ADMIN        |
| GET         | /performances       | Retrieves all performances         | ADMIN & USER |
| GET         | /performances/{id}  | Retrieves a performances by ID     | ADMIN & USER |
| PUT         | /performances/{id}  | Updates a performances by ID       | ADMIN        |
| DELETE      | /performances/{id}  | Deletes a performances by ID       | ADMIN        |

### Task

| HTTP Method | Endpoint    | Description                   | Authority    |
|-------------|-------------|-------------------------------|--------------|
| POST        | /tasks      | Creates a new task            | ADMIN        |
| GET         | /tasks      | Retrieves all tasks           | ADMIN & USER |
| GET         | /tasks/{id} | Retrieves an task by ID       | ADMIN & USER |
| PUT         | /tasks/{id} | Updates an task by ID         | ADMIN        |
| DELETE      | /tasks/{id} | Deletes an task by ID         | ADMIN        |

### Teams

| HTTP Method | Endpoint         | Description                  | Authority    |
|-------------|------------------|------------------------------|--------------|
| POST        | /teams           | Creates a new team           | ADMIN        |
| GET         | /teams           | Retrieves all teams          | ADMIN & USER |
| GET         | /teams/{id}      | Retrieves a team by ID       | ADMIN & USER |
| PUT         | /teams/{id}      | Updates a team by ID         | ADMIN        |
| DELETE      | /teams/{id}      | Deletes a team by ID         | ADMIN        |


## Authentication

> Super User email: `superuser@gmail.com`

> Super User password: `SuperUser123`
- Bearer Authentication with email verification  is used for authentication and authorization.
- Endpoints specify required authorities (ADMIN or USER) for access.

## Additional Features

>**http://localhost:8088/api/v1/swagger-ui/index.html** for Swagger ui

>**applicaton-dev.yml** for developers configuration

>**docker-compose.yml** for docker configuration


### ERD

![Employee Tracking ERD](https://github.com/AbdullahKarakoc/Employee-Tracking-Backend/blob/main/images/EmployeeTrackingERD.png)


### Downloads

[Download first version explanation pdf](https://github.com/AbdullahKarakoc/Employee-Tracking-Backend/raw/main/EmployeeTrack_V1.zip)

[Download Postman Collection](https://github.com/AbdullahKarakoc/Employee-Tracking-Backend/raw/main/Employee%20Tracking.postman_collection.zip)

[Download ERD.drawio](https://github.com/AbdullahKarakoc/Employee-Tracking-Backend/raw/main/EmployeeTracking_ERD.zip    )



## Author

>**Abdullah Karakoç**

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.

