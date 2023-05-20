# Lottery System
This is a back end service to handle a lottery system. Users can participate in open lottery events and play as many
ballots as they want if their balance is enough to buy a ballot. The ballots have a field called "guess", which is a 
string composed of numbers and has a fixed length (by default 6). There can only be one lottery event per
day and the event closes at midnight each day. After the event closes, a random winner ballot is selected. The winner 
participant earns the lottery prize, which gets added to their balance.
It is possible to schedule future lotteries. They automatically open when it's the date to start.

## Tools
- Java Development Kit (JDK) 17
- Spring Boot 3.0.4
- PostgreSQL database server (local or hosted) - optional.
- JUnit 5.5.2 and Mockito 3.11.2 are used for testing.

By default, the service uses the H2 in memory database. Steps to set up the PostgreSQL database can be found below: 
## PostgreSQL Database Setup
- Install PostgreSQL
- Use pgAdmin or the command line interface (psql) to create
a database with the desired name.
- Add the dependency to the `build.gradle` file and rebuild gradle:
  `runtimeOnly 'org.postgresql:postgresql'`
- Update the `application.properties` file with the database connection
details.
```
spring.datasource.url=jdbc:postgresql://[hostname/ip]:[port]/[database name]
spring.datasource.username=[username]
spring.datasource.password=[password]
```
### Build
`gradle build`

### Running
`gradle run`

After the server runs, the end points can be navigated from http://localhost:8080/

### Testing
`gradle test`