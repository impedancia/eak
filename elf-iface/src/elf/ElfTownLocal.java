package elf;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Local;

@Local
public interface ElfTownLocal {
	void createFactories(boolean empty, String ...factoryNames);
	void createPresent(String factoryName);
	void createPresentFor(String factoryName, String name);
	void setPresentFor(String factoryName, int index, String name);
	String deliverOnePresent(String factoryName) throws EJBException;
	List<String> getFactoryNames();
	List<String> getChildrenIn(String factoryName);
}
