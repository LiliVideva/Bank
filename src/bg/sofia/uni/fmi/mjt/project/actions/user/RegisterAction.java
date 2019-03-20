package bg.sofia.uni.fmi.mjt.project.actions.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class RegisterAction extends UserAction {

	public RegisterAction(UserProfile userProfile) {
		super(userProfile);
	}

	@Override
	public List<String> receiveArguments(String lineArguments) {
		List<String> arguments = new ArrayList<>();

		if (getUserProfile() != null) {
			arguments.add(getUserProfile().getStatus().toString());
		} else {
			arguments.add(Status.UNREGISTERED.toString());
		}

		if (lineArguments != null) {
			arguments.addAll(Arrays.asList(lineArguments.split(" ")));
		}
		return arguments;
	}

	@Override
	public boolean execute(List<String> arguments, Status userStatus, UsersDatabase users)
			throws InvalidFieldException, UserAlreadyLoggedException {
		if (userStatus.equals(Status.UNREGISTERED) || userStatus.equals(Status.ADMIN)) {
			return users.registerUser(arguments);
		}
		throw new UserAlreadyLoggedException();
	}

}
