package bg.sofia.uni.fmi.mjt.project.accounts.types;

import java.io.Serializable;

public class TraditionalIRA extends IndividualRetirementAccount implements Serializable {
	private static final long serialVersionUID = 77777777L;
	private static final String TYPE = "traditionalIRA";
	private static final String DESCRIPTION = "Contributions are tax-deductible.";
	private static final int CONTRIBUTION_LIMIT = 300;

	public TraditionalIRA() {
		super(TYPE, DESCRIPTION, CONTRIBUTION_LIMIT);
	}
}
