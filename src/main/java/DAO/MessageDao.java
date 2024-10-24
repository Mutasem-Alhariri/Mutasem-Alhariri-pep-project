package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDao {
    private Connection conn;

    public MessageDao() {
        this.conn = ConnectionUtil.getConnection();
    }

    /**
     * To persist the given message in the database.
     * @param message the message to be persisted
     * @return the message with its auto generated message_id
     */
    public Message save(Message message) {
        String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) " +
            "VALUES(?, ?, ?);";
        
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, message.getPosted_by());
            pstmt.setString(2, message.getMessage_text());
            pstmt.setLong(3, message.getTime_posted_epoch());
            int affectedRows = pstmt.executeUpdate();

            if(affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()) {
                    message.setMessage_id(rs.getInt(1));
                }
            }
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * To get a list of all messages from the database.
     * @return a list of all messages if any, and an empty list otherwise
     */
    public List<Message> getAll() {
        String sql = "SELECT * FROM message;";
        List<Message> messages = new ArrayList<>();
        try{
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery(sql);

           while(rs.next()) {
            Message message = new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
            messages.add(message);
           }
           stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * To get a message with the given message_id
     * @param id the id of the message to be retrieved
     * @return the message if it exists, an empty message object otherwise
     */
    public Message getMessageById(int id) {
        String sql = "SELECT * FROM message WHERE message_id = ?;";
        Message message = new Message();
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
            }
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * To delete the message with the given id from the database.
     * @param id the message_id
     */
    public void remove(int id) {
        String sql = "DELETE FROM message WHERE message_id = ?;";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * To update the message text of the given message.
     * @param message the message to be updated.
     */
    public void update(Message message) {
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, message.getMessage_text());
            pstmt.setInt(2, message.getMessage_id());
            pstmt.executeUpdate();
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param posted_by The account_id of the messages poster.
     * @return a list of all messages posted by the user if any
     */
    public List<Message> getMessagesByUser(int posted_by) {
        String sql = "SELECT * FROM message WHERE posted_by = ?;";
        List<Message> messages = new ArrayList<>();
        try{
           PreparedStatement pstmt = conn.prepareStatement(sql);
           pstmt.setInt(1, posted_by);
           ResultSet rs = pstmt.executeQuery();

           while(rs.next()) {
            Message message = new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
            messages.add(message);
           }
           pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
