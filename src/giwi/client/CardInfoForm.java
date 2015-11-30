package giwi.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import giwi.shared.CardInfo;
import giwi.shared.TransactionInfo;

public class CardInfoForm extends Composite {

	static interface LocaleConstants extends Constants {
		String[] cardStatus();
		String amountDepositAsk();
		String amountWithdrawalAsk();
		String BalanceNegativeAlert();
		String ChangeBalanceSuccessMsg();
		String cardHasBlockedMsg();
		String listOfTransactions();
		String sumHeader();
		String dateHeader();
		String timeHeader();
		String closeButton();
	}

	interface Binder extends UiBinder<Widget, CardInfoForm> {
	};

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField
	Label numberLabel;
	@UiField
	Label statusLabel;
	@UiField
	Label balanceLabel;

	@UiField
	Button depositButton;
	@UiField
	Button withdrawalButton;
	@UiField
	Button doBlockButton;
	@UiField
	Button showTransactionsButton;

	private CardInfo selectedCard;

	private static LocaleConstants constants;

	public CardInfoForm() {

		constants = GWT.create(LocaleConstants.class);

		initWidget(uiBinder.createAndBindUi(this));

		depositButton.setEnabled(false);
		withdrawalButton.setEnabled(false);
		doBlockButton.setEnabled(false);
		showTransactionsButton.setEnabled(false);
		numberLabel.setText("");
		statusLabel.setText("");
		balanceLabel.setText("");

		depositButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeBalance(constants.amountDepositAsk(), 1);
			}
		});

		withdrawalButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeBalance(constants.amountWithdrawalAsk(), -1);
			}
		});

		doBlockButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ClientImplDB.giwiService.sendDoBlockCard(
						ClientImplDB.uuid, selectedCard.getNumber(), 
						new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
					@Override
					public void onSuccess(Void result) {
						Window.alert(constants.cardHasBlockedMsg());
						selectedCard.doBlock();
						doRefreshLayout();
					}
				});
			}
		});

		showTransactionsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				ClientImplDB.giwiService.getTransactions(
						ClientImplDB.uuid, selectedCard.getNumber(), 
						new AsyncCallback<List<TransactionInfo>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
					@Override
					public void onSuccess(List<TransactionInfo> result) {
						final DialogBox dialogBox = new DialogBox();
						dialogBox.setText(constants.listOfTransactions());
						final Button closeButton = new Button(
								constants.closeButton());
						VerticalPanel dialogVPanel = new VerticalPanel();
						SafeHtmlBuilder html = new SafeHtmlBuilder();
						html.appendHtmlConstant("<table>");
						html.appendHtmlConstant("<tr><td>" + 
								constants.sumHeader() + "</td><td>" + 
								constants.dateHeader() + "</td><td>" + 
								constants.timeHeader() + "</td></tr>");
						for (TransactionInfo transaction : result) {
							html.appendHtmlConstant("<tr><td>");
							html.appendEscaped(transaction.getAmount().toString());
							html.appendHtmlConstant("</td><td>");
							html.appendEscaped(transaction.getDate().toString());
							html.appendHtmlConstant("</td><td>");
							html.appendEscaped(transaction.getTime().toString());
							html.appendHtmlConstant("</td></tr>");
						}
						html.appendHtmlConstant("</table>");
						dialogVPanel.add(new HTMLPanel(html.toSafeHtml()));
						dialogVPanel.setHorizontalAlignment(
								VerticalPanel.ALIGN_RIGHT);
						dialogVPanel.add(closeButton);
						dialogBox.setWidget(dialogVPanel);
						closeButton.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								dialogBox.hide();
							}
						});
						dialogBox.center();
						closeButton.setFocus(true);
					}
				});
				
			}
		});

	}

	private void changeBalance(String prompt, int factor) {

		String res = Window.prompt(prompt, "");
// TODO только цифры
		if (null == res) {
			return;
		}
		int amount = factor * Integer.parseInt(res);
		final int newBalance = selectedCard.getBalance() + amount;
		if (newBalance < 0) {
			Window.alert(constants.BalanceNegativeAlert());
			return;
		}
		ClientImplDB.giwiService.sendTransaction(ClientImplDB.uuid, 
				selectedCard.getNumber(), amount, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {
				selectedCard.setBalance(newBalance);
				doRefreshLayout();
				Window.alert(constants.ChangeBalanceSuccessMsg());
			}
		});
	}

	private void doRefreshLayout() {
		this.doRefreshInfoForm();
		CardDB.get().doRefreshListForm();
	}

	private void doRefreshInfoForm() {
		Boolean isActive = !selectedCard.getIsBlocked();
		depositButton.setEnabled(isActive);
		withdrawalButton.setEnabled(isActive);
		doBlockButton.setEnabled(isActive);
		showTransactionsButton.setEnabled(true);
		numberLabel.setText(selectedCard.getNumber().toString());
		statusLabel.setText(constants.cardStatus()[selectedCard.getStatusId()]);
		balanceLabel.setText(selectedCard.getBalance().toString());
	}

	public void selectCard(CardInfo cardInfo) {
		this.selectedCard = cardInfo;
		doRefreshInfoForm();
	}

}
