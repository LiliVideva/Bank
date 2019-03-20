package bg.sofia.uni.fmi.mjt.project.users;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.project.exceptions.InvalidFieldException;
import bg.sofia.uni.fmi.mjt.project.exceptions.NoSuchUserException;

public class UsersDatabase implements Cloneable {
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private static final int FIVE = 5;
	private static final int SIX = 6;

	private static Set<String> clerksUsernames;
	private static Set<String> adminsUsernames;

	private String database;
	private List<UserProfile> usersProfiles;

	public UsersDatabase(String database) {
		this.database = database;
		clerksUsernames = new HashSet<>();
		adminsUsernames = new HashSet<>();

		usersProfiles = new ArrayList<>();
		readFromDatabase();

		if (usersProfiles.isEmpty()) {
			defineAdminAndClerk();
			writeProfilesInDatabase();
		}

		collectClerksAndAdminsUsernames();
	}

	@Override
	public UsersDatabase clone() {
		UsersDatabase clonedUsersInformation = new UsersDatabase(this.database);

		List<UserProfile> clonedUsersProfiles = new ArrayList<>();
		for (UserProfile userProfile : usersProfiles) {
			clonedUsersProfiles.add(userProfile.clone());
		}
		clonedUsersInformation.usersProfiles = clonedUsersProfiles;

		return clonedUsersInformation;
	}

	public void readFromDatabase() {
		usersProfiles = restoreProfilesFromDatabase();
	}

	private void defineAdminAndClerk() {
		UserProfile admin = new UserProfile(new User("admin", "adminAdmin5", "admin@gmail.com", Status.ADMIN));
		UserProfile clerk = new UserProfile(new User("clerk", "clerk12Clerk", "clerk@gmail.com", Status.CLERK));

		usersProfiles.add(admin);
		usersProfiles.add(clerk);
	}

