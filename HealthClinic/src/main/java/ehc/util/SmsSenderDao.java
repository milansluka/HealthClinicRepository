package ehc.util;

public class SmsSenderDao {
	private static SmsSenderDao singleton;

	public static SmsSenderDao instance() {
		return singleton == null ? singleton = new SmsSenderDao() : singleton;
	}

	public SmsSender create(String user, String pass, String defaultNumberFrom, String testingEnviromentNumberTo) {
		return new SmsSender(user, pass, defaultNumberFrom, testingEnviromentNumberTo);
	}
}
