package bg.sofia.uni.fmi.mjt.project.actions.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchAccountException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;

public class WithdrawMoneyActionTest {
	private static UserProfile userProfile;
	private static AccountsDatabase accounts;
	private static List<String> accountData;
	private static AccountAction withdrawMoneyAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		userProfile = mock(UserProfile.class);
		accounts = mock(AccountsDatabase.class);
		accountData = Arrays.asList("user", "BG11AAAA22223333444455", "200");

		withdrawMoneyAction = new WithdrawMoneyAction(userProfile);
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameWhenGivenNoArguments() {
		when(userProfile.getUsername()).thenReturn("user");
		assertEquals(Arrays.asList("user"), withdrawMoneyAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenLineArguments() {
		when(userProfile.getUsername()).thenReturn("user");

		assertEquals(accountData, withdrawMoneyAction.receiveArguments("BG11AAAA22223333444455 200"));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClientAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.withdrawMoney(null)).thenReturn(false);

		assertFalse(withdrawMoneyAction.execute(null, Status.CLIENT, accounts));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClerkAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.withdrawMoney(null)).thenReturn(false);

		assertFalse(withdrawMoneyAction.execute(null, Status.CLERK, accounts));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.withdrawMoney(null)).thenReturn(false);

		assertFalse(withdrawMoneyAction.execute(null, Status.ADMIN, accounts));

	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClientAndCashingSucceeded()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		when(accounts.withdrawMoney(null)).thenReturn(true);

		assertTrue(withdrawMoneyAction.execute(null, Status.CLIENT, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClerkAndCashingSucceeded() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.withdrawMoney(null)).thenReturn(true);

		assertTrue(withdrawMoneyAction.execute(null, Status.CLERK, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdminAndCashingSucceeded() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.withdrawMoney(null)).thenReturn(true);

		assertTrue(withdrawMoneyAction.execute(null, Status.ADMIN, accounts));
	}

}
