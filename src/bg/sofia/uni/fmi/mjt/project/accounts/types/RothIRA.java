package bg.sofia.uni.fmi.mjt.project.accounts.types;

import java.io.Serializable;

public class RothIRA extends IndividualRetirementAccount implements Serializable {
	private static final long serialVersionUID = 55555555L;
	private static final String TYPE = "rothIRA";
	private static final String DESCRIPTION = "Funds can be withdrawn tax-free in many situations.";
	private static final int CONTRIBUTION_LIMIT = 400;

	public RothIRA() {
		super(TYPE, DESCRIPTION, CONTRIBUTION_LIMIT);

	}
}