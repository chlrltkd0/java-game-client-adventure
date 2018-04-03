package mrhi.adventure.control;

import java.io.IOException;
import java.util.List;

import mrhi.adventure.model.Client;
import mrhi.adventure.model.MyLog;
import mrhi.adventure.model.game.Chat;
import mrhi.adventure.model.game.GameMap;
import mrhi.adventure.model.game.CItem;
import mrhi.adventure.model.game.MainCharacter;
import mrhi.adventure.model.game.CMob;
import mrhi.adventure.model.game.OtherCharacter;
import mrhi.adventure.model.packet.MyPacket;
import mrhi.adventure.model.vo.AccountVO;
import mrhi.adventure.model.vo.AuthenticationVO;
import mrhi.adventure.model.vo.CharacterVO;
import mrhi.adventure.model.vo.ChatVO;
import mrhi.adventure.model.vo.IntegerVO;
import mrhi.adventure.model.vo.ItemVO;
import mrhi.adventure.model.vo.MapVO;
import mrhi.adventure.model.vo.MobVO;
import mrhi.adventure.model.vo.OtherCharacterVO;
import mrhi.adventure.view.ChangePWView;
import mrhi.adventure.view.FindAccountView;
import mrhi.adventure.view.GameView;

public class ReceiveHandler implements Runnable{

private ConnectionManager connectionManager;
	
