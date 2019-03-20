package bg.sofia.uni.fmi.mjt.project.actions.user;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NotLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.UserAlreadyLoggedException;
import bg.sofia.uni.fmi.mjt.project.exceptions.ViolationException;
import bg.sofia.uni.fmi.mjt.project.users.Status;
import bg.sofia.uni.fmi.mjt.project.users.UserProfile;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class LogoutActionTest {
	private static UserProfile userProfile;
	private static UsersDatabase users;

	private UserAction logoutAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		userProfile = mock(UserProfile.class);
		users = mock(UsersDatabase.class);
	}

	@Before
	public void setUp() {
		logoutAction = new LogoutAction(userProfile) {
			@Override
			protected boolean disconnect() throws ViolationException {
				return true;
			}
		};
	}

	@Test
	public void testReceiveArgumentsReturnsNullWhenGivenNoArguments() {
		assertNull(logoutAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsNullWhenGivenAnyArguments() {
		assertNull(logoutAction.receiveArguments("user"));
	}

	@Test(expected = NotLoggedException.class)
	public void testExecuteThrowsNotLoggedExceptionWhenGivenUserStatusIsUnregistered() throws NotLoggedException,
			InvalidFieldException, NoSuchUserException, UserAlreadyLoggedException, ViolationException {
		logoutAction.execute(null, Status.UNREGISTERED, users);
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClient() throws NotLoggedException, InvalidFieldException,
			NoSuchUserException, UserAlreadyLoggedException, ViolationException {
		assertTrue(logoutAction.execute(null, Status.CLIENT, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClerk() throws NotLoggedException, InvalidFieldException,
			NoSuchUserException, UserAlreadyLoggedException, ViolationException {
		assertTrue(logoutAction.execute(null, Status.CLERK, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdmin() throws NotLoggedException, InvalidFieldException,
			NoSuchUserException, UserAlreadyLoggedException, ViolationException {

		assertTrue(logoutAction.execute(null, Status.ADMIN, users));
	}
}
