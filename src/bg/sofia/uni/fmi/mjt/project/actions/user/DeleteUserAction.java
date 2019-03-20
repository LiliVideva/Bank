package bg.sofia.uni.fmi.mjt.project.actions.user;

import java.util.Arrays;
import java.util.List;

import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class DeleteUserAction extends UserAction {

	public DeleteUserAction(UserProfile userProfile) {
		super(userProfile);
	}

	@Override
	protected List<String> receiveArguments(String lineArguments) {
		return super.receiveArguments(lineArguments);
	}

	@Override
	public boolean execute(List<String> arguments, Status userStatus, UsersDatabase users)
			throws NoSuchUserException, NotLoggedException, ViolationException {
		if (!userStatus.equals(Status.UNREGISTERED)) {
			if (users.deleteUser(Arrays.asList(getUserProfile().getUsername())) && disconnect()) {
				setUserProfile(null);
				return true;
			}
			return false;
		}
		throw new NotLoggedException();
	}

	protected boolean disconnect() throws ViolationException {
		SocketDisconnect socketDisconnect = new SocketDisconnect();

		return socketDisconnect.closeSocket(getUserProfile());
	}
}
