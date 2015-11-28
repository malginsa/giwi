package giwi.client;

import java.util.List;

import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

import giwi.shared.CardInfo;

public class CardDB {

	private static CardDB instance;
	
	ListDataProvider<CardInfo> dataProvider = new ListDataProvider<CardInfo>();
	
	private CardDB() {
		queryCards(1);
	}

	public static CardDB get() {
		if (null == instance) {
			instance = new CardDB();
		}
		return instance;
	}
	
//	!!!
	public void put(List<CardInfo> list) {
		List<CardInfo> listData = dataProvider.getList();
		for (CardInfo cardInfo : listData) {
			listData.add(cardInfo);
		}
	}
	
	
	/**
	 * Add a display to the database. The current range of interest of the display
	 * will be populated with data.
	 * 
	 * @param display a {@Link HasData}.
	 */
	public void addDataDisplay(HasData<CardInfo> display) {
		dataProvider.addDataDisplay(display);
	}

	private void queryCards(Integer clientId) {
		List<CardInfo> list = dataProvider.getList();
		for (Integer i = 10; i < 30; i++) {
			list.add(new CardInfo("11111111111111" + i.toString(), Random.nextBoolean(), i));
		}
	}

	/**
	 * Refresh all displays.
	 */
	public void doRefreshListForm() {
		dataProvider.refresh();
	}
	
}
