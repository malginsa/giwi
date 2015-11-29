package giwi.client;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Constants;
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

import giwi.client.CardInfoForm.LocaleConstants;
import giwi.shared.CardInfo;

public class GwtGiwi implements EntryPoint {

	static interface LocaleConstants extends Constants {
		String yourNameAsk();
		String passwordAsk();
		String signInAsAdminMsg();
		String dataProcessingMsg();
		String nothingToUnblockMsg();
		String chooseCardToUnblockAsk();
		String unblockingInProcessMsg();
		String cardIsUnblockedMsg();
		String pleaseWaitMsg();
	}

	private static LocaleConstants constants;

	public void onModuleLoad() {
		constants = GWT.create(LocaleConstants.class);
		signIn();
	}

	public void signIn() {
		
		RootPanel.get().clear();

		VerticalPanel verticalPanel = new VerticalPanel();

		final TextBox nameTextBox = new TextBox();
		verticalPanel.add(new HorizontalPanel() {{ 
			add(new Label(constants.yourNameAsk())); 
			add(nameTextBox); 
		}} );

		final TextBox passwordTextBox = new PasswordTextBox();
		verticalPanel.add(new HorizontalPanel() {{ 
			add(new Label(constants.passwordAsk())); 
			add(passwordTextBox); 
		}} );

		final Button okButton = new Button("Ok");
		verticalPanel.add(okButton);

		RootPanel.get().add(verticalPanel);
		nameTextBox.setFocus(true);

		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				okButton.setEnabled(false);
				ClientImplDB.giwiService.signIn(nameTextBox.getText(), 
					passwordTextBox.getText(), new AsyncCallback<Long>() {
						@Override
						public void onSuccess(Long result) {
							ClientImplDB.clientName = nameTextBox.getText();
							ClientImplDB.uuid = result;
							if (0 == result) {
								Window.alert(constants.signInAsAdminMsg());
								adminPanel();
							} else {
								acquireCardInfo();
							}
						}
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
							nameTextBox.setText("");
							passwordTextBox.setText("");
							okButton.setEnabled(true);
							nameTextBox.setFocus(true);
						}
				});
			}
		});
	}

	private void acquireCardInfo() {

		showProcessingPanel(constants.dataProcessingMsg());

		ClientImplDB.giwiService.getCardInfo(ClientImplDB.uuid, 
				new AsyncCallback<List<CardInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				RootPanel.get().clear();
				RootPanel.get().add(new Label(caught.getMessage()));
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

		showProcessingPanel(constants.dataProcessingMsg());
		ClientImplDB.giwiService.getBlockedCards(
				new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				RootPanel.get().clear();
				RootPanel.get().add(new Label(caught.getMessage()));
				RootPanel.get().add(new Label(constants.nothingToUnblockMsg()));
			}
			@Override
			public void onSuccess(List<String> result) {
				BlockedCardsSelectionPanel(result);
			}
		});
	}

	private void BlockedCardsSelectionPanel(List<String> cardNumbers) {
		RootPanel.get().clear();
		RootPanel.get().add(new Label(constants.chooseCardToUnblockAsk()));

		CellList<String> cellList = new CellList<>(new TextCell());
		final SingleSelectionModel<String> selectionModel = 
				new SingleSelectionModel<String>();
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(
				new SelectionChangeEvent.Handler() {
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
		showProcessingPanel(constants.unblockingInProcessMsg());
		ClientImplDB.giwiService.sendUnblocking(ClientImplDB.uuid, CardNumber, 
				new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				adminPanel();
			}
			@Override
			public void onSuccess(Void result) {
				Window.alert(constants.cardIsUnblockedMsg());
				adminPanel();
			}
		});
	}

	private void showProcessingPanel(String message) {
		RootPanel.get().clear();
		RootPanel.get().add(new Label(message + constants.pleaseWaitMsg()));
	}

}
