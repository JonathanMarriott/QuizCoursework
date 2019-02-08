package server.controllers;

import org.json.simple.JSONObject;
import server.Logger;
import server.models.QuestionAttempt;
import server.models.User;
import server.models.services.QuestionAttemptService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("/questionAttempt")
public class QuestionAttemptController {

    @Path("/add") // Handles requests on /quiz/search
    @POST // for POST requests
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Takes in URL encoded string
    @Produces(MediaType.TEXT_PLAIN) //return JSON to browser
    public String addAttempt(@CookieParam("sessionToken") Cookie sessionCookie, // takes in session cookie
                             @FormParam("questionID") int questionID,
                             @FormParam("attemptID") int attemptID,
                             @FormParam("correct") boolean correct) { //takes in the entered search item
        Logger.log("Request on /questionAttempt/add");
        User currentUser = UserController.validateSessionCookie(sessionCookie); //check the session token is valid
        if(currentUser==null) { // If token is invalid error is returned
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error", "Invalid user session token");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else{
            QuestionAttempt currentAttempt = new QuestionAttempt(attemptID,questionID,correct);
            if(!QuestionAttemptService.insert(currentAttempt).equals("OK")){
                return "Error: Could not record question attempt"; // adds an error to the JSON object
            }
            else{
                return "OK";
            }
        }
    }
}
