package main.displayStock.buyOrdersTable;


import events.loadFileEvent.LoadFileEventsGenerator;
import events.newDealEvent.NewDealEventsHandler;
import events.stockSelectedEvent.StockSelectedEventsHandler;
import data.TradeData;
import exception.OnlyPositiveNumberException;
import exception.SymbolDidntExistException;
import trade.Buy;
import trade.OrdinanceType;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;

public class buyOrdersTableController implements StockSelectedEventsHandler , NewDealEventsHandler {

    final private static Engine.Engine ENGINE= Engine.Engine.getEngine();//Model

    @FXML private TableView<TradeData> table;
    @FXML private TableColumn<TradeData, String> userColumn;
    @FXML private TableColumn<TradeData, String> dateColumn;
    @FXML private TableColumn<TradeData, String> typeColumn;
    @FXML private TableColumn<TradeData, String> amountColumn;
    @FXML private TableColumn<TradeData, String> priceColumn;

    SimpleStringProperty stockSymbol;
SimpleBooleanProperty isStockSelected;

    public buyOrdersTableController() {
        stockSymbol=new SimpleStringProperty();
        isStockSelected=new SimpleBooleanProperty();

    }

    @FXML
    private void initialize() {
        userColumn.setCellValueFactory(new PropertyValueFactory("User"));
        dateColumn.setCellValueFactory(new PropertyValueFactory("Date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory("Type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory("Amount"));
        priceColumn.setCellValueFactory(new PropertyValueFactory("Price"));
        isStockSelected.setValue(false);
    }

    @Override
    public void stockSelectedEventHappened(String symbol) {
        stockSymbol.setValue(symbol);
        isStockSelected.setValue(true);
buildTable(symbol);

    }

    @Override
    public void NewDealEventHappened() {
        if(isStockSelected.getValue()==true) {
            buildTable(stockSymbol.getValue());
        }
    }

    private void buildTable(String symbol)  {
        try {


            table.getItems().clear();

            table.setEditable(true);

            table.getItems().addAll(FXCollections.observableArrayList(ENGINE.getBuyOrdinanceData(symbol)));


        } catch (SymbolDidntExistException e) {
            e.printStackTrace();
        }
    }

}
