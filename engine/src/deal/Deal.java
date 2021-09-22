package deal;

import trade.OrdinanceType;
import user.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Deal implements Comparable<Deal>{
    final private Date DATE;
    final private int AMOUNT;
    final private int PRICE;
    final private float VALUE;//סה"כ שווי עסקה
    final private User SELLER;
    final private User BUYER;
    final private OrdinanceType TYPE;

    public Deal( int AMOUNT, int PRICE,User seller,User buyer) {
        this.DATE = new Date();
        this.AMOUNT = AMOUNT;
        this.PRICE = PRICE;
        this.VALUE = AMOUNT*PRICE;
        this.SELLER= seller;
        this.BUYER=buyer;
        this.TYPE=OrdinanceType.LMT;
    }

    public OrdinanceType getType() {
        return TYPE;
    }

    public float getValue() {
        return VALUE;
    }

    public int getPrice() {
        return PRICE;
    }

    public int getAmount() {
        return AMOUNT;
    }

    public Date getDate() {
        return DATE;
    }

    public User getSeller() {
        return SELLER;
    }

    public User getBuyer() {
        return BUYER;
    }

    @Override
    public String toString() {
        return "Date=" + new SimpleDateFormat("HH:mm:ss:SSS").format(DATE) +
                "\nAmount=" + AMOUNT +
                "\nSales price=" + PRICE +
                "\nValue of transaction=" + VALUE +
                "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deal deal = (Deal) o;
        return AMOUNT == deal.AMOUNT && Float.compare(deal.PRICE, PRICE) == 0 && Float.compare(deal.VALUE, VALUE) == 0 && Objects.equals(DATE, deal.DATE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DATE, AMOUNT, PRICE, VALUE);
    }

    @Override
    public int compareTo(Deal o) {
        return this.DATE.compareTo(o.DATE);
    }
}
