package bg.sofia.uni.fmi.mjt.project.accounts.types;

import java.io.Serializable;

public abstract class IndividualRetirementAccount extends AccountType implements Serializable {
	private static final long serialVersionUID = 33333333L;
	private static final String DESCRIPTION = "Save independently for your retirement.";
	private static final int MINIMUM_OPENING_DEPOSIT = 200;
	private static final int PERIOD_SERVICE_FEE = 2;
	private static final double INTEREST_RATE = 0.1;

	private int contributionLimit;

	protected IndividualRetirementAccount(String type, String description, int contributionLimit) {
		super(type, String.format("%s %s", DESCRIPTION, description), MINIMUM_OPENING_DEPOSIT, PERIOD_SERVICE_FEE,
				INTEREST_RATE);
		this.contributionLimit = contributionLimit;
	}

	int getContributionLimit() {
		return contributionLimit;
	}

}
