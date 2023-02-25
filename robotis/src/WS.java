import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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
	public static String buffer = "";
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

	private void initialize() {
		PrintStream _out = System.out;
		PrintStream _err = System.err;
		System.setOut(robotis.out);
		System.setErr(robotis.out);
		robotis.initialize();
		System.setOut(_out);
		System.setErr(_err);
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
			initialize();
			try {
				sendText(session, "connect " + robotis.bluetooth.name);
				notify(session, "Connect" + robotis.bluetooth.name + "\n");
				return;
			} catch (Exception e) {
				notify(session, "Fial connect\n");
			}
			sendText(session, "disconnect robotis");
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