	@SuppressWarnings("unchecked")
	protected List<UserProfile> restoreProfilesFromDatabase() {
		File usersDatabase = new File(database);

		if (usersDatabase.exists() && usersDatabase.length() != 0) {
			try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(database))) {
				synchronized (database) {
					return (List<UserProfile>) objectInputStream.readObject();
				}
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(String.format("Problem reading from users database!%n%s", e.getMessage()));
			}
		}
		return new ArrayList<>();
	}

	public void writeProfilesInDatabase() {
		setUpDatabase();

		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(database, false))) {
			synchronized (database) {
				objectOutputStream.writeObject(usersProfiles);
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format("Problem writing in users database!%n%s", e.getMessage()));
		}
	}

	private void setUpDatabase() {
		synchronized (database) {
			File resources = new File("resources");
			if (!resources.exists()) {
				resources.mkdir();
			}

			File usersDatabase = new File(database);
			if (!usersDatabase.exists()) {
				try {
					usersDatabase.createNewFile();
				} catch (IOException e) {
					throw new RuntimeException(String.format("Problem creating users database!%n%s", e.getMessage()));
				}
			}
		}
	}

	private void collectClerksAndAdminsUsernames() {
		usersProfiles.stream().filter(x -> x.getStatus().equals(Status.ADMIN))
				.forEach(x -> adminsUsernames.add(x.getUsername()));
		usersProfiles.stream().filter(x -> x.getStatus().equals(Status.CLERK))
				.forEach(x -> clerksUsernames.add(x.getUsername()));
	}

	public synchronized boolean registerUser(List<String> arguments) throws InvalidFieldException {
		if (validateArgumentsListSize(arguments, SIX)) {
			Status currentUserStatus = Status.valueOf(arguments.get(0));
			String username = arguments.get(1);
			String password = arguments.get(2);
			String repeatedPassword = arguments.get(THREE);
			String email = arguments.get(FOUR);
			Status status = Status.valueOf(arguments.get(FIVE).toUpperCase());

			if (status.equals(Status.CLERK)) {
				clerksUsernames.add(username);
			}
			if (status.equals(Status.ADMIN)) {
				if (currentUserStatus.equals(Status.ADMIN)) {
					adminsUsernames.add(username);
				} else {
					System.out.println("Can't register as admin!");
					return false;
				}
			}

			if (usernameAlreadyInTheSystem(username)) {
				System.out.println("User already exists!");
				return false;
			}

			if (checkArguments(password, repeatedPassword, email)) {
				UserProfile newUserProfile = new UserProfile(new User(username, password, email, status));

				try {
					getPersonalDetails(Arrays.asList(username, username));
				} catch (NoSuchUserException e) {
					usersProfiles.add(newUserProfile);
					return true;
				}
			}
			System.out.println("Invalid arguments!");
		}
		return false;
	}

	public boolean loginUser(List<String> arguments) throws InvalidFieldException {
		if (validateArgumentsListSize(arguments, 2)) {
			String username = arguments.get(0);
			String password = arguments.get(1);

			if (usernameAlreadyInTheSystem(username)) {
				for (UserProfile userProfile : usersProfiles) {
					User user = userProfile.getUser();

					if (username.equals(user.getUsername())) {
						if ((password.hashCode()) == user.getPassword()) {
							System.out.printf("You have %d new message(s) from users!%n",
									userProfile.unreadMessagesCount());
							return true;
						}
						throw new InvalidFieldException(String.format("Invalid password: %s", password));
					}
				}
			}
			throw new InvalidFieldException(String.format("Invalid username: %s", username));
		}

		return false;
	}

	public synchronized boolean deleteUser(List<String> arguments) throws NoSuchUserException {
		if (validateArgumentsListSize(arguments, 1)) {
			String username = arguments.get(0);

			return usersProfiles.remove(findProfileByUsername(username));
		}
		return false;
	}

	public UserProfile getPersonalDetails(List<String> arguments) throws NoSuchUserException {
		if (validateArgumentsListSize(arguments, 2)) {
			String currentUserUsername = arguments.get(0);
			String argument = arguments.get(1);
			String checkedUserUsername = "";

			try {
				if (validatePassword(argument)) {
					checkedUserUsername = currentUserUsername;
				}
			} catch (InvalidFieldException e) {
				checkedUserUsername = argument;
			}

			UserProfile currentUserProfile = findProfileByUsername(currentUserUsername);
			UserProfile checkedUserProfile = findProfileByUsername(checkedUserUsername);

			if (checkedUserProfile.equals(currentUserProfile)
					|| (currentUserProfile.getStatus().equals(Status.CLERK)
							&& checkedUserProfile.getStatus().equals(Status.CLIENT))
					|| currentUserProfile.getStatus().equals(Status.ADMIN)) {

				return checkedUserProfile;
			}
		}
		return null;
	}

	public boolean printPersonalDetails(List<String> arguments, PrintWriter printWriter)
			throws NoSuchUserException, InvalidFieldException {
		UserProfile userProfile = getPersonalDetails(arguments);

		if (userProfile == null) {
			printWriter.println("Problem getting details! Access denied or wrong number of arguments!");
			return false;
		}
		userProfile.printUserDetails(printWriter);
		return true;
	}

	public Status checkUserStatus(String username) {
		if (adminsUsernames.contains(username)) {
			return Status.ADMIN;
		} else if (clerksUsernames.contains(username)) {
			return Status.CLERK;
		} else {
			try {
				findProfileByUsername(username);
			} catch (NoSuchUserException e) {
				return Status.UNREGISTERED;
			}
		}
		return Status.CLIENT;
	}

	public boolean sendMessage(List<String> arguments, PrintWriter printWriter) throws NoSuchUserException {
		if (validateArgumentsListSize(arguments, THREE)) {
			String sender = arguments.get(0);
			String receiver = arguments.get(1);
			String message = arguments.get(2);

			UserProfile receiverProfile = findProfileByUsername(receiver);
			synchronized (receiverProfile) {
				return receiverProfile.receiveMessage(sender, message, printWriter);
			}
		}
		return false;
	}

	public boolean readMessages(List<String> arguments, PrintWriter printWriter) throws NoSuchUserException {
		if (validateArgumentsListSize(arguments, 1)) {
			String username = arguments.get(0);

			UserProfile userProfile = findProfileByUsername(username);
			synchronized (userProfile) {
				userProfile.printMessagesInbox(printWriter);
			}
			return true;
		}
		return false;
	}

	private boolean validateArgumentsListSize(List<String> arguments, int expectedSize) {
		return expectedSize == arguments.size();
	}

	private boolean checkArguments(String password, String repeatedPassword, String email)
			throws InvalidFieldException {
		return (validatePassword(password) && password.equals(repeatedPassword) && validateEmail(email));
	}

	private boolean usernameAlreadyInTheSystem(String username) {
		return usersProfiles.stream().map(UserProfile::getUsername).anyMatch(x -> x.equals(username));
	}

	private boolean validatePassword(String password) throws InvalidFieldException {
		if (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")) {
			return true;
		}
		throw new InvalidFieldException(String.format("Invalid password format: %s", password));
	}

	private boolean validateEmail(String email) throws InvalidFieldException {
		if (email.matches("^([a-z]+[A-Za-z0-9_-]*)@([a-z0-9]+\\.[a-z]+)$")) {
			return true;
		}
		throw new InvalidFieldException(String.format("Invalid email format: %s", email));
	}

	private UserProfile findProfileByUsername(String username) throws NoSuchUserException {
		Optional<UserProfile> userProfile = usersProfiles.stream().filter(x -> x.getUsername().equals(username))
				.findFirst();

		if (userProfile.isPresent()) {
			return userProfile.get();
		}
		throw new NoSuchUserException(String.format("No such user: %s", username));
	}

	List<UserProfile> getUsers() {
		return usersProfiles;
	}

}
