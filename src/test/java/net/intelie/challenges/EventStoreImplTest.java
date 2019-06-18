package net.intelie.challenges;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventStoreImplTest {
	private EventStoreImpl eventStore;
	private TreeSet<Event> emptySet;
	private Event event1, event2, event3, event4, event5, event6, event7, event8;

	@BeforeEach
	void setup() {
		eventStore = new EventStoreImpl();
		emptySet = new TreeSet<>();
		event1 = new Event("type1", 1);
		event2 = new Event("type1", 2);
		event3 = new Event("type1", 3);
		event4 = new Event("type1", 4);
		event5 = new Event("type3", 10);
		event6 = new Event("type4", 7);
		event7 = new Event("type2", 20);
		event8 = new Event("type1", 5);
	}

	@Test
	void testInsert() {
		eventStore.insert(event1);
		eventStore.insert(event2);
		eventStore.insert(event3);
		eventStore.insert(event4);

		TreeSet<Event> expectedSet = new TreeSet<>();
		expectedSet.add(event1);
		expectedSet.add(event2);
		expectedSet.add(event3);
		expectedSet.add(event4);

		Assert.assertEquals(expectedSet, eventStore.set());
		assertEquals(4, eventStore.set().size());
	}

	@Test
	void testRemoveAllWhenTypesAreAllTheSame() {
		String typeToRemove = "type1";
		eventStore.set().clear();
		eventStore.insert(event1);
		eventStore.insert(event2);
		eventStore.insert(event3);
		eventStore.insert(event4);
		eventStore.removeAll(typeToRemove);

		assertEquals(emptySet, eventStore.set());
		assertEquals(0, eventStore.set().size());
	}

	@Test
	void testRemoveAllWhenTypesArentAllTheSame() {
		String typeToRemove = "type1";
		eventStore.insert(event1);
		eventStore.insert(event2);
		eventStore.insert(event3);
		eventStore.insert(event4);
		eventStore.insert(event5);
		eventStore.insert(event6);
		eventStore.insert(event7);
		eventStore.insert(event8);
		eventStore.removeAll(typeToRemove);
		EventIterator iterator = eventStore.query(typeToRemove, 0, 2000000);
		assertFalse(iterator.moveNext());
		assertEquals(3, eventStore.set().size());
	}

	@Test
	void testQuery() {
		String iteratorType = "type1";
		long lowerBound = 4, higherBound = 20;
		ArrayList<Event> eventList = new ArrayList<Event>();
		eventList.add(event4);
		eventList.add(event8);

		eventStore.insert(event1);
		eventStore.insert(event2);
		eventStore.insert(event3);
		eventStore.insert(event4);
		eventStore.insert(event5);
		eventStore.insert(event6);
		eventStore.insert(event7);
		eventStore.insert(event8);

		EventIterator iterator = eventStore.query(iteratorType, lowerBound, higherBound);
		int count = 0;
		while (iterator.moveNext()) {
			assertTrue(eventList.contains(iterator.current()));
			count++;
		}
		assertEquals(count, eventList.size());
	}

	@Test
	void testQueryWhenNoEvents() {
		String iteratorType = "type1";
		long lowerBound = 4, higherBound = 20;
		eventStore.set().clear();

		EventIterator iterator = eventStore.query(iteratorType, lowerBound, higherBound);
		int count = 0;
		while (iterator.moveNext()) {
			count++;
		}
		assertEquals(0, count);
	}

}
