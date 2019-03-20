package bg.sofia.uni.fmi.mjt.project.accounts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountType;
import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountTypeFactory;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchAccountException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;

public class AccountsDatabase implements Cloneable {
	private static final double RESERVE_PERCENTAGE = 0.05;
	private static final double SAVE_PERCENTAGE = 0.9;
	private static final int THREE = 3;
	private static final int FOUR = 4;

	private AccountTypeFactory accountTypeFactory;
	private Map<String, List<AccountProfile>> usersAccounts;
	private String database;

	public AccountsDatabase(AccountTypeFactory accountTypeFactory, String database) {
		this.accountTypeFactory = accountTypeFactory;
		this.database = database;
		this.usersAccounts = new HashMap<>();
		readFromDatabase();
	}

	@Override
	public AccountsDatabase clone() {
		AccountsDatabase clonedAccountsDatabase = new AccountsDatabase(this.accountTypeFactory, this.database);

		List<AccountProfile> clonedAccountProfiles = new ArrayList<>();
		for (String key : this.usersAccounts.keySet()) {
			this.usersAccounts.get(key).stream().forEach(x -> clonedAccountProfiles.add(x.clone()));
			clonedAccountsDatabase.usersAccounts.put(key, clonedAccountProfiles);
			clonedAccountProfiles.clear();
		}

		return clonedAccountsDatabase;
	}

	public void readFromDatabase() {
		usersAccounts = restoreAccountsFromDatabase();
	}

