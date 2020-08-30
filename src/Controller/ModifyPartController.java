package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
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

public class ModifyPartController implements Initializable {
  @Override
  public void initialize(URL url, ResourceBundle rb) {

  }

  Stage stage;
  Parent scene;

  @FXML
  private RadioButton modPartInHouse;

  @FXML
  private ToggleGroup partSource;

  @FXML
  private RadioButton modPartOutsourced;

  @FXML
  private Label modPartVariableName;

  @FXML
  private TextField partIdField;

  @FXML
  private TextField modPartVariableField;

  @FXML
  private TextField partPriceField;

  @FXML
  private TextField partStockField;

  @FXML
  private TextField partNameField;

  @FXML
  private TextField partMaxField;

  @FXML
  private TextField partMinField;

  @FXML
  void onActionModPartIn(ActionEvent event) {

    modPartVariableName.setText("Machine ID:");
  }

  @FXML
  void onActionModPartOut(ActionEvent event) {

    modPartVariableName.setText("Company Name:");
  }

  @FXML
  void onActionReturnToMainScreen(ActionEvent event) throws IOException {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Changes wont be saved, continue?");
    alert.setTitle("CONFIRMATION");

    Optional<ButtonType> result = alert.showAndWait();

    if(result.isPresent() && result.get() == ButtonType.OK) {
      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
      stage.setScene(new Scene(scene));
      stage.show();
    }

  }

  @FXML
  void onActionSave(ActionEvent event) throws IOException {


    if (Integer.parseInt(partStockField.getText()) >= Integer.parseInt(partMaxField.getText()) || Integer.parseInt(partStockField.getText()) <= Integer.parseInt(partMinField.getText())) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Please make sure that inventory quantity is greater than minimum and less than the maximum value.");
      alert.showAndWait();
    } else {

      int id = Integer.parseInt(partIdField.getText());
      String name = partNameField.getText();
      int stock = Integer.parseInt(partStockField.getText());
      double price = Double.parseDouble(partPriceField.getText());
      int min = Integer.parseInt(partMinField.getText());
      int max = Integer.parseInt(partMaxField.getText());

      for(Part part: Inventory.getAllParts()) {

        if (part.getId() == id) {

          int partIndex = Inventory.getAllParts().indexOf(part);

          if (modPartInHouse.isSelected()) {
            if (part instanceof InHouse) {
              part.setName(name);
              part.setStock(stock);
              part.setPrice(price);
              part.setMax(max);
              part.setMin(min);

              ((InHouse) part).setMachineId(Integer.parseInt(modPartVariableField.getText()));
            } else {
              Part inHousePart = new InHouse(id, name, price,stock,min,max, Integer.parseInt(modPartVariableField.getText()));
              Inventory.updatePart(partIndex, inHousePart);
            }
          } else {
            if(part instanceof Outsourced) {
              part.setName(name);
              part.setStock(stock);
              part.setPrice(price);
              part.setMax(max);
              part.setMin(min);

              ((Outsourced) part).setCompanyName(modPartVariableField.getText());
            } else {
              Part outSrcPart = new Outsourced(id, name, price, stock, min, max, modPartVariableField.getText());
              Inventory.updatePart(partIndex, outSrcPart);
            }
          }
          break;
        }
      }
      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
      stage.setScene(new Scene(scene));
      stage.show();
    }
  }

  public void sendPartInfo(Part part) {

    if (!(part instanceof InHouse)) {
      modPartOutsourced.setSelected(true);
      modPartVariableName.setText("Company Name:");
      modPartVariableField.setText(((Outsourced) part).getCompanyName());
    } else {
      modPartInHouse.setSelected(true);
      modPartVariableName.setText("Machine ID:");
      modPartVariableField.setText(String.valueOf(((InHouse) part).getMachineId()));
    }

    partIdField.setText(String.valueOf(part.getId()));
    partNameField.setText(part.getName());
    partStockField.setText(String.valueOf(part.getStock()));
    partPriceField.setText(String.valueOf(part.getPrice()));
    partMaxField.setText(String.valueOf(part.getMax()));
    partMinField.setText(String.valueOf(part.getMin()));

  }

}
