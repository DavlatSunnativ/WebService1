import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public abstract class User {
    private String name;
    private String surname;
    private int id;
    private boolean status;
    private double bank = 1000.00;
    private String email;
    private String password;

    Scanner sc = new Scanner(System.in);

    String connectionString = "jdbc:postgresql://localhost:5433/postgres";

    Connection con = null;

    public Connection connect() throws ClassNotFoundException, SQLException {

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(connectionString, "postgres", "3052");
            return con;
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
            return con;

        }
    }
    public abstract void signUp() throws SQLException, ClassNotFoundException;
    public User login() throws SQLException, ClassNotFoundException {
        System.out.println("Write down your email:");
        String email = sc.nextLine();
        System.out.println("Write down your password:");
        String password = sc.nextLine();
        Connection con = connect();

        String sql = "SELECT name,surname,id,status,bank  FROM users WHERE email=? AND password=?;";
        PreparedStatement stmnt = con.prepareStatement(sql);
        stmnt.setString(1,email);
        stmnt.setString(2,password);
        ResultSet rs = stmnt.executeQuery();

        User currentUser = null;

        if(rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean status = rs.getBoolean("status");
            double bank = rs.getDouble("bank");

            System.out.println("Welcome back, " + name + " " + surname + " !");

            if(status){
                currentUser = new Buyer(name,surname,id,status,bank,email,password);
            }
            else{
                currentUser = new Salesman(name,surname,id,status,bank,email,password);
            }
        } else {
            System.out.println("Invalid email or password");
        }
        return currentUser;
    }
    public void regPage(int option) throws SQLException, ClassNotFoundException {
        try {
            if (option == 1) {
                System.out.println("You are ...\n1.Buyer\n2.Salesman");
                int option1 = sc.nextInt();
                signUpPage(option1);
            }else if (option == 2) {
                User user = new Buyer();
                user.login();
                if(user.isStatus()){
                    user.buyerMenu(user);
                }else{
                    user.salesmanMenu(user);
                }
            }else{
                System.out.println("Invalid input");
            }
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public void signUpPage(int option) throws SQLException {
        try{
            if(option == 1) {
                Buyer buyer = new Buyer();
                buyer.signUp();
                buyer.buyerMenu(buyer);
            }else if (option == 2) {
                Salesman salesman = new Salesman();
                salesman.signUp();
                salesman.salesmanMenu(salesman);
            }
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public void buyerMenu(User user) throws SQLException, ClassNotFoundException {
        System.out.println("1.List of products\n2.My orders\n3.My bank");
        int option3 = sc.nextInt();
        try {
            if (option3 == 1) {
                ((Buyer) user).showProducts();
                System.out.println("Choose product, that you want to buy by its id: ");
                int id = sc.nextInt();
                System.out.println("How many do you want to buy?");
                int quantity = sc.nextInt();
                ((Buyer) user).buyProducts(user, id, quantity);
                user.buyerMenu(user);
            } else if (option3 == 2) {
                ((Buyer) user).showOrders();
                user.buyerMenu(user);
            } else if (option3 == 3) {
                System.out.println(user.getBank());
                user.buyerMenu(user);
            } else {
                System.out.println("Invalid input");
            }
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public void salesmanMenu(User user) throws SQLException, ClassNotFoundException {
        System.out.println("1.My products\n2.My bank");
        int option3 = sc.nextInt();
        try{
            if (option3 == 1) {
                ((Salesman)user).showProducts(user.getId());
                System.out.println("Input 0 to add new product");
                int option4 = sc.nextInt();
                if(option4 == 0) {
                    ((Salesman) user).addProduct(user.getId());
                    user.salesmanMenu(user);
                }else{
                    System.out.println("Invalid input");
                    user.salesmanMenu(user);
                }
            } else if (option3 == 2) {
                System.out.println(user.getBank());
            }else {
                System.out.println("Invalid input");
            }
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
}
