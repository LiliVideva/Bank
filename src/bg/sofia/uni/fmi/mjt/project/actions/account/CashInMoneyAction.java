package bg.sofia.uni.fmi.mjt.project.actions.account;

import java.util.ArrayList;
import java.util.Arrays;
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

public class CashInMoneyAction extends AccountAction {

	public CashInMoneyAction(UserProfile userProfile) {
		super(userProfile);
	}

	@Override
	public boolean executeAction(String lineArguments, UserProfile factoryCurrentUserProfile, UsersDatabase users,
			AccountsDatabase accounts) throws InvalidFieldException, NoSuchUserException, NotLoggedException,
			UserAlreadyLoggedException, ViolationException, NoSuchAccountException {

		setUserProfile(factoryCurrentUserProfile);
		Status userStatus = checkUserStatus(getUserProfile(), users);

		List<String> arguments = receiveArguments(lineArguments);
		return execute(arguments, userStatus, users, accounts);
	}

	@Override
	protected List<String> receiveArguments(String lineArguments) {
		List<String> arguments = new ArrayList<>();

		if (lineArguments != null) {
			arguments.addAll(Arrays.asList(lineArguments.split(" ")));
		}

		return arguments;
	}

	@Override
	protected boolean execute(List<String> arguments, Status userStatus, AccountsDatabase accounts)
			throws InvalidFieldException, NoSuchUserException, NoSuchAccountException, ViolationException {
		return accounts.cashInMoney(arguments);
	}

}
