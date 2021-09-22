package main.tradingOrder;


import events.loadFileEvent.LoadFileEventsGenerator;
import events.newDealEvent.NewDealEventsGenerator;
import events.newDealEvent.NewDealEventsHandler;
import events.userSelectedEvent.userSelectedEventsHandler;
import exception.OnlyPositiveNumberException;
import javafx.scene.input.KeyEvent;
import main.MainController;
import exception.SymbolDidntExistException;
import exception.UserDidntExistException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.popUp.PopUp;

import java.util.Collection;

public class TradingOrderController implements userSelectedEventsHandler {

    final private static Engine.Engine ENGINE= Engine.Engine.getEngine();//Model

    @FXML private VBox MainComponent;
    @FXML private HBox upperComponent;
    @FXML private RadioButton sellOrdinanceRadioButton;
    @FXML private RadioButton buyOrdinanceRadioButton;
    @FXML private SplitMenuButton selectStockButton;
    @FXML private HBox ordinanceTypeComponent;
    @FXML private RadioButton MTKRadioButton;
    @FXML private RadioButton LMTRadioButton;
    @FXML private HBox amountComponent;
    @FXML private Label amountLabel;
    @FXML private TextField amountTextField;
    @FXML private Button doneButton;
    @FXML private Label limitLabel;
    @FXML private TextField limitTextField;

    //properties
    private SimpleStringProperty userNameProperty;
    private SimpleBooleanProperty isStockSelectedProperty;
    private SimpleStringProperty StockSelectedProperty;

    //first component
    private SimpleBooleanProperty isTypeOfOrdinanceSelectedProperty;
    private SimpleBooleanProperty isSellOrdinanceSelectedProperty;
    private SimpleBooleanProperty isBuyOrdinanceSelectedProperty;

    //events
    LoadFileEventsGenerator loadFileEventsGenerator;
    NewDealEventsGenerator newDealEventsGenerator;
    MainController mainController;

 public TradingOrderController(){
     isStockSelectedProperty=new SimpleBooleanProperty(false);
     isTypeOfOrdinanceSelectedProperty=new SimpleBooleanProperty(false);
     isSellOrdinanceSelectedProperty=new SimpleBooleanProperty(false);
     isBuyOrdinanceSelectedProperty=new SimpleBooleanProperty(false);
     StockSelectedProperty=new SimpleStringProperty();
     userNameProperty=new SimpleStringProperty();
     loadFileEventsGenerator=new LoadFileEventsGenerator();
     newDealEventsGenerator=new NewDealEventsGenerator();
 }

