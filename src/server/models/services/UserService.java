package server.models.services;

import server.Logger;
import server.DatabaseConnection;
import server.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    public static User selectByEmail(String email) {
        User result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(// SQL select statement
                    "SELECT userID, firstName, lastName, email, password, sessionToken FROM Users WHERE email = ?"
            );
            if (statement != null) { //Replaces the ? with the email, in lower case as emails are not case sensitive
                statement.setString(1, email.toLowerCase());
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {// checks the database returned a user, then creates an object using the data
                    result = new User(results.getInt("userID"), results.getString("firstName"), results.getString("lastName"), results.getString("email"), results.getString("password"), results.getString("sessionToken"));
                }
            }
        } catch (SQLException resultsException) {//logs any errors
            String error = "Database error - can't select by email from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;//returns the object of the users data


    }
    public static User selectBySessionToken(String token) {
        User result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(// SQL select statement
                    "SELECT userID, firstName, lastName, email, password,sessionToken FROM Users WHERE SessionToken = ?"
            );
            if (statement != null) {
                statement.setString(1, token); //replaces the ? with the user's session token passed in
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {// checks the database returned a user, then creates an object using the data
                    result = new User(results.getInt("userID"), results.getString("firstName"), results.getString("lastName"), results.getString("email"), results.getString("password"), results.getString("sessionToken"));
                Logger.log("Selected by SessionToken from 'Users' table"+results.getInt("userID"));
                }
            }
        } catch (SQLException resultsException) {//logs any errors
            String error = "Database error - can't select by email from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;//returns the object of the users data

    }
    public static String insert(User itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(// SQL insert statement
                    "INSERT INTO Users (firstName, lastName, email, password, sessionToken) VALUES (?, ?, ?, ?, ?)"
            );
            //ID is automatically created by the auto-increment function of the database
            statement.setString(1, itemToSave.getFirstName());// Fills the ?'s with the attributes of the object passed in
            statement.setString(2, itemToSave.getLastName());
            statement.setString(3, itemToSave.getEmail().toLowerCase());
            statement.setString(4, itemToSave.getPassword());
            statement.setString(5, itemToSave.getSessionToken());
            statement.executeUpdate(); // Executes the SQL statement
            return "OK"; // returns ok if no errors raised
        } catch (SQLException resultsException) { // logs and returns any errors
            String error = "Database error - can't insert into 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;// Errors are returned so the user can be informed
        }
    }
    public static String selectAllInto(List<User> targetList) {
        targetList.clear(); // empties the list
        try {
            //Creates a SQL statement to select all fields ensuring correct order
            PreparedStatement statement = DatabaseConnection.newStatement(// SQL select statement
                    "SELECT userID, firstName, lastName, email, password, sessionToken FROM Users"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) { //adds the results to the list of User objects
                        targetList.add(new User(results.getInt("userID"), results.getString("firstName"), results.getString("lastName"), results.getString("email"), results.getString("password"), results.getString("sessionToken")));
                    }
                }
            }
        } catch (SQLException resultsException) { // returns any database errors
            String error = "Database error - can't select all from 'Users' table: " + resultsException.getMessage();
            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static User selectById(int id) {
        User result = null;
        try { // prepared statement using ? syntax to prevent SQL injection
            PreparedStatement statement = DatabaseConnection.newStatement( // SQL select statement
                    "SELECT userID, firstName, lastName, email, password, sessionToken FROM Users WHERE userID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id); // replaces the ? in the statement with the id parameter
                ResultSet results = statement.executeQuery(); //sends statement to database
                if (results != null && results.next()) {
                    result = new User(results.getInt("userID"), results.getString("firstName"), results.getString("lastName"), results.getString("email"), results.getString("password"), results.getString("sessionToken"));
                } // creates a new user object with the results from the database
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Users' table: " + resultsException.getMessage();
            Logger.log(error);  // returns any database errors for debugging
        }
        return result; // returns the user object containing the requested information
    }


    public static String update(User itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement( // SQL update statement
                    "UPDATE Users SET firstName = ?, lastName = ?, email = ?, password = ?, sessionToken = ? WHERE userID = ?"
            );
            // replaces the ?'s with the attributes from the user object parameter to prevent SQL injection
            statement.setString(1, itemToSave.getFirstName());
            statement.setString(2, itemToSave.getLastName());
            statement.setString(3, itemToSave.getEmail());
            statement.setString(4, itemToSave.getPassword());
            statement.setString(5, itemToSave.getSessionToken());
            statement.setInt(6, itemToSave.getId());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {  // catches any database errors and returns them for debugging
            String error = "Database error - can't update 'Users' table: " + resultsException.getMessage();
            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Users WHERE userID = ?" // SQL delete statement
            );
            statement.setInt(1, id); //replaces the ? with the ID parameter
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) { // return any database errors for debugging
            String error = "Database error - can't delete by id from 'Users' table: " + resultsException.getMessage();
            Logger.log(error);
            return error;
        }
    }

}