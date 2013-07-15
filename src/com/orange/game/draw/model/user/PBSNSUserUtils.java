package com.orange.game.draw.model.user;

import java.util.List;

import com.orange.game.constants.ServiceConstant;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameUser;
import com.orange.network.game.protocol.model.GameBasicProtos.PBSNSUser;

public class PBSNSUserUtils {
	private static boolean isBindSNS(PBGameUser pbUser, int snsType){
		List<PBSNSUser> list = pbUser.getSnsUsersList();
		if (list == null || list.size() == 0){
			return false;
		}
		
		for (PBSNSUser user : list){
			if (user.getType() == snsType)
				return true;
		}
		
		return false;
	}
	
	public static boolean isBindFacebook(PBGameUser pbUser){
		return isBindSNS(pbUser, ServiceConstant.REGISTER_TYPE_FACEBOOK);
	}

	public static boolean isBindSina(PBGameUser pbUser){
		return isBindSNS(pbUser, ServiceConstant.REGISTER_TYPE_SINA);
	}

	public static boolean isBindQQ(PBGameUser pbUser){
		return isBindSNS(pbUser, ServiceConstant.REGISTER_TYPE_QQ);
	}
}
