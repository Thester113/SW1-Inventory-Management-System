package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
/** Contains methods to add parts*/
public class AddPartController implements Initializable {
  @Override
  public void initialize(URL url, ResourceBundle rb) {

  }

  Stage stage;
  Parent scene;

  @FXML
  private RadioButton addPartInHouse;

  @FXML
  private ToggleGroup partSource;

  @FXML
  private RadioButton addPartOutsourced;

  @FXML
  private Label addPartVariableLabel;

  @FXML
  private TextField addPartID;
  @FXML
  private TextField addPartName;

  @FXML
  private TextField addPartInventory;

  @FXML
  private TextField addPartPrice;

  @FXML
  private TextField addPartMax;

  @FXML
  private TextField addPartMin;

  @FXML
  private TextField addPartVariableField;

  /**
   * Adds part through part UI with InHouse
   */
  @FXML
  public void onActionAddPartIn(ActionEvent event) {

    addPartVariableLabel.setText("Machine ID:");
  }

  /**
   * Adds part through part UI with Outsourced
   */
  @FXML
  void onActionAddPartOut(ActionEvent event) {

    addPartVariableLabel.setText("Company Name:");
  }

  /**
   * Return to main screen UI
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

  /**
   * Saves part through part UI
   */
  @FXML
  public void onActionSave(ActionEvent event) throws IOException {

    int id = Inventory.getAllParts().size() + 1;
    String name = addPartName.getText();
    double price = Double.parseDouble(addPartPrice.getText());
    int stock = Integer.parseInt(addPartInventory.getText());
    int min = Integer.parseInt(addPartMin.getText());
    int max = Integer.parseInt(addPartMax.getText());

    if (stock >= max || stock <= min) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Please make sure that inventory quantity is greater than minimum and less than the maximum value.");
      alert.showAndWait();
    } else {

      if (addPartInHouse.isSelected()) {

        int machineId = Integer.parseInt(addPartVariableField.getText());
        Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
      } else {

        String companyName = addPartVariableField.getText();
        Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
      }

      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
      stage.setScene(new Scene(scene));
      stage.show();
    }
  }

}