package user;

import exception.CompanyNameAlreadyExistException;
import exception.SymbolAlreadyExistException;
import exception.SymbolDidntExistException;
import exception.UserNameAlreadyExistException;
import generated.*;
import stock.Stock;
import stock.StocksList;

import java.util.List;

public class UserListMaker {

    private RizpaStockExchangeDescriptor xmlInput;
    private UsersList newUsersList;
    private final StocksList STOCKS_LIST;

    public UserListMaker(RizpaStockExchangeDescriptor xmlInput,StocksList stockList) throws UserNameAlreadyExistException, SymbolDidntExistException {
        this.newUsersList=new UsersList();
        this.xmlInput = xmlInput;
        this.STOCKS_LIST=stockList;
        makeUsersList();
    }

    private void makeUsersList() throws UserNameAlreadyExistException, SymbolDidntExistException {
        List<RseUser> rseUsersList=  xmlInput.getRseUsers().getRseUser();

        for (RseUser rseUser:rseUsersList){
            addUser(rseUser);
        }
    }

    private void addUser(RseUser rseUser) throws UserNameAlreadyExistException, SymbolDidntExistException {
        User newUser=newUsersList.addUser(rseUser.getName());

        List<RseItem> rseHoldingItems=  rseUser.getRseHoldings().getRseItem();
        for (RseItem item:rseHoldingItems) {
            addHolding(newUser, item);
        }
    }

    private void addHolding(User user,RseItem item) throws SymbolDidntExistException {

        String symbol=item.getSymbol();
        int amount=item.getQuantity();

        Stock stock= STOCKS_LIST.getStockBySymbol(symbol);

        if (stock==null){
            throw new SymbolDidntExistException(symbol);
        }
        else{
            user.addHolding(stock,amount);
        }
    }


    public UsersList getNewUsersList(){
        return newUsersList;
    }

}
