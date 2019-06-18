package net.intelie.challenges;

public class Main {
	public static void main(String[] args) {
		EventStoreImpl ev = new EventStoreImpl();
		for (int i = 0; i < 20; i++) {
			ev.insert(new Event("test1", i));
			ev.insert(new Event("test2", i));
		}

		System.out.println(ev.set().size());
		ev.removeAll("test2");
		System.out.println(ev.set().size());

//		while (iterator.moveNext()) {
//			if (iterator.current().timestamp() % 4 == 0) {
//				iterator.remove();
//			}
//		}

//		iterator = ev.query("test2", 3, 12);
//		while (iterator.moveNext()) {
//			System.out.println(iterator.current().type() + " " + iterator.current().timestamp());
//		}

	}
}