<h1> api </h1>
This application was generated using JHipster 4.14.5. The purpose of this application is to build api and service layer between cordapp and frontend

<h2> Development </h2>
<ul>
    <li>
        <h3> Database setup </h3>
        Start a postgresql database in a docker container, run:
        <pre>docker-compose -f src/main/docker/postgresql.yml up -d</pre>
        To stop it and remove the container, run:
        <pre> docker-compose -f src/main/docker/postgresql.yml down </pre>
        <h4>OR</h4>
        Downlad and run postgresql binary <br>
        <br>
        <h4>Creating Database and user:</h4>
        <pre>
        docker exec -ti -u postgres docker_postgresname /bin/bash or login as postgres user if using binary
        psql
        create database gbis;
        create user gbis with login;
        create database ibfs;
        create user ibfs with login;
        </pre>        
    </li>
    <li>
        <h3> Running application </h3>
        To start your application in the gbis profile, simply run:
        <pre>./mvnw -Dspring.profiles.active=gbis</pre>
        To start your application in the ibfs profile, simply run:
        <pre>./mvnw -Dspring.profiles.active=ibfs</pre>
        This will run two instances of backends and connect to their respective cordapps.
    </li>
</ul>

### Doing API-First development using swagger-codegen

[Swagger-Codegen]() is configured for this application. You can generate API code from the `src/main/resources/swagger/api.yml` definition file by running:
```bash
./mvnw generate-sources
```
Then implements the generated interfaces with `@RestController` classes.

To edit the `api.yml` definition file, you can use a tool such as [Swagger-Editor](). Start a local instance of the swagger-editor using docker by running: `docker-compose -f src/main/docker/swagger-editor.yml up -d`. The editor will then be reachable at [http://localhost:7742](http://localhost:7742).

## Building for production

To optimize the api application for production, run:

    ./mvnw -Pprod clean package

To ensure everything worked, run:

    java -jar target/*.jar

## Testing

To launch your application's tests, run:

    ./mvnw clean test
    
## Keycloak

Steps:
<ul>
    <li>Create a user with the username 'gbis-admin' and password '1111'</li>
    <li>In the users, edit gbis-admin and then go to role-mapping tab.</li>
    <li>In the client roles, select realm management and assign realm-admin role to this user.</li>
    <li>Create roles with name ROLE_MAKER,ROLE_CHECKER,ROLE_VIEWER</li>
</ul>
