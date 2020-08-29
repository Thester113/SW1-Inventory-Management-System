package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class MainScreenController {

  Stage stage;
  Parent scene;

  @FXML
  private TextField partSearchField;

  @FXML
  private TableView<Part> partTableView;

  @FXML
  private TableColumn<Part, Integer> partIdCol;

  @FXML
  private TableColumn<Part, String> partNameCol;

  @FXML
  private TableColumn<Part, Integer> partInventoryCol;

  @FXML
  private TableColumn<Part, Double> partPriceCol;

  @FXML
  private TextField productSearchField;

  @FXML
  private TableView<Product> productTableView;

  @FXML
  private TableColumn<Product, Integer> productIdCol;

  @FXML
  private TableColumn<Product, String> productNameCol;

  @FXML
  private TableColumn<Product, Integer> productInventoryCol;

  @FXML
  private TableColumn<Product, Double> productPriceCol;

  @FXML
  void onActionAddPart(ActionEvent event) throws IOException {

    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
    scene = FXMLLoader.load(getClass().getResource("/view/AddPartView.fxml"));
    stage.setScene(new Scene(scene));
    stage.show();

  }
  @FXML
  void onActionAddProduct(ActionEvent event) throws IOException {

    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
    scene = FXMLLoader.load(getClass().getResource("/view/AddProductView.fxml"));
    stage.setScene(new Scene(scene));
    stage.show();

  }
  @FXML
  void onActionModifyPart(ActionEvent event) throws IOException {

    try {
      // Specify which view to load
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/View/ModifyPartView.fxml"));
      loader.load();

      ModifyPartController modPartController = loader.getController();
      modPartController.sendPartInfo(partTableView.getSelectionModel().getSelectedItem());

      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      Parent scene = loader.getRoot();
      stage.setScene(new Scene(scene));
      stage.show();
    } catch (NullPointerException e) {
      System.out.println("Exception: " + e);
      System.out.println("No part selected!");
    }

  }

  @FXML
  void onActionDeletePart(ActionEvent event) {

    if(partTableView.getSelectionModel().getSelectedItem() != null) {

      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the part, do you want to continue?");
      alert.setTitle("CONFIRMATION");

      Optional<ButtonType> result = alert.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
        Inventory.deletePart(partTableView.getSelectionModel().getSelectedItem());
      }
    } else {
      System.out.println("No part has been selected");
    }
  }


  @FXML
  public void onActionExit(ActionEvent e) {

    System.exit(0);

  }

  @FXML
  public void onActionPartSearch(ActionEvent actionEvent) {
    String partInput = partSearchField.getText();

    try {
      int partId = Integer.valueOf(partInput);
      ObservableList<Part> searchResult = FXCollections.observableArrayList();
      searchResult.add(Inventory.lookupPart(partId));

      partTableView.setItems(searchResult.get(0) == null ? Inventory.getAllParts() : searchResult);
    } catch (NumberFormatException e) {
      partTableView.setItems(Inventory.lookupPart(partInput));
    }
  }

  @FXML
  void onActionProductsSearch(ActionEvent event) {

    String productInput = productSearchField.getText();

    try {
      int productId = Integer.valueOf(productInput);
      ObservableList<Product> searchResult = FXCollections.observableArrayList();
      searchResult.add(Inventory.lookupProduct(productId));

      productTableView.setItems(searchResult.get(0) == null ? Inventory.getAllProducts() : searchResult);
    } catch (NumberFormatException e) {
      productTableView.setItems(Inventory.lookupProduct(productInput));
    }

  }


}

