package server.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Logger;
import server.models.*;
import server.models.services.AnswerService;
import server.models.services.QuestionService;
import server.models.services.QuizService;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import static server.models.Answer.answers;
import static server.models.Question.questions;
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

    @Path("/search") // Handles requests on /quiz/search
    @POST // for POST requests
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Takes in URL encoded string
    @Produces(MediaType.APPLICATION_JSON) //return JSON to browser
    public String searchQuiz(@CookieParam("sessionToken")Cookie sessionCookie, // takes in session cookie
                          @FormParam("search") String search) { //takes in the entered search item
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
            for(Quiz currentQuiz: quizs){ // Goes through each quiz
                //Below checks if the quiz title contains the search item (case insensitive)
                if (currentQuiz.getQuizTitle().toLowerCase().contains(search.toLowerCase())){
                    // Finds the user who made the quiz from the database
                    User quizUser = UserService.selectById(currentQuiz.getUserID());
                    JSONObject quizData = currentQuiz.toJSON(); //Turns the quiz into JSON
                    quizData.put("first",quizUser.getFirstName()); // Adds the users names to the JSON
                    quizData.put("last",quizUser.getLastName());
                    matches.add(quizData); //Adds the quiz's JSON to the array
                }
            }
            JSONObject response = new JSONObject(); // Creates new JSON object for response
            response.put("quizzes", matches);// adds all matched quizzes to the response
            return response.toString(); // returns the JSON object with the quizzes
        }
    }


    @Path("/play/{id}") // takes url parameter
    @POST
    @Produces(MediaType.APPLICATION_JSON) //return JSON to browser
    public String playQuiz(@CookieParam("sessionToken")Cookie sessionCookie, // takes in session cookie
                             @PathParam("id") String quizID) { //takes in the ID in the URL of API
        Logger.log("Request on /quiz/play for: " + quizID);
        User currentUser = UserController.validateSessionCookie(sessionCookie); //check the session token is valid
        Quiz theQuiz = QuizService.selectById(Integer.parseInt(quizID)); // finds the quiz in the DB
        if (currentUser == null) { // If token is invalid error is returned
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error", "Invalid user session token");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else if(theQuiz==null){ //checks the quiz exists in the database
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error", "Quiz not found");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else {
            QuestionService.selectAllInto(questions); // gets all the questions from DB
            AnswerService.selectAllInto(answers); // gets all the answers from the DB
            JSONObject quizResponse = theQuiz.toJSON(); // Puts the question into the JSON response
            JSONArray questionArray = new JSONArray(); // creates an array for the questions
            for(Question currentQuestion: questions){ // goes through all the questions
                if (currentQuestion.getQuizID()==theQuiz.getId()){ // Checks if the question is for requested Quiz
                    Logger.log("Found Question: "+currentQuestion.getQuestionID());
                    JSONArray answerArray = new JSONArray(); // If true sets up array to find the answers
                    for(Answer currentAnswer: answers){ // goes through all the answers
                        // checks if the answer is for the current question
                        if(currentAnswer.getQuestionID()==currentQuestion.getQuestionID()){
                            Logger.log("Found Answer: "+currentAnswer.getAnswerID());
                            answerArray.add(currentAnswer.toJSON()); // Adds the answer to the array
                        }
                    }
                    // adds the question and its array of answers to the question array
                    JSONObject questionData = currentQuestion.toJSON();
                    questionData.put("answers",answerArray);
                    questionArray.add(questionData);
                }
            }
            // Adds the question array to the response
            quizResponse.put("questions",questionArray);
            //Creates the quiz attempt
            QuizAttempt currentQuizAttempt = QuizAttemptController.addAttempt(currentUser.getId(),theQuiz.getId());
            if (currentQuizAttempt == null){
                JSONObject response = new JSONObject(); // Creates new JSON object
                response.put("error", "Could not record quiz attempt");// adds an error to the JSON object
                return response.toString(); // returns the JSON object with the error
            }
            quizResponse.put("attemptID",currentQuizAttempt.getAttemptID()); // adds the attempt id to response
            return quizResponse.toString(); // return the JSON response to the browser

        }
    }
}
