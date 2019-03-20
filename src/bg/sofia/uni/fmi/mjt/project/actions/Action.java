package bg.sofia.uni.fmi.mjt.project.actions;

import java.util.List;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchAccountException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public abstract class Action {
	private UserProfile userProfile;

	protected Action(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	protected UserProfile getUserProfile() {
		return userProfile;
	}

	protected void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public abstract boolean executeAction(String lineArguments, UserProfile factoryCurrentUserProfile,
			UsersDatabase users, AccountsDatabase accounts) throws InvalidFieldException, NoSuchUserException,
			NotLoggedException, UserAlreadyLoggedException, ViolationException, NoSuchAccountException;

	protected abstract List<String> receiveArguments(String lineArguments);

	protected abstract boolean execute(List<String> arguments, Status userStatus, UsersDatabase users,
			AccountsDatabase accounts) throws InvalidFieldException, NoSuchUserException, NotLoggedException,
			UserAlreadyLoggedException, ViolationException, NoSuchAccountException;

	protected Status checkUserStatus(UserProfile userProfile, UsersDatabase users) {
		Status userStatus = (userProfile == null) ? Status.UNREGISTERED
				: users.checkUserStatus(userProfile.getUsername());
		return userStatus;
	}
}