    @FXML
    private void initialize() {
     selectStockButton.disableProperty().bind(isTypeOfOrdinanceSelectedProperty.not());
      doneButton.disableProperty().bind(isStockSelectedProperty.not());

      // force the field to be numeric only
        amountTextField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue,
                                        String newValue) {
                        if (!newValue.matches("\\d*")) {
                            amountTextField.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    }
                });
        limitTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    limitTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
 }

    public void setMainController(MainController controller) {
     mainController=controller;
        newDealEventsGenerator.addHandler(mainController);
    }

    @FXML
    void LMTRadioButtonOnAction(ActionEvent event) {
MTKRadioButton.setSelected(false);
limitTextField.setDisable(false);
 }

    @FXML
    void MTKRadioButtonOnAction(ActionEvent event) throws SymbolDidntExistException {
        LMTRadioButton.setSelected(false);
        limitTextField.setDisable(true);
        limitTextField.clear();

        if (isStockSelectedProperty.getValue()){

        Boolean availableToBuy=ENGINE.availableToBuy(StockSelectedProperty.get());
        Boolean sellOrdinance = isSellOrdinanceSelectedProperty.getValue();
        Boolean availableToSell=ENGINE.availableToSell(StockSelectedProperty.get());
        Boolean buyOrdinance = isBuyOrdinanceSelectedProperty.getValue();

        if (availableToBuy&&sellOrdinance){
            limitTextField.setText(String.valueOf(ENGINE.getBuyRate(StockSelectedProperty.getValue())));
       }
        else if(availableToSell&&buyOrdinance){

            limitTextField.setText(String.valueOf(ENGINE.getSalesRate(StockSelectedProperty.getValue())));
        }}
 }

    @FXML
    void limitTextFieldOnAction(ActionEvent event) {

    }

    @FXML
    void doneButtonOnAction(ActionEvent event) {
if(submitTradeOrdinance()){
    clearAllSelected();
    newDealEventsGenerator.fireEvent();
}
    }

    //return true if submit sucsseffuly
    private boolean submitTradeOrdinance()  {
     String type;
     int amount;
     int limit=0;

     // LMT/MTK
     if (MTKRadioButton.isSelected()==true){ type="MKT"; }
    else if (LMTRadioButton.isSelected()==true){ type="LMT";
         if(limitTextField.getText().isEmpty()){ new PopUp("Please write limit");return false; }
         else { limit= Integer.valueOf(limitTextField.getText());
             if(limit<=0){new PopUp("Please write limit up to 0");return false; }
         }
    }
    else{new PopUp("Please selected LMT/MTK ordinance");return false; }

    if(amountTextField.getText().isEmpty()){ new PopUp("please write amount");return false; }
    else {
        amount= Integer.valueOf(amountTextField.getText());
        if(amount<=0){new PopUp("Please write amount up to 0");return false; }
        if((sellOrdinanceRadioButton.isSelected())&&(amount>ENGINE.getHoldingsAmount(userNameProperty.getValue(),StockSelectedProperty.get()))){
            {new PopUp("You have only "+ENGINE.getHoldingsAmount(userNameProperty.getValue(),StockSelectedProperty.get())+' '+StockSelectedProperty.getValue()+" stocks");return false; }
        }
    }

 if(sellOrdinanceRadioButton.isSelected()){
     try {
         new PopUp(ENGINE.saleOrdinance(StockSelectedProperty.getValue(), amount, limit, userNameProperty.getValue(), type),500,400);
         return true;
     }
     catch (Exception e) {
         new PopUp(e.getMessage());
         return false;
     } }

 else if(buyOrdinanceRadioButton.isSelected()){
        if(buyOrdinanceRadioButton.isSelected()){
            try {
                new PopUp(ENGINE.buyOrdinance(StockSelectedProperty.getValue(), amount, limit, userNameProperty.getValue(), type),500,400);
                return true;
            }
            catch (Exception e) {
                new PopUp(e.getMessage());
                return false;
            }
        }
 }
 return false;
 }

    private void clearAllSelected() {

     sellOrdinanceRadioButton.setSelected(false);
        isBuyOrdinanceSelectedProperty.setValue(false);
     buyOrdinanceRadioButton.setSelected(false);
        isSellOrdinanceSelectedProperty.setValue(false);
        isTypeOfOrdinanceSelectedProperty.setValue(false);

      selectStockButton.setText("Select stock");
isStockSelectedProperty.setValue(false);

       MTKRadioButton.setSelected(false);
      LMTRadioButton.setSelected(false);
        amountTextField.clear();
       limitTextField.clear();
        limitTextField.setDisable(false);

    }

    @FXML
    void selectStockButtonOnAction(ActionEvent event) {

    }

    @FXML
    void sellOrdinanceRadioButtonOnAction(ActionEvent event) throws UserDidntExistException {
     clearAllSelected();
        makeStocksList(ENGINE.getUserHoldingsSymbols(userNameProperty.get()));
  sellOrdinanceRadioButton.setSelected(true);
        isTypeOfOrdinanceSelectedProperty.setValue(true);
        isSellOrdinanceSelectedProperty.setValue(true);

    }

    @FXML
    void buyOrdinanceRadioButtonOnAction(ActionEvent event) {
        clearAllSelected();
        makeStocksList(ENGINE.getStocksListNames());
        isTypeOfOrdinanceSelectedProperty.setValue(true);
        isBuyOrdinanceSelectedProperty.setValue(true);
        buyOrdinanceRadioButton.setSelected(true);
    }

    public void makeStocksList(Collection<String> symbols) {
        selectStockButton.getItems().clear();

        for (String name : symbols) {
            MenuItem newMenuItem = new MenuItem(name);

            //what happend when we select a stock
            newMenuItem.setOnAction((e) -> {
                selectStockButton.setText(newMenuItem.getText());
                isStockSelectedProperty.set(true);
                StockSelectedProperty.set(newMenuItem.getText());
                amountTextField.setEditable(true);
                limitTextField.clear();
                MTKRadioButton.setSelected(false);
                LMTRadioButton.setSelected(false);
            });
            selectStockButton.getItems().add(newMenuItem);
        }
    }


    @FXML
    void amountTextChanged(KeyEvent event) {
    }

    @FXML
    void amountTextFieldOnAction(ActionEvent event) {
}

    @Override
    public void userSelectedEventHappened(String UserName) throws UserDidntExistException {
        userNameProperty.set(UserName);
        clearAllSelected();
        if (isSellOrdinanceSelectedProperty.getValue()){
            makeStocksList(ENGINE.getUserHoldingsSymbols(UserName));
        }
        else if (isBuyOrdinanceSelectedProperty.getValue()){
            makeStocksList(ENGINE.getStocksListNames());
        }
    }
}
