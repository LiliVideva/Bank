package bg.sofia.uni.fmi.mjt.project.actions.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

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

public class DisplayAccountsActionTest {
	private static PrintWriter printWriter;
	private static UserProfile userProfile;
	private static AccountsDatabase accounts;
	private static AccountAction displayAccountsAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		printWriter = mock(PrintWriter.class);
		userProfile = mock(UserProfile.class);
		accounts = mock(AccountsDatabase.class);

		displayAccountsAction = new DisplayAccountsAction(userProfile, printWriter);
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameWhenGivenNoArguments() {
		when(userProfile.getUsername()).thenReturn("user");
		assertEquals(Arrays.asList("user"), displayAccountsAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameWhenGivenAnyArguments() {
		when(userProfile.getUsername()).thenReturn("user");
		assertEquals(Arrays.asList("user"), displayAccountsAction.receiveArguments("otherUser"));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndCashingFailed()
			throws UserAlreadyLoggedException, IndexOutOfBoundsException, InvalidFieldException, NoSuchUserException,
			NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.displayAccounts(null, printWriter)).thenReturn(false);

		assertFalse(displayAccountsAction.execute(null, Status.ADMIN, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClientAndCashingSucceeded()
			throws UserAlreadyLoggedException, IndexOutOfBoundsException, InvalidFieldException, NoSuchUserException,
			NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.displayAccounts(null, printWriter)).thenReturn(true);

		assertTrue(displayAccountsAction.execute(null, Status.CLIENT, accounts));
	}
}
