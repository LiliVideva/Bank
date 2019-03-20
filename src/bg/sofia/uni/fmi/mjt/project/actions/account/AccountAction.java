package bg.sofia.uni.fmi.mjt.project.actions.account;

import java.util.List;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.actions.Action;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchAccountException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

abstract class AccountAction extends Action {
	public AccountAction(UserProfile userProfile) {
		super(userProfile);
	}

	private boolean isLogged(Status userStatus) throws NotLoggedException {
		if (userStatus.equals(Status.UNREGISTERED)) {
			throw new NotLoggedException();
		}
		return true;
	}

	protected abstract boolean execute(List<String> arguments, Status userStatus, AccountsDatabase accounts)
			throws NoSuchUserException, InvalidFieldException, NoSuchAccountException, ViolationException;

	@Override
	public boolean executeAction(String lineArguments, UserProfile factoryCurrentUserProfile, UsersDatabase users,
			AccountsDatabase accounts) throws InvalidFieldException, NoSuchUserException, NotLoggedException,
			UserAlreadyLoggedException, ViolationException, NoSuchAccountException {
		setUserProfile(factoryCurrentUserProfile);
		Status userStatus = checkUserStatus(getUserProfile(), users);

		if (isLogged(userStatus)) {
			List<String> arguments = receiveArguments(lineArguments);
			return execute(arguments, userStatus, users, accounts);
		}
		return false;
	}

	@Override
	protected List<String> receiveArguments(String lineArguments) {
		return null;
	}

	@Override
	protected boolean execute(List<String> arguments, Status userStatus, UsersDatabase users,
			AccountsDatabase accounts)
			throws ViolationException, InvalidFieldException, NoSuchUserException, NoSuchAccountException {
		return execute(arguments, userStatus, accounts);
	}
}
