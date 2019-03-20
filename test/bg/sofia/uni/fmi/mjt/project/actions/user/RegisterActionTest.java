package bg.sofia.uni.fmi.mjt.project.actions.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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

public class RegisterActionTest {
	private static UsersDatabase users;
	private static List<String> regularUserData;
	private static List<String> fromAdminUserData;

	private UserProfile userProfile;
	private UserAction registerAction;

	@BeforeClass
	public static void setUpAll() throws IOException {
		users = mock(UsersDatabase.class);
		regularUserData = Arrays.asList("UNREGISTERED", "user", "user66Us", "user66Us", "user@abv.bg", "CLERK");
		fromAdminUserData = Arrays.asList("ADMIN", "user", "user66Us", "user66Us", "user@abv.bg", "CLERK");

	}

	@Before
	public void setUp() {
		userProfile = mock(UserProfile.class);
		registerAction = new RegisterAction(userProfile);
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithStatusUnregisteredWhenGivenNoArguments() {
		userProfile = null;
		registerAction = new RegisterAction(userProfile);

		assertEquals(Arrays.asList(Status.UNREGISTERED.toString()), registerAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithStatusClerkWhenGivenNoArguments() {
		when(userProfile.getStatus()).thenReturn(Status.CLERK);
		assertEquals(Arrays.asList(Status.CLERK.toString()), registerAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsArrayWithStatusAdminWhenGivenNoArguments() {
		when(userProfile.getStatus()).thenReturn(Status.ADMIN);
		assertEquals(Arrays.asList(Status.ADMIN.toString()), registerAction.receiveArguments(null));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenUserProfileNullAndLineArguments() {
		userProfile = null;
		registerAction = new RegisterAction(userProfile);

		assertEquals(regularUserData, registerAction.receiveArguments("user user66Us user66Us user@abv.bg CLERK"));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenUserStatusUnregisteredAndLineArguments() {
		when(userProfile.getStatus()).thenReturn(Status.UNREGISTERED);

		assertEquals(regularUserData, registerAction.receiveArguments("user user66Us user66Us user@abv.bg CLERK"));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenUserStatusClerkAndLineArguments() {
		when(userProfile.getStatus()).thenReturn(Status.CLERK);

		assertEquals(Arrays.asList("CLERK", "user", "user66Us", "user66Us", "user@abv.bg", "CLERK"),
				registerAction.receiveArguments("user user66Us user66Us user@abv.bg CLERK"));
	}

	@Test
	public void testReceiveArgumentsReturnsFilledArrayWhenGivenUserStatusAdminAndLineArguments() {
		when(userProfile.getStatus()).thenReturn(Status.ADMIN);

		assertEquals(fromAdminUserData, registerAction.receiveArguments("user user66Us user66Us user@abv.bg CLERK"));
	}

	@Test(expected = UserAlreadyLoggedException.class)
	public void testExecuteThrowsUserAlreadyLoggedExceptionWhenGivenUserStatusIsClient() throws InvalidFieldException,
			UserAlreadyLoggedException, NoSuchUserException, NotLoggedException, ViolationException {
		registerAction.execute(null, Status.CLIENT, users);
	}

	@Test(expected = UserAlreadyLoggedException.class)
	public void testExecuteThrowsUserAlreadyLoggedExceptionWhenGivenUserStatusIsClerk() throws InvalidFieldException,
			UserAlreadyLoggedException, NoSuchUserException, NotLoggedException, ViolationException {
		registerAction.execute(null, Status.CLERK, users);

	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsUnregisteredAndRegisterFailed()
			throws InvalidFieldException, UserAlreadyLoggedException, NoSuchUserException, NotLoggedException,
			ViolationException {
		when(users.registerUser(null)).thenReturn(false);

		assertFalse(registerAction.execute(null, Status.UNREGISTERED, users));

	}

	@Test
	public void testExecuteReturnsFalseWhenGivenUserStatusIsAdminAndRegisterFailed() throws InvalidFieldException,
			UserAlreadyLoggedException, NoSuchUserException, NotLoggedException, ViolationException {
		when(users.registerUser(null)).thenReturn(false);

		assertFalse(registerAction.execute(null, Status.ADMIN, users));

	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsUnregisteredAndRegisterSucceeded()
			throws UserAlreadyLoggedException, InvalidFieldException, NoSuchUserException, NotLoggedException,
			ViolationException {
		when(users.registerUser(null)).thenReturn(true);

		registerAction.execute(null, Status.UNREGISTERED, users);
	}

	@Test
	public void testExecuteReturnsTrueWhenGivenUserStatusIsAdminAndRegisterSucceeded()
			throws UserAlreadyLoggedException, IndexOutOfBoundsException, InvalidFieldException, NoSuchUserException,
			NotLoggedException, ViolationException {
		when(users.registerUser(null)).thenReturn(true);
		registerAction.execute(null, Status.ADMIN, users);
	}
}
