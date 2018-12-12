package elf.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@RequiredArgsConstructor
@ToString(of= {"id", "forChild"})
@NoArgsConstructor
public class Present {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private int id;
	@NonNull private String forChild;
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY, optional=false)
	private Factory factory;
}
