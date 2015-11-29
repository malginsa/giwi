package giwi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import giwi.shared.CardInfo;
import giwi.shared.CardTransactionInfo;

/**
 * The async counterpart of <code>GiwiService</code>.
 */
public interface GiwiServiceAsync {

	void signIn(String name, String password, AsyncCallback<Long> callback);

	void getCardInfo(Long uuid, AsyncCallback<List<CardInfo>> asyncCallback);

	void signOut(Long uuid, AsyncCallback<Void> callback);

	void sendTransaction(Long uuid, String cardNumber, Integer amount, 
			AsyncCallback<Void> callback);

	void sendDoBlockCard(Long uuid, String cardNumber, AsyncCallback<Void> callback);

	void getBlockedCards(AsyncCallback<List<String>> callback);
	
	void sendUnblocking(Long uuid, String cardNumber, AsyncCallback<Void> callback);
	
	void getTransactions(Long uuid, String cardNumber, AsyncCallback<List<CardTransactionInfo>> callback);

//	void getAccounts(Integer idClient, AsyncCallback<List<Account>> callback);
//
//	void sendTransaction(Integer idClient, String fromCard, 
//			String toCard, Integer amount, AsyncCallback<Void> callback);
//
//	void sendIncrement(Integer uuid, String cardNumber, Integer amount,
//			AsyncCallback<Void> asyncCallback);
//
//	void sendBlockCard(Integer uuid, String cardNumber, 
//			AsyncCallback<Void> asyncCallback);
//
//	void getBlockedCards(AsyncCallback<List<Account>> asyncCallback);
//
//	void sendUnblocking(Integer uuid, String cardNumber, AsyncCallback<Void> asyncCallback);
//
}
