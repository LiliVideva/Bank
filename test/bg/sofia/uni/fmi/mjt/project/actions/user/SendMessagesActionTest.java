package bg.sofia.uni.fmi.mjt.project.actions.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
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

public class SendMessagesActionTest {
	private static PrintWriter printWriter;
	private static UsersDatabase users;
	private static List<String> messageData;

	private UserProfile userProfile;
	private UserAction sendMessageAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		printWriter = mock(PrintWriter.class);
		users = mock(UsersDatabase.class);
		messageData = Arrays.asList("user", "receiver", "This is a message.");
	}

	@Before
	public void setUp() {
		userProfile = mock(UserProfile.class);
		sendMessageAction = new SendMessageAction(userProfile, printWriter);
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameNoneWhenGivenNoArguments() {
		userProfile = null;
		sendMessageAction = new SendMessageAction(userProfile, printWriter);

		assertEquals(Arrays.asList("none"), sendMessageAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithOnlyUsernameWhenGivenNoArguments() {
		when(userProfile.getUsername()).thenReturn("user");
		assertEquals(Arrays.asList("user"), sendMessageAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenUsernameNoneAndLineArguments() {
		userProfile = null;
		sendMessageAction = new SendMessageAction(userProfile, printWriter);

		assertEquals(Arrays.asList("none", "receiver", "This is a message."),
				sendMessageAction.receiveArguments("receiver This is a message."));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenUsernameAndLineArguments() {
		when(userProfile.getUsername()).thenReturn("user");

		assertEquals(messageData, sendMessageAction.receiveArguments("receiver This is a message."));
	}

	@Test(expected = NotLoggedException.class)
	public void testExecuteThrowsNotLoggedExceptionWhenGivenUserStatusIsUnregistered() throws NotLoggedException,
			NoSuchUserException, InvalidFieldException, UserAlreadyLoggedException, ViolationException {
		sendMessageAction.execute(null, Status.UNREGISTERED, users);
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClientAndRegisterFailed() throws InvalidFieldException,
			UserAlreadyLoggedException, NoSuchUserException, NotLoggedException, ViolationException {
		when(users.sendMessage(null, printWriter)).thenReturn(false);

		assertFalse(sendMessageAction.execute(null, Status.CLIENT, users));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsClerkAndRegisterFailed() throws InvalidFieldException,
			UserAlreadyLoggedException, NoSuchUserException, NotLoggedException, ViolationException {
		when(users.sendMessage(null, printWriter)).thenReturn(false);

		assertFalse(sendMessageAction.execute(null, Status.CLERK, users));
	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndRegisterFailed() throws InvalidFieldException,
			UserAlreadyLoggedException, NoSuchUserException, NotLoggedException, ViolationException {
		when(users.sendMessage(null, printWriter)).thenReturn(false);

		assertFalse(sendMessageAction.execute(null, Status.ADMIN, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClientAndSendingSucceeded() throws InvalidFieldException,
			UserAlreadyLoggedException, NoSuchUserException, NotLoggedException, ViolationException {
		when(users.sendMessage(messageData, printWriter)).thenReturn(true);

		assertTrue(sendMessageAction.execute(messageData, Status.CLIENT, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsClerkAndSendingSucceeded() throws InvalidFieldException,
			UserAlreadyLoggedException, NoSuchUserException, NotLoggedException, ViolationException {
		when(users.sendMessage(messageData, printWriter)).thenReturn(true);

		assertTrue(sendMessageAction.execute(messageData, Status.CLERK, users));
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdminAndSendingSucceeded() throws InvalidFieldException,
			UserAlreadyLoggedException, NoSuchUserException, NotLoggedException, ViolationException {
		when(users.sendMessage(messageData, printWriter)).thenReturn(true);

		assertTrue(sendMessageAction.execute(messageData, Status.ADMIN, users));
	}

}
