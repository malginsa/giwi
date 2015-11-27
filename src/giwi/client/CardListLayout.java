package giwi.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import giwi.shared.Card;

public class CardListLayout extends Composite {

	interface Binder extends UiBinder<Widget, CardListLayout> { 
	};
	
	static class CardCell extends AbstractCell<Card> {
		@Override
		public void render(Context context, Card card, SafeHtmlBuilder sb) {
			if (null == card) {
				return;
			}
	      sb.appendHtmlConstant("<table><tr><td>");
	      sb.appendEscaped(card.getNumber());
	      sb.appendHtmlConstant("</td></tr><tr><td>");
	      sb.appendEscaped(card.getBalance().toString());
	      sb.appendHtmlConstant("</td></tr></table>");
		}
	}

	@UiField
	CardInfoForm cardForm;
	@UiField
	Button signOutButton;
	@UiField
	ShowMorePagerPanel pagerPanel;

	private CellList<Card> cellList;

	public CardListLayout() {

		cellList = new CellList<Card>(new CardCell(), Card.KEY_PROVIDER);

		final SingleSelectionModel<Card> selectionModel = new SingleSelectionModel<Card>(
				Card.KEY_PROVIDER);
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				cardForm.selectCard(selectionModel.getSelectedObject());
			}
		});

		Binder binder = GWT.create(Binder.class);
		Widget widget = binder.createAndBindUi(this);
		initWidget(widget);

		CardDatabase.get().addDataDisplay(cellList);

		pagerPanel.setDisplay(cellList);

		signOutButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Вы действительно желали бы выйти?");
			}
		});
	}
}
