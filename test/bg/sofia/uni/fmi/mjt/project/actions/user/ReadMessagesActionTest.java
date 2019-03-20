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

public class ReadMessagesActionTest {
	private static UserProfile userProfile;
	private static UsersDatabase users;
	private static PrintWriter printWriter;

	private UserAction readMessagesAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		userProfile = mock(UserProfile.class);
		users = mock(UsersDatabase.class);
		printWriter = mock(PrintWriter.class);

	}

	@Before
	public void setUp() {
		readMessagesAction = new ReadMessagesAction(userProfile, printWriter);
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameWhenGivenNoArguments() {
		when(userProfile.getUsername()).thenReturn("user");
		assertEquals(Arrays.asList("user"), readMessagesAction.receiveArguments(null));
	}

	@Test(expected = NotLoggedException.class)
	public void testExecuteThrowsNotLoggedExceptionWhenGivenUserStatusIsUnregistered() throws NotLoggedException,
			NoSuchUserException, InvalidFieldException, UserAlreadyLoggedException, ViolationException {
		readMessagesAction.execute(null, Status.UNREGISTERED, users);
	}

	@Test
	public void testExecuteReturnsFalseWhenReadMessagesFailed() throws NoSuchUserException, InvalidFieldException,
			NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.readMessages(null, printWriter)).thenReturn(false);

		assertFalse(readMessagesAction.execute(null, Status.CLIENT, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClient() throws NoSuchUserException, InvalidFieldException,
			NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.readMessages(null, printWriter)).thenReturn(true);

		assertTrue(readMessagesAction.execute(null, Status.CLIENT, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClerk() throws NoSuchUserException, InvalidFieldException,
			NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.readMessages(null, printWriter)).thenReturn(true);

		assertTrue(readMessagesAction.execute(null, Status.CLERK, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdmin() throws NoSuchUserException, InvalidFieldException,
			NotLoggedException, UserAlreadyLoggedException, ViolationException {
		when(users.readMessages(null, printWriter)).thenReturn(true);

		assertTrue(readMessagesAction.execute(null, Status.ADMIN, users));
	}

}
