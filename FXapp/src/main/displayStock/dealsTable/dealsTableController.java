package main.displayStock.dealsTable;

import events.newDealEvent.NewDealEventsHandler;
import events.stockSelectedEvent.StockSelectedEventsHandler;
import data.TradeData;
import exception.SymbolDidntExistException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class dealsTableController implements StockSelectedEventsHandler , NewDealEventsHandler {

    final private static Engine.Engine ENGINE= Engine.Engine.getEngine();//Model


    @FXML private TableView<TradeData> table;
    @FXML private TableColumn<TradeData, String> sellerColumn;
    @FXML private TableColumn<TradeData, String> dateColumn;
    @FXML private TableColumn<TradeData, String> typeColumn;
    @FXML private TableColumn<TradeData, String> amountColumn;
    @FXML private TableColumn<TradeData, String> priceColumn;
    @FXML private TableColumn<TradeData, String> buyerColumn;


    SimpleStringProperty stockSymbol;
    SimpleBooleanProperty isStockSelected;

    public dealsTableController() {
        stockSymbol=new SimpleStringProperty();
        isStockSelected=new SimpleBooleanProperty();}

    @FXML
    private void initialize() {
        buyerColumn.setCellValueFactory(new PropertyValueFactory("Buyer"));
        sellerColumn.setCellValueFactory(new PropertyValueFactory("Saller"));
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

            table.getItems().addAll(FXCollections.observableArrayList(ENGINE.getDealsData(symbol)));


        } catch (SymbolDidntExistException e) {
            e.printStackTrace();
        }
    }


}
