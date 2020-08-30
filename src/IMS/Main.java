package IMS;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
    primaryStage.setTitle("Inventory Management System");
    primaryStage.setScene(new Scene(root, 1200, 440));
    primaryStage.show();
  }


  public static void main(String[] args) {
    Part part1 = new InHouse(1, "Part1", 7.99, 5,1,5, 1234);
    Part part2 = new Outsourced(2,"Part2", 3.44, 7,1,21,"Tom's Farm");
    Part part3 = new InHouse(3, "Part3", 6.99, 13,11,20, 1234);
    Part part4 = new Outsourced(4,"Part14", 21.95, 25,15,30,"Happy days");
    Part part5 = new InHouse(3, "Part9", 3.99, 13,11,20, 1234);
    Part part6 = new Outsourced(4,"Part7", 1.95, 25,15,30,"Ash cash");

    Inventory.addPart(part1);
    Inventory.addPart(part2);
    Inventory.addPart(part3);
    Inventory.addPart(part4);
    Inventory.addPart(part5);
    Inventory.addPart(part6);

    Product product1 = new Product(1,"Product1",29.99,5,1,5);
    Product product2 = new Product(2,"Product2",19.99,11,9,17);
    Product product3 = new Product(3,"Product3",39.99,8,5,20);

    Inventory.addProduct(product1);
    Inventory.addProduct(product2);
    Inventory.addProduct(product3);


    launch(args);
  }
}
