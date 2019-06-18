package net.intelie.challenges;

import java.util.NavigableSet;
import java.util.TreeSet;

public class EventStoreImpl implements EventStore {

	/**
	 * The decision to use a tree set was an approach to the problem of
	 * optimization. Using a binary search tree we are able to keep the events
	 * sorted by time stamps, without a sorting algorithm running on an array, since
	 * the tree set is based on a red-black tree, that is balanced. Because of this
	 * we get a complexity of O(log n) on insertion. With an array, for example, we
	 * would have a complexity of O(n) to insertion which is far worse.
	 * 
	 * For the removeAll method, we will have complexity of O(n), since we have to
	 * search all nodes. It's the same complexity we would have using an array.
	 * 
	 * The query method, using the tree set, will have complexity O(log n), because
	 * we need to check for the start and the end of the interval. It's the same
	 * complexity we could have using an array with binary search for getting both
	 * extremities.
	 * 
	 * TODO: Optimize the remove and query methods, by, instead of using a tree set,
	 * use a hash map, where the key would be the type of Event, and the value the
	 * tree set of that type of event. Like this we can change the removeAll
	 * complexity to O(1), just deleting the tree set of that type
	 * 
	 * About the concurrency problem: tree sets do not have thread-safety included
	 * in it's implementation, since it is an implementation of a Navigable Set
	 * based on a tree map, that also is not thread-safe. Because of this, if more
	 * than one thread access the tree set concurrently and at least one of them
	 * modify it, we need to synchronize it externally.
	 * 
	 * Because of this, the methods of insertion, removal and querying need to be
	 * Synchronized. That way we make sure there is no race condition between the
	 * threads trying to access or modify data. Since the methods have been tested,
	 * it's plausible to think there will be no occurrences of deadlocks. On the
	 * other hand, thread priorities where not taken into account here, so we can't
	 * talk too much about starvation, but with equal priorities it's unlikely to
	 * happen.
	 */

	private final TreeSet<Event> set;

	public EventStoreImpl() {
		set = new TreeSet<>();
	}

	public TreeSet<Event> set() {
		return set;
	}

	@Override
	public synchronized void insert(Event event) {
		set.add(event);
	}

	@Override
	public synchronized void removeAll(String type) {
		set.removeIf(e -> e.type().contentEquals(type));
	}

	@Override
	public synchronized EventIterator query(String type, long startTime, long endTime) {
		NavigableSet<Event> subSet = set.subSet(new Event(type, startTime), true, new Event(type, endTime), false);
		return new EventIteratorImpl(subSet.iterator(), type);
	}
}