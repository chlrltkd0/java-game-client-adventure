package mrhi.adventure.model;

import java.io.IOException;
import java.net.Socket;

import mrhi.adventure.control.ConnectionManager;
import mrhi.adventure.control.ReceiveHandler;
import mrhi.adventure.control.SendHandler;

public class Client {
	
	private static Client theInstance = new Client();
	private ConnectionManager connectionManager; //연결하는 소켓클래스
	private AccountManager accountManager = new AccountManager();//로그인 클래스
	private CharacterManager characterManager = new CharacterManager();//로그인 클래스
	private GameManager gameManager = new GameManager();//게임클래스
	private SendHandler sendHandler = new SendHandler();
	
	private Client() { }
	
	public void connect()
	{
		try {
			this.connectionManager = new ConnectionManager(new Socket("192.168.0.96", 21212));
			System.out.println("연결성공!");
			
			ReceiveHandler rh = new ReceiveHandler(connectionManager);
			Thread rThread = new Thread(rh);
			rThread.start();
			Thread sThread = new Thread(sendHandler);
			sThread.start();
			
		} catch (IOException e) {
			System.out.println("서버가 열려있지 않습니다.");
			e.printStackTrace();
		}
	}
	
	public static Client getInstance() {
		return theInstance;
	}
	
	public AccountManager getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(AccountManager accountManagement) {
		this.accountManager = accountManagement;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager game) {
		this.gameManager = game;
	}

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public CharacterManager getCharacterManager() {
		return characterManager;
	}

	public void setCharacterManager(CharacterManager characterManager) {
		this.characterManager = characterManager;
	}

	public SendHandler getSendHandler() {
		return sendHandler;
	}

	public void setSendHandler(SendHandler sendHandler) {
		this.sendHandler = sendHandler;
	}
}
