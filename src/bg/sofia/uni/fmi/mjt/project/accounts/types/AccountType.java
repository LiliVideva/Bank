package bg.sofia.uni.fmi.mjt.project.accounts.types;

import java.io.Serializable;

public abstract class AccountType implements Serializable {
	private static final long serialVersionUID = 1592603748L;

	private String type;
	private String description;
	private int minimumOpeningDeposit;
	private int periodServiceFee;
	private double interestRate;

	AccountType(String type, String description, int minimumOpeningDeposit, int periodServiceFee, double interestRate) {
		this.type = type;
		this.description = description;
		this.minimumOpeningDeposit = minimumOpeningDeposit;
		this.periodServiceFee = periodServiceFee;
		this.interestRate = interestRate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		long temp;
		temp = Double.doubleToLongBits(interestRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + minimumOpeningDeposit;
		result = prime * result + periodServiceFee;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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

		AccountType other = (AccountType) obj;
		return (((description == null && other.description == null) || description.equals(other.description))
				&& (Double.doubleToLongBits(interestRate) == Double.doubleToLongBits(other.interestRate))
				&& (minimumOpeningDeposit == other.minimumOpeningDeposit)
				&& (periodServiceFee == other.periodServiceFee)
				&& ((type == null && other.type == null) || type.equals(other.type)));
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public int getMinimumOpeningDeposit() {
		return minimumOpeningDeposit;
	}

	public int getPeriodServiceFee() {
		return periodServiceFee;
	}

	public double getTypeInterestRate() {
		return interestRate;
	}

	@Override
	public String toString() {
		return String.format(
				"AccountType[Name: %s, Descrition: %s, Minimum opening deposit: %d,"
						+ " Period service fee: %d, Interest rate: %.2f]",
				type, description, minimumOpeningDeposit, periodServiceFee, interestRate);
	}
}
