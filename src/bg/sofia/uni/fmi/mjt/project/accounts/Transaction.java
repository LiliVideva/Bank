package bg.sofia.uni.fmi.mjt.project.accounts;

import java.io.Serializable;
import java.util.Date;

class Transaction implements Cloneable, Serializable {
	private static final long serialVersionUID = 1236547890L;

	private Date date;
	private double money;

	Transaction(double money) {
		this.date = new Date();
		this.money = money;
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
	protected Transaction clone() {
		Transaction clonedTransaction = new Transaction(this.money);
		clonedTransaction.date = this.date;
		return clonedTransaction;
	}

	Date getDate() {
		return date;
	}

	double getMoney() {
		return money;
	}

	@Override
	public String toString() {
		return String.format("Transaction[Date: %s, Money: %.2f]", date, money);
	}
}
