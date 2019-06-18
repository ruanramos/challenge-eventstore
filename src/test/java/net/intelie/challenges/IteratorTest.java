package net.intelie.challenges;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IteratorTest {

	TreeSet<Event> emptySet, sameTypeSet, difTypeSet;
	EventIteratorImpl iteratorObject;

	@BeforeEach
	public void setup() {
		emptySet = new TreeSet<>();
		sameTypeSet = new TreeSet<>();
		sameTypeSet.add(new Event("type1", 1));
		sameTypeSet.add(new Event("type1", 2));
		sameTypeSet.add(new Event("type1", 7));
		sameTypeSet.add(new Event("type1", 8));
		sameTypeSet.add(new Event("type1", 3));
		sameTypeSet.add(new Event("type1", 11));
		sameTypeSet.add(new Event("type1", 5));

		difTypeSet = new TreeSet<>();
		difTypeSet.add(new Event("type1", 1));
		difTypeSet.add(new Event("type3", 3));
		difTypeSet.add(new Event("type1", 4));
		difTypeSet.add(new Event("type4", 2));
		difTypeSet.add(new Event("type1", 8));
		difTypeSet.add(new Event("type2", 10));
		difTypeSet.add(new Event("type1", 9));
	}

	@Test
	void testConstructor() {
		Iterator<Event> it = emptySet.iterator();
		iteratorObject = new EventIteratorImpl(it, "type");
		assertEquals(it, iteratorObject.iterator());
		assertEquals("type", iteratorObject.type);
	}

	@Test
	void testMoveNextWhenThereAreNoEvents() {
		iteratorObject = new EventIteratorImpl(emptySet.iterator(), "type1");
		assertEquals(false, iteratorObject.moveNext());
	}

	@Test
	void testMoveNextWhenThereIsNextEventAndCorrectType() {
		iteratorObject = new EventIteratorImpl(sameTypeSet.iterator(), "type1");
		assertEquals(true, iteratorObject.moveNext());
	}

	@Test
	void testMoveNextWhenThereAreEventsButDontFindTheType() {
		iteratorObject = new EventIteratorImpl(sameTypeSet.iterator(), "type2");
		assertEquals(false, iteratorObject.moveNext());
	}

	@Test
	void testMoveNextWhenMoveNextIsTrueAndThereAreMoreEvents() {
		iteratorObject = new EventIteratorImpl(difTypeSet.iterator(), "type1");
		for (int i = 0; i < 3; i++) {
			iteratorObject.moveNext();
		}
		assertEquals(true, iteratorObject.moveNext());
	}

	@Test
	void testMoveNextWhenMoveNextIsTrueAndThereAreNotMoreEvents() {
		iteratorObject = new EventIteratorImpl(difTypeSet.iterator(), "type1");
		for (int i = 0; i < sameTypeSet.size() - 1; i++) {
			iteratorObject.moveNext();
		}
		assertEquals(false, iteratorObject.moveNext());
	}

	@Test
	void testMoveNextWhenMoveNextIsFalse() {
		iteratorObject = new EventIteratorImpl(difTypeSet.iterator(), "type1");
		while (iteratorObject.moveNext()) {
		}
		assertEquals(false, iteratorObject.moveNext());
	}

	@Test
	void testCurrentWhenThereAreNoEvents() {
		iteratorObject = new EventIteratorImpl(emptySet.iterator(), "type1");
		assertThrows(IllegalStateException.class, () -> {
			iteratorObject.current();
		});
	}

	@Test
	void testCurrentWhenMoveNextIsTrue() {
		iteratorObject = new EventIteratorImpl(difTypeSet.iterator(), "type1");
		for (int i = 0; i < 3; i++) {
			iteratorObject.moveNext();
		}
		assertEquals(8, iteratorObject.current().timestamp());
	}

	@Test
	void testCurrentWhenMoveNextIsFalse() {
		iteratorObject = new EventIteratorImpl(sameTypeSet.iterator(), "type1");
		while (iteratorObject.moveNext()) {
		}
		assertThrows(IllegalStateException.class, () -> {
			iteratorObject.current();
		});
	}

	@Test
	void testRemoveWhenEmpty() {
		iteratorObject = new EventIteratorImpl(emptySet.iterator(), "type1");
		assertThrows(IllegalStateException.class, () -> {
			iteratorObject.remove();
		});
	}

	@Test
	void testRemoveWhenThereAreEventsButNoMoveNext() {
		iteratorObject = new EventIteratorImpl(sameTypeSet.iterator(), "type1");
		assertThrows(IllegalStateException.class, () -> {
			iteratorObject.remove();
		});
	}

	@Test
	void testRemoveWhenThereAreEventsButMoveNextIsFalse() {
		iteratorObject = new EventIteratorImpl(sameTypeSet.iterator(), "type1");
		while (iteratorObject.moveNext()) {
		}
		assertThrows(IllegalStateException.class, () -> {
			iteratorObject.remove();
		});
	}

	@Test
	void testRemoveWhenThereAreEventsAndMoveNextIsTrue() {
		iteratorObject = new EventIteratorImpl(sameTypeSet.iterator(), "type1");
		for (int i = 0; i < 3; i++) {
			iteratorObject.moveNext();
		}
		Event current = iteratorObject.current();
		iteratorObject.remove();
		assertFalse(sameTypeSet.contains(current));
	}
}
