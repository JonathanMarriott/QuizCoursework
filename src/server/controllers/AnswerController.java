package server.controllers;

import server.Logger;
import server.models.Answer;
import server.models.services.AnswerService;

import static server.models.Answer.answers;

public class AnswerController {

    public static String addAnswers(String[][] ansArray,int questionID){ //  takes in array of answers and the questionID they are for
        for(int i =0;i<ansArray[0].length;i++){ // loops through array
            boolean correct;// Declared here for larger scope
            //null= unticked, "on"= ticked
            if(ansArray[1][i]==null){correct = false;} // checks if the checkbox was ticked
            else{correct = true;}// if ticked answer is correct
            AnswerService.selectAllInto(answers); // Used to find the next ID
            Answer currentAns = new Answer(Answer.nextId(),questionID,ansArray[0][i],correct); // Forms an Answer object
            if(!AnswerService.insert(currentAns).equals("OK")){return "error";}// sends to database, returns error if it fails
        }
        return "OK";
    }
}
