package giwi.client;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import giwi.shared.CardInfo;

public class GwtGiwi implements EntryPoint {

	public static final GiwiServiceAsync giwiService = GWT.create(GiwiService.class);

	public static Long uuid;
	public static String clientName;

	public void onModuleLoad() {
		signIn();
	}

	public void signIn() {
		
		RootPanel.get().clear();

		VerticalPanel verticalPanel = new VerticalPanel();

		final TextBox nameTextBox = new TextBox();
		verticalPanel.add(new HorizontalPanel() {{ 
			add(new Label("Ваше имя ")); 
			add(nameTextBox); 
		}} );

		final TextBox passwordTextBox = new PasswordTextBox();
		verticalPanel.add(new HorizontalPanel() {{ 
			add(new Label("Пароль ")); 
			add(passwordTextBox); 
		}} );

		final Button okButton = new Button("Ok");
		verticalPanel.add(okButton);

		RootPanel.get().add(verticalPanel);
		nameTextBox.setFocus(true);

		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				giwiService.signIn(nameTextBox.getText(), 
					passwordTextBox.getText(), new AsyncCallback<Long>() {

						@Override
						public void onSuccess(Long result) {
							clientName = nameTextBox.getText();
							uuid = result;
							if (0 == result) {
								Window.alert("Вы вошли в систему как администратор");
								adminPanel();
							} else {
								acquireCardInfo();
							}
						}

						@Override
						public void onFailure(Throwable caught) {
//							okButton.setEnabled(false);
							nameTextBox.setText("");
							passwordTextBox.setText("");
							Window.alert(caught.getMessage());
							okButton.setEnabled(true);
							nameTextBox.setFocus(true);
						}
					});
			}
		});
	}

	private void acquireCardInfo() {

		showProcessingPanel("Получение данных с сервера");

		giwiService.getCardInfo(uuid, new AsyncCallback<List<CardInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				byePanel(caught.getMessage());
			}
			@Override
			public void onSuccess(List<CardInfo> result) {
				CardDB.get().put(result);
				RootPanel.get().clear();
				RootPanel.get().add(new CardListLayout());
			}
		});
	}

	private void adminPanel() { 

		showProcessingPanel("Получение данных с сервера");
		giwiService.getBlockedCards(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				RootPanel.get().clear();
				RootPanel.get().add(new Label(caught.getMessage()));
				RootPanel.get().add(new Label("Разблокировать нечего"));
			}
			@Override
			public void onSuccess(List<String> result) {
				BlockedCardsSelectionPanel(result);
			}
		});
	}

	private void BlockedCardsSelectionPanel(List<String> cardNumbers) {
		RootPanel.get().clear();
		RootPanel.get().add(new Label("Выберите карту для разблокировки:"));

		CellList<String> cellList = new CellList<>(new TextCell());
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				String selected = selectionModel.getSelectedObject();
				if (null != selected) {
					doUnblockCard(selected);
				}
			}
		});
		cellList.setRowCount(cardNumbers.size(), true);
		cellList.setRowData(cardNumbers);
		RootPanel.get().add(cellList);
	}
	
	private void doUnblockCard(String CardNumber) {
		showProcessingPanel("Выполняется активация");
		giwiService.sendUnblocking(uuid, CardNumber, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
					adminPanel();
				}
				@Override
				public void onSuccess(Void result) {
					Window.alert("Карта разблокирована");
					adminPanel();
				}
			});
	}

	private void showProcessingPanel(String message) {
		RootPanel.get().clear();
		RootPanel.get().add(new Label(message + ", пожалуйста, подождите..."));
	}

	private void byePanel(String message) {
		RootPanel.get().clear();
		RootPanel.get().add(new Label(message));
	}

}
