package bg.sofia.uni.fmi.mjt.project.actions;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.actions.user.LoginAction;
import bg.sofia.uni.fmi.mjt.project.actions.user.RegisterAction;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchAccountException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class ClientActionFactory implements ActionFactory {
	private static final String PROGRAM_TERMINATOR = "exit";

	private UserProfile userProfile;
	private PrintWriter printWriter;
	private UsersDatabase users;
	private AccountsDatabase accounts;
	private Map<String, Action> actionsList;

	public ClientActionFactory(UsersDatabase users, AccountsDatabase accounts) {
		this.userProfile = null;
		this.actionsList = new HashMap<>();
		setActionList();
		this.users = users;
		this.accounts = accounts;
	}

	private void setActionList() {
		actionsList.put(ActionsConstants.REGISTER, new RegisterAction(userProfile));
		actionsList.put(ActionsConstants.LOGIN, new LoginAction(userProfile, this));
	}

	@Override
	public boolean processAction(String line) {
		if (line == null || line.isBlank()) {
			System.out.println("No command entered!");
			return true;
		}

		if (!line.equals(PROGRAM_TERMINATOR)) {
			String[] splittedLine = line.split(" ", 2);
			String actionName = splittedLine[0];
			String lineArguments = (splittedLine.length == 2) ? splittedLine[1] : null;

			try {
				Action action = getAction(actionName);
				if (action == null) {
					if (printWriter == null) {
						System.out.println("Can't enter! Try to login first!");
						return false;
					}
					printWriter.println(line);
				} else {
					users.readFromDatabase();
					accounts.readFromDatabase();

					if (action.executeAction(lineArguments, userProfile, users, accounts)) {
						this.userProfile = action.getUserProfile();
						users.writeProfilesInDatabase();
						accounts.writeAccountsInDatabase();
						System.out.println("Action executed successfully!");
						return true;
					}
					System.out.println("Action failed! [Hint: Check number of arguments]");
				}
			} catch (InvalidFieldException | NoSuchUserException | NotLoggedException | UserAlreadyLoggedException
					| ViolationException | NoSuchAccountException e) {
				System.out.printf("Action failed!%n%s", e.getMessage());
			}
			return true;
		}

		if (printWriter != null) {
			printWriter.println("logout");
			printWriter.close();
		}
		return false;
	}

	protected Action getAction(String actionName) {
		return actionsList.get(actionName);
	}

	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	@Override
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

}
