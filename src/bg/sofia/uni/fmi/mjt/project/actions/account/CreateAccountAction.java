package bg.sofia.uni.fmi.mjt.project.actions.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.Status;

public class CreateAccountAction extends AccountAction {

	public CreateAccountAction(UserProfile userProfile) {
		super(userProfile);
	}

	@Override
	protected List<String> receiveArguments(String lineArguments) {
		List<String> arguments = new ArrayList<>();
		arguments.add(getUserProfile().getUsername());

		if (lineArguments != null) {
			arguments.addAll(Arrays.asList(lineArguments.split(" ")));
		}
		return arguments;
	}

	@Override
	protected boolean execute(List<String> arguments, Status userStatus, AccountsDatabase accounts)
			throws InvalidFieldException, ViolationException {
		return accounts.createAccount(arguments);
	}
}
