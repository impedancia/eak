package elf.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(of = {"name"})
public class Factory {
	@Id @NonNull String name;

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, mappedBy="factory")
	@EqualsAndHashCode.Exclude private List<Present> presents;



	public void addPresent(Present present) {
		if (presents == null) presents = new ArrayList<Present>();
		presents.add(present);
		present.setFactory(this);
	}
}
