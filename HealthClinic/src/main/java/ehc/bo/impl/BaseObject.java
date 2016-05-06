package ehc.bo.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import ehc.util.DateUtil;

@MappedSuperclass
public class BaseObject {
	private long id;
	private User createdBy;
	private Date createdOn;
	private User modifiedBy;
	private Date modifiedOn;

	protected BaseObject() {
		this.createdOn = DateUtil.now();
		this.setModifiedOn(createdOn);
	}

	public BaseObject(User executor) {
		this();
		this.createdBy = executor;
		this.setModifiedBy(executor);
	}

	@Id
	/* @GeneratedValue(strategy = GenerationType.SEQUENCE) */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "created_on")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modified_by")
	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}
