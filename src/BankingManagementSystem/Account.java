package BankingManagementSystem;
import java.sql.*;
import java.util.Scanner;
class Account{
    private Connection con;
    private Scanner sc;
         public Account(Connection con,Scanner sc){
               this.con=con;
               this.sc=sc;
            }
         public long openAccount(String email){
             if(!accountExist(email)){
                 String query="INSERT INTO Account VALUES(?,?,?,?,?)";
                 sc.nextLine();
                 System.out.print("Enter Full Name: ");
                 String fullName=sc.nextLine();
                 System.out.print("Enter Initial Amount: ");
                 double balance=sc.nextDouble();
                 sc.nextLine();
                 System.out.print("Enter Security Pin: ");
                 String securityPin=sc.nextLine();
                 try{
                     long accountNumber=generateAccountNumber();
                     PreparedStatement stmt=con.prepareStatement(query);
                     stmt.setLong(1,accountNumber);
                     stmt.setString(2,fullName);
                     stmt.setString(3,email);
                     stmt.setDouble(4,balance);
                     stmt.setString(5,securityPin);
                     int rowsAffected=stmt.executeUpdate();
                     if(rowsAffected>0)return accountNumber;
                     else
                         throw new RuntimeException("Account Creation failed!!!");
                 }
                 catch(SQLException e){
                     System.out.println(e.getMessage());
                 }
             }
             throw new RuntimeException("Account Already Exist!");
         }
         public long getAccountNumber(String email){
             String query="Select account_number FROM Account WHERE email=?";
             try{
                 PreparedStatement stmt=con.prepareStatement(query);
                 stmt.setString(1,email);
                 ResultSet rs=stmt.executeQuery();
                 if(rs.next())
                       return rs.getLong("account_number");
             }
             catch(SQLException e){
                 System.out.println(e.getMessage());
             }
             throw new RuntimeException("Account Number Doesn't Exist!");
         }
         private long generateAccountNumber(){
             try{
                 Statement stmt=con.createStatement();
                 String query="SELECT account_number FROM Account ORDER BY account_number DESC limit 1";
                 ResultSet rs=stmt.executeQuery(query);
                 if(rs.next()){
                     long lastAccountNumber=rs.getLong("account_number");
                     return lastAccountNumber+1;
                 }
             }
             catch(SQLException e){
                 System.out.println(e.getMessage());
             }
             return 10000100;
         }
         public  boolean accountExist(String email){
             String query="SELECT account_number FROM Account WHERE email=? ";
             try{
                 PreparedStatement stmt=con.prepareStatement(query);
                 stmt.setString(1,email);
                 ResultSet rs=stmt.executeQuery();
                 if(rs.next())return true;
             }
             catch (SQLException e){
                 System.out.println(e.getMessage());
             }
             return false;
         }
}