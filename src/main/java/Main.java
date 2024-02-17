
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        User user = new Salesman();
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose the option:\n" + "1.Sign up\n" + "2.Login\n");
        int option = sc.nextInt();
        if (option == 1) {
            System.out.println("You are ...\n1.Buyer\n2.Salesman");
            int option1 = sc.nextInt();

            try {
                if (option1 == 1) {
                    Buyer buyer = new Buyer();
                    buyer.signUp();
                } else if (option1 == 2) {
                    Salesman salesman = new Salesman();
                    salesman.signUp();
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }if (option == 1) {
            option = 2;
            System.out.println("You can login now! ");
        } else if (option == 2) {
            user = user.login();
        }else {
            System.out.println("invalid input");
        }

        while (true) {
                    if (user.isStatus()) {
                        System.out.println("1.List of products\n2.My orders\n3.My bank");
                        int option3 = sc.nextInt();
                        if (option3 == 1) {
                            ((Buyer) user).showProducts();
                            System.out.println("Choose product, that you want to buy by its id: ");
                            int id = sc.nextInt();
                            System.out.println("How many do you want to buy?");
                            int quantity = sc.nextInt();
                            ((Buyer) user).buyProducts(user, id, quantity);
                        } else if (option3 == 2) {
                            ((Buyer) user).showOrders();
                        } else if (option3 == 3) {
                            System.out.println(user.getBank());
                        } else {
                            System.out.println("Invalid input");
                        }
                    } else {
                        System.out.println("1.My products\n2.My bank");
                        int option3 = sc.nextInt();
                        if (option3 == 1) {
                            ((Salesman) user).showProducts(user.getId());
                            System.out.println("Input 0 to add new product");
                            int option4 = sc.nextInt();
                            if (option4 == 0) {
                                ((Salesman) user).addProduct(user.getId());
                            } else {
                                System.out.println("Invalid input");
                            }
                        } else if (option3 == 2) {
                            System.out.println(user.getBank());
                        } else {
                            System.out.println("Invalid input");
                        }
                    }
                }
            }



    }