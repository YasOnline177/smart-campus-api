# Smart Campus API

## Overview

This API simulates a smart campus system for managing rooms, sensors, and sensor readings. It is built using JAX-RS (Jersey) and deployed on Apache Tomcat.

The system supports:

- Room management (create, retrieve, delete)
- Sensor management with validation
- Sensor reading using sub-resource locators
- Filtering using query parameters
- Proper HTTP status codes with exception handling
- Centralised request/response logging using filters

---

## Setup Instructions

1. Clone the repository:
	https://github.com/YasOnline177/smart-campus-api.git

2. Open the project in NetBeans
3. Ensure Apache Tomcat 10 is installed and configured
4. Build the project:
	`mvn clean install`

5. Run the project in NetBeans (Tomcat)
6. Access the API at:
	http://localhost:8080/SmartCampusAPI/api/v1

---

## Sample curl Commands
### 1. Get API Info

```
curl http://localhost:8080/SmartCampusAPI/api/v1 
```

---
### 2. Create a Room

```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \ 
-H "Content-Type: application/json" \ 
-d '{"id":"R1","name":"Lab A","capacity":30}'
```

---
### 3. Get All Rooms

```
curl http://localhost:8080/SmartCampusAPI/api/v1/rooms
```

---
### 4. Create a Sensor

```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \ 
-H "Content-Type: application/json" \ 
-d '{"id":"S1","type":"CO2","status":"ACTIVE","currentValue":0.0,"roomId":"R1"}'
```

---
### 5. Get Filtered Sensors

```
curl http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2
```

---
### 6. Add Sensor Reading

```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/readings \
-H "Content-Type: application/json" \
-d '{"id":"R1","value":450.5,"timestamp":1710000000}'
```