package bg.sofia.uni.fmi.mjt.project.accounts.types;

import java.io.Serializable;

public class CertificateOfDeposit extends AccountType implements Serializable {
	private static final long serialVersionUID = 11111111L;
	private static final String TYPE = "certificateOfDeposit";
	private static final String DESCRIPTION = "Invest your money at a set interest rate for a pre-set period of time.";
	private static final int MINIMUM_OPENING_DEPOSIT = 700;
	private static final int PERIOD_SERVICE_FEE = 3;
	private static final double INTEREST_RATE = 0.1;
	private static final int STANDARD_PERIOD_IN_YEARS = 5;

	private int periodInYears;

	public CertificateOfDeposit() {
		super(TYPE, DESCRIPTION, MINIMUM_OPENING_DEPOSIT, PERIOD_SERVICE_FEE, INTEREST_RATE);
		this.periodInYears = STANDARD_PERIOD_IN_YEARS;
	}

	public CertificateOfDeposit(int periodInYears) {
		super(TYPE, DESCRIPTION, MINIMUM_OPENING_DEPOSIT, PERIOD_SERVICE_FEE, INTEREST_RATE);
		this.periodInYears = periodInYears;
	}

	int getPeriodInYears() {
		return periodInYears;
	}

}
