package events.newDealEvent;

import java.util.ArrayList;
import java.util.List;

public class NewDealEventsGenerator {

    private List<NewDealEventsHandler> handlers;

    public NewDealEventsGenerator() {
        handlers = new ArrayList<>();
    }

    public void addHandler (NewDealEventsHandler handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void fireEvent () {
        List<NewDealEventsHandler> handlersToInvoke = new ArrayList<>(handlers);
        for (NewDealEventsHandler handler : handlersToInvoke) {
            handler.NewDealEventHappened();
        }
    }
}
