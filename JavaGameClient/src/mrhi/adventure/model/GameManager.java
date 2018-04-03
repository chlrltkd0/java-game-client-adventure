package mrhi.adventure.model;

import mrhi.adventure.model.game.GameMap;
import mrhi.adventure.model.game.MainCharacter;
import mrhi.adventure.model.game.OtherCharacter;
import mrhi.adventure.model.packet.MyPacket;
import mrhi.adventure.model.vo.ChatVO;
import mrhi.adventure.model.vo.IntegerVO;
import mrhi.adventure.model.vo.ItemVO;
import mrhi.adventure.model.vo.MapVO;
import mrhi.adventure.model.vo.MobVO;

public class GameManager {
	private MainCharacter mainCharacter;
	private GameMap currentMap; //게임맵 그리기
	private Thread timeThread;
	
	public GameManager() { }
	
	public void startTimeElapse() {
		TimeElapseRun te = new TimeElapseRun();
		timeThread = new Thread(te);
		timeThread.start();
	}
	
	public void stopTimeElapse() {
		timeThread.interrupt();
	}
	
	public void upStat(IntegerVO integerVO) {
		System.out.println("스탯올리기 요청 패킷 전송!");
		Client.getInstance().getSendHandler().addPacket(new MyPacket(80, integerVO));
	}
	
	public void requestMap(MapVO map) {
		System.out.println("맵 요청 패킷 전송!");
		Client.getInstance().getSendHandler().addPacket(new MyPacket(30, map));
	}
	
	public void notifyPosition() {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(24, mainCharacter.makeInfoCharacter()));
	}
	
	public void killMob(MobVO mobVO) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(40, mobVO));
	}
	
	public void gatherItem(ItemVO itemVO) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(44, itemVO));
	}
	
	public void sendChat(ChatVO chatVO) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(50, chatVO));
	}
	
	public void requestItemList() {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(60, null));
	}
	
	public GameMap getCurrentMap() {
		return currentMap;
	}

	public void setCurrentMap(GameMap currentMap) {
		this.currentMap = currentMap;
	}
	
	public MainCharacter getMainCharacter() {
		return mainCharacter;
	}

	public void setMainCharacter(MainCharacter mainCharacter) {
		this.mainCharacter = mainCharacter;
	}

	public Thread getTimeThread() {
		return timeThread;
	}

	public void setTimeThread(Thread timeThread) {
		this.timeThread = timeThread;
	}

	public class TimeElapseRun implements Runnable {
		@Override
		public void run() {
			while(true)
			{
				if(mainCharacter!=null) {
					mainCharacter.autoMove();
				}
				
				for(OtherCharacter oc :currentMap.getOtherCharList())
				{
					if(mainCharacter.getChrIdx()!= oc.getChr_idx())
						oc.autoMove();
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
		}
		
	}
}
