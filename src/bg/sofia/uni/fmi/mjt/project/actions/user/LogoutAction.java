package bg.sofia.uni.fmi.mjt.project.actions.user;

import java.util.List;

import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class LogoutAction extends UserAction {

	public LogoutAction(UserProfile userProfile) {
		super(userProfile);
	}

	@Override
	protected List<String> receiveArguments(String lineArguments) {
		return super.receiveArguments(lineArguments);
	}

	@Override
	protected boolean execute(List<String> arguments, Status userStatus, UsersDatabase users)
			throws NotLoggedException, ViolationException {
		if (!userStatus.equals(Status.UNREGISTERED)) {
			if (getUserProfile() != null && disconnect()) {
				setUserProfile(null);
				return true;
			}
		}
		throw new NotLoggedException();
	}

	protected boolean disconnect() throws ViolationException {
		SocketDisconnect socketDisconnect = new SocketDisconnect();

		return socketDisconnect.closeSocket(getUserProfile());
	}
}
