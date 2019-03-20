package bg.sofia.uni.fmi.mjt.project.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.project.BankServer;
import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;

public class UsersDatabaseTest {
	private static PrintWriter printWriter;
	private static List<String> registerData;
	private UsersDatabase users;

	@BeforeClass
	public static void setUpAll() {
		printWriter = mock(PrintWriter.class);
		registerData = Arrays.asList("UNREGISTERED", "dummyUser", "user66User", "user66User", "user@gmail.com",
				"CLIENT");
	}

	@Before
	public void setUp() {
		users = new UsersDatabase("users.txt") {
			@Override
			protected synchronized List<UserProfile> restoreProfilesFromDatabase() {
				List<UserProfile> usersProfiles = new ArrayList<>();
				usersProfiles
						.add(new UserProfile(new User("client", "clinetClient45", "client@gmail.com", Status.CLIENT)));
				usersProfiles.add(new UserProfile(new User("clerk", "clerkClerk43", "clerk@gmail.com", Status.CLERK)));
				usersProfiles.add(new UserProfile(new User("admin", "adminAdmin13", "admin@gmail.com", Status.ADMIN)));
				return usersProfiles;
			}
		};

	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidPasswordWithoutAtLeastOneDigit()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "userUser", "userUser", "user@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserReturnsInvalidFieldExceptionWhenGivenInvalidPasswordWithoutAtLeastOneLowerCaseLetter()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "USER66US", "USER66US", "user@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidPasswordWithoutAtLeastOneUpperCaseLetter()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66us", "user66us", "user@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidPasswordWithoutAtLeastEightSigns()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user6Us", "user6Us", "user@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidEmailStartingWithUpperCaseLetter()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "User@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidEmailStartingWithDigit()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "6user@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidEmailStartingWithDash()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "-user@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidEmailStartingWithUnderscore()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "-user@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidEmailStartingWithAt()
			throws InvalidFieldException {
		users.registerUser(Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidEmailContainingPunctuationSign()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "us!er@gmail.com", "CLIENT"));
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "us.er@gmail.com", "CLIENT"));
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "us?er@gmail.com", "CLIENT"));
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "us,er@gmail.com", "CLIENT"));
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "us;er@gmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidEmailWithoutAt()
			throws InvalidFieldException {
		users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "usergmail.com", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidEmailWithEndingAt()
			throws InvalidFieldException {
		users.registerUser(Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "user@", "CLIENT"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testRegisterUserThrowsInvalidFieldExceptionWhenGivenInvalidEmailWithoutEnding()
			throws InvalidFieldException {
		users.registerUser(Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66Us", "user@gmail", "CLIENT"));
	}

	@Test
	public void testRegisterUserReturnsFalseWhenGivenWrongNumberOfArguments() throws InvalidFieldException {
		assertFalse(
				users.registerUser(Arrays.asList("UNREGISTERED", "dummyUser", "userUser", "user@gmail.com", "CLIENT")));
	}

	@Test
	public void testRegisterUserReturnsFalseWhenGivenDifferentPasswordAndRepeatPassword() throws InvalidFieldException {
		assertFalse(users.registerUser(
				Arrays.asList("UNREGISTERED", "dummyUser", "user66Us", "user66User", "user@gmail.com", "CLIENT")));
	}

	@Test
	public void testRegisterUserReturnsFalseWhenGivenNewAdminWithCorrectDataAndCurrentUserStatusIsNotAdmin()
			throws InvalidFieldException {
		assertFalse(users.registerUser(
				Arrays.asList("CLIENT", "otherUser", "user66User", "user66User", "user@gmail.com", "ADMIN")));
	}

	@Test
	public void testRegisterUserReturnsFalseWhenGivenUserAlreadyExists() throws InvalidFieldException {
		assertTrue(users.registerUser(registerData));

		assertFalse(users.registerUser(registerData));
	}

	@Test
	public void testRegisterUserReturnsTrueWhenGivenNewClientWithCorrectData() throws InvalidFieldException {
		assertTrue(users.registerUser(registerData));
	}

	@Test
	public void testRegisterUserReturnsTrueWhenGivenNewClerkWithCorrectData() throws InvalidFieldException {
		assertTrue(users.registerUser(
				Arrays.asList("UNREGISTERED", "otherUser", "user66User", "user66User", "user@gmail.com", "CLERK")));
	}

	@Test
	public void testRegisterUserReturnsTrueWhenGivenNewAdminWithCorrectDataAndCurrentUserStatusIsAdmin()
			throws InvalidFieldException {
		assertTrue(users.registerUser(
				Arrays.asList("ADMIN", "otherUser", "user66User", "user66User", "user@gmail.com", "ADMIN")));
	}

	@Test(expected = InvalidFieldException.class)
	public void testLoginUserThrowsInvalidFieldExceptionWhenGivenInvalidUsername() throws InvalidFieldException {
		assertTrue(users.registerUser(registerData));

		users.loginUser(Arrays.asList("userS", "user66User"));
	}

	@Test(expected = InvalidFieldException.class)
	public void testLoginUserThrowsInvalidFieldExceptionWhenGivenInvalidPassword() throws InvalidFieldException {
		assertTrue(users.registerUser(registerData));

		users.loginUser(Arrays.asList("dummyUser", "user66Users"));
	}

	@Test
	public void testLoginUserReturnsFalseWhenGivenWrongNumberOfArguments() throws InvalidFieldException {
		assertTrue(users.registerUser(registerData));

		assertFalse(users.loginUser(Arrays.asList("dummyUser")));
	}

	@Test
	public void testLoginUserReturnsTrueWhenGivenCorrectData() throws InvalidFieldException {
		assertTrue(users.registerUser(registerData));

		assertTrue(users.loginUser(Arrays.asList("dummyUser", "user66User")));
	}

	@Test(expected = NoSuchUserException.class)
	public void testDeleteUserThrowsNoSuchUserExceptionWhenGivenInvalidUsername()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		users.deleteUser(Arrays.asList("userS"));
	}

	@Test
	public void testDeleteUserReturnsFalseWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		assertFalse(users.deleteUser(new ArrayList<>()));
	}

	@Test
	public void testDeleteUserReturnsTrueWhenGivenCorrectData() throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));
		assertTrue(users.deleteUser(Arrays.asList("dummyUser")));
	}

	@Test(expected = NoSuchUserException.class)
	public void testGetPersonalDetailsThrowsNoSuchUserExceptionWhenGivenUsernameNotInTheSystem()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		users.getPersonalDetails(Arrays.asList("dummyUser", "otherUser"));
	}

	@Test
	public void testGetPersonalDetailsReturnsNullWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		assertNull(users.getPersonalDetails(Arrays.asList("dummyUser")));
	}

	@Test
	public void testGetPersonalDetailsReturnsNullWhenGivenCurrentUserClerkAndCheckedUserAdmin()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		assertNull(users.getPersonalDetails(Arrays.asList("clerk", "admin")));
	}

	@Test
	public void testGetPersonalDetailsReturnsUserProfileWhenGivenUsernameAndPassword()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		assertEquals("dummyUser", users.getPersonalDetails(Arrays.asList("dummyUser", "dummyUser66")).getUsername());
		assertEquals(Status.CLIENT, users.getPersonalDetails(Arrays.asList("dummyUser", "dummyUser66")).getStatus());
	}

	@Test
	public void testGetPersonalDetailsReturnsUserProfileWhenGivenCurrentUserClerkAndCheckedUserUsername()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		assertEquals("dummyUser", users.getPersonalDetails(Arrays.asList("clerk", "dummyUser")).getUsername());
		assertEquals(Status.CLIENT, users.getPersonalDetails(Arrays.asList("clerk", "dummyUser")).getStatus());
	}

	@Test
	public void testGetPersonalDetailsReturnsUserProfileWhenGivenCurrentUserAdminAndCheckedUserUsername()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		assertEquals("dummyUser", users.getPersonalDetails(Arrays.asList("admin", "dummyUser")).getUsername());
		assertEquals(Status.CLIENT, users.getPersonalDetails(Arrays.asList("admin", "dummyUser")).getStatus());
	}

	@Test
	public void testGetPersonalDetailsReturnsUserProfileWhenGivenAdminIsSearchingForOtherUserInfo()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));
		assertTrue(users.registerUser(
				Arrays.asList("UNREGISTERED", "otherUser", "user66User", "user66User", "user@gmail.com", "CLERK")));

		assertEquals("otherUser", users.getPersonalDetails(Arrays.asList("admin", "otherUser")).getUsername());
		assertEquals(Status.CLERK, users.getPersonalDetails(Arrays.asList("admin", "otherUser")).getStatus());
	}

	@Test
	public void testCheckStatusReturnsUnregisteredWhenGivenUsernameIsUnregistered()
			throws InvalidFieldException, NoSuchUserException {
		assertEquals(Status.UNREGISTERED, users.checkUserStatus("someUser"));
	}

	@Test
	public void testCheckStatusReturnsClientWhenGivenUsernameIsAClient()
			throws InvalidFieldException, NoSuchUserException {
		assertEquals(Status.CLIENT, users.checkUserStatus("client"));
	}

	@Test
	public void testCheckStatusReturnsClerkWhenGivenUsernameIsAClerk()
			throws InvalidFieldException, NoSuchUserException {
		assertEquals(Status.CLERK, users.checkUserStatus("clerk"));
	}

	@Test
	public void testCheckStatusReturnsAdminWhenGivenUsernameIsAnAdmin()
			throws InvalidFieldException, NoSuchUserException {
		assertEquals(Status.ADMIN, users.checkUserStatus("admin"));
	}

	@Test(expected = NoSuchUserException.class)
	public void testSendMessageThrowsNoSuchUserExceptionWhenGivenUsernameNotInTheSystem()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		users.sendMessage(Arrays.asList("dummyUser", "otherUser", "message"), printWriter);
	}

	@Test
	public void testSendMessageReturnsFalseWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		assertFalse(users.sendMessage(Arrays.asList("dummyUser", "message"), printWriter));
	}

	@Test
	public void testSendMessageReturnsTrueWhenGivenCorrectData()
			throws InvalidFieldException, NoSuchUserException, IOException {
		assertTrue(users.registerUser(registerData));
		assertTrue(users.registerUser(
				Arrays.asList("UNREGISTERED", "otherUser", "user66User", "user66User", "user@gmail.com", "CLERK")));

		Socket socket = mock(Socket.class);
		BankServer.addUserSocket("otherUser", socket);
		when(socket.getOutputStream()).thenReturn(System.out);
		assertTrue(users.sendMessage(Arrays.asList("dummyUser", "otherUser", "message"), printWriter));
	}

	@Test(expected = NoSuchUserException.class)
	public void testReadMessagesThrowsNoSuchUserExceptionWhenGivenUsernameNotInTheSystem()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		users.readMessages(Arrays.asList("userS"), printWriter);
	}

	@Test
	public void testReadMessagesReturnsFalseWhenGivenWrongNumberOfArguments()
			throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		assertFalse(users.readMessages(new ArrayList<>(), printWriter));
	}

	@Test
	public void testReadMessagesReturnsTrueWhenGivenCorrectData() throws InvalidFieldException, NoSuchUserException {
		assertTrue(users.registerUser(registerData));

		assertTrue(users.readMessages(Arrays.asList("dummyUser"), printWriter));
	}
}
