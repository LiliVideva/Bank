package bg.sofia.uni.fmi.mjt.project.actions.user;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class GetUserAction extends UserAction {
	private PrintWriter printWriter;

	public GetUserAction(UserProfile userProfile, PrintWriter printWriter) {
		super(userProfile);

		this.printWriter = printWriter;
	}

	@Override
	protected List<String> receiveArguments(String lineArguments) {
		List<String> arguments = new ArrayList<>();

		if (getUserProfile() != null) {
			arguments.add(getUserProfile().getUsername());
		} else {
			arguments.add("none");
		}

		if (lineArguments != null) {
			arguments.add(lineArguments);
		}
		return arguments;
	}

	@Override
	public boolean execute(List<String> arguments, Status userStatus, UsersDatabase users)
			throws NoSuchUserException, InvalidFieldException, NotLoggedException {
		if (!userStatus.equals(Status.UNREGISTERED)) {
			return users.printPersonalDetails(arguments, printWriter);
		}
		throw new NotLoggedException();
	}

}
