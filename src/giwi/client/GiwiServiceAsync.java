package giwi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import giwi.shared.Account;

/**
 * The async counterpart of <code>GiwiService</code>.
 */
public interface GiwiServiceAsync {
	
//	void pickCard(String input, AsyncCallback<String> callback) 
//		throws IllegalArgumentException;

	void signIn(String name, String password, AsyncCallback<Integer> callback);

	void getAccounts(Integer idClient, AsyncCallback<List<Account>> callback);

	void sendTransaction(Integer idClient, String fromCard, 
			String toCard, Integer amount, AsyncCallback<Void> callback);

}