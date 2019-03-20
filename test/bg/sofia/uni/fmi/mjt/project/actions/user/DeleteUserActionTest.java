package bg.sofia.uni.fmi.mjt.project.actions.user;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

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

public class DeleteUserActionTest {
	private static UserProfile userProfile;
	private static UsersDatabase users;
	private static List<String> userData;

	private UserAction deleteUserAction;

	@BeforeClass
	public static void setUpAll() {
		userProfile = mock(UserProfile.class);
		users = mock(UsersDatabase.class);
		userData = Arrays.asList("user");
	}

	@Before
	public void setUp() {
		deleteUserAction = new DeleteUserAction(userProfile) {
			@Override
			protected boolean disconnect() throws ViolationException {
				return true;
			}
		};
	}

	@Test
	public void testReceiveArgumentsReturnsNullWhenGivenNoArguments() {
		assertNull(deleteUserAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsNullWhenGivenAnyArguments() {
		assertNull(deleteUserAction.receiveArguments("user"));
	}

	@Test(expected = NotLoggedException.class)
	public void testExecuteReturnsNotLoggedExceptionWhenGivenUserStatusIsUnregistered() throws NotLoggedException,
			NoSuchUserException, InvalidFieldException, UserAlreadyLoggedException, ViolationException {
		deleteUserAction.execute(null, Status.UNREGISTERED, users);
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClientAndDeleteFailed() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(userProfile.getUsername()).thenReturn("user");
		when(users.deleteUser(userData)).thenReturn(false);

		assertFalse(deleteUserAction.execute(userData, Status.CLIENT, users));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClerkAndDeleteFailed() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(userProfile.getUsername()).thenReturn("user");
		when(users.deleteUser(userData)).thenReturn(false);

		assertFalse(deleteUserAction.execute(userData, Status.CLERK, users));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndDeleteFailed() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(userProfile.getUsername()).thenReturn("user");
		when(users.deleteUser(userData)).thenReturn(false);

		assertFalse(deleteUserAction.execute(userData, Status.ADMIN, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClientAndDeleteSucceeded() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(userProfile.getUsername()).thenReturn("user");
		when(users.deleteUser(userData)).thenReturn(true);

		assertTrue(deleteUserAction.execute(userData, Status.CLIENT, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClerkAndDeleteSucceeded() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(userProfile.getUsername()).thenReturn("user");
		when(users.deleteUser(userData)).thenReturn(true);

		assertTrue(deleteUserAction.execute(userData, Status.CLERK, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdminAndDeleteSucceeded() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(userProfile.getUsername()).thenReturn("user");
		when(users.deleteUser(userData)).thenReturn(true);

		assertTrue(deleteUserAction.execute(userData, Status.ADMIN, users));
	}
}