	@SuppressWarnings("unchecked")
	protected Map<String, List<AccountProfile>> restoreAccountsFromDatabase() {
		File accountsDatabase = new File(database);

		if (accountsDatabase.exists() && accountsDatabase.length() != 0) {
			try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(database))) {
				synchronized (database) {
					return (Map<String, List<AccountProfile>>) objectInputStream.readObject();
				}
			} catch (Exception e) {
				throw new RuntimeException(
						String.format("Problem reading from accounts database!%n%s", e.getMessage()));
			}
		}
		return new HashMap<>();
	}

	public void writeAccountsInDatabase() {
		setUpDatabase();

		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(database, false))) {
			synchronized (database) {
				objectOutputStream.writeObject(usersAccounts);
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format("Problem writing in accounts database!%n%s", e.getMessage()));
		}
	}

	private void setUpDatabase() {
		synchronized (database) {
			File resources = new File("resources");
			if (!resources.exists()) {
				resources.mkdir();
			}

			File usersDatabase = new File(database);
			if (!usersDatabase.exists()) {
				try {
					usersDatabase.createNewFile();
				} catch (IOException e) {
					throw new RuntimeException(
							String.format("Problem creating accounts database!%n%s", e.getMessage()));
				}
			}
		}
	}

	public synchronized boolean createAccount(List<String> arguments) throws InvalidFieldException, ViolationException {
		if (validateArgumentsListSize(arguments, THREE)) {
			String holder = arguments.get(0);
			AccountType type = accountTypeFactory.getAccountType(arguments.get(1));
			double money = Double.parseDouble(arguments.get(2));

			if (satisfyMinimumOpeningDeposit(type, money)) {
				Account account = new Account(type, money);
				List<AccountProfile> accountProfiles = usersAccounts.get(holder);
				accountProfiles = (accountProfiles == null) ? new ArrayList<>() : accountProfiles;
				accountProfiles.add(new AccountProfile(account));
				usersAccounts.put(holder, accountProfiles);
				return true;
			}
		}
		return false;
	}

	public boolean transferMoney(List<String> arguments)
			throws NoSuchUserException, InvalidFieldException, NoSuchAccountException, ViolationException {
		if (validateArgumentsListSize(arguments, FOUR)) {
			String sender = arguments.get(0);
			String senderAccountIban = arguments.get(1);
			String receiverAccountIban = arguments.get(2);
			double money = Double.parseDouble(arguments.get(THREE));

			if (validateIban(senderAccountIban) && validateIban(receiverAccountIban)
					&& !senderAccountIban.equals(receiverAccountIban)) {
				AccountProfile senderAccountProfile = findAccountProfileByHolderAndIban(sender, senderAccountIban);
				AccountProfile receiverAccountProfile = findAccountProfileByIban(receiverAccountIban);

				if (senderAccountProfile.takeMoney(money) && receiverAccountProfile.receiveMoney(money)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean cashInMoney(List<String> arguments)
			throws InvalidFieldException, NoSuchAccountException, ViolationException {
		if (validateArgumentsListSize(arguments, 2)) {
			double money = Double.parseDouble(arguments.get(0));
			String iban = arguments.get(1);

			if (validateIban(iban)) {
				AccountProfile accountProfile = findAccountProfileByIban(iban);

				return accountProfile.receiveMoney(money);
			}
		}
		return false;
	}

	public boolean withdrawMoney(List<String> arguments)
			throws NoSuchUserException, InvalidFieldException, NoSuchAccountException, ViolationException {
		if (validateArgumentsListSize(arguments, THREE)) {
			String holder = arguments.get(0);
			String iban = arguments.get(1);
			double money = Double.parseDouble(arguments.get(2));

			if (validateIban(iban)) {
				AccountProfile accountProfile = findAccountProfileByHolderAndIban(holder, iban);

				return accountProfile.takeMoney(money);
			}
		}
		return false;
	}

	public boolean displayAccounts(List<String> arguments, PrintWriter printWriter) throws NoSuchUserException {
		if (validateArgumentsListSize(arguments, 1)) {
			String holder = arguments.get(0);
			List<AccountProfile> accountProfiles = usersAccounts.get(holder);

			if (accountProfiles == null) {
				printWriter.println("No accounts!");
				return false;
			}

			accountProfiles.stream().sorted(AccountProfile::compareTo).map(AccountProfile::getAccount)
					.forEach(x -> x.printAccountDetails(printWriter));
			return true;
		}
		return false;
	}

	public void printViolationMessages(String holder, PrintWriter printWriter) {
		if (usersAccounts.get(holder) != null) {
			usersAccounts.get(holder).stream().forEach(accountProfile -> {
				if (!accountProfile.getViolationMessages().isEmpty()) {
					printWriter.printf("Account: %s%n", accountProfile.getIban());
					printWriter.println("Violation messages:");
					accountProfile.getViolationMessages().stream().forEach(message -> {
						printWriter.println(message);
					});
				}
			});
		}
	}

	public double calculateBestSavings(List<String> arguments) throws NoSuchAccountException {
		if (validateArgumentsListSize(arguments, 1)) {
			String holder = arguments.get(0);
			List<AccountProfile> accountProfiles = usersAccounts.get(holder);

			if (accountProfiles == null) {
				throw new NoSuchAccountException("No accounts to make calculations on!");
			}

			double incomes = accountProfiles.stream()
					.filter(x -> !x.getAccountType().getType().equals("savingsAccount"))
					.mapToDouble(AccountProfile::getIncomes).sum();
			double outcomes = accountProfiles.stream()
					.filter(x -> !x.getAccountType().getType().equals("savingsAccount"))
					.mapToDouble(AccountProfile::getOutcomes).sum();
			double currentMoney = accountProfiles.stream()
					.filter(x -> !x.getAccountType().getType().equals("savingsAccount"))
					.mapToDouble(AccountProfile::getMoney).sum();
			double accountsFees = accountProfiles.stream().map(AccountProfile::getAccountType)
					.mapToInt(AccountType::getPeriodServiceFee).sum();

			if (incomes > outcomes) {
				double percentageSpent = Double
						.parseDouble(String.format(Locale.ENGLISH, "%.2f", (outcomes / incomes)));
				double spareMoneyWithBankPayments = (1 - percentageSpent - RESERVE_PERCENTAGE) * incomes + currentMoney;
				double moneyCanSave = (spareMoneyWithBankPayments - accountsFees) * SAVE_PERCENTAGE;
				return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", moneyCanSave));
			}

		}
		return 0;
	}

	public void movePeriod(List<String> arguments) {
		usersAccounts.entrySet().stream().forEach(userAccounts -> userAccounts.getValue().stream()
				.forEach(AccountProfile -> AccountProfile.movePeriod()));
	}

	public boolean setAccountInterestRate(List<String> arguments) throws NoSuchAccountException, NoSuchUserException {
		if (validateArgumentsListSize(arguments, 2)) {
			String iban = arguments.get(0);
			double interestRate = Double.parseDouble(arguments.get(1));

			AccountProfile accountProfile = findAccountProfileByIban(iban);
			accountProfile.setInterestRate(interestRate);
			return true;
		}
		return false;
	}

	private boolean validateArgumentsListSize(List<String> arguments, int expectedSize) {
		return expectedSize == arguments.size();
	}

	private boolean validateIban(String iban) throws InvalidFieldException {
		if (iban.matches("^BG\\d{2}[A-Z]{4}\\d{6}[0-9A-Z]{8}$")) {
			return true;
		}
		throw new InvalidFieldException(String.format("Invalid IBAN format: %s", iban));
	}

	private boolean satisfyMinimumOpeningDeposit(AccountType type, double money)
			throws ViolationException, InvalidFieldException {
		if (type == null) {
			throw new InvalidFieldException("No such account type!");
		}

		int minimumOpeningDeposit = type.getMinimumOpeningDeposit();

		if (money >= minimumOpeningDeposit) {
			return true;
		}
		throw new ViolationException(
				String.format("Less money than the minimum opening deposit required: %s", minimumOpeningDeposit));
	}

	private AccountProfile findAccountProfileByHolderAndIban(String holder, String iban)
			throws NoSuchUserException, NoSuchAccountException {
		List<AccountProfile> profiles = usersAccounts.get(holder);
		if (profiles == null) {
			throw new NoSuchUserException(String.format("No such holder: %s", holder));
		}

		try {
			return usersAccounts.get(holder).stream().filter(accountProfile -> iban.equals(accountProfile.getIban()))
					.findFirst().get();
		} catch (NoSuchElementException e) {
			throw new NoSuchAccountException(String.format("Holder with no such account: %s", iban));
		}
	}

	private AccountProfile findAccountProfileByIban(String iban) throws NoSuchAccountException {
		for (List<AccountProfile> accountProfiles : usersAccounts.values()) {
			for (AccountProfile accountProfile : accountProfiles) {
				if (iban.equals(accountProfile.getIban())) {
					return accountProfile;
				}
			}
		}
		throw new NoSuchAccountException(String.format("No such account: %s", iban));
	}
}
