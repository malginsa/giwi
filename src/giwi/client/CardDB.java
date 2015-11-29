package giwi.client;

import java.util.List;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

import giwi.shared.CardInfo;

public class CardDB {

	private static CardDB instance;
	
	ListDataProvider<CardInfo> dataProvider = new ListDataProvider<CardInfo>();
	
	private CardDB() {
	}

	public static CardDB get() {
		if (null == instance) {
			instance = new CardDB();
		}
		return instance;
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

	public void doRefreshListForm() {
		dataProvider.refresh();
	}
	
	public void put(List<CardInfo> list) {
		List<CardInfo> listData = dataProvider.getList();
		for (CardInfo cardInfo : list) {
			listData.add(cardInfo);
		}
// !!!
		dataProvider.refresh();
	}
	
}
