# Phonebook-app
Simple project with REST api for managing users and contacts in their phonebooks.


# How to run the service
This is a Spring Boot project built with Java 8 and Maven. You can run the app using one of the following ways:
  - Build the project with mvn -install and then run it as a packaged application with java -jar target/PhoneContactsApp-1.0.jar.
  - Run the project via your favourite IDE.

# How to use REST api in this project.
When you run the application REST api will be exposed under the URLs: http://localhost:5000/api/v1/users and http://localhost:5000/api/v1/contacts for contacts.
You can access this URL via browser or other programms for testing http requests like Postman(which I highly recommend), but there will be empty users and contacts listls when tou run it. So I recommend to use POST requests to populate it first.

Also for complete documentation I used Swagger, so after you run an app you can access a full list of API requests via the URL http://localhost:5000/swagger-ui.html on your browser.

Here are some samples of REST methods calls:
   - POST http method http://localhost:5000/api/v1/users to add new user, it requires Json body as a parameter 
   
       {
        "id": 0,
        "firstName": "string",
        "lastName": "string"
        }

    if you successfully create new user there will be response code 201 - CREATED.
    
    - GET http method http://localhost:5000/api/v1/users to get a list of all users as a Json.
      You'll get 201 OK respone if there are users in the list or 404 NOT FOUND if there are none.
    - GET http method http://localhost:5000/api/v1/users/userId to get a single user if it exists.
      You'll get 201 OK response if there is a user with such ID or 404 NOT FOUND if such a user doesn't exist.
    - PUT http method http://localhost:5000/api/v1/users/userId to update existing user. It will also require Json body as a parameter.
      You'll get 201 OK response if user successfully updated or 304 NOT MODIFIED if something went wrong during updating proccess or if there is no user with this ID.
    - DELETE http method http://localhost:5000/api/v1/users/userId to delete user from Phonebook app.
      You'll get 201 OK response if user successfully deleted or 304 NOT MODIFIED.
      
 You can also add contacts to existing user:
   - POST http method http://localhost:5000/api/v1/users/userId/contacts to add new contact to a user's phonebook.
