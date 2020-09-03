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

/**Contains methods to add products */
public class AddProductController implements Initializable {

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    /** Sets the Parts table view*/
    inventoryPartsTableView.setItems(Inventory.getAllParts());

    /** Sets the associated parts table view*/
    associatedPartsTableView.setItems(tempAssociatedPartsList);


    /** Fills the Parts column with values*/
    inventoryPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
    inventoryPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    inventoryStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
    inventoryPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    /** Fills the associated parts column with values*/

    associatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
    associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    associatedStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
    associatedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


  }

  Stage stage;
  Parent scene;
  ObservableList<Part> tempAssociatedPartsList = FXCollections.observableArrayList();

  @FXML
  private TextField addProductId;

  @FXML
  private TextField addProductName;

  @FXML
  private TextField addProductInventory;

  @FXML
  private TextField addProductPrice;

  @FXML
  private TextField addProductMax;

  @FXML
  private TextField addProductMin;

  @FXML
  private TextField addProductPartSearchField;

  @FXML
  private TableView<Part> inventoryPartsTableView;

  @FXML
  private TableColumn<Part, Integer> inventoryPartID;

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

  /**Adds part through product UI*/
  @FXML
  public void onActionAddPart(ActionEvent event) {
    tempAssociatedPartsList.add(inventoryPartsTableView.getSelectionModel().getSelectedItem());
  }
  /**Deletes part through product UI
   * (The application confirms the “Delete” and “Remove” actions.)
   * */
  @FXML
  public void onActionDeletePart(ActionEvent event) {

    if (associatedPartsTableView.getSelectionModel().getSelectedItem() != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Part will be permanently deleted, do you want to continue?");
      alert.setTitle("CONFIRMATION");

      Optional<ButtonType> result = alert.showAndWait();

      if (!result.isPresent() || result.get() != ButtonType.OK) {
        return;
      }
      tempAssociatedPartsList.remove(associatedPartsTableView.getSelectionModel().getSelectedItem());
    }
  }
  /**return to Main Screen through product UI*/
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
  /** Saves through product UI
   * Checks/validates for acceptable Inventory Quantity and fields
   * (Min should be less than Max; and Inv should be between those two values.)
   *(The application will not crash when inappropriate user data is entered in the forms;
   * instead, error messages should be generated.)
   * @exception IOException Not able to save if no fields are filled.
   * */
  @FXML
  public void onActionSave(ActionEvent event) throws IOException {
    try{
      int id = Inventory.getAllProducts().size() + 1;
      String name = addProductName.getText();
      double price = Double.parseDouble(addProductPrice.getText());
      int stock = Integer.parseInt(addProductInventory.getText());
      int min = Integer.parseInt(addProductMin.getText());
      int max = Integer.parseInt(addProductMax.getText());

      if (stock >= max || stock <= min) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Please make sure that inventory quantity is greater than minimum and less than the maximum value.");
        alert.showAndWait();
      } else {
        Inventory.addProduct(new Product(id, name, price, stock, min, max));

        tempAssociatedPartsList.forEach(tempPart -> Inventory.getAllProducts().get(Inventory.getAllProducts().size() - 1).addAssociatedPart(tempPart));

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
      }
    }
    catch (NumberFormatException e){
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Please fill out fields to save");
      alert.showAndWait();
    }


  }
  /** Search part through product UI
   * @exception NumberFormatException if part not found.
   * */
  @FXML
  public void onActionSearchProductPart(ActionEvent event) {

    String partInput = addProductPartSearchField.getText();

    try {
      int partId = Integer.parseInt(partInput);
      ObservableList<Part> searchResult = FXCollections.observableArrayList();
      searchResult.add(Inventory.lookupPart(partId));

      if (searchResult.get(0) == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("Part not found in search, please enter valid part");
        alert.showAndWait();
        inventoryPartsTableView.setItems(Inventory.getAllParts());
      } else {
        inventoryPartsTableView.setItems(searchResult);
      }

    } catch (NumberFormatException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("ERROR");
      alert.setContentText("Part not found in search, please enter part");
      alert.showAndWait();
      inventoryPartsTableView.setItems(Inventory.lookupPart(partInput));
    }

  }

}
