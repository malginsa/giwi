package giwi.server;

import giwi.client.GiwiService;
import giwi.shared.Account;
import giwi.shared.FieldVerifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GiwiServiceImpl 
		extends RemoteServiceServlet implements GiwiService 
	{

	static final Logger logger = 
		LogManager.getLogger(GiwiServiceImpl.class.getName());
	
		@Override
		public Integer signIn(String name, String password) 
				throws IllegalArgumentException 
		{
			Integer result = DBManager.getClientId(name, password);
			logger.info(name + " logged in");
			return result;
		}

		@Override
		public List<Account> getAccounts(Integer idClient) 
				throws IllegalArgumentException 
		{
			List<Account> result = DBManager.getAccounts(idClient);
			logger.info("client with " + idClient + " requested accounts: " + 
					result);
			Utils.pause(3_000); // imitation of timeout to extract data from DB
			return result;
		}

		@Override
		public void sendTransaction(Integer idClient, String fromCard, 
				String toCard, Integer amount) throws IllegalArgumentException {
			// TODO принадлежность счёта клиенту, ... uuid <-> idClient
			// TODO FieldVirifier: fromCard, idClient, toCard, amount
			Integer balance = DBManager.getBalance(fromCard);
			if (balance < amount) {
				throw new IllegalArgumentException("Недостаточно денег на счёте");
			}
			DBManager.decreaseBalance(fromCard, amount);
			// DBManager.storeTransaction(idClient, fromAccount, toAccount);
		}
}
