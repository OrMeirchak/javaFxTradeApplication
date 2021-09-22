package trade;

import deal.Deal;
import deal.Deals;
import stock.Stock;
import user.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

public class Trade {

    private Ordinance ordinance;
    private Collection<Deal> deals;
    final private Stock STOCK;
    final private String ordinanceType;

    public Trade(Ordinance ordinance) {
        this.ordinance = ordinance;
        this.deals = new LinkedList<Deal>();
        this.STOCK = ordinance.getStock();
        this.ordinanceType=ordinance.getClass().toString().toLowerCase(Locale.ROOT);

        makeTrade();
    }

    private void makeTrade() {
        if (ordinance instanceof Sale) {
            if (ordinance.getType() == OrdinanceType.LMT) {
                LMTSaleOrdinance();
            } else if (ordinance.getType() == OrdinanceType.MKT) {
                MKTSaleOrdinance();
            }
        }
        else if (ordinance instanceof Buy) {
            if (ordinance.getType() == OrdinanceType.LMT) {
                LMTBuyOrdinance();
            } else if (ordinance.getType() == OrdinanceType.MKT) {
                MKTBuyOrdinance();
            }
        }

                handlingTheRestOfTheOrdinance();

        if(!deals.isEmpty()){
        STOCK.addDeals(this.deals);

        for (Deal deal:deals){
            deal.getSeller().subAmount(STOCK,deal.getAmount());
            deal.getBuyer().addHolding(STOCK,deal.getAmount());
        }
    }
    }


    public void updatePrice(int newPrice){
        STOCK.setPrice(newPrice);
    }

