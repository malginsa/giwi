package giwi.server;

import giwi.client.GiwiService;
import giwi.shared.CardInfo;
import giwi.shared.FieldVerifier;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GiwiServiceImpl extends RemoteServiceServlet implements GiwiService {

	static final Logger logger = LogManager.getLogger(GiwiServiceImpl.class.getName());

	OnLineClientDB onLineClientDB;
	
	{
		onLineClientDB = new OnLineClientDB();
	}
	
	@Override
	public Long signIn(String name, String password) 
			throws IllegalArgumentException 
	{
		if (DBManager.isAdmin(name, password)) {
			logger.info("admin " + name + " logged in");
			return 0L;
		}
		Integer clientId = DBManager.getClientId(name, password);
		logger.info(name + " logged in");
		Long uuid = onLineClientDB.addClienId(clientId);
		return uuid;
	}

	@Override
	public void signOut(Long uuid) {
		if (!onLineClientDB.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: Sign Out from non-existent uuid detected");
			throw new IllegalArgumentException(
					"Обратитесь в нашу службу безопасности за разъяснениями");
		}
		onLineClientDB.deleteClientId(uuid);
		Utils.pause(1_000); // imitation of timeout to extract data from DB
	}

	@Override
	public List<CardInfo> getCardInfo(Long uuid) 
			throws IllegalArgumentException
	{
		if (!onLineClientDB.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: request from non-existent uuid detected");
			throw new IllegalArgumentException(
					"Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer clientId = onLineClientDB.getClientId(uuid);
		List<CardInfo> result = DBManager.getCardInfo(clientId);
		logger.info("client with " + clientId + " requested CardInfo: " + result);
		Utils.pause(1_000); // imitation of timeout to extract data from DB
		return result;
	}

//	@Override
//	public List<Account> getAccounts(Integer idClient) 
//			throws IllegalArgumentException 
//	{
//		List<Account> result = DBManager.getAccounts(idClient);
//		logger.info("client with " + idClient + " requested accounts: " + result);
//		Utils.pause(1_000); // imitation of timeout to extract data from DB
//		return result;
//	}
//
//	@Override
//	public List<Account> getBlockedCards() {
//		List<Account> result = DBManager.getBlockedAccounts();
//		logger.info("admin requested blocked accounts: " + result);
//		Utils.pause(1_000); // imitation of timeout to extract data from DB
//		return result;
//	}
//
//	@Override
//	public void sendTransaction(Integer idClient, String fromCard, String toCard, Integer amount)
//			throws IllegalArgumentException {
//		// TODO принадлежность счёта клиенту, ... uuid <-> idClient
//		// TODO FieldVirifier: fromCard, idClient, toCard, amount
//		Integer balance = DBManager.getBalance(fromCard);
//		if (balance < amount) {
//			throw new IllegalArgumentException("Недостаточно денег на счёте");
//		}
//		DBManager.changeBalance(fromCard, -amount);
//		logger.info(amount + " списано с катры " + fromCard);
//		DBManager.storeTransaction(new Transaction(idClient, fromCard, toCard, amount));
//		logger.info(
//				idClient + " совершил транзакцию с карты " + fromCard + " на карту " + toCard + " на сумму: " + amount);
//		Utils.pause(3_000); // imitation of timeout to extract data from DB
//	}
//
//	@Override
//	public void sendIncrement(Integer uuid, String cardNumber, Integer amount) {
//		// TODO amount > 0, cardNumber существует и uuid==client_id
//		DBManager.changeBalance(cardNumber, amount);
//		Integer idClient = uuid; // uuid -> idClient
//		logger.info(idClient + " пополнил карту " + cardNumber + " на сумму " + amount);
//		DBManager.storeTransaction(new Transaction(idClient, "", cardNumber, amount));
//		logger.info(idClient + " совершил транзакцию: пополнил карту " + cardNumber + " на сумму: " + amount);
//		Utils.pause(3_000); // imitation of timeout to extract data from DB
//	}
//
//	@Override
//	public void sendBlockCard(Integer uuid, String cardNumber) throws IllegalArgumentException {
//		// TODO проверка uuid == client_id
//		DBManager.doBlockCard(cardNumber);
//		Integer idClient = uuid; // uuid -> idClient
//		logger.info(idClient + " заблокировал карту " + cardNumber);
//		Utils.pause(3_000); // imitation of timeout to extract data from DB
//	}
//
//	@Override
//	public void sendUnblocking(Integer uuid, String cardNumber) throws IllegalArgumentException {
//		// TODO uuid - admin
//		DBManager.doUnblockCard(cardNumber);
//		Integer idClient = uuid; // uuid -> idClient
//		logger.info(idClient + " разблокировал карту " + cardNumber);
//		Utils.pause(3_000); // imitation of timeout to extract data from DB
//	}

}
