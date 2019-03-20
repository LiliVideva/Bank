package bg.sofia.uni.fmi.mjt.project.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountType;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;

public class AccountProfileTest {
	private static final int NEGATIVE_AMOUNT = -10;
	private static final int SMALL_POSITIVE_AMOUNT = 10;
	private static final int MEDIUM_POSITIVE_AMOUNT = 490;
	private static final int HIGH_POSITIVE_AMOUNT = 700;
	private static final int INITIAL_AMOUNT = 500;

	private static AccountType type;
	private static Account account;
	private AccountProfile accountProfile;

	@BeforeClass
	public static void setUpAll() {
		type = mock(AccountType.class);
	}

	@Before
	public void setUp() {
		account = new Account(type, INITIAL_AMOUNT);
		accountProfile = new AccountProfile(account);
	}

	@Test(expected = ViolationException.class)
	public void testTakeMoneyThrowsViolationExceptionWhenGivenAccountIsBlocked() throws ViolationException {
		accountProfile = new AccountProfile(new Account(type, SMALL_POSITIVE_AMOUNT));

		accountProfile.takeMoney(MEDIUM_POSITIVE_AMOUNT);
	}

	@Test(expected = ViolationException.class)
	public void testTakeMoneyThrowsViolationExceptionWhenGivenAmountIsNegativeNumber() throws ViolationException {
		accountProfile.takeMoney(NEGATIVE_AMOUNT);
	}

	@Test(expected = ViolationException.class)
	public void testTakeMoneyThrowsViolationExceptionWhenGivenAmountIsZero() throws ViolationException {
		accountProfile.takeMoney(0);
	}

	@Test(expected = ViolationException.class)
	public void testTakeMoneyThrowsViolationExceptionWhenGivenAmountHigherThanMoneyInAccount()
			throws ViolationException {
		accountProfile.takeMoney(HIGH_POSITIVE_AMOUNT);
	}

	@Test
	public void testTakeMoneyReturnsFalseWhenGivenIncorrectAmountOfMoneyInAccount() throws ViolationException {
		Account tempAccount = mock(Account.class);
		accountProfile = new AccountProfile(tempAccount);
		when(tempAccount.takeMoney(NEGATIVE_AMOUNT)).thenReturn(false);
		accountProfile.takeMoney(NEGATIVE_AMOUNT);
	}

	@Test
	public void testTakeMoneyReturnsTrueAndBlocksAccountAndAddsViolationMessageWhenGivenAccountIsNotBlocked()
			throws ViolationException { // WhenGivenAmountLowerThanMoneyInAccountButResultMoneyIsLessThanMinimum
		int violationMessagesCount = accountProfile.getViolationMessages().size();

		assertTrue(accountProfile.takeMoney(MEDIUM_POSITIVE_AMOUNT));
		assertEquals(violationMessagesCount + 1, accountProfile.getViolationMessages().size());
	}

	@Test
	public void testTakeMoneyReturnsTrueWhenGivenAccountIsNotBlockedAndAmountLowerThanMoneyInAccount()
			throws ViolationException {
		assertTrue(accountProfile.takeMoney(MEDIUM_POSITIVE_AMOUNT));
	}

	@Test(expected = ViolationException.class)
	public void testReceiveMoneyThrowsViolationExceptionWhenGivenAmountIsNegativeNumber() throws ViolationException {
		accountProfile.receiveMoney(NEGATIVE_AMOUNT);
	}

	@Test(expected = ViolationException.class)
	public void testReceiveMoneyThrowsViolationExceptionWhenGivenAmountIsZero() throws ViolationException {
		accountProfile.receiveMoney(0);
	}

	@Test
	public void testReceiveMoneyReturnsFalseWhenGivenIncorrectAmountOfMoneyInAccount() throws ViolationException {
		Account tempAccount = mock(Account.class);
		accountProfile = new AccountProfile(tempAccount);
		when(tempAccount.receiveMoney(NEGATIVE_AMOUNT)).thenReturn(false);
		accountProfile.receiveMoney(NEGATIVE_AMOUNT);
	}

	@Test
	public void testReceiveMoneyReturnsTrueAndUnblockesAccountAndRemovesViolationMessageWhenGivenAccountIsBlocked()
			throws ViolationException { // WhenGivenAmountLowerThanMoneyInAccountAndResultMoneyIsEvenOrMoreThanMinimum
		accountProfile = new AccountProfile(new Account(type, SMALL_POSITIVE_AMOUNT));

		int violationMessagesCount = accountProfile.getViolationMessages().size();

		assertTrue(accountProfile.receiveMoney(MEDIUM_POSITIVE_AMOUNT));
		assertEquals(violationMessagesCount - 1, accountProfile.getViolationMessages().size());
	}

	@Test
	public void testReceiveMoneyReturnsTrueWhenGivenAccountIsNotBlockedAndAmountLowerThanMoneyInAccount()
			throws ViolationException {
		assertTrue(accountProfile.receiveMoney(MEDIUM_POSITIVE_AMOUNT));
	}

}
