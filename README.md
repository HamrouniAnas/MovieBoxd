# Software Testing Lab

The MovieBoxd is an application that allows you to buy copies of movies.

## DevOps Lab

As part of The DevOps lab, we built a CI/CD pipeline using Github actions.

### Build and Test

Build and Test the backend with maven and save the jar file to use it later.
Build the Frontend with maven as well and save the jar file to use it later.

### Package

Package the frontend and the backend separately into two docker images using the generated jar files and push them to DockerHub.

The images are tagged with The commit **SHA**. 

### End-to-End Tests

* Create a ``.env`` file with the commit SHA that will be used by the ``docker-compose.yml`` file to pull the newly pushed Docker images.

* Build the cluster with ``docker-compose`` inside the Runner.

* Run the Cypress tests and save the generated video file as an artifact.

### Deployment 

* SSH into the EC2 instance with a secret key. The EC2 instance has docker and docker-compose installed and its security group has an inbound rule that exposes port 9000. 

* Create a ``.env`` file with the commit SHA and the MySQL parameters which will be used by the ``docker-compose.yml`` file.

* Pull from the git repository to ensure the ``docker-compose`` file is updated.

* Build the cluster with ``docker-compose``.

## Software Testing Lab

For the Software Testing Lab, we will perform these levels of tests :

- Unit Tests
- Integration Tests
- System Tests

### Unit Tests

Testing the different actions (functions) in the MovieController in the **Backend**.

Mockito was used to mock the calls for the movie repository that is used in the Unit Tests. That way, we can make sure we are truly isolating the functions and testing only the functionalities of the controller's actions.

The test can be run manually with :
```
cd Back
mvn test
```

### Integration Tests

Here we test the integration of different parts of the Backend.

I will be using a separate database for testing so that it won't affect the actual Database. 

For that, I used the **Testcontainers** Library. This Java library is used to create a lightweight, throwaway instance of MySQL database using a Docker image. The container can be controlled with Java code which is very convenient.

You can run the test manually with :
```
cd Back
mvn test
```

### System Tests

I used Cypress to do an End-to-End test for the whole application.

I have 3 different scenarios:

1.  Create a new movie
2.  Edit a movie
3.  Delete a movie

You can run the test manually with :
```
cd e2e-tests
npm run cypress:run
```
