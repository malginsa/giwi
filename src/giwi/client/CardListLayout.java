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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import giwi.shared.CardInfo;

public class CardListLayout extends Composite {

	interface Binder extends UiBinder<Widget, CardListLayout> { 
	};
	
	static class CardCell extends AbstractCell<CardInfo> {
		@Override
		public void render(Context context, CardInfo card, SafeHtmlBuilder sb) {
			if (null == card) {
				return;
			}
	      sb.appendHtmlConstant("<table><tr><td>");
	      sb.appendEscaped(card.getNumber());
	      sb.appendHtmlConstant("</td></tr><tr><td>");
	      sb.appendEscaped(card.getIsBlocked().toString());
	      sb.appendHtmlConstant("</td><td>");
	      sb.appendEscaped(card.getBalance().toString());
	      sb.appendHtmlConstant("</td><td>");
	      sb.appendHtmlConstant("</td></tr></table>");
		}
	}

	@UiField
	CardInfoForm cardForm;
	@UiField
	Button signOutButton;
	@UiField
	ShowMorePagerPanel pagerPanel;
//	ScrollPanel scroller;

	private CellList<CardInfo> cellList;

	public CardListLayout() {

		cellList = new CellList<CardInfo>(new CardCell(), CardInfo.KEY_PROVIDER);

		final SingleSelectionModel<CardInfo> selectionModel = 
				new SingleSelectionModel<CardInfo>(CardInfo.KEY_PROVIDER);
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

		CardDB.get().addDataDisplay(cellList);

//		scroller.add(cellList);
		pagerPanel.setDisplay(cellList);

		signOutButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get().clear();
				RootPanel.get().add(new Label("Выполняется выход, пожалуйста, подождите..."));
				GwtGiwi.giwiService.signOut(GwtGiwi.uuid, new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						RootPanel.get().clear();
						RootPanel.get().add(new Label("Вы вышли из системы"));
					}
					@Override
					public void onFailure(Throwable caught) {
						RootPanel.get().clear();
						RootPanel.get().add(new Label(caught.getMessage()));
					}
				});
			}
		});
	}
}
