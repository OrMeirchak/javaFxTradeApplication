package user;

import stock.Stock;

public class Holding {
    final private Stock STOCK;
    private int amount;

    public Holding(Stock STOCK, int amount) {
        this.STOCK = STOCK;
        this.amount = amount;
    }

    public Stock getSTOCK() {
        return STOCK;
    }

    public void addAmount(int amount){
       this.amount+=amount;
    }

    public void subAmount(int amount){
        this.amount-=amount;
    }

    public String getStockSymbol(){
return STOCK.getSYMBOL();
    }
    public int getTotalValue(){
        return amount*(STOCK.getCurrentPrice());
    }
    public int getAmount(){
return amount;
    }
    public int getRate(){
return STOCK.getCurrentPrice();
    }
}
