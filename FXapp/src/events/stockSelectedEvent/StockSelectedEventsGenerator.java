package events.stockSelectedEvent;

import java.util.ArrayList;
import java.util.List;

public class StockSelectedEventsGenerator {

    private List<StockSelectedEventsHandler> handlers;

    public StockSelectedEventsGenerator() {
        handlers = new ArrayList<>();
    }

    public void addHandler (StockSelectedEventsHandler handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void fireEvent (String symbol) {
        List<StockSelectedEventsHandler> handlersToInvoke = new ArrayList<>(handlers);
        for (StockSelectedEventsHandler handler : handlersToInvoke) {
            handler.stockSelectedEventHappened(symbol);
        }
    }
}
