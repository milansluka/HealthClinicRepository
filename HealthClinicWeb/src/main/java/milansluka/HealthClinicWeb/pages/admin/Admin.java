package milansluka.HealthClinicWeb.pages.admin;

import org.apache.tapestry5.annotations.InjectPage;

public class Admin {
	
	@InjectPage
	private CreateUser createUser;
	
	
	Object onActionFromCreateUser() {
		return createUser;
	}

}
