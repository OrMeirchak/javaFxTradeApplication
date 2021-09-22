package main;

import events.loadFileEvent.LoadFileEventsGenerator;
import events.newDealEvent.*;
import events.userSelectedEvent.UserSelectedEventsGenerator;
import main.displayStock.displayStockController;
import main.displayUser.displayUserController;
import main.popUp.PopUp;
import main.tradingOrder.TradingOrderController;
import exception.UserDidntExistException;
import user.UsersList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

public class MainController implements NewDealEventsHandler {
    final private static Engine.Engine ENGINE = Engine.Engine.getEngine();//Model

    @FXML private TitledPane displayStock;
    @FXML private TitledPane displayUser;
    @FXML private TitledPane tradingOrder;
    @FXML private Button loadFileButton;
    @FXML private Button exitButton;
    @FXML private SplitMenuButton chooseUser;
    @FXML private RadioButton fastLoadSpeedRadioButton;
    @FXML private RadioButton slowLoadSpeedRadioButton;
    @FXML private Label loadFileStatusLabel;
    @FXML private Label loadFileLabel;

    //childrens controllers
    @FXML private displayStockController displayStockComponentController;
    @FXML private displayUserController displayUserComponentController;
    @FXML private TradingOrderController tradingOrderComponentController;



    private SimpleStringProperty selectedFileProperty;
    private SimpleStringProperty selectedUserProperty;
    private SimpleBooleanProperty isFileSelected;
    private SimpleBooleanProperty isFileValid;
    private SimpleBooleanProperty isUserSelected;
    private SimpleBooleanProperty slowLoadSpeedIsSelected;
    private SimpleBooleanProperty fastLoadSpeedIsSelected;


    private Stage primaryStage;
    LoadFileEventsGenerator loadFileEventsGenerator;
    UserSelectedEventsGenerator userSelectedEventsGenerator;
    NewDealEventsGenerator newDealEventsGenerator;

    public MainController() {
        selectedUserProperty = new SimpleStringProperty();
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        isFileValid = new SimpleBooleanProperty(false);
        isUserSelected = new SimpleBooleanProperty(false);
        loadFileEventsGenerator = new LoadFileEventsGenerator();
        userSelectedEventsGenerator = new UserSelectedEventsGenerator();
        newDealEventsGenerator = new NewDealEventsGenerator();
        fastLoadSpeedIsSelected=new SimpleBooleanProperty(true);
        slowLoadSpeedIsSelected=new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        chooseUser.disableProperty().bind(isFileValid.not());
        displayStock.disableProperty().bind(isFileValid.not());
        displayUser.disableProperty().bind(isUserSelected.not());
        tradingOrder.disableProperty().bind(isUserSelected.not());

        fastLoadSpeedRadioButton.setSelected(true);
        slowLoadSpeedRadioButton.setSelected(false);
        loadFileStatusLabel.setText("");
        loadFileLabel.setText("");

        //events
        loadFileEventsGenerator.addHandler(displayStockComponentController);
        userSelectedEventsGenerator.addHandler(tradingOrderComponentController);
        userSelectedEventsGenerator.addHandler(displayUserComponentController);
        newDealEventsGenerator.addHandler(displayUserComponentController);
        newDealEventsGenerator.addHandler(displayStockComponentController);
        //

        tradingOrderComponentController.setMainController(this);

        ENGINE.setMainController(this);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void exitButtonClicked(ActionEvent event) {
    }


    @FXML void slowLoadSpeedRadioButtonOnAction(ActionEvent event){
        slowLoadSpeedIsSelected.setValue(true);

       fastLoadSpeedRadioButton.setSelected(false);
        fastLoadSpeedIsSelected.setValue(false);

    }

    @FXML
    void fastLoadSpeedRadioButtonOnAction(ActionEvent event){
        slowLoadSpeedIsSelected.setValue(false);
        slowLoadSpeedRadioButton.setSelected(false);

        fastLoadSpeedIsSelected.setValue(true);
    }

    @FXML
    void loadFileButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);

        Consumer<String> exceptionHandling  = msg -> { new PopUp(msg+" please upload a valid file");};

        int sleepTime=0;

        if(slowLoadSpeedIsSelected.getValue()==true){
            sleepTime=2000;
        }

        try {
            ENGINE.loadFile(selectedFile.getPath(), sleepTime,() -> {
                isFileValid.set(true);
                new PopUp("File loaded successfully");
                buildUsersList();
                loadFileEventsGenerator.fireEvent();
            },
                    exceptionHandling);

        } catch (Exception e) {
            new PopUp(e.getMessage() + '\n' + "please select another file");
        }
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        loadFileLabel.setText("Load file status : ");
        loadFileStatusLabel.textProperty().bind(aTask.messageProperty());


            aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                onTaskFinished(Optional.ofNullable(onFinish),aTask.getValue()); });
    }

    public void onTaskFinished(Optional<Runnable> onFinish,boolean taskSucceeded) {
        loadFileStatusLabel.textProperty().unbind();
        loadFileStatusLabel.setText("");
        loadFileLabel.setText("");

        if (taskSucceeded) {
            onFinish.ifPresent(Runnable::run);
        }
    }

    void buildUsersList(){
//remove the old items
        chooseUser.getItems().clear();

        for (String name:ENGINE.getUsersListNames()) {
            MenuItem newMenuItem = new MenuItem(name);

            //what happend when we select a user
            newMenuItem.setOnAction((e)-> {
                selectedUserProperty.set(newMenuItem.getText());
                isUserSelected.set(true);
                chooseUser.setText(newMenuItem.getText());

                try {
                    userSelectedEventsGenerator.fireEvent(newMenuItem.getText());
                } catch (UserDidntExistException userDidntExistException) {
                    userDidntExistException.printStackTrace();
                }
            });

            chooseUser.getItems().add(newMenuItem);
        }
    }

    @FXML
    void chooseUserOnAction(ActionEvent event) {

    }

    @Override
    public void NewDealEventHappened() {
        newDealEventsGenerator.fireEvent();
    }
}
