package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "company")
@PrimaryKeyJoinColumn(name = "party_id")
public class Company extends Party {
	protected Company() {
		super();
	}

	public Company(User executor, String name) {
		super(executor, name);
	}

}
