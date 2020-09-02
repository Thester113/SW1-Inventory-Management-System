package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.canDeleteProduct;
import static java.lang.Integer.*;

/**Main user interface controller for parts and products*/
public class MainScreenController implements Initializable {
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

  /**
   * Populates tables and columns with values
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    /** Creates table view with parts */
    partTableView.setItems(Inventory.getAllParts());
    /** Creates table view with products and values */
    productTableView.setItems(Inventory.getAllProducts());

    /** Adds Parts to columns with values */
    partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    /** Adds Products to columns with values */
    productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

  }

  /**
   * Adds part through UI
   */
  @FXML
  public void onActionAddPart(ActionEvent event) throws IOException {

    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    scene = FXMLLoader.load(getClass().getResource("/view/AddPartView.fxml"));
    stage.setScene(new Scene(scene));
    stage.show();

  }

  /**
   * Adds product through UI
   */
  @FXML
  public void onActionAddProduct(ActionEvent event) throws IOException {

    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    scene = FXMLLoader.load(getClass().getResource("/view/AddProductView.fxml"));
    stage.setScene(new Scene(scene));
    stage.show();

  }

  /**
   * Deletes part through UI
   */
  @FXML
  void onActionDeletePart(ActionEvent event) {
    if (partTableView.getSelectionModel().getSelectedItem() != null) {

      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the part permanently, do you want to continue?");
      alert.setTitle("CONFIRMATION");

      Optional<ButtonType> result = alert.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
        Inventory.deletePart(partTableView.getSelectionModel().getSelectedItem());
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "No part selected");
      alert.setTitle("INFORMATION");
      alert.showAndWait();

    }


  }

  /**
   * Deletes product through UI
   */
  @FXML
  public void onActionDeleteProduct(ActionEvent event) {
    Product product = productTableView.getSelectionModel().getSelectedItem();
    try {
      if (!canDeleteProduct(product)) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ERROR!");
        alert.setHeaderText("This product cannot be removed");
        alert.setContentText("This product has parts associated with it. Please remove parts associated with the product and then try again.");
        alert.showAndWait();
      } else {
        if (productTableView.getSelectionModel().getSelectedItem() == null)
          System.out.println("No product selected.");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the product permanently, do you want to continue?");
        alert.setTitle("CONFIRMATION");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
          Inventory.deleteProduct(productTableView.getSelectionModel().getSelectedItem());
        }
      }
    } catch (NullPointerException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("No product selected .");
      alert.showAndWait();

    }
  }

  /**
   * Exit the program
   */
  @FXML
  public void onActionExit(ActionEvent event) {

    System.exit(0);

  }

  /**
   * Modify part through UI
   */
  @FXML
  public void onActionModifyPart(ActionEvent event) throws IOException {

    try {

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
      Alert alert = new Alert(Alert.AlertType.ERROR, "No part selected to modify");
      alert.setTitle("ERROR");
      alert.showAndWait();
    }

  }

  /**
   * Modify product through UI
   */
  @FXML
  public void onActionModifyProduct(ActionEvent event) throws IOException {

    try {

      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/View/ModifyProductView.fxml"));
      loader.load();

      ModifyProductController modProdController = loader.getController();
      modProdController.sendProductInfo(productTableView.getSelectionModel().getSelectedItem());

      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      Parent scene = loader.getRoot();
      stage.setScene(new Scene(scene));
      stage.show();
    } catch (NullPointerException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR, "No product selected to modify");
      alert.setTitle("ERROR");
      alert.showAndWait();
    }

  }

  /**
   * Search part through UI
   */
  @FXML
  public void onActionPartsSearch(ActionEvent event) {

    String partInput = partSearchField.getText();

    try {
      int partId = valueOf(partInput);
      ObservableList<Part> searchResult = FXCollections.observableArrayList();
      searchResult.add(Inventory.lookupPart(partId));

      if (searchResult.get(0) == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Part not found in search, please enter part");
        alert.showAndWait();
        partTableView.setItems(Inventory.getAllParts());

      } else {
        partTableView.setItems(searchResult);

      }
      if (partSearchField.getText().equals("")) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Part does not exist");
        alert.showAndWait();

        partTableView.setItems(Inventory.getAllParts());
      }

    } catch (NumberFormatException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Please enter valid part to search.");
      partTableView.setItems(Inventory.lookupPart(partInput));

    }
  }

  /**
   * Search product through UI
   */
  @FXML
  public void onActionProductsSearch(ActionEvent event) {

    String productInput = productSearchField.getText();

    try {
      int productId = valueOf(productInput);
      ObservableList<Product> searchResult = FXCollections.observableArrayList();
      searchResult.add(Inventory.lookupProduct(productId));

      if (searchResult.get(0) == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Product not found in search, please enter product");
        alert.showAndWait();
        productTableView.setItems(Inventory.getAllProducts());
      } else {
        productTableView.setItems(searchResult);
      }

    } catch (NumberFormatException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Please enter valid product to search.");
      productTableView.setItems(Inventory.lookupProduct(productInput));
    }

  }

}