    private void LMTSaleOrdinance() {

        if (STOCK.getBuyOrdersStack().isEmpty())
        {
            return;
        }

        Ordinance buyOrdinance=STOCK.getBuyOrdersStack().top();
        Ordinance saleOrdinance=ordinance;

       final int BUY_ORDINANCE_AMOUNT = buyOrdinance.getAmount();
        final int BUY_ORDINANCE_LIMIT = buyOrdinance.getLimit();
        final User BUYER=buyOrdinance.getUser();

        final int SALE_ORDINANCE_AMOUNT = saleOrdinance.getAmount();
       final int SALE_ORDINANCE_LIMIT = saleOrdinance.getLimit();
        final User SELLER=saleOrdinance.getUser();

        if (SALE_ORDINANCE_LIMIT > BUY_ORDINANCE_LIMIT) {
            return;
        }
        else {
            if (BUY_ORDINANCE_AMOUNT > SALE_ORDINANCE_AMOUNT) {

                deals.add(new Deal(SALE_ORDINANCE_AMOUNT, BUY_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(BUY_ORDINANCE_LIMIT);

                buyOrdinance.subtractAmount(SALE_ORDINANCE_AMOUNT);

                saleOrdinance.setAmount(0);

            } else if (BUY_ORDINANCE_AMOUNT < SALE_ORDINANCE_AMOUNT) {

                deals.add(new Deal(BUY_ORDINANCE_AMOUNT, BUY_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(BUY_ORDINANCE_LIMIT);

                saleOrdinance.subtractAmount(BUY_ORDINANCE_AMOUNT);

                STOCK.getBuyOrdersStack().pop();

                LMTSaleOrdinance();
            } else {
                deals.add(new Deal(BUY_ORDINANCE_AMOUNT, BUY_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(BUY_ORDINANCE_LIMIT);

                STOCK.getBuyOrdersStack().pop();

                saleOrdinance.setAmount(0);
            }
        }
    }

    private void MKTSaleOrdinance() {

        if (STOCK.getBuyOrdersStack().isEmpty())
        {
            return;
        }

        Ordinance buyOrdinance=STOCK.getBuyOrdersStack().top();
        Ordinance saleOrdinance=ordinance;

        final int BUY_ORDINANCE_AMOUNT = buyOrdinance.getAmount();
        final int BUY_ORDINANCE_LIMIT = buyOrdinance.getLimit();
        final User BUYER=buyOrdinance.getUser();

        final int SALE_ORDINANCE_AMOUNT = saleOrdinance.getAmount();
        final User SELLER=saleOrdinance.getUser();


            if (BUY_ORDINANCE_AMOUNT > SALE_ORDINANCE_AMOUNT) {

                deals.add(new Deal(SALE_ORDINANCE_AMOUNT, BUY_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(BUY_ORDINANCE_LIMIT);

                buyOrdinance.subtractAmount(SALE_ORDINANCE_AMOUNT);

                saleOrdinance.setAmount(0);

            } else if (BUY_ORDINANCE_AMOUNT < SALE_ORDINANCE_AMOUNT) {

                deals.add(new Deal(BUY_ORDINANCE_AMOUNT, BUY_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(BUY_ORDINANCE_LIMIT);

                saleOrdinance.subtractAmount(BUY_ORDINANCE_AMOUNT);

                STOCK.getBuyOrdersStack().pop();

                MKTSaleOrdinance();
            } else {
                deals.add(new Deal(BUY_ORDINANCE_AMOUNT, BUY_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(BUY_ORDINANCE_LIMIT);

                STOCK.getBuyOrdersStack().pop();

                saleOrdinance.setAmount(0);
            }
        }

        private void LMTBuyOrdinance() {

        if (STOCK.getSaleOrdersStack().isEmpty()) {
            return;
        }
        Ordinance saleOrdinance=STOCK.getSaleOrdersStack().top();
        Ordinance buyOrdinance=ordinance;

        final int BUY_ORDINANCE_AMOUNT = buyOrdinance.getAmount();
        final int BUY_ORDINANCE_LIMIT = buyOrdinance.getLimit();
        final User BUYER=buyOrdinance.getUser();

        final int SALE_ORDINANCE_AMOUNT = saleOrdinance.getAmount();
        final int SALE_ORDINANCE_LIMIT = saleOrdinance.getLimit();
        final User SELLER=saleOrdinance.getUser();

        if (SALE_ORDINANCE_LIMIT > BUY_ORDINANCE_LIMIT) {
            return;
        }
        else {
            if (BUY_ORDINANCE_AMOUNT > SALE_ORDINANCE_AMOUNT) {
                deals.add(new Deal(SALE_ORDINANCE_AMOUNT, SALE_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(SALE_ORDINANCE_LIMIT);
                buyOrdinance.subtractAmount(SALE_ORDINANCE_AMOUNT);
                STOCK.getSaleOrdersStack().pop();
                LMTBuyOrdinance();
            }
            else if (BUY_ORDINANCE_AMOUNT < SALE_ORDINANCE_AMOUNT) {
                    deals.add(new Deal(BUY_ORDINANCE_AMOUNT, SALE_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(SALE_ORDINANCE_LIMIT);
                    saleOrdinance.subtractAmount(BUY_ORDINANCE_AMOUNT);
                    buyOrdinance.setAmount(0);
                }
            else{
                    deals.add(new Deal(SALE_ORDINANCE_AMOUNT, SALE_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(SALE_ORDINANCE_LIMIT);
                    buyOrdinance.setAmount(0);
                STOCK.getSaleOrdersStack().pop();
                } } }

    private void MKTBuyOrdinance() {

        if (STOCK.getSaleOrdersStack().isEmpty()) {
            return;
        }
        Ordinance saleOrdinance=STOCK.getSaleOrdersStack().top();
        Ordinance buyOrdinance=ordinance;

        final int BUY_ORDINANCE_AMOUNT = buyOrdinance.getAmount();
        final User BUYER=buyOrdinance.getUser();

        final int SALE_ORDINANCE_AMOUNT = saleOrdinance.getAmount();
        final int SALE_ORDINANCE_LIMIT = saleOrdinance.getLimit();
        final User SELLER=saleOrdinance.getUser();



            if (BUY_ORDINANCE_AMOUNT > SALE_ORDINANCE_AMOUNT) {
                deals.add(new Deal(SALE_ORDINANCE_AMOUNT, SALE_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(SALE_ORDINANCE_LIMIT);
                buyOrdinance.subtractAmount(SALE_ORDINANCE_AMOUNT);
                STOCK.getSaleOrdersStack().pop();
                MKTBuyOrdinance();
            }
            else if (BUY_ORDINANCE_AMOUNT < SALE_ORDINANCE_AMOUNT) {
                deals.add(new Deal(BUY_ORDINANCE_AMOUNT, SALE_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(SALE_ORDINANCE_LIMIT);
                saleOrdinance.subtractAmount(BUY_ORDINANCE_AMOUNT);
                buyOrdinance.setAmount(0);
            }
            else{
                deals.add(new Deal(SALE_ORDINANCE_AMOUNT, SALE_ORDINANCE_LIMIT,SELLER,BUYER));
                updatePrice(SALE_ORDINANCE_LIMIT);
                buyOrdinance.setAmount(0);
                STOCK.getSaleOrdersStack().pop();
            } }

    private void handlingTheRestOfTheOrdinance() {
        if(ordinance.getAmount()!=0){
            if(ordinance.getType()==OrdinanceType.MKT){
                ordinance.setLimit(STOCK.getCurrentPrice());
            }

            STOCK.addOrdinance(ordinance);
        }
    }

public String toString(){
String res="";
        if (!deals.isEmpty()){
            res+="The following deal";
            if(deals.size()!=1){res+='s';};
            res+=" have been done for you:\n";
            res+= Deals.toString(deals)+'\n';
            if(ordinance.getAmount()!=0){
                res+="\nAnd this is the res of your ordinance that has been added to the orders list:\n";
                res+=ordinance.toString()+'\n';
            }
        }
        else{
            res+="No suitable ordinance was found.\n" +
                    "your ordinance has been added to the orders list\n";
            res+=ordinance.toString()+'\n';
        }
        return res;
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(ordinance, trade.ordinance) && Objects.equals(deals, trade.deals) && Objects.equals(STOCK, trade.STOCK) && Objects.equals(ordinanceType, trade.ordinanceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordinance, deals, STOCK, ordinanceType);
    }
}
