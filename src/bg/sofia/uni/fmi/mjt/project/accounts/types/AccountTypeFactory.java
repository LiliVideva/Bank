package bg.sofia.uni.fmi.mjt.project.accounts.types;

import java.util.HashMap;
import java.util.Map;

public class AccountTypeFactory {
	private Map<String, AccountType> accountTypesList;

	public AccountTypeFactory() {
		this.accountTypesList = new HashMap<>();
		setAccountTypes();
	}

	private void setAccountTypes() {
		accountTypesList.put(AccountTypesContstants.CHECKING_ACCOUNT, new CheckingAccount());
		accountTypesList.put(AccountTypesContstants.SAVINGS_ACCOUNT, new SavingsAccount());
		accountTypesList.put(AccountTypesContstants.CERTIFICATE_OF_DEPOSIT, new CertificateOfDeposit());
		accountTypesList.put(AccountTypesContstants.MONEY_MARKET_ACCOUNT, new MoneyMarketAccount());
		accountTypesList.put(AccountTypesContstants.TRADITIONAL_IRA, new TraditionalIRA());
		accountTypesList.put(AccountTypesContstants.ROTH_IRA, new RothIRA());
	}

	public AccountType getAccountType(String accountType) {
		return accountTypesList.get(accountType);
	}

	public void printAccountTypesList() {
		accountTypesList.entrySet().stream().forEach(x -> {
			System.out.printf("Type: %s%n Description: %s", x.getKey(), x.getValue().getDescription());
		});
	}

}
