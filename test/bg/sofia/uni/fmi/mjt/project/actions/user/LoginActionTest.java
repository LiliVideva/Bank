package bg.sofia.uni.fmi.mjt.project.actions.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.actions.ActionFactory;
import bg.sofia.uni.fmi.mjt.project.actions.ClientActionFactory;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class LoginActionTest {
	private static ActionFactory actionFactory;
	private static UserProfile userProfile;
	private static UsersDatabase users;
	private static AccountsDatabase accounts;
	private static List<String> userData;
	private static Socket socket;
	private static UserAction loginAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		actionFactory = mock(ClientActionFactory.class);
		userProfile = mock(UserProfile.class);
		users = mock(UsersDatabase.class);
		accounts = mock(AccountsDatabase.class);
		socket = mock(Socket.class);
		userData = Arrays.asList("user", "user66Us");

		loginAction = new LoginAction(userProfile, actionFactory) {
			@Override
			protected Socket connect(UserProfile userProfile) {
				return socket;
			}

		};
	}

	@Test
	public void testReceiveArgumentsReturnsEmptyArrayWhenGivenNoArguments() {
		assertTrue(loginAction.receiveArguments(null).isEmpty());
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenLineArguments() {
		when(userProfile.getUsername()).thenReturn("user");

		assertEquals(userData, loginAction.receiveArguments("user user66Us"));
	}

	@Test(expected = UserAlreadyLoggedException.class)
	public void testExecuteThrowsUserAlreadyLoggedExceptionWhenGivenUserStatusIsClient()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			ViolationException {
		loginAction.execute(null, Status.CLIENT, users, accounts);
	}

	@Test(expected = UserAlreadyLoggedException.class)
	public void testExecuteThrowsUserAlreadyLoggedExceptionWhenGivenUserStatusIsClerk()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			ViolationException {
		loginAction.execute(null, Status.CLERK, users, accounts);
	}

	@Test(expected = UserAlreadyLoggedException.class)
	public void testExecuteReturnsUserAlreadyLoggedExceptionWhenGivenUserStatusIsAdmin()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			ViolationException {
		loginAction.execute(null, Status.ADMIN, users, accounts);
	}

	@Test
	public void testExecuteReturnsFalseWhenLoginFailed() throws InvalidFieldException, NoSuchUserException,
			UserAlreadyLoggedException, NotLoggedException, ViolationException, UnknownHostException, IOException {
		when(users.loginUser(userData)).thenReturn(false);

		assertFalse(loginAction.execute(userData, Status.UNREGISTERED, users, accounts));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsUnregisteredAndLoginSucceeded()
			throws InvalidFieldException, NoSuchUserException, UserAlreadyLoggedException, NotLoggedException,
			ViolationException, UnknownHostException, IOException {
		when(users.loginUser(userData)).thenReturn(true);
		when(users.getPersonalDetails(userData)).thenReturn(userProfile);
		when(userProfile.getUsername()).thenReturn("user");
		when(socket.getOutputStream()).thenReturn(System.out);

		assertTrue(loginAction.execute(userData, Status.UNREGISTERED, users, accounts));
	}
}
