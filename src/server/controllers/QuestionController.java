package server.controllers;

import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Logger;
import server.models.Question;
import server.models.User;
import server.models.services.QuestionService;
import server.models.services.QuizService;

import java.util.UUID;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static server.models.Question.questions;

@Path("/question")
public class QuestionController {

    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA) // multipart data due to file upload
    @Produces(MediaType.APPLICATION_JSON) // returns JSON to browser
    public String addQuiz(@CookieParam("sessionToken") Cookie sessionCookie, //Takes in the session token cookie
                          @FormDataParam("question") String question, /// all the form fields are accepted
                          @FormDataParam("answer1") String answer1,
                          @FormDataParam("answer2") String answer2,
                          @FormDataParam("answer3") String answer3,
                          @FormDataParam("answer4") String answer4,
                          @FormDataParam("checkAns1") String checkAns1,
                          @FormDataParam("checkAns2") String checkAns2,
                          @FormDataParam("checkAns3") String checkAns3,
                          @FormDataParam("checkAns4") String checkAns4,
                          @FormDataParam("explanation") String explanation,
                          @FormDataParam("quizID") String quizID,
                          @FormDataParam("picture") InputStream uploadedInputStream, // Actual file data
                          @FormDataParam("picture") FormDataContentDisposition fileDetail){ //Contains the file type& name
        Logger.log("Request on /quiz/create/ ");
        User currentUser = UserController.validateSessionCookie(sessionCookie); // Checks the session cookie
        String newFileName = ""; // Declared here for correct scope
        if(currentUser == null){ // Checks the session token was valid
            Logger.log("Error: Invalid user session token"); // Logs the user was not found
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Invalid user session token");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else if(quizID.equals("undefined") || QuizService.selectById(Integer.parseInt(quizID))==null){ // Checks the quizID is valid
            Logger.log("Error: Invalid quizID"); // Logs the user was not found
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Invalid quizID");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else if(explanation.length()>800){// Checks the explanations is less than 800 characters
            Logger.log("Error: Explanation too long"); // Logs the user was not found
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Explanation too long (max 800 characters)");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else if(!fileDetail.getFileName().equals("")){ //Checks if a file was uploaded
            Logger.log("File Detected");
            String fileName = fileDetail.getFileName(); // Gets the filename
            int dot = fileName.lastIndexOf('.');//finds where the dot is to get the file extension
            String fileExtension = fileName.substring(dot + 1);//get file extension from fileName
            if(!(fileExtension.equals("jpg") ||fileExtension.equals("png"))){
                Logger.log("Error: Non image file uploaded"); // Logs the user was not found
                JSONObject response = new JSONObject(); // Creates new JSON object
                response.put("error","Non image file uploaded");// adds an error to the JSON object
                return response.toString(); // returns the JSON object with the error
            }
            newFileName = "img/" + UUID.randomUUID() + "." + fileExtension;  //create a new unique identifier for file and append extension
            String uploadedFileLocation = "C:\\Users\\Jonathan\\Documents\\QuizCoursework\\resources\\client\\"+newFileName; //location to save the file
            try {//Attempts to output the file to the above location
                OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
                int read = 0;
                byte[] bytes = new byte[1024];
                out = new FileOutputStream(new File(uploadedFileLocation));
                while ((read = uploadedInputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
            } catch (IOException e) { // If any errors occur they are caught and sent to the log & client
                e.printStackTrace();
                JSONObject response = new JSONObject(); // Creates new JSON object
                response.put("error",e.toString());// adds an error to the JSON object
                return response.toString(); // returns the JSON object with the error
            }
        }
        QuestionService.selectAllInto(questions); //Needed to find the next ID
        //below creates a Question object of the users question
        Question currentQuestion = new Question(Question.nextId(),Integer.parseInt(quizID),question,newFileName,explanation);
        if(QuestionService.insert(currentQuestion).equals("OK")){ //inserts the question into the database & checks if successful
            Logger.log("Question added to DB"); //below creates a 2Dim String array of the answers & if they are right
            String ansArray[][] = {{answer1,answer2,answer3,answer4},{checkAns1,checkAns2,checkAns3,checkAns4}};
            if (AnswerController.addAnswers(ansArray,currentQuestion.getQuestionID()).equals("error")) { // Sends the array & the question ID to be saved
                JSONObject response = new JSONObject(); // Creates new JSON object
                response.put("error", "Error: Could not add Answers to database");// adds an error to the JSON object
                return response.toString(); // returns the JSON object with the error
            }
            else{
                JSONObject response = currentQuestion.toJSON(); //All details saved & question data put into JSON
                JSONArray allAns = new JSONArray();
                JSONArray checkAnsArray = new JSONArray();
                allAns.add(answer1);
                allAns.add(answer2);
                allAns.add(answer3);
                allAns.add(answer4);
                checkAnsArray.add(checkAns1);
                checkAnsArray.add(checkAns2);
                checkAnsArray.add(checkAns3);
                checkAnsArray.add(checkAns4);
                response.put("answers",allAns);
                response.put("checkAns",checkAnsArray);
                return response.toString(); // Sends the JSON of the saved question & answers back to the browser
            }
        }
        else { // returns an error if the question could not be added
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Error: Could not add question to database");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error

        }

    }
}
