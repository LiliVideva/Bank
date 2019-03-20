package bg.sofia.uni.fmi.mjt.project.accounts.types;

import java.io.Serializable;

public class SavingsAccount extends AccountType implements Serializable {
	private static final long serialVersionUID = 66666666L;
	private static final String TYPE = "savingsAccount";
	private static final String DESCRPTION = "Accumulate interest on funds you have saved for future needs.";
	private static final int MINIMUM_OPENING_DEPOSIT = 150;
	private static final int PERIOD_SERVICE_FEE = 2;
	private static final double INTEREST_RATE = 0.05;

	public SavingsAccount() {
		super(TYPE, DESCRPTION, MINIMUM_OPENING_DEPOSIT, PERIOD_SERVICE_FEE, INTEREST_RATE);
	}
}
