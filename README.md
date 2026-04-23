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
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d '{"id":"R1","name":"Lab A","capacity":30}'
```

---
### 3. Get All Rooms

```
curl http://localhost:8080/SmartCampusAPI/api/v1/rooms
```

---
### 4. Create a Sensor

```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d '{"id":"S1","type":"CO2","status":"ACTIVE","currentValue":0.0,"roomId":"R1"}'
```

---
### 5. Get Filtered Sensors

```
curl "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2"
```

---
### 6. Add Sensor Reading

```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/readings -H "Content-Type: application/json" -d '{"id":"R1","value":450.5,"timestamp":1710000000}'
```

---

## Report Questions

### Question 1: JAX-RS Resource Lifecycle

By default, JAX-RS resource classes follow a per-request lifecycle, meaning a new instance of the resource class is created for each incoming HTTP request. This ensures that instance variables are not shared between requests, which improves thread safety at the object level.

However, shared data structures such as rooms and sensors are stored in static collections that are accessed by multiple threads concurrently, so thread safety must still be managed carefully. If a standard HashMap is used, concurrent access can lead to race conditions and data inconsistency. To address this, ConcurrentHashMap is used instead, as it supports safe concurrent read and write operations without requiring manual synchronisation.

### Question 2: HATEOAS (Hypermedia as the Engine of Application State)

HATEOAS is a core principle of RESTful design because it allows clients to dynamically discover available actions through hyperlinks embedded in API responses. Rather than relying on hardcoded knowledge of endpoints, the client navigates the API by following links provided by the server at runtime.

This benefits client developers in several ways. It reduces dependence on static documentation, and if the API structure changes, the client can still function correctly by reading the updated links from responses. It also improves the flexibility and maintainability of the overall system, as the server retains control over available interactions and guides the client through application state. In summary, HATEOAS makes APIs more self-descriptive and adaptable, allowing client implementations to evolve without requiring significant rework.

### Question 3: IDs vs Full Object Response

Returning only IDs produces a smaller response payload, which reduces network bandwidth usage and improves efficiency, particularly when handling large datasets. However, this approach requires the client to make additional follow-up requests to retrieve full details for each resource, increasing the total number of API calls and the amount of client-side processing required.

Returning full room objects, on the other hand, provides all necessary information in a single response. This simplifies client-side logic and eliminates the need for additional requests, though it does result in a larger payload that can increase bandwidth consumption. The appropriate choice therefore depends on the specific requirements of the system, balancing network efficiency against client simplicity and overall performance.

### Question 4: DELETE Idempotency

The DELETE operation is considered idempotent because performing the same request multiple times produces the same final state of the system. In this implementation, when a client sends a DELETE request for a room, the room is removed from the in-memory data structure if it exists. If the same request is sent again, the room is no longer present and no further changes occur to the system state.

This means that regardless of how many times the DELETE request is repeated, the system remains in the same consistent state following the first successful deletion. In cases where the room does not exist, the API returns an appropriate error response such as 404 Not Found, but the overall state of the system remains unchanged. The operation therefore satisfies the definition of idempotency.

### Question 5: @Consumes(MediaType.APPLICATION_JSON)

The @Consumes(MediaType.APPLICATION_JSON) annotation specifies that the endpoint only accepts requests containing a JSON payload. JAX-RS uses this information during request processing to determine whether it can handle the incoming request based on the Content-Type header provided by the client.

If a client sends data in an unsupported format such as text/plain or application/xml, JAX-RS will not find a suitable message body reader capable of converting the request body into the expected Java object. As a result, the request is rejected before it reaches the resource method. In such cases, JAX-RS automatically returns a 415 Unsupported Media Type response, indicating that the server cannot process the request due to the content type mismatch. This mechanism ensures that only correctly formatted requests are processed, preventing data parsing errors and maintaining consistency in API behaviour.

### Question 6: @QueryParam vs Path Parameter for Filtering

Using @QueryParam for filtering is generally preferred because query parameters are designed to represent optional criteria for searching or filtering a collection. For example, /sensors?type=CO2 clearly communicates that the request is retrieving a collection of sensors narrowed down by a specific attribute, without changing the identity of the resource being accessed. 

In contrast, embedding the filter value in the path such as /sensors/type/CO2 implies a hierarchical relationship between resources, which is semantically incorrect in this context. Path parameters are more appropriate for identifying a specific resource, for example /sensors/{id}, rather than expressing how a collection should be queried.

Query parameters also offer greater flexibility, as multiple filters can be combined without altering the URL structure, for example /sensors?type=CO2&status=ACTIVE. This makes the API more scalable, intuitive, and consistent with RESTful design principles. For these reasons, query parameters are the more appropriate choice for filtering and searching collections. 

### Question 7: Sub-Resource Locator Pattern

The sub-resource locator pattern improves API design by enabling a hierarchical and modular structure in which responsibility for handling nested resources is delegated to separate, dedicated classes. Rather than defining all nested endpoints within a single large resource class, each class is responsible only for its own specific logic. 

This approach offers several architectural benefits. It promotes separation of concerns, as each class focuses on a specific resource such as sensors or sensor readings, making the codebase easier to understand and maintain. It also improves scalability, as additional functionality can be introduced to sub-resource without increasing the complexity of the parent resource class. 

In contrast, defining all nested paths within a single controller class leads to code that is large, tightly coupled, and difficult to maintain or test as the application grows. By delegating logic to separate classes, the sub-resource locator pattern produces a cleaner, more modular API design that is particularly beneficial in larger applications. 

### Question 8: HTTP 422 vs 404

HTTP 422 Unprocessable Entity is considered more semantically accurate than 404 Not Found when the request is structurally valid but contains data that cannot be processed due to a business rule violation, such as referencing a room that does not exist when creating a sensor. 

A 404 response communicates that the requested endpoint or resource could not be found. However, in this scenario the endpoint is valid and the request is correctly formed. The issue lies with the content of the payload, not the location of the resource. Using 422 therefore provides more precise feedback to the client, clearly indicating that the request was understood but could not be fulfilled due to invalid reference data. This aligns more accurately with RESTful principles of meaningful and descriptive status code usage. 

### Question 9: Security Risks of Exposing Stack Traces

Exposing internal Java stack traces to external API consumers presents significant security risks because it reveals detailed information about the internal structure and implementation of the application. A stack trace can disclose class names, package structures, method names, file paths, and line numbers, which allows an attacker to understand how the system is designed and identify potential entry points for exploitation. 

Stack traces may also reveal the technologies and frameworks in use, enabling attackers to target known vulnerabilities associated with specific library versions. In some cases, internal logic or configuration details may be exposed, making it easier to craft malicious inputs or carry out targeted attacks such as injection or denial of service. 

To mitigate these risks, stack traces should never be included in API responses. Instead, a generic error message such as Internal Server Error should be returned to the client, while detailed diagnostic information is retained securely in server-side logs for internal debugging purposes only. 

### Question 10: Logging Using JAX-RS Filters

Using JAX-RS filters for cross-cutting concerns such as logging is advantageous because it centralises the logging logic in a single location. Rather than inserting logging statements inside every individual resource method, filters automatically intercept all incoming requests and outgoing responses, ensuring that logging is applied consistently across the entire application. 

This approach improves maintainability, as any changes to logging behaviour can be made in one place without requiring modification to multiple resource classes. It also promotes code reuse, reduces duplication, and results in resource methods that are cleaner and focused solely on business logic. 

Manually inserting Logger statements into each method, in contrast, produces repetitive code, introduces the risk of inconsistency, and makes the system increasingly difficult to maintain as it scales. Using filters therefore provides a more scalable, modular, and maintainable solution for managing cross-cutting concerns in JAX-RS application. 
