package bg.sofia.uni.fmi.mjt.project.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountType;
import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountTypeFactory;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchAccountException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;

public class AccountsDatabaseTest {
	private static final double PRECISION = 0.00001;
	private static final int FEE = 5;
	private static final double SMALL_POSITIVE_AMOUNT = 10;
	private static final int MEDIUM_POSITIVE_AMOUNT = 200;
	private static final double INITIAL_AMOUNT = 500.0;
	private static final double LOW_SAVINGS = 453.6;
	private static final double HIGH_SAVINGS = 907.2;
	private static final double INCOMES = 20.0;
	private static final double OUTCOMES = 30.0;

	private static AccountType type;
	private static AccountProfile accountProfileOne;
	private static AccountProfile accountProfileTwo;
	private static AccountTypeFactory accountTypeFactory;
	private static AccountsDatabase accounts;
	private static PrintWriter printWriter;

	@BeforeClass
	public static void setUpAll() {
		type = mock(AccountType.class);
		accountProfileOne = mock(AccountProfile.class);
		accountProfileTwo = mock(AccountProfile.class);
		accountTypeFactory = mock(AccountTypeFactory.class);
		accounts = new AccountsDatabase(accountTypeFactory, "resources/accounts.txt") {
			@Override
			protected synchronized Map<String, List<AccountProfile>> restoreAccountsFromDatabase() {
				Map<String, List<AccountProfile>> usersAccounts = new HashMap<>();
				usersAccounts.put("user", Arrays.asList(accountProfileOne, accountProfileTwo));
				return usersAccounts;
			}
		};
		printWriter = mock(PrintWriter.class);
	}

	@Test(expected = InvalidFieldException.class)
	public void testCreateAccountThrowsInvalidFieldExceptionWhenGivenInvalidAccountType()
			throws InvalidFieldException, ViolationException {
		accounts.createAccount(Arrays.asList("newUser", "childAccount", "300"));
	}

	@Test(expected = ViolationException.class)
	public void testCreateAccountThrowsViolationExceptionWhenGivenAmountOfMoneyIsLessThanMinimumDeposit()
			throws InvalidFieldException, ViolationException {
		when(accountTypeFactory.getAccountType("savingsAccount")).thenReturn(type);
		when(type.getMinimumOpeningDeposit()).thenReturn(MEDIUM_POSITIVE_AMOUNT);

		accounts.createAccount(Arrays.asList("newUser", "savingsAccount", "50"));
	}

	@Test
	public void testCreateAccountReturnsFalseWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, ViolationException {
		assertFalse(accounts.createAccount(Arrays.asList("newUser", "savingsAccount")));
	}

	@Test
	public void testCreateAccountReturnsTrueWhenGivenCorrectData() throws InvalidFieldException, ViolationException {
		when(accountTypeFactory.getAccountType("savingsAccount")).thenReturn(type);
		when(type.getMinimumOpeningDeposit()).thenReturn(MEDIUM_POSITIVE_AMOUNT);

		assertTrue(accounts.createAccount(Arrays.asList("newUser", "savingsAccount", "250")));
	}

