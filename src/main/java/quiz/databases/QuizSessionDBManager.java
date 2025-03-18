
package quiz.databases;

import databases.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizSessionDBManager {
    
    private final DatabaseConnector connector = new DatabaseConnector();
    
    public void createSession(String sessionKey) {
        String sql = "INSERT INTO quiz_session (session_key, session_status) "
                + "VALUES (?, ?)";
        
        try (Connection connection = connector.connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sessionKey);
            ps.setBoolean(2, true);
            
            ps.executeUpdate();
            System.out.println("A new quiz session has been created.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void startSession(String sessionKey) {
        String sql = "UPDATE quiz_session SET session_status = ? WHERE session_key = ?";
        
        try (Connection connection = connector.connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, true);
            ps.setString(2, sessionKey);
            
            ps.executeUpdate();
            System.out.println("A quiz session has been started.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void endSession(String sessionKey) {
        String sql = "UPDATE quiz_session SET session_status = ? WHERE session_key = ?";
        
        try (Connection connection = connector.connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, false);
            ps.setString(2, sessionKey);
            
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public boolean getSessionStatus(String sessionKey) {
        String sql = "SELECT session_status FROM quiz_session WHERE session_key = ?";
        boolean sessionStatus = false;
        
        try (Connection connection = connector.connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sessionKey);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                sessionStatus = rs.getBoolean("session_status");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return sessionStatus;
    }
    
    public boolean checkKey(String sessionKey) {
        String sql = "SELECT session_key FROM quiz_session WHERE session_key = ?";
        
        try (Connection connection = connector.connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sessionKey);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    public String getActiveSessionKey() {
        String sql = "SELECT session_key from quiz_session WHERE session_status = true";
        String sessionKey = null;
        
        try (Connection connection = connector.connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                sessionKey = rs.getString("session_key");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return sessionKey;
    }
}
