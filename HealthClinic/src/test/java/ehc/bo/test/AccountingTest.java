package ehc.bo.test;

import java.io.IOException;
import java.util.Date;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;

import ehc.bo.impl.AccountItem;
import ehc.bo.impl.AccountUtil;
import ehc.bo.impl.ExecutorAccount;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.Physician;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.Assert;

public class AccountingTest extends RootTestCase {
	private IndividualDao individualDao = IndividualDao.getInstance();

	@DataProvider
	public static Object[][] appointmentParams() {
		int year = 2016;
		int month = 8;

		Object[][] params = {
				{ "Janko", "Mrkvicka", "Odstraňovanie pigmentov celá tvár", DateUtil.date(year, month, 1, 7, 0, 0),
						DateUtil.date(year, month, 1, 7, 0, 0), DateUtil.date(year, month, 1, 9, 0, 0) },
				{ "Jozef", "Šemota", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 10, 0, 0),
						DateUtil.date(year, month, 1, 10, 0, 0), DateUtil.date(year, month, 1, 11, 0, 0) },
				{ "Milan", "Sluka", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 13, 0, 0),
						DateUtil.date(year, month, 1, 13, 0, 0), DateUtil.date(year, month, 1, 13, 30, 0) },
				{ "Michaela", "Holienčíková", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 17, 30, 0),
						DateUtil.date(year, month, 1, 17, 30, 0), DateUtil.date(year, month, 1, 18, 0, 0) },
				{ "Ondrej", "Štalmach", "OxyGeneo - tvár", DateUtil.date(year, month, 2, 8, 30, 0),
						DateUtil.date(year, month, 2, 8, 30, 0), DateUtil.date(year, month, 2, 9, 0, 0) },
				{ "Karol", "Kubanda", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 7, 0, 0),
						DateUtil.date(year, month, 1, 7, 0, 0), DateUtil.date(year, month, 1, 7, 30, 0) },
				{ "Jana", "Pavlanská", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 7, 15, 0),
						DateUtil.date(year, month, 1, 7, 30, 0), DateUtil.date(year, month, 1, 8, 0, 0) },
				{ "Martin", "Mrník", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 17, 30, 0),
						DateUtil.date(year, month, 2, 9, 0, 0), DateUtil.date(year, month, 2, 9, 30, 0) },
				{ "Tatiana", "Hiková", "Odstraňovanie pigmentov celá tvár", DateUtil.date(year, month, 1, 16, 0, 0),
						DateUtil.date(year, month, 1, 16, 0, 0), DateUtil.date(year, month, 1, 18, 0, 0) },
				{ "Nataša", "Gerthofferová", "Odstraňovanie pigmentov celá tvár",
						DateUtil.date(year, month, 1, 14, 0, 0), DateUtil.date(year, month, 1, 14, 0, 0),
						DateUtil.date(year, month, 1, 16, 0, 0) },
				{ "Zuzana", "Cibulková", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 13, 0, 0),
						DateUtil.date(year, month, 1, 13, 0, 0), DateUtil.date(year, month, 1, 14, 0, 0) },
				{ "Petra", "Mokrá", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 13, 0, 0),
						DateUtil.date(year, month, 2, 8, 30, 0), DateUtil.date(year, month, 2, 9, 30, 0) },
				{ "Marianna", "Pastvová", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 13, 0, 0),
						DateUtil.date(year, month, 2, 9, 30, 0), DateUtil.date(year, month, 2, 10, 30, 0) },
				{ "Olga", "Gablasová", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 12, 15, 0),
						DateUtil.date(year, month, 2, 10, 30, 0), DateUtil.date(year, month, 2, 11, 30, 0) },
				{ "Juraj", "Marček", "Odstraňovanie pigmentov celá tvár", DateUtil.date(year, month, 2, 12, 30, 0),
						DateUtil.date(year, month, 2, 12, 30, 0), DateUtil.date(year, month, 2, 14, 30, 0) },
				{ "Lukáš", "Trnka", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 2, 11, 30, 0),
						DateUtil.date(year, month, 2, 11, 30, 0), DateUtil.date(year, month, 2, 12, 30, 0) },
				{ "Pavol", "Kocinec", "OxyGeneo - tvár", DateUtil.date(year, month, 2, 10, 0, 0),
						DateUtil.date(year, month, 2, 10, 0, 0), DateUtil.date(year, month, 2, 10, 30, 0) },
				{ "Tomáš", "Krivko", "OxyGeneo - tvár", DateUtil.date(year, month, 2, 8, 15, 0),
						DateUtil.date(year, month, 2, 9, 30, 0), DateUtil.date(year, month, 2, 10, 0, 0) },
				{ "František", "Zicho", "Epilácia brucha", DateUtil.date(year, month, 3, 9, 0, 0),
						DateUtil.date(year, month, 3, 9, 0, 0), DateUtil.date(year, month, 3, 10, 0, 0) },
				{ "Peter", "Korbel", "Epilácia brucha", DateUtil.date(year, month, 3, 9, 0, 0),
						DateUtil.date(year, month, 3, 9, 0, 0), DateUtil.date(year, month, 3, 10, 0, 0) }, };

		return params;
	}

	@BeforeClass
	public void beforeClass() {
		setUpSystem2();
		int year = 2016;
		int month = 8;

		// create appointments
		createAppointment("Lukáš", "Trnka", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 13, 0, 0),
				DateUtil.date(year, month, 1, 13, 0, 0), DateUtil.date(year, month, 1, 13, 30, 0));
		createAppointment("Michaela", "Holienčíková", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 17, 30, 0),
				DateUtil.date(year, month, 1, 17, 30, 0), DateUtil.date(year, month, 1, 18, 0, 0));
		createAppointment("Ondrej", "Štalmach", "OxyGeneo - tvár", DateUtil.date(year, month, 2, 8, 30, 0),
				DateUtil.date(year, month, 2, 8, 30, 0), DateUtil.date(year, month, 2, 9, 0, 0));
		createAppointment("Karol", "Kubanda", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 7, 0, 0),
				DateUtil.date(year, month, 1, 7, 0, 0), DateUtil.date(year, month, 1, 7, 30, 0));
		createAppointment("Jana", "Pavlanská", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 7, 15, 0),
				DateUtil.date(year, month, 1, 7, 30, 0), DateUtil.date(year, month, 1, 8, 0, 0));

		createAppointment("František", "Zicho", "OxyGeneo - tvár", DateUtil.date(year, 9, 1, 7, 15, 0),
				DateUtil.date(year, month, 1, 7, 30, 0), DateUtil.date(year, 9, 1, 8, 0, 0));

		createAppointment("Tomáš", "Krivko", "OxyGeneo - tvár", DateUtil.date(year, 9, 7, 7, 15, 0),
				DateUtil.date(year, month, 1, 7, 30, 0), DateUtil.date(year, 9, 7, 8, 0, 0));

		// create treatments
		morphAppointmentsToTreatments();

		// pay for treatments
		payForAppointments();

	}

	@AfterClass
	public void afterClass() {
		tearDownSystem();

	}

	@Test
	public void createExecutorAccount() {
		HibernateUtil.beginTransaction();
		String physicianFirstName = "Marika";
		String physicianLastName = "Piršelová";
		Individual individual = individualDao.findByFirstAndLastName(physicianFirstName, physicianLastName);

		// create account from 1.8.2016 to 31.8.2016
		Date from = DateUtil.date(2016, 8, 1, 7, 0, 0);
		Date to = DateUtil.date(2016, 8, 31, 9, 30, 0);

		Login login = new Login();
		User accountCreator = login.login("admin", "admin");
		Physician executor = (Physician) individual.getReservableSourceRoles().get(0);

		AccountUtil accountUtil = new AccountUtil();
		ExecutorAccount executorAccount = accountUtil.createAccount(accountCreator, executor, from, to);
		HibernateUtil.save(executorAccount);

		// create account from 1.9.2016 to 31.9.2016
		from = DateUtil.date(2016, 9, 1, 7, 0, 0);
		to = DateUtil.date(2016, 9, 31, 9, 30, 0);

		executorAccount = accountUtil.createAccount(accountCreator, executor, from, to);
		HibernateUtil.save(executorAccount);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		individual = individualDao.findByFirstAndLastName(physicianFirstName, physicianLastName);
		executor = (Physician) individual.getReservableSourceRoles().get(0);
		executorAccount = executor.getExecutorAccounts().get(0);
		ExecutorAccount executorAccount2 = executor.getExecutorAccounts().get(1);
		Assert.assertNotNull(executorAccount);
		Assert.assertNotNull(executorAccount2);
		Assert.assertEquals(new Money(25), executorAccount.getProvisionsSum());	
		Assert.assertEquals(new Money(10), executorAccount2.getProvisionsSum());	
		HibernateUtil.commitTransaction();
	}
	
	@Test
	public void generatePDF() {
		HibernateUtil.beginTransaction();
		Individual individual = individualDao.findByFirstAndLastName("Marika", "Piršelová");
		Physician executor = (Physician) individual.getReservableSourceRoles().get(0);
		ExecutorAccount executorAccount = executor.getExecutorAccounts().get(0);
		try {
		executorAccount.generatePDF();
		} catch (IOException e) {
			
		} catch (DocumentException de) {
			
		}
	}
}
