package net.intelie.challenges;

import java.util.Comparator;

/**
 * This is just an event stub, feel free to expand it if needed.
 */
public class Event implements Comparable<Event>, Comparator<Event>{
    private final String type;
    private final long timestamp;
    //private final int id;

    public Event(String type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    public String type() {
        return type;
    }

    public long timestamp() {
        return timestamp;
    }

    /**
     * To make events comparable
     */
	@Override
	public int compareTo(Event other) {
		if (this.timestamp() < other.timestamp()) {
            return -1;
        }
        if (this.timestamp() > other.timestamp()) {
            return 1;
        }
        return this.type().compareTo(other.type());
	}

	@Override
	public int compare(Event o1, Event o2) {
		if (o1.timestamp() < o2.timestamp()) {
            return -1;
        }
        if (o1.timestamp() > o2.timestamp()){
            return 1;
        }
        return o1.type().compareTo(o2.type());
	}
}
