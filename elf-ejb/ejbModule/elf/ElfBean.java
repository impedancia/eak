package elf;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import elf.ElfTown;
import elf.entity.Factory;
import elf.entity.Present;

/**
 * Session Bean implementation class ElfBean
 */
@Stateless
@LocalBean
public class ElfBean implements ElfTown, ElfTownLocal {
	private static String msgNotAssigned ="Present not assigned to child";
	private static String msgNotPresent ="No present present";
	static String puName = "SEA_20161220";
	static EntityManagerFactory emf;
	static EntityManager em;

	private void withTransaction(Consumer<EntityManager> action) {
		em.getTransaction().begin();
		action.accept(em);
		em.getTransaction().commit();
	}

    /**
     * Default constructor.
     */
    public ElfBean() {
    	emf = Persistence.createEntityManagerFactory(puName);
    	em = emf.createEntityManager();
    }

	@Override
	public void createFactories(boolean empty, String ...factoryNames) {
		if (empty) {
			withTransaction(em -> {
				em.createQuery("DELETE FROM Present p").executeUpdate();
				em.createQuery("DELETE FROM Factory f").executeUpdate();
			});
		}
		withTransaction(em -> {
			for(String factoryName:factoryNames){
				Factory f = new Factory(factoryName);
				em.persist(f);
			}
		});
	}

	@Override
	public void createPresent(String factoryName) {
		withTransaction(em -> {
			Present p = new Present();
			Factory f = em.find(Factory.class, factoryName);
			f.addPresent(p);
			em.persist(f);
		});
	}

	@Override
	public void createPresentFor(String factoryName, String name) {
		withTransaction(em -> {
			Present p = new Present(name);
			Factory f = em.find(Factory.class, factoryName);
			f.addPresent(p);
			em.persist(f);
		});

	}

	@Override
	public void setPresentFor(String factoryName, int index, String name) {
		withTransaction(em -> {
			Factory f = em.find(Factory.class, factoryName);
			Present p = f.getPresents().get(index);
			p.setForChild(name);
			em.persist(p);
		});

	}
/*
 * A deliverOnePresent dobjon EJBException kivételt No present present szöveges üzenettel,
 * ha az adott gyárban az ajándékok listája üres.
 * Ha nem üres a lista, de az elsõ ajándékhoz nincsen gyerek hozzárendelve, az üzenet legyen Present not assigned to child.
 * @see elf.ElfTown#deliverOnePresent(java.lang.String)
 */
	@Override
	public String deliverOnePresent(String factoryName)  throws EJBException {
		String childName = null;
		em.getTransaction().begin();

		Factory f = em.find(Factory.class, factoryName);
		List<Present> presents = f.getPresents();
		if (presents == null || presents.size() == 0)
			throw new EJBException(msgNotPresent);
		Present p = presents.get(0);

		if (p.getForChild().isEmpty() || p.getForChild() == null ) throw new EJBException(msgNotAssigned);

		em.remove(p);
		childName = presents.get(0).getForChild();


		em.getTransaction().commit();
		return childName;
	}

	@Override
	public List<String> getFactoryNames() {
		String sql = "SELECT * FROM factory";
		TypedQuery<Factory> query = em.createQuery(sql,Factory.class);
		List<Factory> factories = query.getResultList();
		return factories.stream().map(e -> e.getName()).collect(Collectors.toList());
	}

	@Override
	public List<String> getChildrenIn(String factoryName) {
		Factory f = em.find(Factory.class, factoryName);
		return f.getPresents().stream().map(e -> e.getForChild()).collect(Collectors.toList());
	}

}
