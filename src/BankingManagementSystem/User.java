package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class User{
    private Connection con;
    private Scanner sc;
    public User(java.sql.Connection con, Scanner sc){
        this.con=con;
        this.sc=sc;

    }
    public void  register(){
        sc.nextLine();
        System.out.print("Full Name: ");
        String fullName=sc.nextLine();
        System.out.print("Email: ");
        String email=sc.nextLine();
        System.out.print("Password: ");
        String password=sc.nextLine();
        if(userExist(email)){
            System.out.println("Already registered!!");
            return;
        }
        String query="INSERT INTO User VALUES(?,?,?)";
        try{
            PreparedStatement stmt=con.prepareStatement(query);
            stmt.setString(1,fullName);
            stmt.setString(2,email);
            stmt.setString(3,password);
            int rowsAffected=stmt.executeUpdate();
            if(rowsAffected>0)
                System.out.println("Registration Successfull!");
            else
                System.out.println("Registration Failed!");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public String login(){
        sc.nextLine();
        System.out.print("Email: ");
        String email=sc.nextLine();
        System.out.print("Password: ");
        String password=sc.nextLine();
        String query="SELECT * FROM User WHERE email=? AND password=?";
        try{
            PreparedStatement stmt=con.prepareStatement(query);
            stmt.setString(1,email);
            stmt.setString(2,password);
            ResultSet rs=stmt.executeQuery();
            if(rs.next())return email;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean userExist(String email){
        String query="SELECT email FROM User WHERE email=?";
        try {
                PreparedStatement stmt=con.prepareStatement(query);
                stmt.setString(1,email);
                ResultSet rs=stmt.executeQuery();
                if(rs.next())return true;
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        return false;
    }
}