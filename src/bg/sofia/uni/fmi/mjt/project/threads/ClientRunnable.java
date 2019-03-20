package bg.sofia.uni.fmi.mjt.project.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientRunnable implements Runnable {
	private Socket socket;

	public ClientRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			while (true) {
				String line = bufferedReader.readLine();

				if (line == null) {
					return;
				}
				System.out.println(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(
					String.format("Problem getting input stream from client socket!%n%s", e.getMessage()));
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				throw new RuntimeException(
						String.format("Problem closing print writer and client socket!%n%s", e.getMessage()));
			}
		}
	}

}
