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
import static java.lang.Integer.valueOf;

/**
 * This method is the Main user interface controller for parts and products
 */
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
   * This method Populates tables and columns with values
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
   * This method Adds parts through UI
   *
   * @throws IOException corrected by letting user know window was not loaded.
   */
  @FXML
  public void onActionAddPart(ActionEvent event) {
    try {
      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      scene = FXMLLoader.load(getClass().getResource("/view/AddPartView.fxml"));
      stage.setScene(new Scene(scene));
      stage.show();
    } catch (IOException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("No FXML associated.");
    }
  }

  /**
   * This method Adds products through UI
   *
   * @throws IOException corrected by letting user know window was not loaded.
   */
  @FXML
  public void onActionAddProduct(ActionEvent event) {
    try {
      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      scene = FXMLLoader.load(getClass().getResource("/view/AddProductView.fxml"));
      stage.setScene(new Scene(scene));
      stage.show();
    } catch (IOException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("No FXML associated.");
    }
  }

  /**
   * This method Deletes part through UI
   * (The application confirms the “Delete” and “Remove” actions)
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
   * This method Deletes product through UI
   *
   * @throws NullPointerException is corrected by validating that a part is not associated or that product has been selected
   *                              (The user should not delete a product that has a part associated with it.)
   *                              (The application confirms the “Delete” and “Remove” actions.)
   *                              ( The application will not crash when inappropriate user data is entered in the forms;
   *                              instead, error messages should be generated.)
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
   * This method exits the program
   */
  @FXML
  public void onActionExit(ActionEvent event) {

    System.exit(0);

  }

  /**
   * This method modifies the part through UI
   *
   * @throws NullPointerException is corrected by issuing an alert that part is missing to modify
   * @throws IOException          if fail to load modify part window.
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
   * This method modifies product through UI
   *
   * @throws NullPointerException is corrected by issuing an alert that product is missing to modify
   * @throws IOException          if fail to load modify product window.
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
   * This method allows a part to be searched through the UI and Checks/Validates part exist or was entered in correctly
   *
   * @throws NumberFormatException has been corrected by issuing an alert that part is not valid.
   *                               * Next Version: Add Ability to search with Part Name or Part ID
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
   * Search product through UI and Checks/Validates product exist or was entered in correctly
   * Next Version: Add Ability to search with Product Name or Product ID
   *
   * @throws NumberFormatException is corrected by giving an alert if part is not valid. E.g ID is not found.
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
