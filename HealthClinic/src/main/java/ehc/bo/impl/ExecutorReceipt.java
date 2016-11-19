package ehc.bo.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.persistence.Transient;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ExecutorReceipt extends BaseObject {
	private ResourcePartyRole executor;
	private Date from;
	private Date to;
	private List<ExecutorReceiptItem> accountItems = new ArrayList<ExecutorReceiptItem>();

	protected ExecutorReceipt() {
		super();
	}

	public ExecutorReceipt(User accountCreator, ResourcePartyRole executor, Date from, Date to) {
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "executorReceipt")
	public List<ExecutorReceiptItem> getAccountItems() {
		return accountItems;
	}

	public void setAccountItems(List<ExecutorReceiptItem> accountItems) {
		this.accountItems = accountItems;
	}

	public void addAccountItem(User accountItemCreator, Treatment treatment) {
		ExecutorReceiptItem accountItem = new ExecutorReceiptItem(accountItemCreator, treatment, executor, this, false);
		accountItems.add(accountItem);
	}

	public void assignExecutor(ResourcePartyRole executor) {
		this.executor = executor;
		executor.addExecutorAccount(this);
	}
	
	public void generatePDF() throws IOException, DocumentException {
	    Individual executorPerson = (Individual)executor.getSource();
	    
		String dest = "src/main/resources/testtable.pdf";
	    Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(11);  
        table.setWidthPercentage(100);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);
           
    	table.addCell("subject first name");
    	table.addCell("subject last name");
    	table.addCell("start");
    	table.addCell("end");
    	table.addCell("treatment price");
    	table.addCell("treatment name");
    	table.addCell("treatment category");
    	table.addCell("payment channel");
    	table.addCell("DPH");
    	table.addCell("executor first name");
    	table.addCell("executor last name");
    	
    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String start = "";
        String end = "";
        for (ExecutorReceiptItem accountItem : accountItems) {
        	table.addCell(accountItem.getSubjectFirstName());
        	table.addCell(accountItem.getSubjectLastName());
        	start = dateFormat.format(accountItem.getFrom());
        	table.addCell(start);
        	end = dateFormat.format(accountItem.getTo());
        	table.addCell(end);
        	table.addCell(""+accountItem.getTreatmentPrice());
        	table.addCell(""+accountItem.getTreatmentTypeName());
        	table.addCell(""+accountItem.getTreatmentGroupName());
        	table.addCell(""+accountItem.getPaymentChannelType());
        	table.addCell(accountItem.isWithDPH() ? "yes" : "no");
        	table.addCell(""+executorPerson.getFirstName());
        	table.addCell(""+executorPerson.getName());
        }
        document.add(table);
        document.close();
	}

	@Transient
	public Money getProvisionsSum() {
		Money provisionSum = new Money();
		for (ExecutorReceiptItem accountItem : getAccountItems()) {
			provisionSum = provisionSum.add(accountItem.getExecutorProvisionAmount());
		}
		return provisionSum;
	}
}
