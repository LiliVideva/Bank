package bg.sofia.uni.fmi.mjt.project.actions.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

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

public class GetUserActionTest {
	private static UsersDatabase users;
	private static PrintWriter printWriter;

	private UserProfile userProfile;
	private UserAction getUserAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		users = mock(UsersDatabase.class);
		printWriter = mock(PrintWriter.class);

	}

	@Before
	public void setUp() {
		userProfile = mock(UserProfile.class);
		getUserAction = new GetUserAction(userProfile, printWriter);
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameNoneWhenGivenNoArguments() {
		userProfile = null;
		getUserAction = new GetUserAction(userProfile, printWriter);

		assertEquals(Arrays.asList("none"), getUserAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameWhenGivenNoArguments() {
		when(userProfile.getUsername()).thenReturn("user");
		assertEquals(Arrays.asList("user"), getUserAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenUsernameNoneAndLineArguments() {
		userProfile = null;
		getUserAction = new GetUserAction(userProfile, printWriter);

		assertEquals(Arrays.asList("none", "user"), getUserAction.receiveArguments("user"));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenUsernameAndLineArguments() {
		when(userProfile.getUsername()).thenReturn("user");

		assertEquals(Arrays.asList("user", "user"), getUserAction.receiveArguments("user"));
	}

	@Test(expected = NotLoggedException.class)
	public void testExecuteThrowsNotLoggedExceptionWhenGivenUserStatusIsUnregistered() throws NotLoggedException,
			NoSuchUserException, InvalidFieldException, UserAlreadyLoggedException, ViolationException {
		getUserAction.execute(null, Status.UNREGISTERED, users);
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClientAndActionFailed() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.printPersonalDetails(null, printWriter)).thenReturn(false);

		assertFalse(getUserAction.execute(null, Status.CLIENT, users));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClerkAndActionFailed() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.printPersonalDetails(null, printWriter)).thenReturn(false);

		assertFalse(getUserAction.execute(null, Status.CLERK, users));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndActionFailed() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.printPersonalDetails(null, printWriter)).thenReturn(false);

		assertFalse(getUserAction.execute(null, Status.ADMIN, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClientAndActionSucceeded() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.printPersonalDetails(null, printWriter)).thenReturn(true);

		assertTrue(getUserAction.execute(null, Status.CLIENT, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClerkAndActionSucceeded() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.printPersonalDetails(null, printWriter)).thenReturn(true);

		assertTrue(getUserAction.execute(null, Status.CLERK, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdminAndActionSucceeded() throws NoSuchUserException,
			InvalidFieldException, NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.printPersonalDetails(null, printWriter)).thenReturn(true);

		assertTrue(getUserAction.execute(null, Status.ADMIN, users));
	}

}
