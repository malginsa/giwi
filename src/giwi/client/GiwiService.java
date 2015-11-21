package giwi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import giwi.shared.Account;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("giwi")
public interface GiwiService extends RemoteService {

//	String pickCard(String name) throws IllegalArgumentException;

	Integer signIn(String name, String password) throws IllegalArgumentException;

	List<Account> getAccounts(Integer idClient) throws IllegalArgumentException;
	
	void sendTransaction(Integer idClient, String fromCard, 
			String toCard, Integer amount) throws IllegalArgumentException;

	void sendIncrement(Integer uuid, String cardNumber, Integer amount);

	void sendBlockCard(Integer uuid, String cardNumber) 
			throws IllegalArgumentException; 
}
