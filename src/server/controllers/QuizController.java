package server.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Logger;
import server.models.Quiz;
import server.models.User;
import server.models.services.QuizService;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import static server.models.Quiz.quizs;

@Path("/quiz")
public class QuizController {

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Takes in URLencoded string
    @Produces(MediaType.APPLICATION_JSON) //return JSON to browser
    public String addQuiz(@CookieParam("sessionToken")Cookie sessionCookie, // takes in session cookie
                          @FormParam("title") String title){ //takes in the entered title
        Logger.log("Request on /quiz/create");
        User currentUser = UserController.validateSessionCookie(sessionCookie); //check the session token is valid
        if(currentUser==null){ // If token is invalid error is returned
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Invalid user session token");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }else if(title.length()>60){ // checks if title is less than 60 characters
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Title too long (max 60 characters)");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else{
            QuizService.selectAllInto(quizs); //Gets all the quiz records into the static list, used to find the next ID
            Quiz currentQuiz = new Quiz(Quiz.nextId(),title,currentUser.getId(),0);//creates a new object of quiz
            if(QuizService.insert(currentQuiz).equals("OK")){ //inserts the record into the database
                Logger.log("Quiz added to DB");
                return currentQuiz.toJSON().toString(); //returns the id title & other info to the browser
            }
            else{// returns an error if the insert into the database fails
                JSONObject response = new JSONObject(); // Creates new JSON object
                response.put("error","Quiz could not be created");// adds an error to the JSON object
                return response.toString(); // returns the JSON object with the error
            }

        }
    }

    @Path("/search")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Takes in URLencoded string
    @Produces(MediaType.APPLICATION_JSON) //return JSON to browser
    public String searchQuiz(@CookieParam("sessionToken")Cookie sessionCookie, // takes in session cookie
                          @FormParam("search") String search) { //takes in the entered title
        Logger.log("Request on /quiz/search for:"+search);
        User currentUser = UserController.validateSessionCookie(sessionCookie); //check the session token is valid
        if(currentUser==null) { // If token is invalid error is returned
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error", "Invalid user session token");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else{
            QuizService.selectAllInto(quizs);// Gets all quizzes from the database
            JSONArray matches = new JSONArray(); // Creates JSON array for matching quizzes
            for(Quiz currentQuiz: quizs){

                if (currentQuiz.getQuizTitle().toLowerCase().contains(search.toLowerCase())){
                    Logger.log(currentQuiz.getQuizTitle());
                    User quizUser = UserService.selectById(currentQuiz.getUserID());
                    JSONObject quizData = currentQuiz.toJSON();
                    quizData.put("first",quizUser.getFirstName());
                    quizData.put("last",quizUser.getLastName());
                    matches.add(quizData);
                }
            }
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("quizzes", matches);// adds all matches to the response
            Logger.log(response.toString());
            return response.toString(); // returns the JSON object with the quizzes

        }
    }
}
