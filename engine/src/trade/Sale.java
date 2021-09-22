package trade;
import interfaces.Stringable;
import stock.*;
import user.User;

public class Sale extends Ordinance implements Comparable<Sale>, Stringable {

    public Sale(Stock stock, int amount, int limit, User user,OrdinanceType type) {
        super(stock,amount,limit,user,type);
    }

    @Override
    public int compareTo(Sale o) {
        if(o.getLimit() - this.getLimit()!=0){
            return this.getLimit() - o.getLimit();
        }
        else{
            return this.getDate().compareTo(o.getDate());
        }
    }
    }

