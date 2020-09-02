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

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

/**Contains controller methods for modifying products */
public class ModifyProductController implements Initializable {
  Stage stage;
  Parent scene;
  ObservableList<Part> modifiedAssociatedParts = FXCollections.observableArrayList();
  @FXML
  private TextField productIdField;
  @FXML
  private TextField productNameField;
  @FXML
  private TextField productStockField;
  @FXML
  private TextField productPriceField;
  @FXML
  private TextField productMaxField;
  @FXML
  private TextField productMinField;
  @FXML
  private TextField partSearchField;
  @FXML
  private TableView<Part> inventoryPartsTableView;
  @FXML
  private TableColumn<Part, Integer> inventoryPartId;
  @FXML
  private TableColumn<Part, String> inventoryPartName;
  @FXML
  private TableColumn<Part, Integer> inventoryStockLevel;
  @FXML
  private TableColumn<Part, Double> inventoryPrice;
  @FXML
  private TableView<Part> associatedPartsTableView;
  @FXML
  private TableColumn<Part, Integer> associatedPartId;
  @FXML
  private TableColumn<Part, String> associatedPartName;
  @FXML
  private TableColumn<Part, Integer> associatedStockLevel;
  @FXML
  private TableColumn<Part, Double> associatedPrice;

  /**
   * Populates tables and columns with values
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    /** Set Parts table view*/
    inventoryPartsTableView.setItems(Inventory.getAllParts());

    /** Fill Parts column with values*/
    inventoryPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
    inventoryPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    inventoryStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
    inventoryPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

  }

  /**
    Add part through modify product UI
   */
  @FXML
  public void onActionAddPart(ActionEvent event) {

    modifiedAssociatedParts.add(inventoryPartsTableView.getSelectionModel().getSelectedItem());
  }

  /**Delete part through modify product UI*/
  @FXML
  public void onActionDeletePart(ActionEvent event) {

    if(associatedPartsTableView.getSelectionModel().getSelectedItem() != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will remove the part from the product, do you want to continue?");
      alert.setTitle("CONFIRMATION");

      Optional<ButtonType> result = alert.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
        modifiedAssociatedParts.removeAll(associatedPartsTableView.getSelectionModel().getSelectedItem());
      }
    }
  }
  /**Return to main screen through product UI
   */
  @FXML
  public void onActionReturnToMainScreen(ActionEvent event) throws IOException {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Changes wont be saved, do you want to continue?");
    alert.setTitle("CONFIRMATION");

    Optional<ButtonType> result = alert.showAndWait();

    if (!result.isPresent() || result.get() != ButtonType.OK) {
      return;
    }
    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
    stage.setScene(new Scene(scene));
    stage.show();
  }

  /**Save through modify product UI
   */
  @FXML
  public void onActionSave(ActionEvent event) throws IOException {

    if (parseInt(productStockField.getText()) >= parseInt(productMaxField.getText()) || parseInt(productStockField.getText()) <= parseInt(productMinField.getText())) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Please make sure that inventory number is greater than minimum and less than the maximum value.");
      alert.showAndWait();
    } else {

      int id = parseInt(productIdField.getText());
      String name = productNameField.getText();
      int stock = parseInt(productStockField.getText());
      double price = Double.parseDouble(productPriceField.getText());
      int min = parseInt(productMinField.getText());
      int max = parseInt(productMaxField.getText());

      Inventory.getAllProducts().stream().filter(product -> product.getId() == id).mapToInt(product -> Inventory.getAllProducts().indexOf(product)).forEach(productIndex -> {
        Product modifiedProduct = new Product(id, name, price, stock, min, max);
        modifiedProduct.getAllAssociatedParts().setAll(modifiedAssociatedParts);
        Inventory.updateProduct(productIndex, modifiedProduct);
      });

      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
      stage.setScene(new Scene(scene));
      stage.show();
    }
  }

  /**Search through modify product UI*/
  @FXML
  public void onActionSearchPart(ActionEvent event) {

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
        inventoryPartsTableView.setItems(Inventory.getAllParts());
      } else {
        inventoryPartsTableView.setItems(Inventory.getAllParts());
        inventoryPartsTableView.setItems(searchResult);
      }
    } catch (NumberFormatException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("ERROR");
      alert.setContentText("Part input not valid");
      alert.showAndWait();
      inventoryPartsTableView.setItems(Inventory.lookupPart(partInput));
    }

  }

  /**Send modifications through modify product UI to other windows*/
  public void sendProductInfo(Product product) {

    /** Set the product info fields*/
    productIdField.setText(Integer.toString(product.getId()));
    productNameField.setText(product.getName());
    productStockField.setText(Integer.toString(product.getStock()));
    productPriceField.setText(Double.toString(product.getPrice()));
    productMaxField.setText(Integer.toString(product.getMax()));
    productMinField.setText(Integer.toString(product.getMin()));

    /** Set the associated parts table view*/
    modifiedAssociatedParts.setAll(product.getAllAssociatedParts());
    associatedPartsTableView.setItems(modifiedAssociatedParts);

    /** Fill the associated parts column*/

    associatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
    associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    associatedStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
    associatedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

  }

}
