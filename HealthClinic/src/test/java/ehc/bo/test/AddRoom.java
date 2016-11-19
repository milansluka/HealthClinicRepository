package ehc.bo.test;

import java.util.ArrayList;
import java.util.List;

import ehc.bo.impl.Login;
import ehc.bo.impl.Room;
import ehc.bo.impl.RoomDao;
import ehc.bo.impl.RoomType;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;

public class AddRoom extends RootTestCase {
	private List<Long> roomIds = new ArrayList<Long>();
	
	public void testApp() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		RoomType roomType = new RoomType(executor);
		String roomName = "test room";
		
		Room room = new Room(executor, roomType, roomName);

		long id = (Long) HibernateUtil.save(room);
		roomIds.add(id);
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		
		RoomDao roomDao = RoomDao.getInstance();
		Room persistedRoom = roomDao.findById(id);
		
		assertTrue(persistedRoom.getName().equals(roomName));	
	}

	protected void tearDown() throws Exception {
		for (long id : roomIds) {
			removeRoom(id);
		}
		
		super.tearDown();
	}

}
