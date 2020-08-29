package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


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

