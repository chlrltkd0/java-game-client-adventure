package mrhi.adventure.model;

import java.util.ArrayList;
import java.util.List;

import mrhi.adventure.model.packet.MyPacket;
import mrhi.adventure.model.vo.CharacterVO;

public class CharacterManager {

	private List<CharacterVO> CharacterList = new ArrayList<CharacterVO>();

	public void requestCharacterList() {
		System.out.println("ĳ���͸���Ʈ ��û ��Ŷ ����!");
		Client.getInstance().getSendHandler().addPacket(new MyPacket(28, null));
	}
	
	public void createCharacter(CharacterVO charVO) {
		System.out.println("�ɸ��� ������û ��Ŷ ����!");
		Client.getInstance().getSendHandler().addPacket(new MyPacket(20, charVO));
	}
	
	public void requestCharacter(CharacterVO charVO) {
		System.out.println("�ɸ��� ��û ��Ŷ ����!");
		Client.getInstance().getSendHandler().addPacket(new MyPacket(22, charVO));
	}
	
	public void deleteCharacter(CharacterVO charVO) {
		System.out.println("�ɸ��� ������û ��Ŷ ����!");
		Client.getInstance().getSendHandler().addPacket(new MyPacket(21, charVO));
	}
	
	public List<CharacterVO> getCharacterList() {
		return CharacterList;
	}

	public void setCharacterList(List<CharacterVO> characterList) {
		CharacterList = characterList;
	} 
}
