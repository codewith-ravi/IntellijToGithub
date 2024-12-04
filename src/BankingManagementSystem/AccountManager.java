package BankingManagementSystem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import  java.util.Scanner;
import java.sql.Connection;
class AccountManager{
    private Connection con;
    private Scanner sc;
    public AccountManager(Connection con,Scanner sc){
        this.con=con;
        this.sc=sc;
    }
    public void creditMoney(long accountNumber) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            if (accountNumber != 0) {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM Account WHERE account_number=? AND security_pin=?");
                stmt.setLong(1, accountNumber);
                stmt.setString(2, securityPin);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String creditQuery = "UPDATE Account SET balance=balance+? WHERE account_number=?";
                    PreparedStatement stmt1=con.prepareStatement(creditQuery);
                    stmt1.setDouble(1, amount);
                    stmt1.setLong(2, accountNumber);
                    int rowsAffected = stmt1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs " + amount + " is credited successfully!");
                        con.commit();
                    } else {
                        System.out.println("Transaction failed!");
                        con.rollback();
                    }
                } else {
                    System.out.println("Invalid Security Pin!");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }
    public  void debitMoney(long accountNumber)throws SQLException{
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            if (accountNumber != 0) {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM Account WHERE account_number=? AND security_pin=?");
                stmt.setLong(1, accountNumber);
                stmt.setString(2, securityPin);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    if (amount <= currentBalance) {
                        String creditQuery = "UPDATE Account SET balance=balance-? WHERE account_number=?";
                        PreparedStatement stmt1=con.prepareStatement(creditQuery);
                        stmt1.setDouble(1, amount);
                        stmt1.setLong(2, accountNumber);
                        int rowsAffected = stmt1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs " + amount + " is debited successfully!");
                            con.commit();
                        } else {
                            System.out.println("Transaction Failed!!");
                            con.rollback();
                        }
                    } else {
                        System.out.println("Insufficient Balance!");
                    }
                }
                else{
                        System.out.println("Invalid Pin!");
                    }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }
    public void transferMoney(long senderAccountNumber)throws  SQLException{
        sc.nextLine();
        System.out.print("Enter Receiver's Account Number: ");
        long receiverAccountNumber=sc.nextLong();
        System.out.print("Enter Amount: ");
        double amount=sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin=sc.nextLine();
        try{
            con.setAutoCommit(false);
            if(senderAccountNumber!=0 && receiverAccountNumber!=0){
                PreparedStatement stmt=con.prepareStatement("SELECT * FROM Account WHERE account_number=? AND security_pin=?");
                stmt.setLong(1,senderAccountNumber);
                stmt.setString(2,securityPin);
                ResultSet rs=stmt.executeQuery();
                if(rs.next()){
                    double currentAmount=rs.getDouble("balance");
                    if(amount<=currentAmount){
                        PreparedStatement creditSTMT=con.prepareStatement("UPDATE Account SET balance=balance+? WHERE account_number=?");
                        PreparedStatement debitSTMT=con.prepareStatement("UPDATE Account SET balance=balance-? WHERE account_number=?");
                        creditSTMT.setDouble(1,amount);
                        creditSTMT.setLong(2,receiverAccountNumber);
                        debitSTMT.setDouble(1,amount);
                        debitSTMT.setLong(2,senderAccountNumber);
                        int rowsAffected1=debitSTMT.executeUpdate();
                        int rowsAffected2=creditSTMT.executeUpdate();
                        if(rowsAffected1==rowsAffected2){
                            System.out.println("Rs. "+amount+" is transferred successfully!");
                            con.commit();
                        }else{
                            System.out.println("Transaction failed!");
                            con.rollback();
                        }
                    }else{
                        System.out.println("Insufficient balance!");
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }else{
                System.out.println("Invalid account number!");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }
    public void checkBalance(long accountNumber){
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin=sc.nextLine();
        try{
            PreparedStatement stmt=con.prepareStatement("SELECT balance FROM Account WHERE account_number=? AND security_pin=?");
            stmt.setLong(1,accountNumber);
            stmt.setString(2,securityPin);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                System.out.println(" Current Balance: "+rs.getDouble("balance"));
            }
            else{
                System.out.println("Invalid Security Pin!");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}