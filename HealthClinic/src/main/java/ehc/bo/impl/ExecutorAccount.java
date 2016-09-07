package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ExecutorAccount extends BaseObject {
	private ResourcePartyRole executor;
	private Date from;
	private Date to;
 /*   private Money provisionSum;
    private Money treatmentPriceSum;*/
	private List<AccountItem> accountItems = new ArrayList<>();
	
	protected ExecutorAccount() {
		super();
	}
	
	public ExecutorAccount(User accountCreator, ResourcePartyRole executor, Date from, Date to) {
		super(accountCreator);
		this.from = from;
		this.to = to;
		assignExecutor(executor);
	}

	
	@ManyToOne
	@JoinColumn(name = "executor")
	public ResourcePartyRole getExecutor() {
		return executor;
	}
	
	public void setExecutor(ResourcePartyRole executor) {
		this.executor = executor;
	}

	@Column(name = "\"from\"")
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	@Column(name = "\"to\"")
	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "executorAccount")
	public List<AccountItem> getAccountItems() {
		return accountItems;
	}

	public void setAccountItems(List<AccountItem> accountItems) {
		this.accountItems = accountItems;
	}
	
	public void addAccountItem(User accountItemCreator, Treatment treatment) {
		AccountItem accountItem = new AccountItem(accountItemCreator, treatment, executor, this, false);
		accountItems.add(accountItem);
	}
	
	public void assignExecutor(ResourcePartyRole executor) {
		this.executor = executor;
		executor.addExecutorAccount(this);
	}
}
