# Conference room booking-system

Steps to run the application.
* Fetch the application for this repository.
* Change directory to the booking folder.
* Run mvn clean install.
* Run mvn spring-boot:run to start the application.
* Browse to http://localhost:8080/swagger-ui/index.html to open Swagger document so that you can start testing the application.
* We use H2 in memory database to store test data and on start up the DB will be initialised with the required info for conference rooms and maintenance slots  
* There are 2 APIs exposed for the project , one for booking and another for listing available slots for a given interval as can be seen on Swagger document.
* To book a room here is the sample request

```JSON
        {
            "startTime": "2023-11-10T09:15:54.561Z",
            "endTime": "2023-11-10T09:30:54.561Z",
            "userId": "12345",
            "numOfAttendees": 3
        }
```
* To list available conference rooms for a given period here is the sample request
```JSON
{
  "startTime": "2023-11-10T09:15:54.561Z",
  "endTime": "2023-11-10T09:30:54.561Z"
}
```
NB!!! Please keep the time format the same as in the shared request as I am taking this as the 24-hour format to use for the test


For the purposes of this test , I am taking that the test application would be run using an IDE like IntelliJ otherwise I would add extra steps for packaging the app into a jar / image
