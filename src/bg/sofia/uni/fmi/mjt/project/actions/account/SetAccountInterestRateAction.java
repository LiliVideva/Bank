package bg.sofia.uni.fmi.mjt.project.actions.account;

import java.io.PrintWriter;
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

public class SetAccountInterestRateAction extends AccountAction {
	private PrintWriter printWriter;

	public SetAccountInterestRateAction(UserProfile userProfile, PrintWriter printWriter) {
		super(userProfile);

		this.printWriter = printWriter;
	}

	@Override
	public boolean executeAction(String lineArguments, UserProfile factoryCurrentUserProfile, UsersDatabase users,
			AccountsDatabase accounts) throws InvalidFieldException, NoSuchUserException, NotLoggedException,
			UserAlreadyLoggedException, ViolationException, NoSuchAccountException {

		setUserProfile(factoryCurrentUserProfile);
		Status userStatus = checkUserStatus(getUserProfile(), users);

		if (userStatus.equals(Status.CLERK) || userStatus.equals(Status.ADMIN)) {
			List<String> arguments = receiveArguments(lineArguments);
			return execute(arguments, userStatus, users, accounts);
		}

		printWriter.println("Not a clerk or admin!");
		return false;
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
			throws ViolationException, InvalidFieldException, NoSuchUserException, NoSuchAccountException {
		return accounts.setAccountInterestRate(arguments);
	}

}
