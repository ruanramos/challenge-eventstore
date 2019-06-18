package net.intelie.challenges;

import java.util.Iterator;

public class EventIteratorImpl implements EventIterator {

	/**
	 * About concurrency: We can have threads creating multiple iterators for the
	 * same tree set. That could also be a problem, since they would access, being
	 * able to read, write or modify the data structure in memory simultaneously.
	 * Again, we do not want it to happen, so we need to synchronize it externally
	 * since tree sets aren't thread-safe. With that in mind, we know our moveNext,
	 * current and remove methods will work, without conflicts and returning
	 * expected results, without threads interfering with each other (one example
	 * would be two threads without locks calling moveNext at the same time and the
	 * iterator moving only once, since we have the read-write-modify process)
	 */

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
	 * This is the worst optimized part of the code. Since there is a tree, we need
	 * to check the types of events to see if they are the type of the iterator.
	 * Could be made better if we stored a hash map of trees, each one with a
	 * different type. This way we could just iterate directly into the tree that
	 * has only one type of events.
	 * 
	 */
	@Override
	public synchronized boolean moveNext() {
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

	@Override
	public synchronized Event current() throws IllegalStateException {
		if (currentEvent == null) {
			throw new IllegalStateException("moveNext() was never called or has already returned false");
		}
		return currentEvent;
	}

	@Override
	public synchronized void remove() throws IllegalStateException {
		if (currentEvent == null) {
			throw new IllegalStateException("moveNext() was never called or has already returned false");
		}
		iterator.remove();
	}

	/**
	 * Since we are not holding releasable resources, the is no need to implement
	 * the method close nor necessity to test it.
	 */
	@Override
	public void close() throws Exception {
	}

}
