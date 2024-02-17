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
}