	public ReceiveHandler(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	@Override
	public void run() {
		AccountVO avo = null;
		CharacterVO cvo = null;
		OtherCharacterVO ic = null;
		MapVO mapvo = null;
		MobVO mobvo = null;
		ItemVO itemVO = null;
		ChatVO chatVO = null;
		IntegerVO ivo = null;
		AuthenticationVO authVO = null;
		List<OtherCharacter> olist = null;
		List<CMob> mlist = null;
		List<CItem> ilist = null;
		List<Chat> clist = null;
		
		while(true)
		{
			MyPacket packet = null;
			try {
				packet = (MyPacket)connectionManager.receive();
			} catch (ClassNotFoundException | IOException e) {
				System.exit(0);
				e.printStackTrace();
			}
			switch(packet.getType())
			{
			case 13:
				MyLog.log("AccountVO 패킷도착!");
				avo = (AccountVO)packet.getSubObject();
				Client.getInstance().getAccountManager().setAccount(avo);
				break;
				
			case 142: // 뷰랑 컨트롤러 분리
				avo = (AccountVO)packet.getSubObject();
				FindAccountView.idsList.add(avo);
				break;
				
			case 152: // 뷰랑 컨트롤러 분리
				System.out.println("비번 인증번호 받기");
				avo = (AccountVO)packet.getSubObject();
				System.out.println(avo.getAcc_id());
				ChangePWView.staticAVO = avo;
				break;
				
			case 162: // 뷰랑 컨트롤러 분리
				System.out.println("가입인증번호 받기");
				authVO = (AuthenticationVO)packet.getSubObject();
				GameView.certification = authVO.isConfirm();
				break;
				
			case 23:
				MyLog.log("CharacterVO 패킷도착!");
				cvo = (CharacterVO)packet.getSubObject();
				System.out.println(cvo);
				System.out.println(Client.getInstance().getGameManager().getCurrentMap());
				Client.getInstance().getGameManager().setMainCharacter(new MainCharacter(cvo));				
				break;
				
			case 25:
				olist = Client.getInstance().getGameManager().getCurrentMap().getOtherCharList();
				ic = (OtherCharacterVO)packet.getSubObject();
				for(int i=0; i<olist.size(); i++)
				{
					if(olist.get(i).getChr_idx()==ic.getChr_idx())
					{
						olist.get(i).update(ic);
					}
				}
				break;
				
			case 26:
				MyLog.log("InfoCharacter 패킷도착!");
				ic = (OtherCharacterVO)packet.getSubObject();
				if(ic.getChr_idx()!=Client.getInstance().getGameManager().getMainCharacter().getChrIdx())
					Client.getInstance().getGameManager().getCurrentMap().getOtherCharList().add(new OtherCharacter(ic));
				break;
				
			case 27:
				MyLog.log("InfoCharacter 패킷도착!");
				olist = Client.getInstance().getGameManager().getCurrentMap().getOtherCharList();
				ic = (OtherCharacterVO)packet.getSubObject();
				for(OtherCharacter otherChar : olist)
				{
					if(otherChar.getChr_idx()==ic.getChr_idx()) {
						olist.remove(otherChar);
						break;
					}
				}
				break;
				
			case 29:
				MyLog.log("CharacterVO 패킷도착! - 리스트에 추가하자");
				cvo = (CharacterVO)packet.getSubObject();
				if(!isContainCharVO(Client.getInstance().getCharacterManager().getCharacterList(), cvo)) {
					Client.getInstance().getCharacterManager().getCharacterList().add(cvo);
				}
				break;
				
			case 31:
				MyLog.log("MapVO 패킷도착!");
				mapvo = (MapVO)packet.getSubObject();
				GameMap gameMap = new GameMap(mapvo);
				Client.getInstance().getGameManager().setCurrentMap(gameMap);
				Client.getInstance().getGameManager().getMainCharacter().setMap(gameMap);
				break;
				
			case 41:
				MyLog.log("MobVO 누가 죽였대!");
				mlist = Client.getInstance().getGameManager().getCurrentMap().getMobList();
				mobvo = (MobVO)packet.getSubObject();
				for(CMob mob : mlist)
				{
					if(mob.getGen_idx()==mobvo.getGen_idx())
					{
						mlist.remove(mob);
						Client.getInstance().getGameManager().getMainCharacter().removeNextObserver(mob);
						break;
					}
				}
				break;
				
			case 42:
				mlist = Client.getInstance().getGameManager().getCurrentMap().getMobList();
				mobvo = (MobVO)packet.getSubObject();
				mlist.add(new CMob(mobvo));
				break;
				
			case 43:
				System.out.println("item DROp!");
				ilist = Client.getInstance().getGameManager().getCurrentMap().getItemList();
				itemVO = (ItemVO)packet.getSubObject();
				ilist.add(new CItem(itemVO));
				break;
				
			case 45:
				System.out.println("item 사라졌다");
				ilist = Client.getInstance().getGameManager().getCurrentMap().getItemList();
				itemVO = (ItemVO)packet.getSubObject();
				for(CItem item : ilist) {
					if(item.getGen_idx()==itemVO.getGen_idx()) {
						ilist.remove(item);
						break;
					}
				}
				break;
				
			case 51:
				clist = Client.getInstance().getGameManager().getCurrentMap().getChatList();
				chatVO = (ChatVO)packet.getSubObject();
				if(clist.size()>30)
					clist.remove(0);
				clist.add(new Chat(chatVO));
				break;
				
			case 61:
				ilist = Client.getInstance().getGameManager().getMainCharacter().getItemList();
				itemVO = (ItemVO)packet.getSubObject();
				ilist.add(new CItem(itemVO));
				System.out.println(itemVO.getItem_name());
				break;
				
			case 62:
				MyLog.log("CharacterVO 경험치 레벨!");
				cvo = (CharacterVO)packet.getSubObject();
				Client.getInstance().getGameManager().getMainCharacter().updateState(cvo);
				break;
				
			case 71:
				ivo = (IntegerVO)packet.getSubObject();
				Client.getInstance().getGameManager().getMainCharacter().minusHp(ivo.getValue());
				break;
				
			case 72:
				Client.getInstance().getGameManager().getMainCharacter().setHp(0);
				Client.getInstance().getGameManager().getMainCharacter().setDead(true);
				break;
				
			case 73:
				MyLog.log("누가 뒤졌대");
				olist = Client.getInstance().getGameManager().getCurrentMap().getOtherCharList();
				ic = (OtherCharacterVO)packet.getSubObject();
				for(OtherCharacter otherChar : olist) {
					if(otherChar.getChr_idx()==ic.getChr_idx()) {
						otherChar.setDead(true);
						break;
					}
				}
				break;
				
			case 81:
				MyLog.log("스탯올린상태 왔다");
				cvo = (CharacterVO)packet.getSubObject();
				Client.getInstance().getGameManager().getMainCharacter().updateStat(cvo);
			}
		}
	}
	
	public boolean isContainCharVO(List<CharacterVO> charVOList, CharacterVO charVO) {
		for(CharacterVO cvo : charVOList) {
			if(cvo.getChr_idx()==charVO.getChr_idx())
				return true;
		}
		return false;
	}
}
