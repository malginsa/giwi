package giwi.client;

import java.util.List;

import com.google.gwt.user.client.Random;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

import giwi.shared.Card;

public class CardDatabase {

	private static CardDatabase instance;
	
	private CardDatabase() {
		queryCards(1);
	}

	public static CardDatabase get () {
		if (null == instance) {
			instance = new CardDatabase();
		}
		return instance;
	}
	
	ListDataProvider<Card> dataProvider = new ListDataProvider<Card>();
	
	/**
	 * Add a display to the database. The current range of interest of the display
	 * will be populated with data.
	 * 
	 * @param display a {@Link HasData}.
	 */
	public void addDataDisplay(HasData<Card> display) {
		dataProvider.addDataDisplay(display);
	}

	private void queryCards(Integer clientId) {
		List<Card> list = dataProvider.getList();
		for (Integer i = 10; i < 30; i++) {
			list.add(new Card("11111111111111" + i.toString(), Random.nextBoolean(), i));
		}
	}

	/**
	 * Refresh all displays.
	 */
	public void doRefreshListForm() {
		dataProvider.refresh();
	}
	
}
