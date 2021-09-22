
package events.stockSelectedEvent;

import java.util.EventObject;

public interface StockSelectedEventsHandler {
    void stockSelectedEventHappened (String symbol);
}
