
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        ArrayList<User> users = new ArrayList<>();

        Buyer currentBuyer = new Buyer();
        Salesman currentSalesman = new Salesman();

        System.out.println("Choose the option:\n"+"1.Sign up\n"+"2.Login\n");
        int option = sc.nextInt();

            if(option == 1) {
                System.out.println("You are ...\n1.Buyer\n2.Salesman");
                int option1 = sc.nextInt();

                try {
                    if (option1 == 1) {
                        Buyer buyer = new Buyer();
                        buyer.signUp();
                        try{
                            currentBuyer = (Buyer) buyer.login();
                            System.out.println("Type 1 to start buying");
                            int option2 = sc.nextInt();
                            if(option2==1){
                                currentBuyer.showProducts();
                            }
                        }catch (Exception e ){
                            System.out.println("Invalid input");
                        }
                    } else if (option1 == 2) {
                        Salesman salesman = new Salesman();
                        salesman.signUp();
                        currentSalesman = (Salesman) salesman.login();
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input");
                }
            }
            else if (option == 2) {
                    User user = new Salesman();
                    user = user.login();

                    if(user.isStatus()){
                        System.out.println("1.List of products\n2.My orders\n3.My bank");
                        int option3 = sc.nextInt();
                        if (option3 == 1){
                            ((Buyer) user).showProducts();
                            System.out.println("Choose product, that you want to buy by its id: ");
                            int id = sc.nextInt();
                            System.out.println("How many do you want to buy?");
                            int quantity = sc.nextInt();
                            ((Buyer) user).buyProducts(user,id,quantity);
                        }
                        else if(option3 == 2){
                            ((Buyer)user).showOrders();
                        }
                        else if(option3 == 3){
                            System.out.println(user.getBank());
                        }
                        else{
                            System.out.println("invalid input2");
                        }
                    }
                    else{
                        System.out.println("1.My products\n2.My bank");
                        int option3 = sc.nextInt();
                        if (option3==1){

                        }
                        else if (option3 == 2){

                        }
                    }
                }
            else{
                System.out.println("invalid input");
            }
    }
}