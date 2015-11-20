package giwi.client;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import giwi.shared.Account;

//import giwi.client.GwtGiwi.Contact;

public class GwtGiwi implements EntryPoint {

	private final GiwiServiceAsync giwiService = GWT.create(GiwiService.class);
	
	private Integer uuid;
	private String clientName;

	public void onModuleLoad() {

		askCredentials();

//		RootPanel.get().add(new VerticalPanel() {{ add(TextCellExample()); add(CellTableExample()); }} );

	}

	private void transactionMenu(final String cardNumber) {
		RootPanel.get().clear();
		RootPanel.get().add(new Label("Добрый день, " + clientName));
		RootPanel.get().add(new Label("Карта номер  " + cardNumber));
		final TextBox amountTextBox = new TextBox();
		RootPanel.get().add(new HorizontalPanel() {{ 
			add(new Label("Сумма ")); 
			add(amountTextBox); 
		}} );
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
					cardOperations(cardNumber);
				}
			});
		}});
		RootPanel.get().add(new Button("Вернуться к операциям с картой") 
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					cardOperations(cardNumber);
				}
			});
		}});
	}
	
	private void doTransaction(final String fromCardNumber, String toCardNumber, Integer amount) {
		giwiService.sendTransaction(uuid, fromCardNumber, toCardNumber, amount, 
			new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
					showAccounts();
				}
				@Override
				public void onSuccess(Void result) {
					Window.alert("Транзакция успешно проведена");
					cardOperations(fromCardNumber);
				}
			});
	}
	
	private void cardOperations(final String cardNumber) {
		RootPanel.get().clear();
		
		RootPanel.get().add(new Label("Добрый день, " + clientName));
		RootPanel.get().add(new Label("Карта номер  " + cardNumber));
		RootPanel.get().add(new Label("Выберите операцию"));
		
		RootPanel.get().add(new Button("Сделать платёж") 
		{{ 
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					transactionMenu(cardNumber);
				}
			});
		}});
		
		RootPanel.get().add(new Button("Send StackTrace") 
		{{ 
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					
					Exception ex = new Exception();
					
					String message = "";
					for (StackTraceElement el : ex.getStackTrace()) {
						message = message + 
								el.getClassName() + " : " + 
								el.getMethodName() + " : " +
								el.getLineNumber() + "\n";
					}
					
					Window.alert(message);
					cardOperations(cardNumber);
				}
			});
		}});
		
		RootPanel.get().add(new Button("Пополнить счёт"));
		
		RootPanel.get().add(new Button("Просмотр операций"));
		
		RootPanel.get().add(new Button("Заблокировать карту"));
		
		RootPanel.get().add(new Button("Выйти") 
		{{
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					RootPanel.get().clear();
					RootPanel.get().add(new Label("Вы вышли из систеты"));
				}
			});
		}});

//		VerticalPanel verticalPanel = new VerticalPanel();
//		Button doOperationsButton = new Button("Совершить операцию");
//		verticalPanel.add(doOperationsButton);
//		Button showTransactionsButton = new Button("Просмотр операций");
//		verticalPanel.add(showTransactionsButton);
//		Button doBlockCardButton = new Button("Заблокировать карту");
//		verticalPanel.add(doBlockCardButton);
//
//		RootPanel.get().add(verticalPanel);
//		
//		verticalPanel.add(new Button("Выйти") 
//		{{
//			addClickHandler(new ClickHandler() {
//				@Override
//				public void onClick(ClickEvent event) {
//					RootPanel.get().clear();
//					RootPanel.get().add(new Label("Вы вышли из систеты"));
//				}
//			});
//		}});
	}

	private void acquireAccounts() {
		RootPanel.get().clear();
		RootPanel.get().add(new Label("Получение данных с сервера, пожалуйста, подождите..."));
		giwiService.getAccounts(uuid, new AsyncCallback<List<Account>>() {
			@Override
			public void onFailure(Throwable caught) {
				RootPanel.get().clear();
				RootPanel.get().add(new Label(caught.getMessage()));
				RootPanel.get().add(new Label("Посетите наш офис для оформления карты"));
			}
			@Override
			public void onSuccess(List<Account> result) {
				ClientAccounts.setAccounts(result);
				showAccounts();
			}
		});
	}

	private void showAccounts() {
		
		RootPanel.get().clear();
		
		CellList<String> cellList = new CellList<>(new TextCell());
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				String selected = selectionModel.getSelectedObject();
				if (null != selected) {
					cardOperations(selected);
				}
			}
		});
		
		cellList.setRowCount(ClientAccounts.size(), true);
		cellList.setRowData(ClientAccounts.getCardNumbers());
		
		RootPanel.get().add(new Label("Добрый день, " + clientName));
		RootPanel.get().add(new Label("Выберите карту:"));
		RootPanel.get().add(cellList);
	}
	
	private void askCredentials() {

		VerticalPanel verticalPanel = new VerticalPanel();

		final TextBox nameTextBox = new TextBox();
		verticalPanel.add(new HorizontalPanel() {{ 
			add(new Label("Ваше имя ")); 
			add(nameTextBox); 
		}} );

		final TextBox passwordTextBox = new TextBox();
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
//							okButton.setEnabled(false);
							nameTextBox.setText("");
							passwordTextBox.setText("");
							acquireAccounts();
						}

						@Override
						public void onFailure(Throwable caught) {
							okButton.setEnabled(false);
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


	// examples

//	private static class Contact {
//		private final String address;
//		private final String name;
//
//		public Contact(String name, String address) {
//			this.name = name;
//			this.address = address;
//		}
//		
//		@Override
//		public String toString() {
//			return name + " " + address;
//		}
//	}
//
//	private static CellList<String> TextCellExample() {
//
//		List<String> DAYS = Arrays.asList("Sunday", "Monday", "Tuesday", 
//				"Wednesday", "Thursday", "Friday", "Saturday");
//
//		TextCell textCell = new TextCell();
//		CellList<String> cellList = new CellList<String>(textCell);
//		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
//
//		final SingleSelectionModel<String> selectionModel = 
//				new SingleSelectionModel<String>();
//		cellList.setSelectionModel(selectionModel);
//
//		selectionModel.addSelectionChangeHandler(
//			new SelectionChangeEvent.Handler() {
//				public void onSelectionChange(SelectionChangeEvent event) {
//					String selected = selectionModel.getSelectedObject();
//					if (selected != null) {
//						Window.alert("You selected: " + selected);
//					}
//				}
//			}
//		);
//
//		cellList.setRowCount(DAYS.size(), true);
//		cellList.setRowData(0, DAYS);
////		cellList.setStyleName("login");
//		return cellList;
//
//	}
//	
//	private static CellTable<Contact> CellTableExample() {
//
//		List<Contact> CONTACTS = Arrays.asList(
//				new Contact("John", "123 Fourth Road"),
//				new Contact("Mary", "456 Lancer Lane"));
//
//		CellTable<Contact> table = new CellTable<Contact>();
//
//		table.addColumn(new TextColumn<Contact>() {
//			@Override
//			public String getValue(Contact contact) {
//				return contact.name;
//			}
//		}, "Name");
//
//		table.addColumn(new TextColumn<Contact>() {
//			@Override
//			public String getValue(Contact contact) {
//				return contact.address;
//			}
//		}, "Address");
//
//		table.setRowCount(CONTACTS.size(), true);
//		table.setRowData(0, CONTACTS);
//		return table;
//	}

}
