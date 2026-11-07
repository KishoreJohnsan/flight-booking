# flight-booking

Backend for a flight booking app (the emails are signed "Intercontinental Escapes"), built as a training assignment. The frontend is in [flight-booking-frontend](https://github.com/KishoreJohnsan/flight-booking-frontend), an Angular app.
Admins manage airlines and flight schedules. Users search flights, book and cancel tickets, download a PDF ticket, and get a confirmation email.
It's a set of Spring Boot services behind an API gateway, each with its own MySQL database.

## Services

| Service | Port | What it does |
|---|---|---|
| `discoveryserver` | 8761 | Eureka server. The other services register here. |
| `gateway` | 3000 | Spring Cloud Gateway, the entry point. |
| `authservice` | 3050 | Registration, login, JWT and role checks. Calls the other services on the user's behalf. |
| `airlineservice` | 3060 | Airlines (CRUD). Also publishes to Kafka. |
| `flightscheduleservice` | 3070 | Flight schedules (CRUD), search by source and destination. |
| `bookingservice` | 3080 | Bookings, cancellations, PDF tickets. |
| `discountservice` | 3090 | Discount codes (CRUD). |
| `emailservice` | n/a | An AWS Lambda handler, not a server. Sends the booking and cancellation emails. |

## How requests flow

Everything goes through the gateway on port 3000. Requests to `/api/v1/service/**` go to `authservice`, which handles register and login, checks the JWT and role, and then calls the other services through Eureka.

The gateway also routes `/api/v1/flight/{airlines,schedules,bookings,discounts}/**` straight to each service. Only `authservice` has Spring Security, so those direct routes have no login check. The frontend only uses the `/service` routes.

## API

All paths below are under `http://localhost:3000/api/v1/service`.

**Public:** `POST /register`, `POST /authenticate` (returns a JWT), `GET /scheduleByStn/{src}/{dest}`.

**Admin** (`/admin/...`, role `ADMIN`):

| Method | Path |
|---|---|
| GET | `/airline`, `/airlineById/{id}`, `/airline/{name}` |
| POST, PUT, DELETE | `/airline` |
| GET | `/schedule`, `/scheduleById/{id}` |
| POST, PUT, DELETE | `/schedule` |

**User** (`/user/...`, role `USER`):

| Method | Path |
|---|---|
| GET | `/schedule/{src}/{dest}`, `/scheduleById/{id}` |
| GET | `/bookingByUser/{user}`, `/bookingById/{id}` |
| POST | `/booking` |
| PUT | `/cancelBooking/{id}` |
| DELETE | `/booking` |
| GET | `/downloadTicket/{id}` (PDF), `/sendEmail/{id}` |

Send the token as `Authorization: Bearer <token>`.

## Email and Kafka

- **Email:** `bookingservice` posts the booking to the Lambda URL in `aws.lambda.url`. The handler in `emailservice` builds the message and sends it over SMTP. The code uses Mailtrap's sandbox, so mail lands in a Mailtrap inbox and not at real addresses.
- **Kafka:** `GET /api/v1/flight/airlines/airline/{name}` publishes the airline to the `airline_topic` topic. `authservice` listens and prints it to the console.

## Stack

- Java 11, Spring Boot 2.6.7, Spring Cloud 2021.0.2
- Spring Cloud Gateway, Netflix Eureka
- Spring Data JPA with MySQL
- Spring Security with JWT
- Kafka
- iText for PDF tickets
- AWS Lambda with JavaMail
- springdoc OpenAPI

## Running it locally

You need Java 11, MySQL on `localhost:3306`, and Kafka on `localhost:9092`.

1. Create five empty databases: `userdetails`, `airlines`, `flightschedule`, `booking`, `discount`. The tables are created on startup.
2. In each service's `application.properties`, set the MySQL password to match your local `root` user.
3. In `bookingservice/src/main/resources/application.properties`, the ticket and logo paths are absolute Windows paths (`D:/...`). Point them at the files under `src/main/resources/static`. Relative paths would work on any OS.
4. Start the services in this order, each in its own terminal, from its own directory:

```sh
./mvnw spring-boot:run
```

`discoveryserver`, then `airlineservice`, `flightscheduleservice`, `bookingservice`, `discountservice`, `authservice`, and finally `gateway`. `emailservice` isn't started locally. It runs as a Lambda.
