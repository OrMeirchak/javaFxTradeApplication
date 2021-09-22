package events.loadFileEvent;

import java.util.ArrayList;
import java.util.List;

public class LoadFileEventsGenerator {

    private List<LoadFileEventsHandler> handlers;

    public LoadFileEventsGenerator() {
        handlers = new ArrayList<>();
    }

    public void addHandler (LoadFileEventsHandler handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void fireEvent () {
        List<LoadFileEventsHandler> handlersToInvoke = new ArrayList<>(handlers);
        for (LoadFileEventsHandler handler : handlersToInvoke) {
            handler.loadFileEventHappened();
        }
    }
}
