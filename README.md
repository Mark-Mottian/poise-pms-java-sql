# PoisePMS

PoisePMS is a Java console application for managing construction projects and the people linked to those projects.

The program uses JDBC to connect to a MariaDB database. Project and person data are read from and written to the database. No text files are used.

## Features

- View all projects
- View all people linked to projects
- Add new people
- Capture new projects
- Update existing projects
- Delete project and person data
- Finalise projects with a completion date
- View incomplete projects
- View overdue projects
- Search for projects by project number or project name

## Technologies

- Java
- JDBC
- MariaDB
- Eclipse IDE
- Javadoc

## Database

Database name:

```text
PoisePMS
```

Tables used:

```text
customers
architects
contractors
structural_engineers
project_managers
projects
```

## How to Run

1. Open the project in Eclipse.
2. Make sure MariaDB is running.
3. Make sure the `PoisePMS` database exists.
4. Check the database details in `DatabaseManager.java`.
5. Run `PoisePMS.java` as a Java application.

## Database Connection

The database connection details are stored in `DatabaseManager.java`:

```java
private static final String DB_URL = "jdbc:mariadb://localhost:3306/PoisePMS";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "password123";
```

Update these values if your local database details are different.

## Javadoc Documentation

Javadoc API documentation has been generated in the `doc` folder.

Open this file to view it:

```text
doc/index.html
```

## Main Files

- `DatabaseManager.java` - stores database connection details and creates database connections.
- `InputHelper.java` - handles safe user input.
- `PersonService.java` - handles people-related database actions.
- `ProjectService.java` - handles project-related database actions.
- `PoisePMS.java` - runs the main menu and controls program flow.

## Maintainer

Created by Mark Mottian for a Java Database Programming Capstone Project.
