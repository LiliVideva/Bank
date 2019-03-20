package bg.sofia.uni.fmi.mjt.project.actions.user;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class SendMessageAction extends UserAction {
	private PrintWriter printWriter;

	public SendMessageAction(UserProfile userProfile, PrintWriter printWriter) {
		super(userProfile);

		this.printWriter = printWriter;
	}

	@Override
	public List<String> receiveArguments(String lineArguments) {
		List<String> arguments = new ArrayList<>();

		if (getUserProfile() != null) {
			arguments.add(getUserProfile().getUsername());
		} else {
			arguments.add("none");
		}

		if (lineArguments != null) {
			arguments.addAll(Arrays.asList(lineArguments.split(" ", 2)));
		}
		return arguments;
	}

	@Override
	public boolean execute(List<String> arguments, Status userStatus, UsersDatabase users)
			throws NoSuchUserException, NotLoggedException {
		if (!userStatus.equals(Status.UNREGISTERED)) {
			return users.sendMessage(arguments, printWriter);
		}
		throw new NotLoggedException();
	}
}
