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
			if (DBManager.isAdmin(name, password)) {
				logger.info("admin " + name + " logged in");
				return 0;
			}
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
			Utils.pause(1_000); // imitation of timeout to extract data from DB
			return result;
		}

		@Override
		public List<Account> getBlockedCards() {
			List<Account> result = DBManager.getBlockedAccounts();
			logger.info("admin requested blocked accounts: " +	result);
			Utils.pause(1_000); // imitation of timeout to extract data from DB
			return result;
		}
		
		@Override
		public void sendTransaction(Integer idClient, String fromCard, 
				String toCard, Integer amount) throws IllegalArgumentException 
		{
			// TODO принадлежность счёта клиенту, ... uuid <-> idClient
			// TODO FieldVirifier: fromCard, idClient, toCard, amount
			Integer balance = DBManager.getBalance(fromCard);
			if (balance < amount) {
				throw new IllegalArgumentException("Недостаточно денег на счёте");
			}
			DBManager.changeBalance(fromCard, -amount);
			logger.info(amount + " списано с катры " + fromCard);
			DBManager.storeTransaction(new Transaction(
					idClient, fromCard, toCard, amount));
			logger.info(idClient + " совершил транзакцию с карты " +	fromCard + 
					" на карту " + toCard + " на сумму: " + amount);
			Utils.pause(3_000); // imitation of timeout to extract data from DB
		}

		@Override
		public void sendIncrement(Integer uuid, String cardNumber, Integer amount) 
		{
			// TODO amount > 0, cardNumber существует и uuid==client_id
			DBManager.changeBalance(cardNumber, amount);
			Integer idClient = uuid; // uuid -> idClient
			logger.info(idClient + " пополнил карту " + cardNumber + 
					" на сумму " + amount);
			DBManager.storeTransaction(new Transaction(
					idClient, "", cardNumber, amount));
			logger.info(idClient + " совершил транзакцию: пополнил карту " +	cardNumber + 
					" на сумму: " + amount);
			Utils.pause(3_000); // imitation of timeout to extract data from DB
		}

		@Override
		public void sendBlockCard(Integer uuid, String cardNumber) 
				throws IllegalArgumentException 
		{
			// TODO проверка uuid == client_id
			DBManager.doBlockCard(cardNumber);
			Integer idClient = uuid; // uuid -> idClient
			logger.info(idClient + " заблокировал карту " + cardNumber);
			Utils.pause(3_000); // imitation of timeout to extract data from DB
		}

		@Override
		public void sendUnblocking(Integer uuid, String cardNumber)
				throws IllegalArgumentException 
		{
			// TODO uuid - admin
			DBManager.doUnblockCard(cardNumber);
			Integer idClient = uuid; // uuid -> idClient
			logger.info(idClient + " разблокировал карту " + cardNumber);
			Utils.pause(3_000); // imitation of timeout to extract data from DB
		}

}
