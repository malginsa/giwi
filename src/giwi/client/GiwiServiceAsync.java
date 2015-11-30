package giwi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import giwi.shared.CardInfo;
import giwi.shared.PersonInfo;
import giwi.shared.TransactionInfo;

/**
 * The async counterpart of <code>GiwiService</code>.
 */
public interface GiwiServiceAsync {

	void signIn(String name, String password,	AsyncCallback<PersonInfo> asyncCallback);

	void getCardInfo(Long uuid, AsyncCallback<List<CardInfo>> asyncCallback);

	void signOut(Long uuid, AsyncCallback<Void> callback);

	void sendTransaction(Long uuid, String cardNumber, Integer amount, 
			AsyncCallback<Void> callback);

	void sendDoBlockCard(Long uuid, String cardNumber, AsyncCallback<Void> callback);

	void getBlockedCards(Long uuid, AsyncCallback<List<String>> callback);
	
	void sendDoUnblock(Long uuid, String cardNumber, AsyncCallback<Void> callback);
	
	void getTransactions(Long uuid, String cardNumber, AsyncCallback<List<TransactionInfo>> callback);

}
