package bg.sofia.uni.fmi.mjt.project.actions.user;

import java.io.IOException;
import java.net.Socket;

import bg.sofia.uni.fmi.mjt.project.BankServer;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;

public class SocketDisconnect {

	public SocketDisconnect() {
	}

	boolean closeSocket(UserProfile userProfile) throws ViolationException {
		try {
			String username = userProfile.getUsername();
			Socket socket = BankServer.getUserSocket(username);
			BankServer.removeUserSocket(username);
			socket.close();
			return true;
		} catch (IOException e) {
			throw new ViolationException(
					String.format("Can't disconnect, try to connect first!%n%s%n", e.getMessage()));
		}
	}

}
