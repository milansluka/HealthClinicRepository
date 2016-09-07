package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Attachment extends ModifiableObject {
	private String name;
	private String path;
	private Treatment treatment;
	
	protected Attachment() {
		super();
	}
	
	public Attachment(User executor, String name, String path, Treatment treatment) {
		super(executor);
		this.name = name;
		this.path = path;
		assignTreatment(treatment);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@ManyToOne
	@JoinColumn(name = "treatment")
	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}
	
	public void assignTreatment(Treatment treatment) {
		if (treatment == null) {
			return;
		}
		setTreatment(treatment);
		treatment.addAttachment(this);
	}
}
