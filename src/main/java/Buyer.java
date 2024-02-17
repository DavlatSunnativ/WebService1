
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;


@NoArgsConstructor
@Setter
@Getter
public class Buyer extends User{

    public Buyer(String name, String surname, int id, boolean status, double bank, String email, String password) {
        setName(name);
        setSurname(surname);
        setId(id);
        setStatus(status);
        setBank(bank);
        setEmail(email);
        setPassword(password);
    }

    public void signUp() throws SQLException {
        System.out.println("Write down your email:");
        String email = sc.nextLine();
        System.out.println("Create a password:");
        String password = sc.nextLine();
        System.out.println("Write down your name:");
        String name = sc.nextLine();
        System.out.println("Write down your surname:");
        String surname = sc.nextLine();

        try{
            Connection con = connect();
            String sqlInsert = "INSERT INTO users (name,surname,status,email,password)VALUES (?,?,?,?,?);";
            PreparedStatement pstmnt = con.prepareStatement(sqlInsert);

            pstmnt.setString(1,name);
            pstmnt.setString(2,surname);
            pstmnt.setBoolean(3,true);
            pstmnt.setString(4,email);
            pstmnt.setString(5,password);
            pstmnt.executeUpdate();
            System.out.println("You succesfully signed up! You can log in now. ");
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public void showProducts() throws SQLException, ClassNotFoundException {
        try{
            Connection con = connect();
            String sql = "SELECT name,price,quantity,id FROM product;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ResultSet rs = stmnt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt("id")+". "+rs.getInt("quantity")+" "+rs.getString("name")+"s is available by price "+rs.getDouble("price") );
            }
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public void buyProducts(User buyer,int id, int quantity) throws SQLException, ClassNotFoundException {
        try {
            Connection con = connect();
            String sql = "SELECT quantity,price,salesman_id FROM product WHERE id=?;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1,id);
            ResultSet rs = stmnt.executeQuery();
            double totalPrice = 0;
            if(rs.next()) {
                totalPrice = rs.getDouble("price") * quantity;
            }
            if(isEnoughQuantity(rs.getInt("quantity"),quantity) && isEnoughBank(totalPrice, buyer.getId())){
                updateBank(buyer.getBank(),totalPrice, buyer.getId());
                updateQuantity(rs.getInt("quantity"),quantity,id);
                addOrder(id, buyer.getId(), quantity,totalPrice);
                updateSalesmanBank(totalPrice,rs.getInt("salesman_id"));

                System.out.println("Your order was added to your order list! ");
            }
            else if(!isEnoughBank(totalPrice, buyer.getId())){
                System.out.println("Not enough money(");
            }
            else{
                System.out.println("Not available quantity of product");
            }
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public boolean isEnoughQuantity(int availableQuantity,int requestedQuantity){
        if(availableQuantity>=requestedQuantity){
            return true;
        }
        else{
            System.out.println("quantity");
            return false;
        }
    }
    public void updateQuantity(int availableQuantity,int requestedQuantity,int id) throws SQLException, ClassNotFoundException {
        try{
            Connection con = connect();
            String sql = "UPDATE product SET quantity=? WHERE id=?;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(2,id);
            stmnt.setInt(1,availableQuantity-requestedQuantity);
            stmnt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public boolean isEnoughBank(double totalPrice,int id) throws SQLException, ClassNotFoundException {
        try {
            Connection con = connect();
            String sql = "SELECT bank FROM users WHERE id=?;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, id);
            ResultSet rs = stmnt.executeQuery();

            if (rs.next() && rs.getDouble("bank") >= totalPrice) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Exception" + e.getMessage());
            return false;
        }
    }
    public void updateBank(double bank,double totalPrice,int id) throws SQLException, ClassNotFoundException {
        try{
            Connection con = connect();
            String sql = "UPDATE users SET bank=? WHERE id=?;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDouble(1,bank-totalPrice);
            stmnt.setInt(2,id);
            stmnt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public void updateSalesmanBank(double totalPrice,int id) throws SQLException, ClassNotFoundException {
        try{
            Connection con = connect();
            String saleSql = "SELECT bank FROM users WHERE id=?;";
            String sql = "UPDATE users SET bank=? WHERE id=?;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            PreparedStatement stmnt1 = con.prepareStatement(saleSql);
            stmnt1.setInt(1,id);
            ResultSet rs = stmnt1.executeQuery();
            if (rs.next()) {
                double bank = rs.getDouble("bank");
                double updatedBank = bank + totalPrice;
                stmnt.setDouble(1, updatedBank);
                stmnt.setInt(2, id);
                stmnt.executeUpdate();
            } else {
                System.out.println("No user found with id: " + id);
            }



            stmnt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public void showOrders() throws SQLException, ClassNotFoundException {
        try {
            Connection con = connect();
            String sql = "SELECT * FROM orders;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ResultSet rs = stmnt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt("id")+". \nProduct id: "+ rs.getInt("product_id")+"\nQuantity: "+rs.getInt("quantity")+"\nTotal price: "+rs.getDouble("total_price"));
            }
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
    public void addOrder(int productId,int buyerId,int quantity,double totalPrice) throws SQLException, ClassNotFoundException {
        try{
            Connection con = connect();
            String sql = "INSERT INTO orders (product_id,buyer_id,quantity,total_price) VALUES(?,?,?,?);";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1,productId);
            stmnt.setInt(2,buyerId);
            stmnt.setInt(3,quantity);
            stmnt.setDouble(4,totalPrice);
            stmnt.executeUpdate();
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Exception" + e.getMessage());
        }
    }
}
