package com.orange.game.draw.model.user;

import android.R.integer;

import com.orange.game.draw.model.db.DbManager;
import com.orange.network.game.protocol.model.GameBasicProtos.PBGameCurrency;

public class AccountManager {
	private static final String TAG = AccountManager.class.getName();
	private int coinBalance = 0;
	private int ingotBalance = 0;
	
	// thread-safe singleton implementation
	private static AccountManager manager = new AccountManager();
	
	private AccountManager() {
		loadAccount();
	}
	
	public static AccountManager getInstance() {
		return manager;
	}

	private void loadAccount() {
		coinBalance = DbManager.getInstance().getCoinBalance();
		ingotBalance = DbManager.getInstance().getIngotBalance();
	}
	
	public int getBalance(PBGameCurrency currency) {
		switch (currency) {
		case Coin:
			return coinBalance;
			
		case Ingot:
			return ingotBalance;

		default:
			return 0;
		}
	}
	
	public void setBalance(PBGameCurrency currency, int balance) {
		switch (currency) {
		case Coin:
			coinBalance = balance;
			DbManager.getInstance().saveCoinBalance(coinBalance);
			break;
			
		case Ingot:
			ingotBalance = balance;
			DbManager.getInstance().saveIngotBalance(ingotBalance);
			break;

		default:
			break;
		}
	}
	
	
}
