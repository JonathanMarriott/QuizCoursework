package server.controllers;

import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import server.Logger;
import server.models.Question;
import server.models.User;
import server.models.services.QuestionService;

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
    @Consumes(MediaType.MULTIPART_FORM_DATA) // Takes in form data
    @Produces(MediaType.APPLICATION_JSON)
    public String addQuiz(@CookieParam("sessionToken") Cookie sessionCookie,
                          @FormDataParam("question") String question,
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
                          @FormDataParam("picture") InputStream uploadedInputStream,
                          @FormDataParam("picture") FormDataContentDisposition fileDetail){
        Logger.log("Request on /quiz/create/ ");
        User currentUser = UserController.validateSessionCookie(sessionCookie);
        String newFileName = "";
        if(currentUser == null){
            Logger.log("Error: Invalid user session token"); // Logs the user was not found
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Invalid user session token");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else if(quizID.equals("undefined")){
            Logger.log("Error: Invalid quizID"); // Logs the user was not found
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Invalid quizID");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error
        }
        else if(!fileDetail.getFileName().equals("")){
            Logger.log("File Detected");
            String fileName = fileDetail.getFileName();
            int dot = fileName.lastIndexOf('.');//finds where the dot is to get the file extension
            String fileExtension = fileName.substring(dot + 1);//get file extension from fileName
            newFileName = "img/" + UUID.randomUUID() + "." + fileExtension;  //create a new unique identifier for file and append extension
            String uploadedFileLocation = "C:\\Users\\Jonathan\\Documents\\QuizCoursework\\resources\\client\\"+newFileName;
            try {
                OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
                int read = 0;
                byte[] bytes = new byte[1024];
                out = new FileOutputStream(new File(uploadedFileLocation));
                while ((read = uploadedInputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                JSONObject response = new JSONObject(); // Creates new JSON object
                response.put("error",e.toString());// adds an error to the JSON object
                return response.toString(); // returns the JSON object with the error
            }
        }
        QuestionService.selectAllInto(questions);
        Question currentQuestion = new Question(Question.nextId(),Integer.parseInt(quizID),question,newFileName,explanation);
        if(QuestionService.insert(currentQuestion).equals("OK")){
            Logger.log("Question added to DB");
            String ansArray[][] = {{answer1,answer2,answer3,answer4},{checkAns1,checkAns2,checkAns3,checkAns4}};
            if (AnswerController.addAnswers(ansArray,currentQuestion.getQuestionID()).equals("error")) {
                JSONObject response = new JSONObject(); // Creates new JSON object
                response.put("error", "Error: Could not add Answers to database");// adds an error to the JSON object
                return response.toString(); // returns the JSON object with the error
            }
            else{
                JSONObject response = currentQuestion.toJSON();
                response.put("answer1",answer1);
                response.put("answer2",answer2);
                response.put("answer3",answer3);
                response.put("answer4",answer4);
                response.put("checkAns1",checkAns1);
                response.put("checkAns2",checkAns2);
                response.put("checkAns3",checkAns3);
                response.put("checkAns4",checkAns4);
                return response.toString();
            }
        }
        else {
            JSONObject response = new JSONObject(); // Creates new JSON object
            response.put("error","Error: Could not add question to database");// adds an error to the JSON object
            return response.toString(); // returns the JSON object with the error

        }

    }
}
