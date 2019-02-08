package server.controllers;

import server.models.QuizAttempt;
import server.models.services.QuizAttemptService;
import java.text.SimpleDateFormat;
import java.util.Date;

import static server.models.QuizAttempt.quizattempts;

public class QuizAttemptController {

    public static QuizAttempt addAttempt(int userID, int quizID){
        QuizAttemptService.selectAllInto(quizattempts); // Gets all the quiz attempts from DB
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy"); // Defines the date format
        String date = format.format(new Date()); // Gets the date into a string of specified format
        QuizAttempt currentAttempt = new QuizAttempt(QuizAttempt.nextId(),userID,quizID,date);
        if(!QuizAttemptService.insert(currentAttempt).equals("OK")){ // Inserts the above object into DB, checks for error
            return null; // if error found then returns null
        }
        return currentAttempt; // if successful the QuizAttempt object is returned
    }
}
