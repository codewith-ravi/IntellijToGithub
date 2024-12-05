package BankingManagementSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static  final String url="jdbc:mysql://localhost:3306/banking_system";
    private static final String username="root";
    private static  final  String password="ravi&7890";
    public static void main(String[] args) {
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            catch(ClassNotFoundException e){
                System.out.println(e.getMessage());
            }
            try{
                Connection con = DriverManager.getConnection(url,username,password);
                Scanner sc=new Scanner(System.in);
                User user=new User(con,sc);
                Account account=new Account(con,sc);
                AccountManager accountManager=new AccountManager(con,sc);
                while(true){
                    System.out.println("Welcome to Banking System!");
                    System.out.println();
                    System.out.println("1. Register");
                    System.out.println("2. Login");
                    System.out.println("3. Exit");
                    System.out.print("Enter your choice: ");

                    int choice=sc.nextInt();

                    String email;
                    long accountNumber;
                    switch (choice){
                        case 1:
                            user.register();
                            break;
                        case 2:
                            email=user.login();
                            if(email!=null){
                                System.out.println();
                                System.out.println("User Logged In!");
                                if(!account.accountExist(email)){
                                    System.out.println();
                                    System.out.println("1. Open a new Bank Account");
                                    System.out.println("2. Exit");
                                    System.out.print("Enter your choice: ");

                                    if(sc.nextInt()==1) {
                                        accountNumber = account.openAccount(email);
                                        System.out.println("Account Created Successfully");
                                        System.out.println("Your account number is: " + accountNumber);
                                    }
                                    else
                                        break;
                                }
                                accountNumber=account.getAccountNumber(email);
                                int choice2=0;
                                while (choice2!=5){
                                    System.out.println();
                                    System.out.println("1. Debit Money");
                                    System.out.println("2. Credit Money");
                                    System.out.println("3. Transfer Money");
                                    System.out.println("4. Check Balance");
                                    System.out.println("5. Go Back");
                                    System.out.print("Enter your choice: ");
                                    choice2=sc.nextInt();

                                    switch (choice2){
                                        case 1:
                                            accountManager.debitMoney(accountNumber);
                                            break;
                                        case 2:
                                            accountManager.creditMoney(accountNumber);
                                            break;
                                        case 3:
                                            accountManager.transferMoney(accountNumber);
                                            break;
                                        case 4:
                                            accountManager.checkBalance(accountNumber);
                                            break;
                                        case 5:
                                            break;
                                        default:
                                            System.out.println("Enter Valid Choice!");
                                    }
                                }
                            }
                            else {
                                System.out.println("Incorrect email or password!");
                            }
                            break;
                        case 3:
                            System.out.println("THANK YOU FOR USING BANKING SYSTEM!");
                            System.out.println("Existing System!!!");
                            con.close();
                            return;
                        default:
                            System.out.println("Enter Valid Choice!");
                    }
                }

            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }

    }
}