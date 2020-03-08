//Model of table for tracking changes into database
package employeeDatabase.models;

public class ChangesTracker {
   private String Event,
           Added;

    public ChangesTracker(String event, String added) {
        Event = event;
        Added = added;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getAdded() {
        return Added;
    }

    public void setAdded(String added) {
        Added = added;
    }
}