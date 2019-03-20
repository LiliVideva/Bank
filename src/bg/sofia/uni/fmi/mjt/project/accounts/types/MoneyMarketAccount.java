package bg.sofia.uni.fmi.mjt.project.accounts.types;

import java.io.Serializable;

public class MoneyMarketAccount extends AccountType implements Serializable {
	private static final long serialVersionUID = 44444444L;
	private static final String TYPE = "moneyMarketAccount";
	private static final String DESCRIPTION = "Accumulate interest on funds you have "
			+ "saved for future needs by maintaining a higher balance to avoid a monthly service fee.";
	private static final int MINIMUM_OPENING_DEPOSIT = 250;
	private static final int PERIOD_SERVICE_FEE = 0;
	private static final double INTEREST_RATE = 0.1;

	public MoneyMarketAccount() {
		super(TYPE, DESCRIPTION, MINIMUM_OPENING_DEPOSIT, PERIOD_SERVICE_FEE, INTEREST_RATE);
	}

}
