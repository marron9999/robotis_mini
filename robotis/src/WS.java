import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import robotis.Robotis;
import util.Util;

@ServerEndpoint("/ws")
public class WS {
	public static Robotis robotis = null;
	private static Session session;
	private ExecutorService service = Executors.newCachedThreadPool();

	private static String buffer = "";
	public static void notify(Session session, String t) {
		buffer += t;
		t = "";
		int p = buffer.indexOf("\n");
		if(p >= 0) {
			t = buffer.substring(0, p);
			buffer = buffer.substring(p + 1);
		}
		if(t.length() > 0) {
			try {
				if(session.isOpen())
					session.getBasicRemote().sendText("message " + t);
			} catch (Exception e) {
				// NONE
			}
		}
	}


	@OnOpen
	public void onOpen(Session session) {
		//System.out.println("onOpen: " + session.toString());
		if(robotis == null
		|| robotis.bluetooth == null) {
			robotis = new Robotis();
			try {
				robotis.out = new PrintStream(new OutputStream() {
					@Override
					public void write(byte b[], int off, int len) throws IOException {
						WS.notify(session, new String(b, off, len, "utf-8"));
					}
					@Override
					public void write(byte b[]) throws IOException {
						WS.notify(session, new String(b, "utf-8"));
					}
					@Override
					public void write(int b) throws IOException {
						WS.notify(session, String.valueOf(b));
					}
				}, true, "utf-8");
			} catch (Exception e) {
				// NONE
			}
			//initialize();
		}
		sendText(session, "ready robotis");
	}

	
	private void sendText(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (Exception e) {
			// NONE
		}
	}

	private class initialize implements Runnable {
		@Override
		public void run() {
			Session session = WS.session;
			Thread.currentThread().setName("open_robotis");
			PrintStream _out = System.out;
			PrintStream _err = System.err;
			System.setOut(robotis.out);
			System.setErr(robotis.out);
			robotis.initialize();
			System.setOut(_out);
			System.setErr(_err);

			try {
				WS.notify(session, "Connect " + robotis.bluetooth.name + "\n");
				sendText(session, "connect " + robotis.bluetooth.name);
			} catch (Exception e) {
				WS.notify(session, "Fial connect\n");
				sendText(session, "disconnect robotis");
			}
			Thread.currentThread().setName("-");
			WS.session = null;
		}
	}


	@OnMessage
	public void onMessage(String message, Session session) {
		//System.out.println("onMessage: " + session.toString() + ": " + message);
		String[] ope = message.split(" ");

		if(ope[0].equalsIgnoreCase("motion")) {
			int no = Util.parseInt(ope[1]);
			String t = robotis.properties.getProperty("motion." + no, null);
			sendText(session, "motion " + t);
			robotis.motion(no);
			sendText(session, "motion end");
			return;
		}

		if(ope[0].equalsIgnoreCase("motions")) {
			sendText(session, "motions [");
			int no = 0;
			while(true) {
				String t = robotis.properties.getProperty("motion." + no, null);
				if(t == null) break;
				sendText(session, "motions " + no + " " + t);
				no++;
			}
			sendText(session, "motions ]");
			return;
		}

		if(ope[0].equalsIgnoreCase("open")) {
			if(WS.session == null) {
				WS.session = session;
				service.execute(new initialize());
			}
			return;
		}

		if(ope[0].equalsIgnoreCase("close")) {
			robotis.close();
			sendText(session, "disconnect robotis");
			notify(session, "Disconnect  robotis\n");
			return;
		}

		if(ope[0].equalsIgnoreCase("verbose")) {
			if(ope[1].equalsIgnoreCase("0"))
				robotis.VERBOSE = false;
			else robotis.VERBOSE = true;
			return;
		}
	}

	@OnError
	public void onError(Session session, Throwable cause) {
		//System.out.println("onError: " + session.toString() + ": " + cause.toString());
	}

	@OnClose
	public void onClose(Session session) {
		//System.out.println("onClose: " + session.toString());
		robotis.terminate();
	}
}
