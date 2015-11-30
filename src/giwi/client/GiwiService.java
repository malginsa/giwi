package giwi.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import giwi.shared.CardInfo;
import giwi.shared.PersonInfo;
import giwi.shared.SecurityViolationException;
import giwi.shared.TransactionInfo;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("giwi")
public interface GiwiService extends RemoteService {

	PersonInfo signIn(String name, String password) 
			throws IllegalArgumentException, SecurityViolationException;

	void signOut(Long uuid)	throws SecurityViolationException;

	List<CardInfo> getCardInfo(Long uuid) throws IllegalArgumentException, SecurityViolationException;

	void sendTransaction(Long uuid, String cardNumber, Integer amount) 
			throws IllegalArgumentException, SecurityViolationException;

	void sendDoBlockCard(Long uuid, String cardNumber) 
			throws IllegalArgumentException, SecurityViolationException;

	List<String> getBlockedCards(Long uuid) throws IllegalArgumentException, SecurityViolationException;

	void sendDoUnblock(Long uuid, String cardNumber) throws IllegalArgumentException, SecurityViolationException;
	
	List<TransactionInfo> getTransactions(Long uuid, String cardNumber) throws IllegalArgumentException, SecurityViolationException;

}
