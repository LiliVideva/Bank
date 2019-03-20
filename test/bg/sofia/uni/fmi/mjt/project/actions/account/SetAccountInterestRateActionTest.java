package bg.sofia.uni.fmi.mjt.project.actions.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class SetAccountInterestRateActionTest {
	private static PrintWriter printWriter;
	private static UserProfile userProfile;
	private static AccountsDatabase accounts;
	private static AccountAction setAccountInterestRateAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		printWriter = mock(PrintWriter.class);
		userProfile = mock(UserProfile.class);
		accounts = mock(AccountsDatabase.class);

		setAccountInterestRateAction = new SetAccountInterestRateAction(userProfile, printWriter);
	}

	@Test
	public void testReceiveArgumentsReturnsEmptyArrayWhenGivenNoArguments() {
		assertTrue(setAccountInterestRateAction.receiveArguments(null).isEmpty());
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenLineArguments() {
		List<String> accountData = Arrays.asList("BG11AAAA22223333444455", "2.5");
		assertEquals(accountData, setAccountInterestRateAction.receiveArguments("BG11AAAA22223333444455 2.5"));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndCashingFailed()
			throws UserAlreadyLoggedException, IndexOutOfBoundsException, InvalidFieldException, NoSuchUserException,
			NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.setAccountInterestRate(null)).thenReturn(false);

		assertFalse(setAccountInterestRateAction.execute(null, Status.ADMIN, accounts));

	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClientAndCashingSucceeded()
			throws UserAlreadyLoggedException, IndexOutOfBoundsException, InvalidFieldException, NoSuchUserException,
			NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.setAccountInterestRate(null)).thenReturn(true);

		assertTrue(setAccountInterestRateAction.execute(null, Status.CLIENT, accounts));
	}

	@Test
	public void testExecuteActionReturnsFalseWhenGivenUserStatusClient() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		setAccountInterestRateAction = new SetAccountInterestRateAction(userProfile, printWriter) {
			@Override
			protected Status checkUserStatus(UserProfile userProfile, UsersDatabase users) {
				return Status.CLIENT;
			}
		};
		assertFalse(setAccountInterestRateAction.executeAction(null, userProfile, null, accounts));

	}

	@Test
	public void testExecuteActionReturnsFalseWhenGivenUserStatusClerkAndCashingFailed()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		setAccountInterestRateAction = new SetAccountInterestRateAction(userProfile, printWriter) {
			@Override
			protected Status checkUserStatus(UserProfile userProfile, UsersDatabase users) {
				return Status.CLERK;
			}
		};
		when(setAccountInterestRateAction.execute(new ArrayList<>(), Status.CLERK, accounts)).thenReturn(false);

		assertFalse(setAccountInterestRateAction.executeAction(null, userProfile, null, accounts));

	}

	@Test
	public void testExecuteActionReturnsTrueWhenGivenUserStatusClerkAndCashingSucceeded()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		setAccountInterestRateAction = new SetAccountInterestRateAction(userProfile, printWriter) {
			@Override
			protected Status checkUserStatus(UserProfile userProfile, UsersDatabase users) {
				return Status.CLERK;
			}
		};
		when(setAccountInterestRateAction.execute(new ArrayList<>(), Status.CLERK, accounts)).thenReturn(true);

		assertTrue(setAccountInterestRateAction.executeAction(null, userProfile, null, accounts));
	}

	@Test
	public void testExecuteActionReturnsFalseWhenGivenUserStatusAdminAndCashingFailed()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		setAccountInterestRateAction = new SetAccountInterestRateAction(userProfile, printWriter) {
			@Override
			protected Status checkUserStatus(UserProfile userProfile, UsersDatabase users) {
				return Status.ADMIN;
			}
		};
		when(setAccountInterestRateAction.execute(new ArrayList<>(), Status.ADMIN, accounts)).thenReturn(false);

		assertFalse(setAccountInterestRateAction.executeAction(null, userProfile, null, accounts));

	}

	@Test
	public void testExecuteActionReturnsTrueWhenGivenUserStatusAdminAndCashingSucceeded()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		setAccountInterestRateAction = new SetAccountInterestRateAction(userProfile, printWriter) {
			@Override
			protected Status checkUserStatus(UserProfile userProfile, UsersDatabase users) {
				return Status.ADMIN;
			}
		};
		when(setAccountInterestRateAction.execute(new ArrayList<>(), Status.ADMIN, accounts)).thenReturn(true);

		assertTrue(setAccountInterestRateAction.executeAction(null, userProfile, null, accounts));
	}
}
