package bg.sofia.uni.fmi.mjt.project;

import java.util.Scanner;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountTypeFactory;
import bg.sofia.uni.fmi.mjt.project.actions.ActionFactory;
import bg.sofia.uni.fmi.mjt.project.actions.ClientActionFactory;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class BankClient {

	private UsersDatabase users;
	private AccountsDatabase accounts;

	public BankClient() {
		users = new UsersDatabase(DatabasesConstants.USERS_DATABASE);
		accounts = new AccountsDatabase(new AccountTypeFactory(), DatabasesConstants.ACCOUNTS_DATABASE);
	}

	public static void main(String[] args) {
		BankClient client = new BankClient();
		client.run();
	}

	private void run() {
		ActionFactory actionFactory = new ClientActionFactory(users, accounts);
		System.out.println("Welcome to the bank system! (To leave, write: exit)");

		boolean continueWorking = true;
		try (Scanner scanner = new Scanner(System.in)) {
			do {
				String line = scanner.nextLine();

				continueWorking = actionFactory.processAction(line);
			} while (continueWorking);
		} finally {
			System.out.println("Goodbye! See you soon!");
		}
	}

}
