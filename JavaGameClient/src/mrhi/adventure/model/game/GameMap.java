package mrhi.adventure.model.game;

import java.util.LinkedList;
import java.util.List;

import mrhi.adventure.model.Client;
import mrhi.adventure.model.vo.ItemVO;
import mrhi.adventure.model.vo.MapVO;
import mrhi.adventure.model.vo.MobVO;
import mrhi.adventure.model.vo.OtherCharacterVO;

public class GameMap {
	private int map_idx;
	private int map_width;
	private int map_height;
	private List<OtherCharacter> otherCharList;
	private List<CMob> mobList;
	private List<CItem> itemList;
	private List<Chat> chatList;
	
	public GameMap() {
		super();
	}
	
	public GameMap(MapVO mapVO){
		this.map_height = mapVO.getMap_height();
		this.map_width = mapVO.getMap_width();
		this.map_idx = mapVO.getMap_idx();
		this.otherCharList = new LinkedList<>();
		this.mobList = new LinkedList<>();
		this.itemList = new LinkedList<>();
		this.chatList = new LinkedList<>();
		
		for(OtherCharacterVO infoChar : mapVO.getOtherCharList())
			if(infoChar.getChr_idx()!=Client.getInstance().getGameManager().getMainCharacter().getChrIdx())
				this.otherCharList.add(new OtherCharacter(infoChar));
		
		for(MobVO mobVO : mapVO.getMobList())
			this.mobList.add(new CMob(mobVO));
		
		for(ItemVO item : mapVO.getItemList()) {
			this.itemList.add(new CItem(item));
		}
	}

	public GameMap(int map_idx, int map_width, int map_height) {
		super();
		this.map_idx = map_idx;
		this.map_width = map_width;
		this.map_height = map_height;
		this.otherCharList = new LinkedList<>();
		this.mobList = new LinkedList<>();
		this.chatList = new LinkedList<>();
	}
	public int getMap_idx() {
		return map_idx;
	}
	public void setMap_idx(int map_idx) {
		this.map_idx = map_idx;
	}
	public int getMap_width() {
		return map_width;
	}
	public void setMap_width(int map_width) {
		this.map_width = map_width;
	}
	public int getMap_height() {
		return map_height;
	}
	public void setMap_height(int map_height) {
		this.map_height = map_height;
	}
	public List<OtherCharacter> getOtherCharList() {
		return otherCharList;
	}
	public void setInfoCharList(List<OtherCharacter> otherCharList) {
		this.otherCharList = otherCharList;
	}
	public List<CMob> getMobList() {
		return mobList;
	}
	public void setMobList(List<CMob> mobList) {
		this.mobList = mobList;
	}
	public List<CItem> getItemList() {
		return itemList;
	}
	public List<Chat> getChatList() {
		return chatList;
	}
}
