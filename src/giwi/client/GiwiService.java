package giwi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import giwi.shared.CardInfo;
import giwi.shared.CardTransactionInfo;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("giwi")
public interface GiwiService extends RemoteService {

	Long signIn(String name, String password) 
			throws IllegalArgumentException;

	void signOut(Long uuid) 
			throws IllegalArgumentException;

	List<CardInfo> getCardInfo(Long uuid);

	void sendTransaction(Long uuid, String cardNumber, Integer amount) 
			throws IllegalArgumentException;

	void sendDoBlockCard(Long uuid, String cardNumber) 
			throws IllegalArgumentException;

	List<String> getBlockedCards() throws IllegalArgumentException;

	void sendUnblocking(Long uuid, String cardNumber) throws IllegalArgumentException;
	
	List<CardTransactionInfo> getTransactions(Long uuid, String cardNumber) throws IllegalArgumentException;

}
