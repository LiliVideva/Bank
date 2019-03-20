package bg.sofia.uni.fmi.mjt.project.actions.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.actions.ActionFactory;
import bg.sofia.uni.fmi.mjt.project.actions.ClientActionFactory;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.threads.ClientRunnable;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class LoginAction extends UserAction {
	private static final String HOST = "localhost";
	private static final int PORT = 8086;

	private ActionFactory clientActionFactory;

	public LoginAction(UserProfile userProfile, ActionFactory clientActionFactory) {
		super(userProfile);
		this.clientActionFactory = clientActionFactory;
	}

	@Override
	public List<String> receiveArguments(String lineArguments) {
		List<String> arguments = new ArrayList<>();

		if (lineArguments != null) {
			arguments.addAll(Arrays.asList(lineArguments.split(" ")));
		}
		return arguments;
	}

	@Override
	protected boolean execute(List<String> arguments, Status userStatus, UsersDatabase users, AccountsDatabase accounts)
			throws InvalidFieldException, NoSuchUserException, UserAlreadyLoggedException, ViolationException {
		if (userStatus.equals(Status.UNREGISTERED)) {
			if (users.loginUser(arguments)) {
				UserProfile userProfile = users.getPersonalDetails(arguments);
				setUserProfile(userProfile);

				Socket socket = connect(userProfile);

				try {
					PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
					accounts.printViolationMessages(getUserProfile().getUsername(), printWriter);
				} catch (IOException e) {
					throw new RuntimeException(
							String.format("Problem creating print writer for logging!%n%s%n", e.getMessage()));
				}
				return true;
			}
			return false;
		}
		throw new UserAlreadyLoggedException();
	}

	protected Socket connect(UserProfile userProfile) throws ViolationException {
		try {
			Socket socket = new Socket(HOST, PORT);
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

			Gson gson = new Gson();
			String userJson = gson.toJson(userProfile);
			printWriter.println(userJson);

			((ClientActionFactory) clientActionFactory).setPrintWriter(printWriter);

			ClientRunnable clientRunnable = new ClientRunnable(socket);
			Thread thread = new Thread(clientRunnable);
			thread.start();

			return socket;
		} catch (IOException e) {
			throw new ViolationException(
					String.format("Can't connect to server on %s:%d, make sure that the server is started!%n%s%n", HOST,
							PORT, e.getMessage()));
		}

	}
}
