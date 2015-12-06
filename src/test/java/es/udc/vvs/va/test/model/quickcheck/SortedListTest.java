package es.udc.vvs.va.test.model.quickcheck;

import static net.java.quickcheck.generator.CombinedGenerators.lists;
import static net.java.quickcheck.generator.PrimitiveGenerators.integers;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import net.java.quickcheck.Generator;
import net.java.quickcheck.characteristic.Classification;
import net.java.quickcheck.collection.Pair;
import net.java.quickcheck.generator.iterable.Iterables;

import org.junit.Test;

public class SortedListTest {

	// @Test
	// public void sortedListDeletion() {
	//
	// Classification c = new Classification();
	//
	// // for (List<Integer> anyList : someLists(integers())) {
	// for (List<Integer> anyList : someLists(integers(-50,50))) {
	//
	// SortedList anySortedList = new SortedList(anyList);
	// // Integer anyNumber = integers().next();
	// Integer anyNumber = integers(-50,50).next();
	//
	// // System.out.println("Borrando " + anyNumber + " de " +
	// anySortedList.toList());
	//
	// if (anySortedList.member(anyNumber)) {
	// c.classifyCall("presente");
	// }
	// else {
	// c.classifyCall("ausente");
	// }
	//
	// anySortedList.delete(anyNumber);
	// assertFalse(anySortedList.member(anyNumber));
	// }
	//
	// for (Object cat : c.getCategories()) {
	// System.out.println("[sortedListDeletion] ===> " + cat + " => " +
	// c.getFrequency(cat));
	// }
	// }

	@Test
	public void sortedListDeletionEnsureMembership() {

		Classification c = new Classification();

		for (Pair<Integer, List<Integer>> pair : Iterables
				.toIterable(new ContainsIntegerListGenerator())) {

			Integer anyNumber = pair.getFirst();
			SortedList anySortedList = new SortedList(pair.getSecond());

			// System.out.println("Borrando " + anyNumber + " de " +
			// anySortedList.toList());

			if (anySortedList.member(anyNumber)) {
				c.classifyCall("presente");
			} else {
				c.classifyCall("ausente");
			}

			anySortedList.delete(anyNumber);
			assertFalse(anySortedList.member(anyNumber));
		}

		for (Object cat : c.getCategories()) {
			System.out.println("[sortedListDeletionEnsureMembership] ===> "
					+ cat + " => " + c.getFrequency(cat));
		}
	}

	class ContainsIntegerListGenerator implements
			Generator<Pair<Integer, List<Integer>>> {
		Generator<Integer> iGen = integers();
		Generator<List<Integer>> lGen = lists(integers());

		@Override
		public Pair<Integer, List<Integer>> next() {
			Integer i = iGen.next();
			List<Integer> l = lGen.next();

			l.add(i);

			return new Pair<Integer, List<Integer>>(i, l);
		}
	}

	@Test
	public void sortedListDeletionEnsureDoubleMembership() {

		Classification c = new Classification();

		for (Pair<Integer, List<Integer>> pair : Iterables
				.toIterable(new ContainsTwiceIntegerListGenerator())) {

			Integer anyNumber = pair.getFirst();
			SortedList anySortedList = new SortedList(pair.getSecond());

			// System.out.println("Borrando " + anyNumber + " de " +
			// anySortedList.toList());

			if (anySortedList.member(anyNumber)) {
				c.classifyCall("presente");
			} else {
				c.classifyCall("ausente");
			}
			anySortedList.delete(anyNumber);
			assertTrue(anySortedList.member(anyNumber));
		}

		for (Object cat : c.getCategories()) {
			System.out
					.println("[sortedListDeletionCountAppearancesBA] ===> "
							+ cat + " => " + c.getFrequency(cat));
		}
	}

	class ContainsTwiceIntegerListGenerator implements
			Generator<Pair<Integer, List<Integer>>> {
		Generator<Integer> iGen = integers();
		Generator<List<Integer>> lGen = lists(integers());

		@Override
		public Pair<Integer, List<Integer>> next() {
			Integer i = iGen.next();
			List<Integer> l = lGen.next();

			l.add(i);
			l.add(i);

			return new Pair<Integer, List<Integer>>(i, l);
		}
	}

	@Test
	public void sortedListDeletionCountAppearancesBA() {

		Classification c = new Classification();
		
		final long topTimes = 100;

		ContainsRandomTimesIntegerListGenerator gen =
				new ContainsRandomTimesIntegerListGenerator(topTimes);

		for (Pair<Integer, List<Integer>> pair : Iterables.toIterable(gen)) {

			Integer anyNumber = pair.getFirst();
			SortedList anySortedList = new SortedList(pair.getSecond());
			long currentTimes = gen.getCurrentTimes();

			if (anySortedList.member(anyNumber)) {
				c.classifyCall("presente");
			} else {
				c.classifyCall("ausente");
			}
			assertEquals(currentTimes,
					anySortedList.getNumberOfOcurrences(anyNumber));
			anySortedList.delete(anyNumber);
			assertEquals(currentTimes - 1,
					anySortedList.getNumberOfOcurrences(anyNumber));
		}

		for (Object cat : c.getCategories()) {
			System.out
					.println("[sortedListDeletionEnsureDoubleMembership] ===> "
							+ cat + " => " + c.getFrequency(cat));
		}
	}

	class ContainsRandomTimesIntegerListGenerator implements
			Generator<Pair<Integer, List<Integer>>> {
		Generator<Integer> iGen = integers();
		Generator<List<Integer>> lGen = lists(integers());
		long times;
		long currentTimes;

		ContainsRandomTimesIntegerListGenerator(long times) {
			this.times = times;
		}

		long getCurrentTimes() {
			return currentTimes;
		}

		@Override
		public Pair<Integer, List<Integer>> next() {
			Integer i = iGen.next();
			List<Integer> l = lGen.next();
			long realTimes = new Random().nextInt((int)times) + 1;

			this.currentTimes = realTimes;

			for (int j = 0; j < realTimes; j++) {
				l.add(i);
			}

			return new Pair<Integer, List<Integer>>(i, l);
		}
	}
}