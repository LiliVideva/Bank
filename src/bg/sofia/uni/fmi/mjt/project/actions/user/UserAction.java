package bg.sofia.uni.fmi.mjt.project.actions.user;

import java.util.List;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.actions.Action;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

abstract class UserAction extends Action {

	UserAction(UserProfile userProfile) {
		super(userProfile);
	}

	protected boolean execute(List<String> arguments, Status userStatus, UsersDatabase users)
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException {
		return false;
	}

	@Override
	public boolean executeAction(String lineArguments, UserProfile factoryCurrentUserProfile, UsersDatabase users,
			AccountsDatabase accounts) throws InvalidFieldException, NoSuchUserException, NotLoggedException,
			UserAlreadyLoggedException, ViolationException {
		setUserProfile(factoryCurrentUserProfile);

		Status userStatus = checkUserStatus(getUserProfile(), users);
		List<String> arguments = receiveArguments(lineArguments);

		if (execute(arguments, userStatus, users, accounts)) {
			return true;
		}
		return false;
	}

	@Override
	protected List<String> receiveArguments(String lineArguments) {
		return null;
	}

	@Override
	protected boolean execute(List<String> arguments, Status userStatus, UsersDatabase users,
			AccountsDatabase accounts) throws InvalidFieldException, NoSuchUserException, NotLoggedException,
			UserAlreadyLoggedException, ViolationException {
		return execute(arguments, userStatus, users);
	}
}
