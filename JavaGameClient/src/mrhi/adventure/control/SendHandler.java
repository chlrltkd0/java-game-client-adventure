package mrhi.adventure.control;

import java.util.LinkedList;
import java.util.Queue;

import mrhi.adventure.model.Client;
import mrhi.adventure.model.packet.MyPacket;

public class SendHandler implements Runnable{
	
	private Queue<MyPacket> sendPacketQueue = new LinkedList<>();
	
	@Override
	public void run() {
		while(true) {	
			if(sendPacketQueue.size()!=0) {
				MyPacket packet = sendPacketQueue.remove();
				
				Client.getInstance().getConnectionManager().send(packet);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addPacket(MyPacket packet) {
		sendPacketQueue.add(packet);
	}
	
}