package elf;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import elf.ElfTown;

public class ElfTest {
	private static final String F1 = "factory1";
	private static final String F2 = "factory2";
	private static final String C1 = "child1";
	private static final String C2 = "child2";
	private ElfTown elfTown;

	@Test
	public void testFactoryCreation() {
		elfTown.createFactories(true, F1, F2);
		assertArrayEquals("Factory contains no presents, therefore no children's names", new String[0], elfTown.getChildrenIn(F1).toArray());
		assertArrayEquals("Factory contains no presents, therefore no children's names", new String[0], elfTown.getChildrenIn(F2).toArray());
	}

	@Test
	public void testCreatePresent() {
		elfTown.createFactories(true, F1, F2);
		elfTown.createPresent(F1);
		elfTown.createPresent(F2);
		elfTown.createPresent(F2);
		assertArrayEquals("One empty present in factory", new String[]{null}, elfTown.getChildrenIn(F1).toArray());
		assertArrayEquals("Two empty presents in factory", new String[]{null, null}, elfTown.getChildrenIn(F2).toArray());
	}

	@Test
	public void testCreatePresentFor() {
		elfTown.createFactories(true, F1, F2);
		elfTown.createPresentFor(F1, C1);
		elfTown.createPresentFor(F2, C1);
		elfTown.createPresentFor(F2, C2);
		assertArrayEquals("Child's name is on the present", new String[]{C1}, elfTown.getChildrenIn(F1).toArray());
		assertArrayEquals("Children's names are on the presents", new String[]{C1, C2}, elfTown.getChildrenIn(F2).toArray());
	}

	@Test
	public void testDelivery() {
		elfTown.createFactories(true, F1);
		elfTown.createPresentFor(F1, C1);
		elfTown.createPresentFor(F1, C2);

		assertEquals("Delivered present to " + C1, C1, elfTown.deliverOnePresent(F1));
		assertEquals("Delivered present to " + C2, C2, elfTown.deliverOnePresent(F1));
	}

	@Test
	public void testDeliverMissingPresent() {
		elfTown.createFactories(true, F1);

		now.expect(EJBException.class);
		now.expectMessage("No present present");
		elfTown.deliverOnePresent(F1);
	}

	@Test
	public void testDeliverUnassignedPresent() {
		elfTown.createFactories(true, F1);
		elfTown.createPresent(F1);

		now.expect(EJBException.class);
		now.expectMessage("Present not assigned to child");
		elfTown.deliverOnePresent(F1);
	}


	// -----------------------------------------
	// Do not modify below this point.

	@Rule
	public ExpectedException now = ExpectedException.none();

	private static Context ctx;

	@BeforeClass
	public static void beforeClass() throws NamingException {
		ctx = new InitialContext();
	}

	@Before
	public void before() throws NamingException {
		elfTown = (ElfTown)ctx.lookup("java:global/elf-app/elf-ejb/ElfBean!elf.ElfTown");
	}
}
