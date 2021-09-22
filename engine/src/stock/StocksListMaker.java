package stock;
import exception.CompanyNameAlreadyExistException;
import exception.SymbolAlreadyExistException;
import generated.*;
import java.util.List;

public class StocksListMaker {

   private RizpaStockExchangeDescriptor xmlInput;
   private StocksList  newStocksList;

    public StocksListMaker(RizpaStockExchangeDescriptor xmlStocksList) throws CompanyNameAlreadyExistException, SymbolAlreadyExistException {
        this.newStocksList=new StocksList();
        this.xmlInput=xmlStocksList;
        makeStocksList();
    }

    private void makeStocksList() throws CompanyNameAlreadyExistException, SymbolAlreadyExistException {
        List<RseStock> rseStockList=  xmlInput.getRseStocks().getRseStock();

        for (RseStock rseStock:rseStockList){
            addStock(rseStock);
        }
    }

    private void addStock(RseStock rseStock) throws CompanyNameAlreadyExistException, SymbolAlreadyExistException {
        newStocksList.addStock(rseStock.getRseSymbol(),rseStock.getRseCompanyName(),rseStock.getRsePrice());
    }

    public StocksList getNewStocksList(){
        return newStocksList;
    }

    }
