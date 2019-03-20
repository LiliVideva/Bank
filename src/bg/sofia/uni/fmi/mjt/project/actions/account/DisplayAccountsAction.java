package bg.sofia.uni.fmi.mjt.project.actions.account;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.Status;

public class DisplayAccountsAction extends AccountAction {
	private PrintWriter printWriter;

	public DisplayAccountsAction(UserProfile userProfile, PrintWriter printWriter) {
		super(userProfile);

		this.printWriter = printWriter;
	}

	@Override
	protected List<String> receiveArguments(String lineArguments) {
		List<String> arguments = new ArrayList<>();
		arguments.add(getUserProfile().getUsername());

		return arguments;
	}

	@Override
	protected boolean execute(List<String> arguments, Status userStatus, AccountsDatabase accounts)
			throws InvalidFieldException, ViolationException, NoSuchUserException {
		return accounts.displayAccounts(arguments, printWriter);
	}
}
