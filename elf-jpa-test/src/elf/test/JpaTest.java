package elf.test;

import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import elf.entity.Factory;
import elf.entity.Present;

public class JpaTest {
	EntityManager em;

	@Test
	public void testCreateTwoFactories() {
		Factory factory1 = new Factory("fac1");
		Factory factory2 = new Factory("fac2");
		em.persist(factory1);
		em.persist(factory2);
		Assert.assertNotEquals("Created Factory IDs are different", factory1.getName(), factory2.getName());
	}

	@Test
	public void testCreateTwoPresents() {
		Present present1 = new Present();
		Present present2 = new Present();
		em.persist(present1);
		em.persist(present2);
		Assert.assertNotEquals("Created Present IDs are different", present1.getId(), present2.getId());
	}

	@Test
	public void testPresents() {
		String factoryName = "fac1";

		int[] presentIds = new int[2];

		withTransaction(em -> {
			Present present1 = new Present("Jack");
			Present present2 = new Present("Jill");
			Factory factory = new Factory(factoryName);
			factory.addPresent(present1);
			factory.addPresent(present2);
			em.persist(factory);

			presentIds[0] = present1.getId();
			presentIds[1] = present2.getId();
		});

		withTransaction(em -> {
			Factory factory = em.find(Factory.class, factoryName);
			Assert.assertEquals("Has 2 presents", presentIds.length, factory.getPresents().size());
			Assert.assertEquals("Traverses factory.getPresents() OK 1", presentIds[0], factory.getPresents().get(0).getId());
			Assert.assertEquals("Traverses factory.getPresents() OK 2", presentIds[1], factory.getPresents().get(1).getId());

			Present present = em.find(Present.class, presentIds[0]);
			Assert.assertEquals("Traverses present.getFactory() OK", factoryName, present.getFactory().getName());
		});
	}


	// -----------------------------------------
	// Do not modify below this point.

	static String puName = "SEA_20161220";
	static EntityManagerFactory emf;

	@BeforeClass
	public static void beforeClass() {
        emf = Persistence.createEntityManagerFactory(puName);
	}

	@Before
	public void before() {
		em = emf.createEntityManager();

		// delete all previous entities
		withTransaction(em -> {
			em.createQuery("DELETE FROM Present p").executeUpdate();
			em.createQuery("DELETE FROM Factory f").executeUpdate();
		});

		// make sure that the entity manager is empty, too
		em.clear();
	}

	@After
	public void after() {
		em.close();
	}

	private void withTransaction(Consumer<EntityManager> action) {
		em.getTransaction().begin();
		action.accept(em);
		em.getTransaction().commit();
	}
}
