package ehc.bo;

import java.util.List;

import ehc.bo.impl.User;

public interface UserDao {
	void addUser(User user);
	void deleteUser(User user);
	User findUserByLogin(User user);
	void updateUser(User user);
	List<User> getAllUsers();

}
