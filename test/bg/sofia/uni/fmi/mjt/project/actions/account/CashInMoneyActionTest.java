package bg.sofia.uni.fmi.mjt.project.actions.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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

public class CashInMoneyActionTest {
	private static UserProfile userProfile;
	private static AccountsDatabase accounts;
	private static List<String> accountData;
	private static AccountAction cashInMoneyAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		userProfile = mock(UserProfile.class);
		accounts = mock(AccountsDatabase.class);
		accountData = Arrays.asList("200", "BG11AAAA22223333444455");

		cashInMoneyAction = new CashInMoneyAction(userProfile) {
			@Override
			protected Status checkUserStatus(UserProfile userProfile, UsersDatabase users) {
				return Status.CLIENT;
			}
		};
	}

	@Test
	public void testReceiveArgumentsReturnsEmptyArrayWhenGivenNoArguments() {
		assertTrue(cashInMoneyAction.receiveArguments(null).isEmpty());
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenLineArguments() {
		assertEquals(accountData, cashInMoneyAction.receiveArguments("200 BG11AAAA22223333444455"));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsUnregisteredAndCashingFailed()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		when(accounts.cashInMoney(null)).thenReturn(false);

		assertFalse(cashInMoneyAction.execute(null, Status.UNREGISTERED, accounts));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClientAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.cashInMoney(null)).thenReturn(false);

		assertFalse(cashInMoneyAction.execute(null, Status.CLIENT, accounts));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClerkAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.cashInMoney(null)).thenReturn(false);

		assertFalse(cashInMoneyAction.execute(null, Status.CLERK, accounts));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.cashInMoney(null)).thenReturn(false);

		assertFalse(cashInMoneyAction.execute(null, Status.ADMIN, accounts));

	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsUnregisteredAndCashingSucceeded()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		when(accounts.cashInMoney(null)).thenReturn(true);

		assertTrue(cashInMoneyAction.execute(null, Status.UNREGISTERED, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClientAndCashingSucceeded()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			NoSuchAccountException, ViolationException {
		when(accounts.cashInMoney(null)).thenReturn(true);

		assertTrue(cashInMoneyAction.execute(null, Status.CLIENT, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClerkAndCashingSucceeded() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.cashInMoney(null)).thenReturn(true);

		assertTrue(cashInMoneyAction.execute(null, Status.CLERK, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdminAndCashingSucceeded() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(accounts.cashInMoney(null)).thenReturn(true);

		assertTrue(cashInMoneyAction.execute(null, Status.ADMIN, accounts));
	}

	@Test
	public void testExecuteActionReturnsFalseWhenCashingFailed() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(cashInMoneyAction.execute(new ArrayList<>(), Status.CLIENT, accounts)).thenReturn(false);

		assertFalse(cashInMoneyAction.executeAction(null, userProfile, null, accounts));

	}

	@Test
	public void testExecuteActionReturnsTrueWhenCashingSucceeded() throws UserAlreadyLoggedException,
			InvalidFieldException, NoSuchUserException, NotLoggedException, NoSuchAccountException, ViolationException {
		when(cashInMoneyAction.execute(new ArrayList<>(), Status.CLIENT, accounts)).thenReturn(true);

		assertTrue(cashInMoneyAction.executeAction(null, userProfile, null, accounts));
	}
}
