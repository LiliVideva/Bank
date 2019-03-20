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

public class TransferMoneyActionTest {
	private static UserProfile userProfile;
	private static AccountsDatabase accounts;
	private static List<String> accountData;
	private static AccountAction transferMoneyAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		userProfile = mock(UserProfile.class);
		accounts = mock(AccountsDatabase.class);
		accountData = Arrays.asList("user", "BG11AAAA22223333444455", "BG11AAAA22223333444466", "200");

		transferMoneyAction = new TransferMoneyAction(userProfile);
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameWhenGivenNoArguments() {
		when(userProfile.getUsername()).thenReturn("user");
		assertEquals(Arrays.asList("user"), transferMoneyAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenLineArguments() {
		when(userProfile.getUsername()).thenReturn("user");

		assertEquals(accountData,
				transferMoneyAction.receiveArguments("BG11AAAA22223333444455 BG11AAAA22223333444466 200"));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClientAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.transferMoney(null)).thenReturn(false);

		assertFalse(transferMoneyAction.execute(null, Status.CLIENT, accounts));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClerkAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.transferMoney(null)).thenReturn(false);

		assertFalse(transferMoneyAction.execute(null, Status.CLERK, accounts));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.transferMoney(null)).thenReturn(false);

		assertFalse(transferMoneyAction.execute(null, Status.ADMIN, accounts));

	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClientAndCashingSucceeded()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		when(accounts.transferMoney(null)).thenReturn(true);

		assertTrue(transferMoneyAction.execute(null, Status.CLIENT, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClerkAndCashingSucceeded() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.transferMoney(null)).thenReturn(true);

		assertTrue(transferMoneyAction.execute(null, Status.CLERK, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdminAndCashingSucceeded() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.transferMoney(null)).thenReturn(true);

		assertTrue(transferMoneyAction.execute(null, Status.ADMIN, accounts));
	}

}
