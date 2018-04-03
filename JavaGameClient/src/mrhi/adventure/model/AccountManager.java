package mrhi.adventure.model;

import mrhi.adventure.model.packet.MyPacket;
import mrhi.adventure.model.vo.AccountVO;
import mrhi.adventure.model.vo.AuthenticationVO;

public class AccountManager {
	
	private AccountVO account;

	public AccountManager() { }

	public void register(AccountVO avo) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(10, avo));
	}
	public void login(AccountVO avo) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(12, avo));
	}
	public void requestFindIDAuthEmail(AccountVO avo) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(140, avo));
	}
	public void requestFindPWAuthEmail(AccountVO avo) {
		System.out.println(avo.getAcc_id());
		System.out.println(avo.getAcc_name());
		Client.getInstance().getSendHandler().addPacket(new MyPacket(150, avo));
	}
	public void requestRegisterAuthEmail(AccountVO avo) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(160, avo));
	}
	public void requestFindIDAuth(AuthenticationVO avo) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(141, avo));
	}
	public void requestFindPWAuth(AuthenticationVO avo) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(151, avo));
	}
	public void requestRegisterAuth(AuthenticationVO avo) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(161, avo));
	}
	public void changePassword(AccountVO avo) {
		Client.getInstance().getSendHandler().addPacket(new MyPacket(17, avo));
	}
	public AccountVO getAccount() {
		return account;
	}

	public void setAccount(AccountVO account) {
		this.account = account;
	}
}
