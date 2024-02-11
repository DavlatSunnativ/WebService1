import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@NoArgsConstructor
@Setter
@Getter

public class Salesman extends User{


    public Salesman(String name, String surname, int id, boolean status, double bank, String email, String password) {
        setName(name);
        setSurname(surname);
        setId(id);
        setStatus(status);
        setBank(bank);
        setEmail(email);
        setPassword(password);
    }

    @Override
    public void signUp() throws SQLException, ClassNotFoundException {
        System.out.println("Write down your email:");
        String email = sc.nextLine();
        System.out.println("Create a password:");
        String password = sc.nextLine();
        System.out.println("Write down your name:");
        String name = sc.nextLine();
        System.out.println("Write down your surname:");
        String surname = sc.nextLine();


        Connection con = connect();
        String sqlInsert = "INSERT INTO users (name,surname,status,email,password) " + "VALUES (?,?,?,?,?);";
        PreparedStatement pstmnt = con.prepareStatement(sqlInsert);


        pstmnt.setString(1,name);
        pstmnt.setString(2,surname);
        pstmnt.setBoolean(3,false);
        pstmnt.setString(4,email);
        pstmnt.setString(5,password);
        pstmnt.executeUpdate();
    }
    public void addProduct() throws SQLException {
        System.out.println("What is name of your product?");
        String name = sc.nextLine();
        System.out.println("How much cost "+name+"?");
        double price = sc.nextDouble();
        System.out.println("How many of "+name+" do you have?");
        int quantity = sc.nextInt();
    }
}
