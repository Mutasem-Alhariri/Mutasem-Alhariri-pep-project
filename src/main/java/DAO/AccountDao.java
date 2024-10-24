package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDao {
    private Connection conn;

    public AccountDao() {
        this.conn = ConnectionUtil.getConnection();
    }
    /**
     * 
     * @param account the new user account to be created.
     * @return The new account object includiing its auto-generated id
     */
    public Account save(Account account) {
        String sql = "INSERT INTO account(username, password) VALUES(?,?);";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            int affectedRows = pstmt.executeUpdate();

            if(affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()) {
                    account.setAccount_id(rs.getInt("account_id"));
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * Retrieves the account with the given id
     * @param id the id of the account
     * @return the account if it exists
     */
    public Account getAccountById(int id) {
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        Account account = null;
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"),
                    rs.getString("password"));
            }
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return account;
    }

    /**
     * Retrieves an account with the given username
     * @param username the username
     * @return the account if it exists
     */
    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM account WHERE username = ?;";
        Account account = null;
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"),
                    rs.getString("password"));
            }
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * Authenticate the account with the given username and password
     * @param username the username
     * @param password the password
     * @return the account if authenticated, null otherwise
     */
    public Account authenticate(String username, String password) {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
        Account account = null;
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"),
                    rs.getString("password"));
            }
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
}
