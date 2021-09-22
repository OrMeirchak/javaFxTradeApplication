package stock;

import data.TradeData;
import deal.Deal;
import deal.Deals;
import trade.*;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

public class Stock {
    private final String SYMBOL;
    private String companyName;
    private int currentPrice;
    private float totalTurnover;//מחזור עסקאות שבוצעו במנייה
    private Collection<Deal> deals;
    private OrdersStack<Sale> saleOrders;
    private OrdersStack<Buy> buyOrders;

    //constructor
    public Stock(String symbol, String companyName, int currentPrice) {
        this.SYMBOL = symbol.toUpperCase(Locale.ROOT);
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.totalTurnover=0;
        this.deals=new LinkedList<Deal>();
        this.saleOrders=new OrdersStack<Sale>();
        this.buyOrders=new OrdersStack<Buy>();
    }

    //getters
    public String getSYMBOL() {
        return SYMBOL;
    }

    public String getCompanyName() {
        return companyName;
    }

    public OrdersStack<Sale> getSaleOrdersStack() {
        return saleOrders;
    }

    public OrdersStack<Buy> getBuyOrdersStack() {
        return buyOrders;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getSalesRate(){
        return saleOrders.getRate();
    }

    public boolean availableToBuy(){
        return !buyOrders.isEmpty();
    }

    public boolean availableToSell(){
        return !saleOrders.isEmpty();
    }

    public int getBuyRate(){
        return buyOrders.getRate();
    }

    //add
    public void addDeals(Collection<Deal> deals){
        this.deals.addAll(deals);
        updatePrice();
    }

    public void addOrdinance(Ordinance ordinance){
        if(ordinance instanceof Sale){
            saleOrders.push((Sale)ordinance);
        }
        else if(ordinance instanceof Buy){
            buyOrders.push((Buy)ordinance);
        }

    }

    //toString
    public String buyOrdersToString(){
        if(buyOrders.isEmpty()){
            return "There are no buy orders for this stock";
        }
        else {
            return "Buy orders:\n" + buyOrders.toString();
        }
    }


    public Collection<TradeData> saleOrdersToData(){
        return saleOrders.toData();
    }

    public Collection<TradeData> buyOrdersToData(){
        return buyOrders.toData();
    }

    public String saleOrdersToString(){
        if(saleOrders.isEmpty()) {
           return "There are no sale orders for this stock";
        }
         else{
             return "Sale orders:\n" + saleOrders.toString();
        }
    }

    public Collection<TradeData> dealsToData(){
       return TradeData.convertDealsToData(deals);
    }

   public String dealsToString(){
        if(deals.isEmpty()){
          return "No trades were made for this stock";
        }
        else {
            return "Deals:\n" + Deals.toString(deals);
        }
   }



    public String tradeOrdersToString(){
        return SYMBOL
                +"\n\n"+buyOrdersToString()
                +"\n\n"+saleOrdersToString()
                +"\n\n"+dealsToString();
    }

    @Override
    public String toString() {
        return "Stock:" +
                "\nSYMBOL=" + SYMBOL +
                "\nCompany name=" + companyName +
                "\nCurrent price=" + currentPrice +
                "\nHow many deals was be done=" + deals.size() +
                "\nTransaction turn over that done in the stock=" + totalTurnover +
                '\n';
    }

    private void updatePrice() {
        if (!deals.isEmpty()) {
            deals.stream().sorted(Deal::compareTo);
            for (Deal deal:deals){
                this.currentPrice=deal.getPrice();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Float.compare(stock.currentPrice, currentPrice) == 0 && Float.compare(stock.totalTurnover, totalTurnover) == 0 && Objects.equals(SYMBOL, stock.SYMBOL) && Objects.equals(companyName, stock.companyName) && Objects.equals(deals, stock.deals) && Objects.equals(saleOrders, stock.saleOrders) && Objects.equals(buyOrders, stock.buyOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SYMBOL, companyName, currentPrice, totalTurnover, deals, saleOrders, buyOrders);
    }
}