	@Test(expected = InvalidFieldException.class)
	public void testTransferMoneyThrowsInvalidFieldExceptionWhenGivenInvalidIban()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.transferMoney(Arrays.asList("user", "BG11UUUU22223333444455", "BGAAUUUU22223333444455", "300"));
	}

	@Test(expected = NoSuchUserException.class)
	public void testTransferMoneyThrowsNoSuchUserExceptionWhenGivenInvalidSenderUsername()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.transferMoney(Arrays.asList("dummyUser", "BG11UUUU22223333444455", "BG11UUWW22223333444455", "300"));
	}

	@Test(expected = NoSuchAccountException.class)
	public void testTransferMoneyThrowsNoSuchAccountExceptionWhenGivenSenderIbanNotBelongToSender()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.transferMoney(Arrays.asList("user", "BG11AAAA22223333444455", "BG11UUWW22223333444455", "300"));
	}

	@Test(expected = NoSuchAccountException.class)
	public void testTransferMoneyThrowsNoSuchAccountExceptionWhenGivenReceiverIbanNotExist()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.transferMoney(Arrays.asList("user", "BG11UUUU22223333444455", "BG11AAAA22223333444455", "300"));
	}

	@Test
	public void testTransferMoneyReturnsFalseWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		assertFalse(accounts.transferMoney(Arrays.asList("user", "BG11UUUU22223333444455", "BG11UUWW22223333444455")));
	}

	@Test
	public void testTransferMoneyReturnsFalseWhenGivenSenderAndReceiverIbansTheSame()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		assertFalse(accounts
				.transferMoney(Arrays.asList("user", "BG11UAUA22223333444455", "BG11UAUA22223333444455", "10")));
	}

	@Test
	public void testTransferMoneyReturnsFalseWhenTakeMoneySucceededAndReceiveMoneyFailed()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		when(accountProfileOne.takeMoney(SMALL_POSITIVE_AMOUNT)).thenReturn(true);
		when(accountProfileOne.getIban()).thenReturn("BG11UUAA22223333444455");
		when(accountProfileTwo.receiveMoney(SMALL_POSITIVE_AMOUNT)).thenReturn(false);
		when(accountProfileTwo.getIban()).thenReturn("BG11LLLL22223333444455");

		assertFalse(accounts
				.transferMoney(Arrays.asList("user", "BG11UUAA22223333444455", "BG11LLLL22223333444455", "10")));
	}

	@Test
	public void testTransferMoneyReturnsFalseWhenTakeMoneyFailedAndReceiveMoneySucceeded()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		when(accountProfileOne.takeMoney(SMALL_POSITIVE_AMOUNT)).thenReturn(false);
		when(accountProfileOne.getIban()).thenReturn("BG11UUAA22223333444455");
		when(accountProfileTwo.receiveMoney(SMALL_POSITIVE_AMOUNT)).thenReturn(true);
		when(accountProfileTwo.getIban()).thenReturn("BG11LLLL22223333444455");

		assertFalse(accounts
				.transferMoney(Arrays.asList("user", "BG11UUAA22223333444455", "BG11LLLL22223333444455", "10")));
	}

	@Test
	public void testTransferMoneyReturnsTrueWhenGivenCorrectData()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		when(accountProfileOne.takeMoney(SMALL_POSITIVE_AMOUNT)).thenReturn(true);
		when(accountProfileOne.getIban()).thenReturn("BG11UUAA22223333444455");
		when(accountProfileTwo.receiveMoney(SMALL_POSITIVE_AMOUNT)).thenReturn(true);
		when(accountProfileTwo.getIban()).thenReturn("BG11LLLL22223333444455");

		assertTrue(accounts
				.transferMoney(Arrays.asList("user", "BG11UUAA22223333444455", "BG11LLLL22223333444455", "10")));
	}

	@Test(expected = InvalidFieldException.class)
	public void testCashInMoneyThrowsInvalidFieldExceptionWhenGivenInvalidIban()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.cashInMoney(Arrays.asList("300", "BGAAUUUU22223333444455"));
	}

	@Test(expected = NoSuchAccountException.class)
	public void testCashInMoneyThrowsNoSuchAccountExceptionWhenGivenIbanNotExist()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.cashInMoney(Arrays.asList("300", "BG11AAAA22223333444455"));
	}

	@Test
	public void testCashInMoneyReturnsFalseWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		assertFalse(accounts.cashInMoney(Arrays.asList("300")));
	}

	@Test
	public void testCashInMoneyReturnsTrueWhenGivenCorrectData()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		when(accountProfileOne.getIban()).thenReturn("BG11LLLL22223333444455");
		when(accountProfileOne.receiveMoney(MEDIUM_POSITIVE_AMOUNT)).thenReturn(true);
		assertTrue(accounts.cashInMoney(Arrays.asList("200", "BG11LLLL22223333444455")));
	}

	@Test(expected = InvalidFieldException.class)
	public void testWithdrawMoneyThrowsInvalidFieldExceptionWhenGivenInvalidIban()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.withdrawMoney(Arrays.asList("user", "BGAAUUUU22223333444455", "300"));
	}

	@Test(expected = NoSuchUserException.class)
	public void testWithdrawMoneyThrowsNoSuchUserExceptionWhenGivenInvalidHolderUsername()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.withdrawMoney(Arrays.asList("dummyUser", "BG11UUUU22223333444455", "300"));
	}

	@Test(expected = NoSuchAccountException.class)
	public void testWithdrawMoneyThrowsNoSuchAccountExceptionWhenGivenIbanNotExist()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.withdrawMoney(Arrays.asList("user", "BG11AAAA22223333444455", "300"));
	}

	@Test(expected = NoSuchAccountException.class)
	public void testWithdrawMoneyThrowsNoSuchAccountExceptionWhenGivenIbanNotBelongToHolder()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.withdrawMoney(Arrays.asList("user", "BG11LLLL22223333AAAA55", "300"));
	}

	@Test
	public void testWithdrawMoneyReturnsFalseWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		assertFalse(accounts.withdrawMoney(Arrays.asList("user", "BG11UUUU22223333444455")));
	}

	@Test
	public void testWithdrawMoneyReturnsTrueWhenGivenCorrectData()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		when(accountProfileOne.takeMoney(SMALL_POSITIVE_AMOUNT)).thenReturn(true);
		when(accountProfileOne.getIban()).thenReturn("BG11UUAA22223333444455");

		assertTrue(accounts.withdrawMoney(Arrays.asList("user", "BG11UUAA22223333444455", "10")));
	}

	@Test
	public void testDisplayAccountsReturnsFalseWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		assertFalse(accounts.displayAccounts(new ArrayList<>(), printWriter));
	}

	@Test
	public void testDisplayAccountsReturnsFalseWhenGivenNoAccountsBelongingToTheUser()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		assertFalse(accounts.displayAccounts(Arrays.asList("someUser"), printWriter));
	}

	@Test
	public void testDisplayAccountsReturnsTrueWhenGivenCorrectData()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		Account account = new Account(type, MEDIUM_POSITIVE_AMOUNT);
		when(accountProfileOne.getAccount()).thenReturn(account);
		when(accountProfileTwo.getAccount()).thenReturn(account);
		assertTrue(accounts.displayAccounts(Arrays.asList("user"), printWriter));
	}

	@Test(expected = NoSuchAccountException.class)
	public void testCalculateBestSavingsThrowsNoSuchAccountExceptionWhenGivenNoAccountsBelongingToTheUser()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		accounts.calculateBestSavings(Arrays.asList("someUser"));
	}

	@Test
	public void testCalculateBestSavingsReturnsZeroWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		assertEquals(0, accounts.calculateBestSavings(new ArrayList<>()), PRECISION);
	}

	@Test
	public void testCalculateBestSavingsReturnsZeroWhenGivenIncomesAreLessThanOutcomes()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		when(accountProfileOne.getAccountType()).thenReturn(type);
		when(accountProfileTwo.getAccountType()).thenReturn(type);
		when(type.getType()).thenReturn("checkingAccount");
		when(accountProfileOne.getIncomes()).thenReturn(INCOMES);
		when(accountProfileTwo.getIncomes()).thenReturn(INCOMES);
		when(accountProfileOne.getOutcomes()).thenReturn(OUTCOMES);
		when(accountProfileTwo.getOutcomes()).thenReturn(OUTCOMES);
		when(accountProfileOne.getMoney()).thenReturn(INITIAL_AMOUNT);
		when(accountProfileTwo.getMoney()).thenReturn(INITIAL_AMOUNT);
		when(type.getPeriodServiceFee()).thenReturn(FEE);
		assertEquals(0, accounts.calculateBestSavings(Arrays.asList("user")), PRECISION);
	}

	@Test
	public void testCalculateBestSavingsReturnsDoubleValueWhenGivenIncomesAreMoreThanOutcomesAndSomeSavingsAccounts()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		when(accountProfileOne.getAccountType()).thenReturn(type);
		when(type.getType()).thenReturn("checkingAccount");
		AccountType savingsAccountType = mock(AccountType.class);
		when(accountProfileTwo.getAccountType()).thenReturn(savingsAccountType);
		when(savingsAccountType.getType()).thenReturn("savingsAccount");
		when(accountProfileOne.getIncomes()).thenReturn(INCOMES);
		when(accountProfileOne.getOutcomes()).thenReturn(SMALL_POSITIVE_AMOUNT);
		when(accountProfileOne.getMoney()).thenReturn(INITIAL_AMOUNT);
		when(type.getPeriodServiceFee()).thenReturn(FEE);
		assertEquals(LOW_SAVINGS, accounts.calculateBestSavings(Arrays.asList("user")), PRECISION);
	}

	@Test
	public void testCalculateBestSavingsReturnsDoubleValueWhenGivenIncomesAreMoreThanOutcomesAndNoSavingsAccounts()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		when(accountProfileOne.getAccountType()).thenReturn(type);
		when(accountProfileTwo.getAccountType()).thenReturn(type);
		when(type.getType()).thenReturn("checkingAccount");
		when(accountProfileOne.getIncomes()).thenReturn(INCOMES);
		when(accountProfileTwo.getIncomes()).thenReturn(INCOMES);
		when(accountProfileOne.getOutcomes()).thenReturn(SMALL_POSITIVE_AMOUNT);
		when(accountProfileTwo.getOutcomes()).thenReturn(SMALL_POSITIVE_AMOUNT);
		when(accountProfileOne.getMoney()).thenReturn(INITIAL_AMOUNT);
		when(accountProfileTwo.getMoney()).thenReturn(INITIAL_AMOUNT);
		when(type.getPeriodServiceFee()).thenReturn(FEE);
		assertEquals(HIGH_SAVINGS, accounts.calculateBestSavings(Arrays.asList("user")), PRECISION);
	}

	@Test
	public void testSetAccountInterestRateReturnsFalseWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		assertFalse(accounts.setAccountInterestRate(Arrays.asList("BG11LLLL22223333444455")));
	}

	@Test
	public void testSetAccountInterestRateReturnsTrueWhenGivenCorrectData()
			throws InvalidFieldException, ViolationException, NoSuchUserException, NoSuchAccountException {
		assertTrue(accounts.setAccountInterestRate(Arrays.asList("BG11LLLL22223333444455", "0.02")));
	}
}
