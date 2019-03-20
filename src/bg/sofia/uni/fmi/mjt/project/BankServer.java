package bg.sofia.uni.fmi.mjt.project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bg.sofia.uni.fmi.mjt.project.accounts.AccountsDatabase;
import bg.sofia.uni.fmi.mjt.project.accounts.types.AccountTypeFactory;
import bg.sofia.uni.fmi.mjt.project.threads.ServerRunnable;
import bg.sofia.uni.fmi.mjt.project.users.UsersDatabase;

public class BankServer {
	private static final int PORT = 8086;
	private static final int THREADS = 5;

	private static Map<String, Socket> usersSockets = new HashMap<>();

	private UsersDatabase users;
	private AccountsDatabase accounts;

	public BankServer() {
		users = new UsersDatabase(DatabasesConstants.USERS_DATABASE);
		accounts = new AccountsDatabase(new AccountTypeFactory(), DatabasesConstants.ACCOUNTS_DATABASE);

	}

	public static void main(String[] args) {
		BankServer server = new BankServer();
		server.run();
	}

	private void run() {
		ExecutorService service = Executors.newFixedThreadPool(THREADS);

		try (ServerSocket serverSocket = new ServerSocket(PORT)) {

			while (true) {
				Socket socket = serverSocket.accept();

				ServerRunnable serverRunnable = new ServerRunnable(socket, users, accounts);
				service.execute(serverRunnable);
			}
		} catch (IOException e) {
			System.out.println(String.format("Port is used!%n%s", e.getMessage()));
		}
		service.shutdown();
	}

	public static synchronized Socket getUserSocket(String username) {
		return usersSockets.get(username);
	}

	public static synchronized void addUserSocket(String username, Socket socket) {
		usersSockets.put(username, socket);
	}

	public static synchronized void removeUserSocket(String username) {
		usersSockets.remove(username);
	}

}
