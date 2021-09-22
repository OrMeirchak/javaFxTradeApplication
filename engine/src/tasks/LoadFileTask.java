package tasks;

import exception.*;
import fileLoader.XmlFileLoader;
import generated.RizpaStockExchangeDescriptor;
import stock.StocksList;
import stock.StocksListMaker;
import user.UserListMaker;
import user.UsersList;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.FileNotFoundException;
import java.util.function.Consumer;

public class LoadFileTask extends Task<Boolean> {

    private final int SLEEP_TIME;
    private final String PATH;

    StocksListMaker stocksListMaker;
    UserListMaker usersListMaker;

    Consumer<StocksList> updateStocksList;
    Consumer<UsersList> updateUsersList;
    Consumer<String> exceptionHandling;

   public LoadFileTask(String path, int sleepTime,Consumer<StocksList> updateStocksList,Consumer<UsersList> updateUsersList,Consumer<String> exceptionHandling){
        SLEEP_TIME = sleepTime;
        PATH = path;
        this.updateStocksList=updateStocksList;
        this.updateUsersList=updateUsersList;
        this.exceptionHandling=exceptionHandling;
   }


    @Override
    protected Boolean call() throws InterruptedException {

       //Load file
            updateMessage("Fetching file...");
            Thread.sleep(SLEEP_TIME);
            try {
                XmlFileLoader<RizpaStockExchangeDescriptor> xmlFile = new XmlFileLoader<RizpaStockExchangeDescriptor>(PATH);

                //Load stocks list
                updateMessage("Load stocks List...");
                Thread.sleep(SLEEP_TIME);
                stocksListMaker = new StocksListMaker(xmlFile.getObject());

                //Load user List
                updateMessage("Load users List...");
                Thread.sleep(SLEEP_TIME);
                usersListMaker = new UserListMaker(xmlFile.getObject(), getStocksList());

                updateStocksList.accept(getStocksList());
                updateUsersList.accept(getUsersList());

            }
            catch(Exception e){
                Platform.runLater(
                        () -> exceptionHandling.accept(e.getMessage()));
            return false;}
return true;
}

    public StocksList getStocksList() {
        return stocksListMaker.getNewStocksList();
    }

    public UsersList getUsersList() {
        return usersListMaker.getNewUsersList();
    }
}
