package server.models.services;

import server.Logger;
import server.DatabaseConnection;
import server.models.Question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestionService {

    public static String selectAllInto(List<Question> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT questionID, quizID, questionTitle, image, explanation FROM Questions"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Question(results.getInt("questionID"), results.getInt("quizID"), results.getString("questionTitle"), results.getString("image"), results.getString("explanation")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Questions' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static Question selectById(int id) {
        Question result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT questionID, quizID, questionTitle, image, explanation FROM Questions WHERE questionID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Question(results.getInt("questionID"), results.getInt("quizID"), results.getString("questionTitle"), results.getString("image"), results.getString("explanation"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Questions' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(Question itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Questions (quizID, questionTitle, image, explanation) VALUES (?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getQuizID());
            statement.setString(2, itemToSave.getQuestionTitle());
            statement.setString(3, itemToSave.getImage());
            statement.setString(4, itemToSave.getExplanation());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Questions' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(Question itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Questions SET quizID = ?, questionTitle = ?, image = ?, explanation = ? WHERE questionID = ?"
            );
            statement.setInt(1, itemToSave.getQuizID());
            statement.setString(2, itemToSave.getQuestionTitle());
            statement.setString(3, itemToSave.getImage());
            statement.setString(4, itemToSave.getExplanation());
            statement.setInt(5, itemToSave.getQuestionID());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Questions' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Questions WHERE questionID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Questions' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}