package bg.sofia.uni.fmi.mjt.project.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class ServerActionFactoryTest {
	private static PrintWriter printWriter;
	private static UserProfile userProfile;
	private static UsersDatabase users;
	private static AccountsDatabase accounts;

	private Action action;
	private ActionFactory actionFactory;

	@BeforeClass
	public static void setUpAll() throws IOException {
		printWriter = mock(PrintWriter.class);
		userProfile = mock(UserProfile.class);
		users = mock(UsersDatabase.class);
		accounts = mock(AccountsDatabase.class);
	}

	@Before
	public void setUp() {
		action = mock(Action.class);
		actionFactory = new ServerActionFactory(users, accounts) {
			@Override
			protected Action getAction(String actionName) {
				return action;
			}

			@Override
			protected PrintWriter createPrintWriter() {
				return printWriter;
			}
		};

		actionFactory.setUserProfile(userProfile);
	}

	@Test
	public void testProcessActionReturnsFalseWhenGivenNotExistingAction() {
		actionFactory = new ServerActionFactory(users, accounts) {
			@Override
			protected PrintWriter createPrintWriter() {
				return printWriter;
			}

		};
		actionFactory.setUserProfile(userProfile);

		assertFalse(actionFactory.processAction("put"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenExecutingActionFailed()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction("BG11AAAA22223333444455 2.5", userProfile, users, accounts)).thenReturn(false);
		assertTrue(actionFactory.processAction("set-rate BG11AAAA22223333444455 2.5"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedInvalidFieldException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction("BG11AAAA22223333444455 2.5", userProfile, users, accounts))
				.thenThrow(InvalidFieldException.class);
		assertTrue(actionFactory.processAction("set-rate BG11AAAA22223333444455 2.5"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedNoSuchUserException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction("BG11AAAA22223333444455 2.5", userProfile, users, accounts))
				.thenThrow(NoSuchUserException.class);
		assertTrue(actionFactory.processAction("set-rate BG11AAAA22223333444455 2.5"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedNotLoggedException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction("BG11AAAA22223333444455 2.5", userProfile, users, accounts))
				.thenThrow(NotLoggedException.class);
		assertTrue(actionFactory.processAction("set-rate BG11AAAA22223333444455 2.5"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedUserAlreadyLoggedException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction("BG11AAAA22223333444455 2.5", userProfile, users, accounts))
				.thenThrow(UserAlreadyLoggedException.class);
		assertTrue(actionFactory.processAction("set-rate BG11AAAA22223333444455 2.5"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedViolationException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction("BG11AAAA22223333444455 2.5", userProfile, users, accounts))
				.thenThrow(ViolationException.class);
		assertTrue(actionFactory.processAction("set-rate BG11AAAA22223333444455 2.5"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenActionThrowedNoSuchAccountException()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction("BG11AAAA22223333444455 2.5", userProfile, users, accounts))
				.thenThrow(NoSuchAccountException.class);
		assertTrue(actionFactory.processAction("set-rate BG11AAAA22223333444455 2.5"));
	}

	@Test
	public void testProcessActionReturnsTrueWhenGivenActionDespiteIfExistingOrNot()
			throws InvalidFieldException, NoSuchUserException, NotLoggedException, UserAlreadyLoggedException,
			ViolationException, NoSuchAccountException {
		when(action.executeAction("BG11AAAA22223333444455 2.5", userProfile, users, accounts)).thenReturn(true);

		assertTrue(actionFactory.processAction("set-rate BG11AAAA22223333444455 2.5"));
	}

}
