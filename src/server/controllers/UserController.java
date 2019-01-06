package server.controllers;

import org.json.simple.JSONObject;
import server.Logger;
import server.models.User;
import server.models.services.UserService;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("user/") // Covers API requests under /user/
public class UserController {

    @POST // Handles Post requests
    @Path("login") // API call on  /user/login
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Accepts form data
    @Produces(MediaType.TEXT_PLAIN) // returns string
    public String attemptLogin(@FormParam("email") String email, // Takes form fields as parameters
                               @FormParam("password") String password) {

        Logger.log("/user/login - Attempt by " + email); // logs the login attempt
        User currentUser = UserService.selectByEmail(email); // Query database for entry with specified email
        if (currentUser != null) { // check if an entry was found
            if (!currentUser.getPassword().equals(password)) { // checks the password entered matches the database password
                return "Error: Incorrect password"; //No match means the client receives error
            } else {
                String token = UUID.randomUUID().toString(); // Random token generated for the user
                currentUser.setSessionToken(token); // Stores the the new sessionToken created for this login in the object
                String success = UserService.update(currentUser); //Updates the database with the modified object
                if (success.equals("OK")) { // Checks for Database errors
                    return token; // Returns the token to the client for later authentication
                } else {
                    return "Error: Can't create session token."; //returns an error to the client if database error
                }
            }
        } else {
            return "Error: Can't find user account."; // returns error to the client if the account is not found
        }
    }
    @POST// Handles Post requests
    @Path("add") //  bound to /user/add
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) //Accepts Form data
    @Produces(MediaType.TEXT_PLAIN) //returns a string
    public String addUser(@FormParam("email") String email,  //Takes the form fields as parameters
                          @FormParam("firstName") String firstName,
                          @FormParam("lastName") String lastName,
                          @FormParam("password") String password,
                          @FormParam("password2") String password2) {

        Logger.log("/user/add - Attempt by " + email); // logs the sign up request to the console
        if (!password.equals(password2)){  // Checks the two passwords are the same
            return "Error: Passwords do not match";// returns an error if passwords do not match
        }
        else if(UserService.selectByEmail(email)!= null) { // checks there is no other user with the same email
            return "Error: Account with that email already exists";// returns an error if a users email is not unique
        }
        else{
            String token = UUID.randomUUID().toString(); // creates a session token for the new user
            User signUpInfo = new User(-1,firstName,lastName,email,password,token); // creates object with the new user data
            // ID is set to -1 above as it is not sent to Database due to use of autoincrement on that field
            if(UserService.insert(signUpInfo).equals("OK")) { // checks for any database errors
                return token; //returns the sessionToken to the client if the sign up is successful
            }
            else {
                return "Error: Unable to create new user"; //returns an error if adding the user was unsuccessful
            }
        }
    }
    @POST // Handles Post requests
    @Path("delete")  // bound to /user/delete
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Takes in form data
    @Produces(MediaType.TEXT_PLAIN) // returns a string
    public String deleteUser(@FormParam("password") String password, // Form fields are set as parameters
                             @FormParam("password2") String password2,
                             @CookieParam("sessionToken") Cookie sessionCookie // Takes the client sessionToken cookie
    ) {
        User currentUser = validateSessionCookie(sessionCookie); // calls the method to get the users data from the DB
        Logger.log("/user/delete - Attempt by sessionToken" + sessionCookie); // logs the delete attempt
        if(currentUser == null) { // checks the Session token was found in the database
            return "Error: Invalid user session token"; // If the user was not found an error is returned
        }
        else if (!password.equals(password2) || !password.equals(currentUser.getPassword())){ //Checks the 2 passwords entered matched the DB password
            return "Error: Passwords do not match or incorrect password"; // If the passwords dont match an error is returned
        }
        else{
            if(UserService.deleteById(currentUser.getId()).equals("OK")) { // Calls the delete method in the service class
                return "OK"; // Returns OK if the account was sucessfully deleted
            }
            else {
                return "Error: Unable to delete user account"; // Returns an error if the DB could not delete the account

            }

        }
    }
    @GET // Handles Get requests
    @Path("check") // Bound to /user/check
    @Produces(MediaType.APPLICATION_JSON) // Returns JSON to the client
    public String checkLogin(@CookieParam("sessionToken") Cookie sessionCookie) { // Takes the sessionToken cookie as a parameter

        Logger.log("/user/check - Checking user against database"); // Logs the User is being check against the database
        User currentUser = validateSessionCookie(sessionCookie); // Calls method to get the user with matching sessionID from the DB

        if (currentUser == null) { // Checks if the record was not found
            Logger.log("Error: Invalid user session token"); // Logs the user was not found
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Invalid user session token");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        } else {
            return currentUser.toJSON().toString(); // returns the user in the JSON format
        }
    }

    public static User validateSessionCookie(Cookie sessionCookie) { // Takes a session cookie as a parameter
    if (sessionCookie != null) { // Checks the cookie contains data
        String token = sessionCookie.getValue(); // Gets the string token value from the cookie
        User currentUser = UserService.selectBySessionToken(token); // Finds the user record in the DB which matching sessionToken
        if (currentUser != null) { //Checks the user record was found
            Logger.log("Valid session token received."); //Logs the session token was authenticated
            return currentUser; //returns the user object with matching session token
        }
    }
    return null;// If user is not found return null
}
}