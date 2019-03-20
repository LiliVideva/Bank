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

public class CreateAccountActionTest {
	private static UserProfile userProfile;
	private static AccountsDatabase accounts;
	private static List<String> accountData;
	private static AccountAction createAccountAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		userProfile = mock(UserProfile.class);
		accounts = mock(AccountsDatabase.class);
		accountData = Arrays.asList("user", "savingsAccount", "BG11AAAA22223333444455", "200");

		createAccountAction = new CreateAccountAction(userProfile);
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameWhenGivenNoArguments() {
		when(userProfile.getUsername()).thenReturn("user");
		assertEquals(Arrays.asList("user"), createAccountAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenLineArguments() {
		when(userProfile.getUsername()).thenReturn("user");

		assertEquals(accountData, createAccountAction.receiveArguments("savingsAccount BG11AAAA22223333444455 200"));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClientAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.createAccount(null)).thenReturn(false);

		assertFalse(createAccountAction.execute(null, Status.CLIENT, accounts));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClerkAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.createAccount(null)).thenReturn(false);

		assertFalse(createAccountAction.execute(null, Status.CLERK, accounts));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.createAccount(null)).thenReturn(false);

		assertFalse(createAccountAction.execute(null, Status.ADMIN, accounts));

	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClientAndCashingSucceeded()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		when(accounts.createAccount(null)).thenReturn(true);

		assertTrue(createAccountAction.execute(null, Status.CLIENT, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClerkAndCashingSucceeded() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.createAccount(null)).thenReturn(true);

		assertTrue(createAccountAction.execute(null, Status.CLERK, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdminAndCashingSucceeded() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.createAccount(null)).thenReturn(true);

		assertTrue(createAccountAction.execute(null, Status.ADMIN, accounts));
	}

}
