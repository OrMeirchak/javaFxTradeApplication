package main.displayStock;

import events.loadFileEvent.LoadFileEventsGenerator;
import events.loadFileEvent.LoadFileEventsHandler;
import events.newDealEvent.NewDealEventsGenerator;
import events.newDealEvent.NewDealEventsHandler;
import events.stockSelectedEvent.StockSelectedEventsGenerator;
import main.displayStock.buyOrdersTable.buyOrdersTableController;
import main.displayStock.dealsTable.dealsTableController;
import main.displayStock.saleOrdersTable.saleOrdersTableController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;

public class displayStockController implements LoadFileEventsHandler , NewDealEventsHandler {

    final private static Engine.Engine ENGINE = Engine.Engine.getEngine();//Model

    @FXML private SplitMenuButton selectStockSplitMenuButton;
    @FXML private Tab saleOrdersTab;
    @FXML private Tab buyOrdersTab;
    @FXML private Tab dealsTab;

    private SimpleBooleanProperty isStockSelectedProperty;
    private SimpleStringProperty selectedStockProperty;

    //events generator
    StockSelectedEventsGenerator stockSelectedEventsGenerator;
    NewDealEventsGenerator newDealEventsGenerator;

    //controllers
    @FXML private buyOrdersTableController buyOrdersComponentController;
    @FXML private saleOrdersTableController saleOrdersComponentController;
    @FXML private dealsTableController dealsTableComponentController;

    public displayStockController() {
        isStockSelectedProperty = new SimpleBooleanProperty(false);
        selectedStockProperty = new SimpleStringProperty();
        stockSelectedEventsGenerator=new StockSelectedEventsGenerator();
        newDealEventsGenerator=new NewDealEventsGenerator();
    }

    @FXML
    public void initialize() {
        saleOrdersTab.disableProperty().bind(isStockSelectedProperty.not());
        buyOrdersTab.disableProperty().bind(isStockSelectedProperty.not());
        dealsTab.disableProperty().bind(isStockSelectedProperty.not());


        stockSelectedEventsGenerator.addHandler(buyOrdersComponentController);
        stockSelectedEventsGenerator.addHandler(saleOrdersComponentController);
        stockSelectedEventsGenerator.addHandler(dealsTableComponentController);
        //
        newDealEventsGenerator.addHandler(buyOrdersComponentController);
        newDealEventsGenerator.addHandler(saleOrdersComponentController);
        newDealEventsGenerator.addHandler(dealsTableComponentController);

    }



    public void makeStocksList() {
        selectStockSplitMenuButton.getItems().clear();

        for (String name : ENGINE.getStocksListNames()) {
            MenuItem newMenuItem = new MenuItem(name);

            //what happend when we select a stock
            newMenuItem.setOnAction((e) -> {
                selectedStockProperty.set(newMenuItem.getText());
                isStockSelectedProperty.set(true);
                selectStockSplitMenuButton.setText(newMenuItem.getText());
                stockSelectedEventsGenerator.fireEvent(selectedStockProperty.getValue());
            });
            selectStockSplitMenuButton.getItems().add(newMenuItem);
        }
    }

    @FXML
    void selectStockSplitMenuButtonOnAction(ActionEvent event) {

    }
    public void loadFileEventHappened() {
        makeStocksList();
    }

    @Override
    public void NewDealEventHappened() {
newDealEventsGenerator.fireEvent();
    }
}