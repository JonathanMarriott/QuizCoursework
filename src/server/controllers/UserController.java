package server.controllers;

import org.json.simple.JSONObject;
import server.Logger;
import server.models.User;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("user/")
public class UserController {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String attemptLogin(@FormParam("email") String email,
                               @FormParam("password") String password) {

        Logger.log("/user/login - Attempt by " + email);
        User currentUser = UserService.selectByEmail(email);
        if (currentUser != null) {
            if (!currentUser.getPassword().equals(password)) {
                return "Error: Incorrect password";
            } else {
                String token = UUID.randomUUID().toString();
                currentUser.setSessionToken(token);
                String success = UserService.update(currentUser);
                if (success.equals("OK")) {
                    return token;
                } else {
                    return "Error: Can't create session token.";
                }
            }
        } else {
            return "Error: Can't find user account.";
        }
    }
    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String addUser(@FormParam("email") String email,
                          @FormParam("firstName") String firstName,
                          @FormParam("lastName") String lastName,
                          @FormParam("password") String password,
                          @FormParam("password2") String password2) {

        Logger.log("/user/add - Attempt by " + email);
        if (!password.equals(password2)){
            return "Error: Passwords do not match";
        }
        else if(UserService.selectByEmail(email)!= null) {
            return "Error: Account with that email already exists";
        }
        else{
            String token = UUID.randomUUID().toString();
            User signUpInfo = new User(-1,firstName,lastName,email.toLowerCase(),password,token);
            if(UserService.insert(signUpInfo).equals("OK")) {
                return token;
            }
            else {
                return "Error: Unable to create new user";

            }

        }
    }
    @GET
    @Path("check")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkLogin(@CookieParam("sessionToken") Cookie sessionCookie) {

        Logger.log("/user/check - Checking user against database");

        User currentUser = validateSessionCookie(sessionCookie);

        if (currentUser == null) {
            Logger.log("Error: Invalid user session token");
            JSONObject response = new JSONObject();
            response.put("error","Invalid user session token");
            return response.toString();
        } else {
            return currentUser.toJSON().toString();
        }
    }

    public static User validateSessionCookie(Cookie sessionCookie) {
    if (sessionCookie != null) {
        String token = sessionCookie.getValue();
        User currentUser = UserService.selectBySessionToken(token);
        if (currentUser != null) {
            Logger.log("Valid session token received.");
            return currentUser;
        }
    }
    return null;
}
}