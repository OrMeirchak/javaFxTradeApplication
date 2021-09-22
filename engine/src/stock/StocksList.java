package stock;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import data.TradeData;
import exception.*;
import trade.Ordinance;
import user.User;

public class StocksList {
    private Collection<Stock> stocks;

    //constructor
    public StocksList(){
        stocks = new HashSet<Stock>();
    }

    //add
    protected void addStock(String SYMBOL, String companyName, int currentPrice) throws SymbolAlreadyExistException, CompanyNameAlreadyExistException {
        if(getStockBySymbol(SYMBOL)!=null){
            throw new SymbolAlreadyExistException(SYMBOL);
        }
        else if(getStockByCompanyName(companyName)!=null){
            throw new CompanyNameAlreadyExistException(companyName);
        }
        else {
            stocks.add(new Stock(SYMBOL, companyName, currentPrice));
        }
    }

    //get
    public String getSpecificStockInformation(String SYMBOL) throws NoStocksException, SymbolDidntExistException {
        if(stocks.isEmpty()){
            throw new NoStocksException();
        }
        Stock stock=getStockBySymbol(SYMBOL);
        if(stock==null){
            throw new SymbolDidntExistException(SYMBOL);
        }
        return stock.toString()+"\n\n"+stock.dealsToString();
    }

    public String getAllStocksInformation()throws NoStocksException {
        if(stocks.isEmpty())
        {
            throw new NoStocksException();
        }
        String res = "";

        for (Stock stock : stocks) {
            res += '\n'+stock.toString();
        }
        return res;
    }

    public String getAllTradingInformation(){
        String res="";

        if(stocks.isEmpty()){
            res="No stocks in the system";
        }
        else{
            for (Stock stock:stocks){
                res+=stock.tradeOrdersToString()+"\n\n\n";
            }
        }
return res;
    }

    //if didnt found return null
    public Stock getStockBySymbol(String SYMBOL) {
        if(stocks.isEmpty()) {
            return null;
        }
        else {
            String SymbolToUpperCase=SYMBOL.toUpperCase(Locale.ROOT);
            for (Stock stock:stocks) {
                if (stock.getSYMBOL().equals(SymbolToUpperCase)==true) {
                    return stock;
                }}}
        return null;
    }

    //if didnt find return null
    private Stock getStockByCompanyName(String companyName) {
        if(stocks.isEmpty()) {
            return null;
        }
        else {
            for (Stock stock : stocks) {
                if (stock.getCompanyName().equals(companyName)) {
                    return stock;
                } } }
        return null;
    }

    public Collection<String> getStocksListNames() {
        HashSet<String> res=new HashSet<String>();

        for (Stock stock:stocks){
            res.add(stock.getSYMBOL());
        }
        return res;
    }

    public int getSalesRate(String symbol) throws SymbolDidntExistException {
        Stock stock=getStockBySymbol(symbol);
        if (stock==null){
            throw new SymbolDidntExistException(symbol);
        }
        else{
            return stock.getSalesRate();
        }
    }

    public int getBuyRate(String symbol) throws SymbolDidntExistException {
        Stock stock=getStockBySymbol(symbol);
        if (stock==null){
            throw new SymbolDidntExistException(symbol);
        }
        else{
            return stock.getBuyRate();
        }
    }

    public Collection<TradeData> getSaleOrdinanceData(String symbol) throws SymbolDidntExistException {
        Stock stock=getStockBySymbol(symbol);
        if (stock==null){
            throw new SymbolDidntExistException(symbol);
        }
        else{
            return stock.saleOrdersToData();
        }
    }

    public Collection<TradeData> getBuyOrdinanceData(String symbol) throws SymbolDidntExistException {
        Stock stock = getStockBySymbol(symbol);
        if (stock == null) {
            throw new SymbolDidntExistException(symbol);
        } else {
            return stock.buyOrdersToData();
        }
    }

        public Collection<TradeData> getDealsData(String symbol) throws SymbolDidntExistException {
            Stock stock=getStockBySymbol(symbol);
            if (stock==null){
                throw new SymbolDidntExistException(symbol);
            }
            else{
                return stock.dealsToData();
            }
    }

    public boolean availableToBuy(String symbol) throws SymbolDidntExistException {
        Stock stock=getStockBySymbol(symbol);
        if (stock==null){
            throw new SymbolDidntExistException(symbol);
        }
        else{
            return stock.availableToBuy();
        }
    }

    public boolean availableToSell(String symbol) throws SymbolDidntExistException {
        Stock stock=getStockBySymbol(symbol);
        if (stock==null){
            throw new SymbolDidntExistException(symbol);
        }
        else{
            return stock.availableToSell();
        }
    }
}
