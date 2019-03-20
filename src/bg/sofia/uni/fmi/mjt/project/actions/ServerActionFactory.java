package bg.sofia.uni.fmi.mjt.project.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.project.BankServer;
import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.actions.account.CalculateBestSavingsAction;
import bg.sofia.uni.fmi.mjt.project.actions.account.CashInMoneyAction;
import bg.sofia.uni.fmi.mjt.project.actions.account.CreateAccountAction;
import bg.sofia.uni.fmi.mjt.project.actions.account.DisplayAccountsAction;
import bg.sofia.uni.fmi.mjt.project.actions.account.MovePeriodAction;
import bg.sofia.uni.fmi.mjt.project.actions.account.SetAccountInterestRateAction;
import bg.sofia.uni.fmi.mjt.project.actions.account.TransferMoneyAction;
import bg.sofia.uni.fmi.mjt.project.actions.account.WithdrawMoneyAction;
import bg.sofia.uni.fmi.mjt.project.actions.user.DeleteUserAction;
import bg.sofia.uni.fmi.mjt.project.actions.user.GetUserAction;
import bg.sofia.uni.fmi.mjt.project.actions.user.LogoutAction;
import bg.sofia.uni.fmi.mjt.project.actions.user.ReadMessagesAction;
import bg.sofia.uni.fmi.mjt.project.actions.user.SendMessageAction;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchAccountException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class ServerActionFactory implements ActionFactory {
	private UserProfile userProfile;
	private PrintWriter printWriter;
	private UsersDatabase users;
	private AccountsDatabase accounts;
	private Map<String, Action> actionsList;

	public ServerActionFactory(UsersDatabase users, AccountsDatabase accounts) {
		this.userProfile = null;
		this.actionsList = new HashMap<>();
		this.users = users;
		this.accounts = accounts;
	}

	private void setActionList() {
		printWriter = createPrintWriter();

		actionsList.put(ActionsConstants.DELETE_PROFILE, new DeleteUserAction(userProfile));
		actionsList.put(ActionsConstants.GET_USER, new GetUserAction(userProfile, printWriter));
		actionsList.put(ActionsConstants.SEND_MESSAGE, new SendMessageAction(userProfile, printWriter));
		actionsList.put(ActionsConstants.READ_NEW_MESSAGES, new ReadMessagesAction(userProfile, printWriter));
		actionsList.put(ActionsConstants.LOGOUT, new LogoutAction(userProfile));

		actionsList.put(ActionsConstants.CREATE_ACCOUNT, new CreateAccountAction(userProfile));
		actionsList.put(ActionsConstants.LIST_ACCOUNTS, new DisplayAccountsAction(userProfile, printWriter));
		actionsList.put(ActionsConstants.TRANSFER_MONEY, new TransferMoneyAction(userProfile));
		actionsList.put(ActionsConstants.CASH_IN, new CashInMoneyAction(userProfile));
		actionsList.put(ActionsConstants.WITHDRAW, new WithdrawMoneyAction(userProfile));

		actionsList.put(ActionsConstants.SET_ACCOUNT_RATE, new SetAccountInterestRateAction(userProfile, printWriter));
		actionsList.put(ActionsConstants.CALCULATE_BEST_SAVINGS,
				new CalculateBestSavingsAction(userProfile, printWriter));
		actionsList.put(ActionsConstants.MOVE_PERIOD, new MovePeriodAction(userProfile));
	}

	protected PrintWriter createPrintWriter() {
		try {
			return new PrintWriter(BankServer.getUserSocket(userProfile.getUsername()).getOutputStream(), true);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Problem creating print writer!%n%s", e.getMessage()));
		}
	}

	@Override
	public boolean processAction(String line) {
		String[] splittedLine = line.split(" ", 2);
		String actionName = splittedLine[0];
		String lineArguments = (splittedLine.length == 2) ? splittedLine[1] : null;

		try {
			Action action = getAction(actionName);
			if (action == null) {
				printWriter.println("No such command to be executed!");
				return false;
			}
			users.readFromDatabase();
			accounts.readFromDatabase();

			accounts.printViolationMessages(userProfile.getUsername(), printWriter);

			if (action.executeAction(lineArguments, userProfile, users, accounts)) {
				this.userProfile = action.getUserProfile();

				users.writeProfilesInDatabase();
				accounts.writeAccountsInDatabase();
				printWriter.println("Action executed successfully!");
				return true;
			}

			printWriter.println("Action failed! [Hint: Check number of arguments]");
		} catch (InvalidFieldException | NoSuchUserException | NotLoggedException | UserAlreadyLoggedException
				| ViolationException | NoSuchAccountException e) {
			printWriter.printf("Action failed!%n%s", e.getMessage());
		}

		return true;
	}

	protected Action getAction(String actionName) {
		return actionsList.get(actionName);
	}

	@Override
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;

		setActionList();
	}
}
