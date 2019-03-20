package bg.sofia.uni.fmi.mjt.project.accounts;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountType;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;

public class AccountTest {
	private static final int NEGATIVE_AMOUNT = -10;
	private static final int SMALL_POSITIVE_AMOUNT = 10;
	private static final int MEDIUM_POSITIVE_AMOUNT = 300;
	private static final int HIGH_POSITIVE_AMOUNT = 700;
	private static final int INITIAL_AMOUNT = 500;

	private static AccountType type;
	private Account account;

	@BeforeClass
	public static void setUpAll() {
		type = mock(AccountType.class);
	}

	@Before
	public void setUp() {
		account = new Account(type, INITIAL_AMOUNT);
	}

	@Test(expected = ViolationException.class)
	public void testTakeMoneyThrowsViolationExceptionWhenGivenAccountIsBlocked() throws ViolationException {
		account = new Account(type, SMALL_POSITIVE_AMOUNT);
		account.takeMoney(MEDIUM_POSITIVE_AMOUNT);
	}

	@Test(expected = ViolationException.class)
	public void testTakeMoneyThrowsViolationExceptionWhenGivenAmountIsNegativeNumber() throws ViolationException {
		account.takeMoney(NEGATIVE_AMOUNT);
	}

	@Test(expected = ViolationException.class)
	public void testTakeMoneyThrowsViolationExceptionWhenGivenAmountIsZero() throws ViolationException {
		account.takeMoney(0);
	}

	@Test(expected = ViolationException.class)
	public void testTakeMoneyThrowsViolationExceptionWhenGivenAmountHigherThanMoneyInAccount()
			throws ViolationException {
		account.takeMoney(HIGH_POSITIVE_AMOUNT);
	}

	@Test
	public void testTakeMoneyReturnsTrueWhenGivenAccountIsNotBlockedAndAmountLowerThanMoneyInAccount()
			throws ViolationException {
		assertTrue(account.takeMoney(MEDIUM_POSITIVE_AMOUNT));
	}

	@Test(expected = ViolationException.class)
	public void testReceiveMoneyThrowsViolationExceptionWhenGivenAmountIsNegativeNumber() throws ViolationException {
		account.receiveMoney(NEGATIVE_AMOUNT);
	}

	@Test(expected = ViolationException.class)
	public void testReceiveMoneyThrowsViolationExceptionWhenGivenAmountIsZero() throws ViolationException {
		account.receiveMoney(0);
	}

	@Test
	public void testReceiveMoneyReturnsTrueWhenGivenAccountIsNotBlockedAndAmountLowerThanMoneyInAccount()
			throws ViolationException {
		assertTrue(account.receiveMoney(MEDIUM_POSITIVE_AMOUNT));
	}

	@Test
	public void testViolatedMinimalAmountOfMoneyInAccountReturnsFalseWhenGivenMoneyInAccountIsEvenOrMoreThanMinimum()
			throws ViolationException {
		assertFalse(account.violatedMinimalAmountOfMoneyInAccount());
	}

	@Test
	public void testViolatedMinimalAmountOfMoneyInAccountReturnsTrueWhenGivenMoneyInAccountIsLessThanMinimum()
			throws ViolationException {
		account = new Account(type, SMALL_POSITIVE_AMOUNT);
		assertTrue(account.violatedMinimalAmountOfMoneyInAccount());
	}
}
