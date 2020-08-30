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

import static java.lang.Double.*;


public class AddProductController implements Initializable {

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

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Set Parts table view
    inventoryPartsTableView.setItems(Inventory.getAllParts());

    // Fill Parts column with values
    inventoryPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
    inventoryPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    inventoryStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
    inventoryPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


    // Set associated parts table view
    associatedPartsTableView.setItems(tempAssociatedPartsList);

    // Fill associated parts column with values

    associatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
    associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    associatedStockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
    associatedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


  }

  @FXML
  void onActionAddPart(ActionEvent event) {
    tempAssociatedPartsList.add(inventoryPartsTableView.getSelectionModel().getSelectedItem());
  }

  @FXML
  void onActionDeletePart(ActionEvent event) {

    if (associatedPartsTableView.getSelectionModel().getSelectedItem() != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete the part, do you want to continue?");
      alert.setTitle("CONFIRMATION");

      Optional<ButtonType> result = alert.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
        tempAssociatedPartsList.remove(associatedPartsTableView.getSelectionModel().getSelectedItem());
      }
    }
  }

  @FXML
  void onActionReturnToMainScreen(ActionEvent event) throws IOException {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Changes wont be saved, continue?");
    alert.setTitle("CONFIRMATION");

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
      stage.setScene(new Scene(scene));
      stage.show();
    }

  }

  @FXML
  void onActionSave(ActionEvent event) throws IOException {

    int id = Inventory.getAllProducts().size() + 1;
    String name = addProductName.getText();
    double price = Double.parseDouble(addProductPrice.getText());
    int stock = Integer.parseInt(addProductInventory.getText());
    int min = Integer.parseInt(addProductMin.getText());
    int max = Integer.parseInt(addProductMax.getText());

    if (stock < max && stock > min) {
      Inventory.addProduct(new Product(id, name, price, stock, min, max));

      for (Part tempPart : tempAssociatedPartsList) {
        Inventory.getAllProducts().get(Inventory.getAllProducts().size() - 1).addAssociatedPart(tempPart);
      }

      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
      stage.setScene(new Scene(scene));
      stage.show();
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Please make sure that inventory quantity is greater than minimum and less than the maximum value.");
      alert.showAndWait();
    }

  }

  @FXML
  void onActionSearchProductPart(ActionEvent event) {

    String partInput = addProductPartSearchField.getText();

    try {
      int partId = Integer.parseInt(partInput);
      ObservableList<Part> searchResult = FXCollections.observableArrayList();
      searchResult.add(Inventory.lookupPart(partId));

      if (searchResult.get(0) == null) {
        inventoryPartsTableView.setItems(Inventory.getAllParts());
      } else {
        inventoryPartsTableView.setItems(searchResult);
      }
    } catch (NumberFormatException e) {
      inventoryPartsTableView.setItems(Inventory.lookupPart(partInput));
    }

  }


}
