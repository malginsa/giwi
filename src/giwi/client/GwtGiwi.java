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

import giwi.shared.Account;

public class GwtGiwi implements EntryPoint {

	private final GiwiServiceAsync giwiService = GWT.create(GiwiService.class);
	
	private Integer uuid;
	private String clientName;


	
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
					passwordTextBox.getText(), new AsyncCallback<Integer>() {

						@Override
						public void onSuccess(Integer result) {
							clientName = nameTextBox.getText();
							uuid = result;
							if (0 == result) {
								Window.alert("Вы вошли в систему как администратор");
								adminPanel();
							} else {
								acquireAccountsPanel();
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

	private void adminPanel() { 
		
		showProcessingPanel("Получение данных с сервера");
		
		giwiService.getBlockedCards(new AsyncCallback<List<Account>>() {
			@Override
			public void onFailure(Throwable caught) {
				RootPanel.get().clear();
				RootPanel.get().add(new Label(caught.getMessage()));
				RootPanel.get().add(new Label("Разблокировать нечего"));
			}
			@Override
			public void onSuccess(List<Account> result) {
				Accounts.setAccounts(result);
				BlockedCardsSelectionPanel();
			}
		});
	}

	private void acquireAccountsPanel() {
		
		showProcessingPanel("Получение данных с сервера");
		
		giwiService.getAccounts(uuid, new AsyncCallback<List<Account>>() {
			@Override
			public void onFailure(Throwable caught) {
				byePanel(caught.getMessage());
			}
			@Override
			public void onSuccess(List<Account> result) {
				Accounts.setAccounts(result);
//				CardsSelectionPanel();
				RootPanel.get().clear();
				RootPanel.get().add(new CardListLayout());
			}
		});
	}

	private void CardsSelectionPanel() {
		
		RootPanel.get().clear();
		RootPanel.get().add(new Label("Добрый день, " + clientName));
		RootPanel.get().add(new Label("Выберите карту:"));
		
		CellList<String> cellList = new CellList<>(new TextCell());
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				String selected = selectionModel.getSelectedObject();
				if (null != selected) {
					cardOperationsPanel(selected);
				}
			}
		});
		
		cellList.setRowCount(Accounts.size(), true);
		cellList.setRowData(Accounts.getActiveCards());
		
		RootPanel.get().add(cellList);
	}
	
	private void BlockedCardsSelectionPanel() {

		RootPanel.get().clear();
		RootPanel.get().add(new Label("Выберите карту для блокировки:"));

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

		cellList.setRowCount(Accounts.size(), true);
		cellList.setRowData(Accounts.getBlockedCardNumbers());
		
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

	private void cardOperationsPanel(final String cardNumber) {
		
		RootPanel.get().clear();
		RootPanel.get().add(new Label("Добрый день, " + clientName));
		RootPanel.get().add(new Label("Карта номер  " + cardNumber));
		RootPanel.get().add(new Label("Выберите операцию"));
		
		RootPanel.get().add(new Button("Оплатить") 
		{{ 
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					transactionPanel(cardNumber);
				}
			});
		}});
		
		RootPanel.get().add(new Button("Пополнить счёт")
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					incrementAccountPanel(cardNumber);
				}
			});
		}});
		
		RootPanel.get().add(new Button("Просмотр операций"));
		
		RootPanel.get().add(new Button("Заблокировать карту")
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					doBlockCard(cardNumber);
				}
			});
		}});
		
		RootPanel.get().add(new Button("Выбрать другую карту")
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					acquireAccountsPanel();
				}
			});
		}});
		
		RootPanel.get().add(new Button("Выйти") 
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					RootPanel.get().clear();
					RootPanel.get().add(new Label("Вы вышли из системы"));
				}
			});
		}});
	}

	private void transactionPanel(final String cardNumber) {

		RootPanel.get().clear();
		RootPanel.get().add(new Label("Добрый день, " + clientName));
		RootPanel.get().add(new Label("Карта номер  " + cardNumber));

		final TextBox amountTextBox = new TextBox();
		RootPanel.get().add(new HorizontalPanel() {{ 
			add(new Label("Сумма ")); 
			add(amountTextBox); 
		}} );
		amountTextBox.setFocus(true);
		final TextBox toCardNumberTextBox = new TextBox();
		RootPanel.get().add(new HorizontalPanel() 
		{{ 
			add(new Label("На какую карту ")); 
			add(toCardNumberTextBox); 
		}} );
		RootPanel.get().add(new Button("Перевести") 
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					doTransaction(cardNumber, toCardNumberTextBox.getText(), 
							Integer.valueOf(amountTextBox.getText()));
				}
			});
		}});
		RootPanel.get().add(new Button("Вернуться к операциям с картой") 
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					acquireAccountsPanel();
				}
			});
		}});
	}

	private void doTransaction(final String fromCardNumber, 
			String toCardNumber,	Integer amount) {

		showProcessingPanel("Выполняется перевод");
		
		giwiService.sendTransaction(uuid, fromCardNumber, toCardNumber, amount, 
			new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
					acquireAccountsPanel();
				}
				@Override
				public void onSuccess(Void result) {
					Window.alert("Транзакция успешно проведена");
					cardOperationsPanel(fromCardNumber);
				}
			});
	}
	
	private void incrementAccountPanel(final String cardNumber) {
		
		RootPanel.get().clear();
		RootPanel.get().add(new Label("Добрый день, " + clientName));
		RootPanel.get().add(new Label("Карта номер  " + cardNumber));
		
		final TextBox amountTextBox = new TextBox() 
			{{ setFocus(true); }};
		RootPanel.get().add(new HorizontalPanel() {{ 
			add(new Label("Сумма пополнения: ")); 
			add(amountTextBox); 
		}} );
		RootPanel.get().add(new Button("Пополнить") 
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					doIncrement(cardNumber, Integer.valueOf(amountTextBox.getText()));
				}
			});
		}});
		RootPanel.get().add(new Button("Вернуться к операциям с картой") 
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					acquireAccountsPanel();
				}
			});
		}});
	}

	private void doIncrement(final String cardNumber, Integer amount) {
		
		showProcessingPanel("Выполняется пополнение карты");
		
		giwiService.sendIncrement(uuid, cardNumber, amount, 
			new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
					acquireAccountsPanel();
				}
				@Override
				public void onSuccess(Void result) {
					Window.alert("Карта пополнена");
					cardOperationsPanel(cardNumber);
				}
			});
	}

	private void doBlockCard(final String cardNumber) {
		
		showProcessingPanel("Производится блокировка");

		giwiService.sendBlockCard(uuid, cardNumber, 
				new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
						acquireAccountsPanel();
					}
					@Override
					public void onSuccess(Void result) {
						Window.alert("Карта заблокирована");
						acquireAccountsPanel();
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
