package giwi.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import giwi.shared.Card;

public class CardInfoForm extends Composite {

	static interface LocaleConstants extends Constants {
		String[] cardStatuses();
		String askAmountDepositMsg();
		String askAmountWithdrawalMsg();
		String BalanceNegativeAlert();
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

	private Card selectedCard;

	private final String[] statuses;

	public CardInfoForm() {

		final LocaleConstants constants = GWT.create(LocaleConstants.class);
		statuses = constants.cardStatuses();

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
				String prompt = Window.prompt(constants.askAmountDepositMsg(), "");
				if (null != prompt) {
					int newBalance = selectedCard.getBalance() + 
							Integer.parseInt(prompt);
					selectedCard.setBalance(newBalance);
					doRefreshLayout();
				}
			}
		});
		
		withdrawalButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String prompt = Window.prompt(constants.askAmountWithdrawalMsg(), "");
				if (null != prompt) {
					int newBalance = selectedCard.getBalance() - 
							Integer.parseInt(prompt);
					if (newBalance < 0) {
						Window.alert(constants.BalanceNegativeAlert());
					} else {
						selectedCard.setBalance(newBalance);
						doRefreshLayout();
					}
				}
			}
		});
		
		doBlockButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectedCard.doBlock();
				doRefreshLayout();
			}
		});
		
		showTransactionsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("You choosed show transactions");
			}
		});

	}

	public void selectCard(Card card) {
		this.selectedCard = card;
		doRefreshInfoForm();
	}

	private void doRefreshLayout() {
		this.doRefreshInfoForm();
		CardDatabase.get().doRefreshListForm();
	}
	private void doRefreshInfoForm() {
		Boolean isActive = !selectedCard.getIsBlocked();
		depositButton.setEnabled(isActive);
		withdrawalButton.setEnabled(isActive);
		doBlockButton.setEnabled(isActive);
		showTransactionsButton.setEnabled(true);
		numberLabel.setText(selectedCard.getNumber().toString());
		statusLabel.setText(statuses[selectedCard.getStatusId()]);
		balanceLabel.setText(selectedCard.getBalance().toString());
	}
	

}
