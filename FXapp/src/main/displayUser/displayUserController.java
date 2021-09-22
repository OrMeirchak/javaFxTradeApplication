package main.displayUser;

import events.newDealEvent.NewDealEventsHandler;
import events.userSelectedEvent.userSelectedEventsHandler;
import exception.UserDidntExistException;
import user.Holding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class displayUserController implements userSelectedEventsHandler , NewDealEventsHandler {

    final private static Engine.Engine ENGINE= Engine.Engine.getEngine();//Model


    @FXML private Label massage1;
    @FXML private TableView<Holding> table;
    @FXML private TableColumn<Holding, String> symbolColumn;
    @FXML private TableColumn<Holding, Integer> amountColumn;
    @FXML private TableColumn<Holding, Integer> gateColumn;
    @FXML private Label massage2;

    SimpleStringProperty userName;

   @FXML
    private void initialize() {
        symbolColumn.setCellValueFactory(new PropertyValueFactory("StockSymbol"));
        amountColumn.setCellValueFactory(new PropertyValueFactory("Amount"));
        gateColumn.setCellValueFactory(new PropertyValueFactory("Rate"));
       userName=new SimpleStringProperty();
    }

    @Override
    public void userSelectedEventHappened(String UserName) {
        userName.setValue(UserName);
        setUpperLabel(UserName);
        setLowerLabel(UserName);
        buildTable(UserName);
    }

    private void buildTable(String userName) {
        table.getItems().clear();

        table.setEditable(true);

        try {
            table.getItems().addAll(FXCollections.observableArrayList(ENGINE.getHoldingData(userName)));
        } catch (UserDidntExistException e) {
            e.printStackTrace();
        }
    }

    private void setLowerLabel(String userName) {
        try {
            massage2.setText("Total holdings for " + userName+" : "+ENGINE.getUserTotalValue(userName));
        } catch (UserDidntExistException e) {
            e.printStackTrace();
        }
    }

    private void setUpperLabel(String userName) {
        massage1.setText(userName+" holdings:");
    }

    @Override
    public void NewDealEventHappened() {
        buildTable(userName.getValue());
    }
}
