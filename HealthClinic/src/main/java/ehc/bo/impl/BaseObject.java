package ehc.bo.impl;

import java.util.Date;

import javax.persistence.CascadeType;
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
	
	protected BaseObject() {
		this.createdOn = DateUtil.now();
	}

	public BaseObject(User executor) {
		this();
		this.createdBy = executor;
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

	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "created_by")
/*	@JoinColumn(name = "created_by",nullable = false, updatable = false, insertable = true)*/
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

}
