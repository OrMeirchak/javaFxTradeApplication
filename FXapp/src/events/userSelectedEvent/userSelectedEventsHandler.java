package events.userSelectedEvent;

import exception.UserDidntExistException;

public interface userSelectedEventsHandler {

    void userSelectedEventHappened (String UserName) throws UserDidntExistException;
}
