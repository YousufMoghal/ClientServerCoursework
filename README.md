# ClientServerCoursework

Overview:

The Smart Campus API is a RESTful web service built using JAX-RS and deployed on Apache Tomcat. Its based on 3 main resources being Sensor, SensorReading and Room. The API uses JSON for request and response bodies. Core CRUD-style operations are implemented for rooms and sensors, and sensor readings can be retrieved or appended through a sub-resource path. Filtering is supported on the sensor collection using a query parameter (@QueryParam), for example ?type=CO2. This API also includes custom exception handling and logging. Specific error cases are handled using custom exceptions and exception mappers. A global exception mapper is used as a safety net for unexpected runtime errors, and JAX-RS filters log the HTTP method, URI, and final response status code for observability. This system, instead of using a database, uses in-memory data structures such as a HashMap and an ArrayList where shared data is stored in a centralised repository.

How to Build and Run the Project:

- Open Apache NetBeans.
- Choose File -> Open Project and open the ClientServerCW project folder.
- Make sure Apache Tomcat is added in NetBeans under the Services tab.
- Confirm that the project is configured as a Maven Web Application using Java EE 8 Web.
- Open pom.xml and allow Maven to download any required dependencies if prompted.
- Right-click the project and select Clean and Build.
- After the build succeeds, right-click the project and select Run.
- NetBeans will deploy the application to Apache Tomcat.

- Check the NetBeans output window for the deployed context path. In this project, the application runs at:
  http://localhost:8080/ClientServerCW/api/v1

- Open a browser or Postman and test the discovery endpoint:
  http://localhost:8080/ClientServerCW/api/v1

Curl Commands:

- Get all rooms: curl -X GET "http://localhost:8080/ClientServerCW/api/v1/rooms"
- Create a new room: curl -X POST "http://localhost:8080/ClientServerCW/api/v1/rooms" \
                    -H "Content-Type: application/json" \
                    -d "{\"id\":\"ROOM-CURL-1\",\"name\":\"Curl Demo Room\",\"capacity\":40,\"sensorIds\":[]}"
- Get a new room by its ID: curl -X GET "http://localhost:8080/ClientServerCW/api/v1/rooms/ROOM-CURL-1"
- Get all sensors: curl -X GET "http://localhost:8080/ClientServerCW/api/v1/sensors"
- Filter sensors by type: curl -X GET "http://localhost:8080/ClientServerCW/api/v1/sensors?type=CO2"
- Get readings for a sensor: curl -X GET "http://localhost:8080/ClientServerCW/api/v1/sensors/TEMP-001/readings"


Report Questions:

1.1) Typically, when working with JAX-RS resource classes they are request-scoped meaning that a new instance is created for each HTTP request. This usually helps to avoid direct conflicts between the different requests. However, in my coursework I used shared in-memory data structures, similar to a singleton, to store the data for the sensors and rooms that way all the data is from a single centralised point and each class has a global access point to the database.  As well as using a data store that acts as a singleton, I also used “ConcurrentHashMap” over “HashMap” as it provides better thread safety as it allows for multiple threads to read and write from the same data storage and reduces risk of data corruption. Another strategy I used was using “CopyOnWriteArrayList” so that is creates a new internal copy whenever the list is modified for safer iteration.

1.2) Hypermedia (HATEOAS) is considered a hallmark of advanced RESTful design because it reduces tight coupling and allows the client-side to need minimal knowledge on how to interact with the server. This is due to the fact that a client will be able to access the API through a simple RESTful URL call and allows the client to be guided dynamically by including links in the response in which the server sends back. This is beneficial as the client doesn’t need to hardcode all the URLs himself and can discover all the actions through the links given. However, with a static documentation any changes to the URL endpoints would result in the client’s application to break as the client needs  to manually update the code in each of his applications making it less flexible.

2.1) Returning only room IDs is definitely more bandwidth efficient as it reduces the size of the response payload. However, the client needs to send additional request in order to obtain more information. Returning full room objects uses more bandwidth in one call however it’s a lot more practical as it reduces the number of requests needed at the cost of an increased data transfer. In this coursework, returning full room objects is definitely the better outcome.

2.2) Idempotence in programming refers to when an operation or function produces the same result no matter how many times its executed, ensuring that additional calls does not change the systems state after the first call. DELETE is considered idempotent because the first DELETE removes the room and returns a 200 OK then every DELETE afterwards results in the same final state. In this specific API once a room is deleted, repeating the DELETE request will not change the result afterwards and the server state is unchanged even if the next requests return a different response like 404.

3.1) JAX-RS first inspects the Content-Type header before invoking the method. So, because @Consumes(MediaType.APPLICATION_json) annotation only accepts application/json, if any other content type is used, such as text/plain or application/xml, this will cause the runtime to immediately return a 415 Unsupported Media Type error which essentially means the request is rejected before reaching the resource method

3.2) Using @QueryParam is more appropriate because the base stays stable whilst representing a filtered view of the same resource collection rather than a different resource. Using Query Parameters allows for flexibility and multiple filters to be added with ease. On the contrary embedding filters using PathParam can make it appear as if a whole different resource is being accessed which is less consistent that using QueryParam.

4.1) Without a Sub-Resource locater pattern every nested path would pile up on one massive SensorResource class. The locater pattern delegates all the reading logic to a dedicated SensorReadingResource class so that each class has a single responsibility. The benefits of this are that it makes the code easier to read and extend as you build your API.
 
5.1) HTTP 422 is more semantically accurate than a standard 404 in this case because the request is syntactically correct and the endpoint exists, but the data provided is semantically invalid. A 404 Not Found means the URL itself cannot be found. In the case of the coursework the endpoint exists perfectly fine, but the value inside (roomId) references a room that doesn’t exist. A 422 Unprocessable Entity means that the server understood the request, but it cannot process the instructions, in our case because the referenced roomId doesn’t point to anywhere. Using 404 in this scenario would mislead the client into thinking that the endpoint itself is missing instead of the actual data.

5.2) Exposing stack traces can reveal sensitive information such as class names and package hierarchy in which the attacker can map out the entire application structure and identify which classes handle sensitive operations. It can also reveal frameworks and file paths from the server’s filesystem revealing directory structure and how the API was deployed. It can even get as bad as an attacker using an SQL injection or even direct data access if the exception originated from a database where the message includes raw SQL queries, table names or field values.

5.3) Using JAX-RS filters for logging is far superior compared to manually inserting Logger.info() calls into every resource method because filters implement a way in which its written once and then automatically applied to every endpoint in the API in the past as well as in the future. This is beneficial as it ensures there’s no risk of a developer forgetting to add logging to a new method and keeps the log format consistent along with keeping the resource methods clean and logical. Another reason as to why JAX-RS filters are better is because logging manually is a cross-cutting concern meaning it would apply to all parts of the application. I also used “ContainerRequestFilter” and ContainerResponseFilter” in my logging class as these interfaces allow me to provide structured access to the Method, URI and response status codes via the request and response context objects making it perfect to capture exactly what the specification requires without cluttering the actual code which would be significantly harder to achieve using manual logging.
