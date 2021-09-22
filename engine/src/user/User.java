package user;

import stock.Stock;

import java.util.Collection;
import java.util.HashSet;

public class User {
    final String NAME;
    Collection<Holding> holdings;

    public User(String NAME) {
        this.NAME = NAME;
        this.holdings=new HashSet<Holding>();
    }

    int getTotalValue(){
        int res=0;
        for(Holding holding:holdings){
            res+=(holding.getTotalValue());
        }
        return res;
    }




    Collection<Holding> getHoldingData(){
        return holdings;
    }

    public void subAmount(Stock stock, int amount){
        for (Holding holding:holdings) {
            if (holding.getSTOCK() == stock) {
                holding.subAmount(amount);
                return;
            }
        }
    }

    public int getHoldingAmount(Stock stock) {
        for (Holding holding : holdings) {
            if (holding.getSTOCK() == stock) {
                return holding.getAmount();
            }
        }
        return -1;
    }

    public void addHolding(Stock stock, int amount){
        for (Holding holding:holdings){
            if(holding.getSTOCK()==stock){
                holding.addAmount(amount);
                return;
            }
        }
        holdings.add(new Holding(stock,amount));
    }

    public String getName() {
        return NAME;
    }

    //the function return hashset with the names of stocks symbol of the user
public   HashSet<String> getHoldingsSymbols(){
        HashSet<String> res=new HashSet<String>();

        for (Holding holding: holdings){
            res.add(holding.getStockSymbol());
        }
        return res;
}
}
