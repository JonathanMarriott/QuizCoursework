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
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT userID, firstName, lastName, email, password, sessionToken FROM Users WHERE email = ?"
            );
            if (statement != null) {
                statement.setString(1, email.toLowerCase());
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new User(results.getInt("userID"), results.getString("firstName"), results.getString("lastName"), results.getString("email"), results.getString("password"), results.getString("sessionToken"));
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by email from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;

    }
    public static User selectBySessionToken(String token) {
        User result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT userID, firstName, lastName, email, password,sessionToken FROM Users WHERE SessionToken = ?"
            );
            if (statement != null) {
                statement.setString(1, token);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new User(results.getInt("userID"), results.getString("firstName"), results.getString("lastName"), results.getString("email"), results.getString("password"), results.getString("sessionToken"));
                Logger.log("Selected by SessionToken from 'Users' table"+results.getInt("userID"));
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by email from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;

    }
    public static String selectAllInto(List<User> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT userID, firstName, lastName, email, password, sessionToken FROM Users"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new User(results.getInt("userID"), results.getString("firstName"), results.getString("lastName"), results.getString("email"), results.getString("password"), results.getString("sessionToken")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
        return "OK";
    }

    public static User selectById(int id) {
        User result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT userID, firstName, lastName, email, password, sessionToken FROM Users WHERE userID = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new User(results.getInt("userID"), results.getString("firstName"), results.getString("lastName"), results.getString("email"), results.getString("password"), results.getString("sessionToken"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
        }
        return result;
    }

    public static String insert(User itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Users (firstName, lastName, email, password, sessionToken) VALUES (?, ?, ?, ?, ?)"
            );
            statement.setString(1, itemToSave.getFirstName());
            statement.setString(2, itemToSave.getLastName());
            statement.setString(3, itemToSave.getEmail());
            statement.setString(4, itemToSave.getPassword());
            statement.setString(5, itemToSave.getSessionToken());




            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String update(User itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Users SET firstName = ?, lastName = ?, email = ?, password = ?, sessionToken = ? WHERE userID = ?"
            );
            statement.setString(1, itemToSave.getFirstName());
            statement.setString(2, itemToSave.getLastName());
            statement.setString(3, itemToSave.getEmail());
            statement.setString(4, itemToSave.getPassword());
            statement.setString(5, itemToSave.getSessionToken());




            statement.setInt(6, itemToSave.getId());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Users WHERE userID = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Users' table: " + resultsException.getMessage();

            Logger.log(error);
            return error;
        }
    }

}