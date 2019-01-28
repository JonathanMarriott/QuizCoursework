package server.controllers;

import org.json.simple.JSONObject;
import server.Logger;
import server.models.Quiz;
import server.models.User;
import server.models.services.QuizService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("/quiz")
public class QuizController {
    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String addQuiz(@CookieParam("sessionToken")Cookie sessionCookie,
                          @FormParam("title") String title){
        Logger.log("Request on /quiz/create");
        User currentUser = UserController.validateSessionCookie(sessionCookie);
        if(currentUser==null){
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Invalid user session token");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else{
            Quiz currentQuiz = new Quiz(-1,title,currentUser.getId(),0);
            if(QuizService.insert(currentQuiz).equals("OK")){
                Logger.log("Quiz added to DB");
                return currentQuiz.toJSON().toString();
            }
            else{
                JSONObject response = new JSONObject(); // Creates new JSON object
                response.put("error","Quiz could not be created");// adds an error to the JSON object
                return response.toString(); // returns the JSON object with the error
            }

        }
    }
}
