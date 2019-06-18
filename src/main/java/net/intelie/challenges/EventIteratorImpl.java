package net.intelie.challenges;

import java.util.Iterator;

public class EventIteratorImpl implements EventIterator {

	private final Iterator<Event> iterator;
	private Event currentEvent;
	public final String type;

	public EventIteratorImpl(Iterator<Event> eventIterator, String type) {
		iterator = eventIterator;
		this.type = type;
	}
	
	public Iterator<Event> iterator() {
		return iterator;
	}

	/**
	 * This is the worst optimized part of the code. Since there is a
	 * tree, we need to check the types of events to see if they are 
	 * the type of the iterator.
	 * Could be made better if we stored a hash map of trees, each one with 
	 * a different type. This way we could just iterate directly into the 
	 * tree that has only one type of events.
	 */
	@Override
	public boolean moveNext() {
		while (iterator.hasNext()) {
			Event event = (Event) iterator.next();
			if (event.type().contentEquals(type)) {
				currentEvent = event;
				return true;
			}
		}
		currentEvent = null;
		return false;
	}

	/**
	 * 
	 */
	@Override
	public Event current() throws IllegalStateException {
		if (currentEvent == null) {
			throw new IllegalStateException("moveNext() was never called or has already returned false");
		}
		return currentEvent;
	}

	/**
	 * 
	 */
	@Override
	public void remove() throws IllegalStateException {
		if (currentEvent == null) {
			throw new IllegalStateException("moveNext() was never called or has already returned false");
		}
		iterator.remove();
	}

	@Override
	public void close() throws Exception {
	}

}
