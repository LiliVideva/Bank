package bg.sofia.uni.fmi.mjt.project.accounts;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountType;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;

class AccountProfile implements Cloneable, Comparable<AccountProfile>, Serializable {
	private static final long serialVersionUID = 1122334455L;

	private Account account;
	private List<String> violationMessages;
	private Queue<Period> accountHistory;

	AccountProfile(Account account) {
		this.account = account;
		this.violationMessages = new ArrayList<>();
		controlViolatedMinimalAmountOfMoneyInAccountMessage();
		accountHistory = new LinkedList<>();
		startNewPeriod();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result + ((violationMessages == null) ? 0 : violationMessages.hashCode());
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

		AccountProfile other = (AccountProfile) obj;
		return (((account == null && other.account == null) || account.equals(other.account))
				&& ((violationMessages == null && other.violationMessages == null)
						|| violationMessages.equals(other.violationMessages)));
	}

	@Override
	public int compareTo(AccountProfile o) {
		return account.compareTo(o.account);
	}

	@Override
	public AccountProfile clone() {
		AccountProfile clonedAccountProfile = new AccountProfile(this.account.clone());
		this.accountHistory.stream().forEach(x -> clonedAccountProfile.accountHistory.add(x.clone()));
		this.violationMessages.stream().forEach(x -> clonedAccountProfile.violationMessages.add(x));

		return clonedAccountProfile;
	}

	boolean takeMoney(double amount) throws ViolationException {
		if (account.takeMoney(amount)) {
			controlViolatedMinimalAmountOfMoneyInAccountMessage();
			addTransaction("outcome", amount);
			return true;
		}
		return false;
	}

	boolean receiveMoney(double amount) throws ViolationException {
		if (account.receiveMoney(amount)) {
			controlViolatedMinimalAmountOfMoneyInAccountMessage();
			addTransaction("income", amount);
			return true;
		}
		return false;
	}

	double getIncomes() {
		Period lastFullPeriod = getLastFullPeriod();
		return lastFullPeriod.getAccountOperations().get("income").stream().mapToDouble(Transaction::getMoney).sum();
	}

	double getOutcomes() {
		Period lastFullPeriod = getLastFullPeriod();
		return lastFullPeriod.getAccountOperations().get("outcome").stream().mapToDouble(Transaction::getMoney).sum();
	}

	void setInterestRate(double rate) {
		account.setInterestRate(rate);
	}

	void movePeriod() {
		account.payFeeAndInterestRate();
		startNewPeriod();
	}

	void printAccountDetails(PrintWriter printWriter) {
		account.printAccountDetails(printWriter);
	}

	void printLastPeriodHistory(PrintWriter printWriter) {
		accountHistory.peek().printPeriodHistory(printWriter);
	}

	private void startNewPeriod() {
		accountHistory.add(new Period(account.getMoney()));
	}

	private synchronized void controlViolatedMinimalAmountOfMoneyInAccountMessage() {
		if (account.violatedMinimalAmountOfMoneyInAccount()) {
			violationMessages.add("Less than minimal amount of money in account!");
		} else {
			if (!violationMessages.isEmpty()) {
				violationMessages.remove(0);
			}
		}
	}

	private synchronized void addTransaction(String transactionType, double money) {
		Period currentPeriod = accountHistory.poll();
		currentPeriod.addTransaction(transactionType, money);
		accountHistory.add(currentPeriod);
	}

	private synchronized Period getLastFullPeriod() {
		Period currentPeriod = accountHistory.poll();

		if (!accountHistory.isEmpty()) {
			Period lastPeriod = accountHistory.peek();
			accountHistory.add(currentPeriod);
			return lastPeriod;
		}
		accountHistory.add(currentPeriod);
		return currentPeriod;
	}

	Account getAccount() {
		return account;
	}

	List<String> getViolationMessages() {
		return violationMessages;
	}

	AccountType getAccountType() {
		return account.getAccountType();
	}

	String getIban() {
		return account.getIban();
	}

	double getMoney() {
		return account.getMoney();
	}
}
