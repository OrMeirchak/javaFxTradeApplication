package events.userSelectedEvent;


import exception.UserDidntExistException;

import java.util.ArrayList;
import java.util.List;

public class UserSelectedEventsGenerator {

    private List<userSelectedEventsHandler> handlers;

    public UserSelectedEventsGenerator() {
        handlers = new ArrayList<>();
    }

    public void addHandler (userSelectedEventsHandler handler) {
        if (handler != null && !handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void fireEvent (String userName) throws UserDidntExistException {
        List<userSelectedEventsHandler> handlersToInvoke = new ArrayList<>(handlers);
        for (userSelectedEventsHandler handler : handlersToInvoke) {
            handler.userSelectedEventHappened(userName);
        }
    }
}
