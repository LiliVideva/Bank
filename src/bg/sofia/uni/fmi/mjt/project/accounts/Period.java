package bg.sofia.uni.fmi.mjt.project.accounts;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Period implements Cloneable, Serializable {
	private static final long serialVersionUID = 2468013579L;

	private double moneyAtTheStartOfPeriod;
	private Map<String, List<Transaction>> accountOperations;

	Period(double moneyAtTheStartOfPeriod) {
		this.moneyAtTheStartOfPeriod = moneyAtTheStartOfPeriod;
		accountOperations = new HashMap<>();
		accountOperations.put("income", new LinkedList<>());
		accountOperations.put("outcome", new LinkedList<>());
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public Period clone() {
		Period clonedPeriod = new Period(this.moneyAtTheStartOfPeriod);

		List<Transaction> clonedTransactions = new ArrayList<>();
		for (String key : this.accountOperations.keySet()) {
			this.accountOperations.get(key).stream().forEach(x -> clonedTransactions.add(x.clone()));
			clonedPeriod.accountOperations.put(key, clonedTransactions);
			clonedTransactions.clear();
		}
		return clonedPeriod;
	}

	void addTransaction(String transactionType, double money) {
		List<Transaction> transactions = accountOperations.remove(transactionType);
		transactions.add(new Transaction(money));
		accountOperations.put(transactionType, transactions);
	}

	void printPeriodHistory(PrintWriter printWriter) {
		printWriter.printf("Money at the start: %s%n", moneyAtTheStartOfPeriod);
		accountOperations.entrySet().stream().forEach(operation -> {
			printWriter.println(operation.getKey());
			operation.getValue().stream().forEach(transaction -> {
				printWriter.printf("Date of transaction: %s Money: %s%n", transaction.getDate().toString(),
						transaction.getMoney());
			});
		});
	}

	double getMoneyAtTheStartOfPeriod() {
		return moneyAtTheStartOfPeriod;
	}

	Map<String, List<Transaction>> getAccountOperations() {
		return accountOperations;
	}
}
