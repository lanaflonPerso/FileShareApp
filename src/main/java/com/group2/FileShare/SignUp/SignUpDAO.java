package com.group2.FileShare.SignUp;

import com.group2.FileShare.ProfileManagement.PasswordEncoder;
import com.group2.FileShare.database.DatabaseConnection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpDAO implements ISignUpDAO{

    private String query;
    private ResultSet rs;
    private static final Logger logger = LogManager.getLogger(SignUpDAO.class);

    @Override
    public boolean userExist(String email)
    {

        query = "{ call user_exists(?) }";
        boolean userExists = false;

        DatabaseConnection db = DatabaseConnection.getdbConnectionInstance();

        try (Connection conn = db.getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {

            stmt.setString(1, email);

            rs = stmt.executeQuery();

            if(rs.next()) {
                 userExists = rs.getBoolean(1);
            }

        } catch (SQLException ex) {
            logger.log(Level.ERROR, "Failed to check if user exist with query:" +query +" of user email: "+ email +" at userExist()" , ex);
        } finally {
            db.closeConnection();
        }

        return userExists;
    }

    @Override
    public int createProfile(String firstName, String lastName, String email, String rawPassword)
    {

        //hash encode the password
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        String hashedPassword = passwordEncoder.hashPassword(rawPassword);

        //select the stored procedure
        query = "{ call create_profile(?,?,?,?) }";
        int userId = -1;

        //establish database connection
        DatabaseConnection db = DatabaseConnection.getdbConnectionInstance();

        try (Connection conn = db.getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, hashedPassword);

            rs = stmt.executeQuery();

            if(rs.next()) {
                userId = rs.getInt(1);
            }

        } catch (SQLException ex) {
            logger.log(Level.ERROR, "Failed to create user profile of user email: "+ email +" at createProfile()" , ex);
        } finally {
            db.closeConnection();
        }

        return userId;
    }

}
