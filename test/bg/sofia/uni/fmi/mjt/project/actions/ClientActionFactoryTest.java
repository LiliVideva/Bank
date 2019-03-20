package bg.sofia.uni.fmi.mjt.project.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchAccountException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class ClientActionFactoryTest {
	private Action action;
	private ActionFactory actionFactory;
	private static UsersDatabase users;
	private static AccountsDatabase accounts;

	@BeforeClass
	public static void setUpAll() {
		users = mock(UsersDatabase.class);
		accounts = mock(AccountsDatabase.class);
	}

	@Before
	public void setUp() {
		action = mock(Action.class);
		actionFactory = new ClientActionFactory(users, accounts) {
			@Override
			protected Action getAction(String actionName) {
				return action;
			}
		};
	}

	@Test
	public void testProcessActionReturnsFalseWhenGivenExitAction() {
		assertFalse(actionFactory.processAction("exit"));
	}

	@Test
	public void testProcessActionReturnsFalseWhenGivenNotExistingActionAndPrintWriterIsNull() {
		actionFactory = new ClientActionFactory(users, accounts);
		((ClientActionFactory) actionFactory).setPrintWriter(null);
		assertFalse(actionFactory.processAction("put"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenGivenLineNull() {
		assertTrue(actionFactory.processAction(null));
	}

	@Test
	public void testProcessActionReturnsTrueWhenGivenBlankLine() {
		assertTrue(actionFactory.processAction("   "));
	}

	@Test
	public void testProcessActionReturnsTrueWhenGivenNotExistingActionAndPrintWriterIsNotNull()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		actionFactory = new ClientActionFactory(users, accounts);
		((ClientActionFactory) actionFactory).setPrintWriter(mock(PrintWriter.class));
		assertTrue(actionFactory.processAction("put"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenExecutingActionFailed()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction(null, null, null, null)).thenReturn(false);
		assertTrue(actionFactory.processAction("login"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedInvalidFieldException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction(null, null, null, null)).thenThrow(InvalidFieldException.class);
		assertTrue(actionFactory.processAction("login"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedNoSuchUserException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction(null, null, null, null)).thenThrow(NoSuchUserException.class);
		assertTrue(actionFactory.processAction("login user user66Us"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedUserAlreadyLoggedException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction(null, null, null, null)).thenThrow(UserAlreadyLoggedException.class);
		assertTrue(actionFactory.processAction("login user user66Us"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedViolationException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction(null, null, null, null)).thenThrow(ViolationException.class);
		assertTrue(actionFactory.processAction("login user user66Us"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenExecutingActionSucceeded()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction("user user66Us", null, null, null)).thenReturn(true);
		assertTrue(actionFactory.processAction("login user user66Us"));
	}
}
