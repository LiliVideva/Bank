package bg.sofia.uni.fmi.mjt.project.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.project.BankServer;

public class UserProfile implements Cloneable, Serializable {
	private static final long serialVersionUID = 1470258369L;

	private User user;
	private Map<String, List<String>> messagesInbox;

	UserProfile(User user) {
		this.user = user;
		messagesInbox = new HashMap<>();
	}

	UserProfile(User user, Map<String, List<String>> messagesInbox) {
		this.user = user;
		this.messagesInbox = messagesInbox;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messagesInbox == null) ? 0 : messagesInbox.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		UserProfile other = (UserProfile) obj;
		return ((user == null && other.user == null) || user.equals(other.user));
	}

	@Override
	public UserProfile clone() {
		UserProfile clonedProfile = new UserProfile(user.clone());
		clonedProfile.messagesInbox = new HashMap<>();

		List<String> clonedMessages = new ArrayList<>();
		for (String key : this.messagesInbox.keySet()) {
			this.messagesInbox.get(key).stream().forEach(x -> clonedMessages.add(x));
			clonedProfile.messagesInbox.put(key, clonedMessages);
			clonedMessages.clear();
		}
		return clonedProfile;
	}

	public String getUsername() {
		return user.getUsername();
	}

	public Status getStatus() {
		return user.getStatus();
	}

	synchronized boolean receiveMessage(String sender, String message, PrintWriter printWriter) {
		List<String> messagesFromSender = messagesInbox.get(sender);

		if (messagesFromSender == null) {
			messagesFromSender = new ArrayList<>();
		}
		messagesFromSender.add(message);
		messagesInbox.put(sender, messagesFromSender);
		displayPopUp();

		printWriter.println("Sent!");
		return true;
	}

	void displayPopUp() {
		try {
			PrintWriter receiverPrintWriter = new PrintWriter(BankServer.getUserSocket(getUsername()).getOutputStream(),
					true);
			receiverPrintWriter.printf("You have %d new message(s) from users!%n", unreadMessagesCount());
		} catch (IOException e) {
			throw new RuntimeException(String.format("Problem creating receiver print writer!%n%s", e.getMessage()));
		}
	}

	int unreadMessagesCount() {
		return messagesInbox.values().stream().mapToInt(List::size).sum();
	}

	void printMessagesInbox(PrintWriter printWriter) {
		messagesInbox.entrySet().stream().forEach(messagesFromUser -> {
			printWriter.printf("Sender: %s%n", messagesFromUser.getKey());
			messagesFromUser.getValue().stream().forEach(message -> printWriter.printf("Message: %s%n", message));
		});
		messagesInbox.clear();
	}

	void printUserDetails(PrintWriter printWriter) {
		user.printUserDetails(printWriter);
	}

	User getUser() {
		return user;
	}

	Map<String, List<String>> getMessagesInbox() {
		return messagesInbox;
	}

}
