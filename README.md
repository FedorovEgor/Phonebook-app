# Phonebook-app
Simple project with REST api for managing users and contacts in their phonebooks.


# How to run the service
This is a Spring Boot project built with Java 8 and Maven. You can run the app using one of the following ways:
  - Build the project with mvn -install and then run it as a packaged application with java -jar target/PhoneContactsApp-1.0.jar.
  - Run the project via your favourite IDE.

# How to use REST api in this project.
When you run the application REST api will be exposed under the URLs: http://localhost:5000/api/v1/users and http://localhost:5000/api/v1/contacts for contacts.
You can access this URL via browser or other programms for testing http requests like Postman (which I highly recommend), but there will be empty users and contacts listls when tou run it. So I recommend to use POST requests to populate it first. You can also change server port in the application.properties file, which is in the resource folder.

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
      You'll get 200 OK respone if there are users in the list or 404 NOT FOUND if there are none.
   - GET http method http://localhost:5000/api/v1/users/{userId} to get a single user if it exists. Pass a user ID as a number in this request.
      You'll get 200 OK response if there is a user with such ID or 404 NOT FOUND if user doesn't exist.
   - PUT http method http://localhost:5000/api/v1/users/{userId} to update existing user. Pass a user ID as a number in this request, it will also require Json body as a  parameter. 
      You'll get 200 OK response if user successfully updated or 304 NOT MODIFIED if something went wrong during updating proccess or if there is no user with this ID.
   - DELETE http method http://localhost:5000/api/v1/users/{userId} to delete user from Phonebook app. Pass a user ID as a number int this request.
      You'll get 200 OK response if user successfully deleted or 304 NOT MODIFIED.
   - GET http method http://localhost:5000/api/v1/users/q={searchedName} to get list of users with a name as searched string. Pass a user name as a string in this request.
      You'll get 200 OK respone if there are users in the list or 404 NOT FOUND if there are none.
      
 You can also add and get contacts to existing user:
   - POST http method http://localhost:5000/api/v1/users/userId/contacts to add new contact to a user's phonebook.
      It provides 201 CREATED code in response.
   - GET http method http://localhost:5000/api/v1/users/userId/contacts to get list of user's contacts.
      It provides 200 OK or 400 BAD REQUEST or 404 NOT FOUND codes in response.
   - GET http method http://localhost:5000/api/v1/users/userId/contacts/q=phoneNumber allows you to get contact in user's phonebook by phone number (e.g. 89214897788).
      If you pass not a valid phone number as a parameter you'll get 400 BAD REQUEST code response. Or you'll get 200 OK response or 404 NOT FOUND response.
   - DELETE http method http://localhost:5000/api/v1/users/userId/contacts/contactId allows to delete contact from user's phonebook.
      
