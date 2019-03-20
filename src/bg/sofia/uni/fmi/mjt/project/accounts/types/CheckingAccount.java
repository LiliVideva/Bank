package bg.sofia.uni.fmi.mjt.project.accounts.types;

import java.io.Serializable;

public class CheckingAccount extends AccountType implements Serializable {
	private static final long serialVersionUID = 22222222L;
	private static final String TYPE = "checkingAccount";
	private static final String DESCRPTION = "Secure and easy access to your money for your daily transactional needs.";
	private static final int MINIMUM_OPENING_DEPOSIT = 100;
	private static final int PERIOD_SERVICE_FEE = 1;
	private static final double INTEREST_RATE = 0.1;

	public CheckingAccount() {
		super(TYPE, DESCRPTION, MINIMUM_OPENING_DEPOSIT, PERIOD_SERVICE_FEE, INTEREST_RATE);
	}
}