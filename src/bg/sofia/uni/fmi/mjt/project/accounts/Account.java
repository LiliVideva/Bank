package bg.sofia.uni.fmi.mjt.project.accounts;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Locale;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountType;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;

class Account implements Cloneable, Comparable<Account>, Serializable {
	private static final long serialVersionUID = 1234567890L;
	private static final int MINIMUM_MONEY_IN_ACCOUNT = 20;

	private AccountType accountType;
	private String iban;
	private double money;
	private double accountInterestRate;
	private boolean isBlocked;

	Account(AccountType accountType, double money) {
		this.accountType = accountType;
		this.iban = generateIban();
		this.money = money;
		this.accountInterestRate = accountType.getTypeInterestRate();
		this.isBlocked = violatedMinimalAmountOfMoneyInAccount();
	}

	private String generateIban() {
		return Iban.random(CountryCode.BG).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(accountInterestRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		result = prime * result + (isBlocked ? 1231 : 1237);
		temp = Double.doubleToLongBits(money);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Account other = (Account) obj;
		return (((iban == null && other.iban == null) || iban.equals(other.iban))
				&& (Double.doubleToLongBits(accountInterestRate) == Double.doubleToLongBits(other.accountInterestRate))
				&& ((accountType == null && other.accountType == null) || accountType.equals(other.accountType)));
	}

	@Override
	public int compareTo(Account o) {
		return iban.toString().compareTo(o.iban.toString());
	}

	@Override
	public Account clone() {
		Account clonedAccount = new Account(this.accountType, this.money);
		clonedAccount.iban = iban;
		clonedAccount.isBlocked = isBlocked;
		return clonedAccount;
	}

	synchronized boolean takeMoney(double amount) throws ViolationException {
		if (!isBlocked) {
			if (validateAmountOfMoney(amount) && amount <= money) {
				money = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", money - amount));
				return true;
			}
			throw new ViolationException(String.format("Less than desired money in account: %s", money));
		}
		throw new ViolationException("Account blocked, because of violations!");
	}

	synchronized boolean receiveMoney(double amount) throws ViolationException {
		if (validateAmountOfMoney(amount)) {
			money = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", money + amount));

			if (!violatedMinimalAmountOfMoneyInAccount() && isBlocked) {
				isBlocked = false;
			}
			return true;
		}
		return false;
	}

	boolean violatedMinimalAmountOfMoneyInAccount() {
		if (money < MINIMUM_MONEY_IN_ACCOUNT) {
			isBlocked = true;
			return true;
		}
		return false;
	}

	synchronized void payFeeAndInterestRate() {
		money = (money - accountType.getPeriodServiceFee()) * (1 - accountInterestRate);
	}

	synchronized void setInterestRate(double rate) {
		accountInterestRate = rate;
	}

	void printAccountDetails(PrintWriter printWriter) {
		printWriter.printf("Type: %s%n", accountType.getType());
		printWriter.printf("Iban: %s%n", iban.toString());
		printWriter.printf("Interest rate: %.2f%n", accountInterestRate);
		printWriter.printf(Locale.ENGLISH, "Money: %.2f%n", money);
		if (isBlocked) {
			System.out.println("Blocked!");
		}
	}

	private boolean validateAmountOfMoney(double amount) throws ViolationException {
		if (amount > 0) {
			return true;
		}
		throw new ViolationException("Non-positive amount of money!");
	}

	AccountType getAccountType() {
		return accountType;
	}

	String getIban() {
		return iban;
	}

	double getMoney() {
		return money;
	}

	double getAccountInterestRate() {
		return accountInterestRate;
	}

	@Override
	public String toString() {
		return String.format("Account[Type: %s, IBAN: %s, Money: %.2f]", accountType, iban.toString(), money);
	}
}
