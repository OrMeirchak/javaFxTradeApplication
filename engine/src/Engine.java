package Engine;

import data.TradeData;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

import exception.*;
import main.MainController;
import stock.*;
import tasks.LoadFileTask;
import trade.*;
import user.Holding;
import user.User;
import user.UsersList;
import javafx.concurrent.Task;

//Singelton
public class Engine {
  private static Engine singleInstance=null;
  private StocksList stocksList;
  private UsersList usersList;
    private Task<Boolean> currentRunningTask;
    private MainController mainController;

private Engine(){
stocksList=new StocksList();
}

  public static Engine getEngine(){
  if(singleInstance==null){
    singleInstance=new Engine();
  }
  return singleInstance;
}

public void setMainController(MainController mainController){
    this.mainController=mainController;
}

//stocks
    public String getSpecificStockInformation(String SYMBOL) throws NoStocksException, SymbolDidntExistException {
    return stocksList.getSpecificStockInformation(SYMBOL);
    }

    public String getAllStocksInformation() throws Exception {
return stocksList.getAllStocksInformation();
    }



    //ordinance
    public String saleOrdinance(String stockSymbol, int amount, int limit, String userName,String type) throws SymbolDidntExistException, OnlyPositiveNumberException, UserDidntExistException {
Stock stock= stocksList.getStockBySymbol(stockSymbol);
        OrdinanceType ordinanceType=null;

        User user= usersList.getUser(userName);
        if(user==null){
        throw new UserDidntExistException(userName);
        }

        if(type.equalsIgnoreCase("MKT")==true){
            ordinanceType=OrdinanceType.MKT;
        }
        else if(type.equalsIgnoreCase("LMT")==true){
            ordinanceType=OrdinanceType.LMT;
        }


if(stock==null){
    throw new SymbolDidntExistException(stockSymbol);
}
else if(amount<=0){
    throw new OnlyPositiveNumberException(amount);
}
else{
    return (new Trade(new Sale(stock,amount,limit,user,ordinanceType))).toString();
}
    }

    public String buyOrdinance(String stockSymbol, int amount, int limit,String userName,String type) throws SymbolDidntExistException, OnlyPositiveNumberException, UserDidntExistException {
        Stock stock= stocksList.getStockBySymbol(stockSymbol);
        OrdinanceType ordinanceType=null;
        User user= usersList.getUser(userName);
        if(user==null){
            throw new UserDidntExistException(userName);
        }

        if(type.equalsIgnoreCase("MKT")==true){
            ordinanceType=OrdinanceType.MKT;
        }
        else if(type.equalsIgnoreCase("LMT")==true){
            ordinanceType=OrdinanceType.LMT;
        }
        if(stock==null){
            throw new SymbolDidntExistException(stockSymbol);
        }
        else if(amount<=0){
            throw new OnlyPositiveNumberException(amount);
        }
        else{
           return( new Trade(new Buy(stock,amount,limit,user,ordinanceType))).toString();
        }
    }

    public int getHoldingsAmount(String userName,String stockSymbol){
    return usersList.getUser(userName).getHoldingAmount(stocksList.getStockBySymbol(stockSymbol));
    }

    public String getAllTradingInformation(){
return stocksList.getAllTradingInformation();
    }

    public void loadFile(String path,int sleepTime,Runnable onFinish,Consumer<String> exceptionHandling) throws CompanyNameAlreadyExistException, SymbolAlreadyExistException, FileNotFoundException, ExtensionException, NotFullPathException, SymbolDidntExistException, UserNameAlreadyExistException, InterruptedException {
    Consumer<StocksList> updateStocksList = newStocksList -> {
            this.stocksList = newStocksList;
        };

        Consumer<UsersList> updateUsersList = newUsersList -> {
            this.usersList = newUsersList;
        };

        currentRunningTask=new LoadFileTask(path,sleepTime,updateStocksList,updateUsersList,exceptionHandling);

        mainController.bindTaskToUIComponents(currentRunningTask,onFinish);

        new Thread(currentRunningTask).start();
    }

    public Collection<String> getUsersListNames(){
        return usersList.getNames();

    }

    public Collection<Holding> getHoldingData(String name) throws UserDidntExistException {
        return usersList.getHoldingData(name);
    }

    public int getUserTotalValue(String name) throws UserDidntExistException {
    return usersList.getTotalValue(name);
    }

    public Collection<String> getStocksListNames() {
    return stocksList.getStocksListNames();
    }

    public Collection<TradeData> getSaleOrdinanceData(String symbol) throws SymbolDidntExistException {
        return stocksList.getSaleOrdinanceData(symbol);
    }

    public Collection<TradeData> getBuyOrdinanceData(String symbol) throws SymbolDidntExistException {

        return stocksList.getBuyOrdinanceData(symbol);
    }

    public Collection<TradeData> getDealsData(String symbol) throws SymbolDidntExistException {

        return stocksList.getDealsData(symbol);
    }

    public int getSalesRate(String symbol) throws SymbolDidntExistException {
    return stocksList.getSalesRate(symbol);
    }

    public int getBuyRate(String symbol) throws SymbolDidntExistException {
        return stocksList.getBuyRate(symbol);
    }

    public boolean availableToBuy(String symbol) throws SymbolDidntExistException {
    return stocksList.availableToBuy(symbol);
    }

    public boolean availableToSell(String symbol) throws SymbolDidntExistException {
        return stocksList.availableToSell(symbol);
    }

    public HashSet<String> getUserHoldingsSymbols (String name) throws UserDidntExistException {
    return usersList.getUserHoldingsSymbols(name);
    }

}
