package bg.sofia.uni.fmi.mjt.project.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.project.BankServer;
import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.actions.ActionFactory;
import bg.sofia.uni.fmi.mjt.project.actions.ServerActionFactory;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class ServerRunnable implements Runnable {
	private Socket socket;
	private UsersDatabase users;
	private AccountsDatabase accounts;

	public ServerRunnable(Socket socket, UsersDatabase users, AccountsDatabase accounts) {
		this.socket = socket;
		this.users = users;
		this.accounts = accounts;
	}

	@Override
	public void run() {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
			ActionFactory actionFactory = new ServerActionFactory(users, accounts);

			int counter = 0;
			while (true) {
				if (socket.isClosed()) {
					return;
				}

				String line = bufferedReader.readLine();
				if (counter == 0) {
					UserProfile userProfile = addNewUserSocket(line);
					actionFactory.setUserProfile(userProfile);
				} else {
					actionFactory.processAction(line);
				}
				counter++;
			}
		} catch (IOException e) {
			throw new RuntimeException(
					String.format("Problem opening input and output stream on server side!%n%s", e.getMessage()));
		}
	}

	private UserProfile addNewUserSocket(String line) {
		Gson gson = new Gson();
		UserProfile userProfile = gson.fromJson(line, UserProfile.class);
		BankServer.addUserSocket(userProfile.getUsername(), socket);

		return userProfile;
	}
}
