# spring-scheduled-example
Demo Application to illustrate Dynamic Scheduling (@scheduled) in spring boot  using REST API

# Steps to Build and run the Application

	mvn clean package

	docker build . -t springschedule-example:latest
	
	docker run  -p 8080:8080 -t springschedule-example:latest
	
# Check the Swagger end-point
  	
  	http://localhost:8080/swagger-ui.html