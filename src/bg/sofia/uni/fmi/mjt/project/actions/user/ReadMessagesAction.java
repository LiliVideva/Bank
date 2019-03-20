package bg.sofia.uni.fmi.mjt.project.actions.user;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class ReadMessagesAction extends UserAction {
	private PrintWriter printWriter;

	public ReadMessagesAction(UserProfile userProfile, PrintWriter printWriter) {
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
	public boolean execute(List<String> arguments, Status userStatus, UsersDatabase users)
			throws NoSuchUserException, NotLoggedException {
		if (!userStatus.equals(Status.UNREGISTERED)) {
			return users.readMessages(arguments, printWriter);
		}
		throw new NotLoggedException();
	}
}
