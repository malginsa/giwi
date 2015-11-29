package giwi.server;

import giwi.client.GiwiService;
import giwi.shared.CardInfo;
import giwi.shared.CardTransactionInfo;
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
		logger.info(uuid + " logged out");
		Utils.pause(1_000); // imitation of timeout to extract data from DB
	}

	@Override
	public List<CardInfo> getCardInfo(Long uuid) 
			throws IllegalArgumentException
	{
		if (!onLineClientDB.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: request from non-existent uuid detected");
			throw new IllegalArgumentException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer clientId = onLineClientDB.getClientId(uuid);
		List<CardInfo> result = DBManager.getCardInfo(clientId);
		logger.info("client with " + clientId + " requested CardInfo: " + result);
		Utils.pause(1_000); // imitation of timeout to extract data from DB
		return result;
	}


	@Override
	public void sendTransaction(Long uuid, String cardNumber, Integer amount) 
			throws IllegalArgumentException 
	{
		if (!onLineClientDB.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: transaction from non-existent uuid detected");
			throw new IllegalArgumentException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer clientIdFromDB;
		try {
			clientIdFromDB = DBManager.getClientId(cardNumber);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new IllegalArgumentException(
					"Сбой в системе. Обратитесь в нашу службу поддержки");
		}
		Integer clientId = onLineClientDB.getClientId(uuid);
		if (clientIdFromDB != clientId) {
			logger.warn("SECURITY VIOLATION: transaction from fake uuid detected");
			throw new IllegalArgumentException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		
		Account account = DBManager.getAccount(cardNumber);
		Integer balance = account.getBalance();
		if (balance < amount) {
			logger.warn("SECURITY VIOLATION: Недостаточно средств для списания с карты номер " + cardNumber);
			throw new IllegalArgumentException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}

		Card card = DBManager.getCard(cardNumber);
		Boolean isBlocked = card.getIsBlocked();
		if (isBlocked) {
			logger.warn("SECURITY VIOLATION: Попытка списания с заблокированной карты номер " + cardNumber);
			throw new IllegalArgumentException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		
		Integer accountId = account.getId();
		DBManager.changeBalance(accountId, amount);
		logger.info("карта " + cardNumber + " пополнена на " + amount);
		DBManager.storeTransaction(new Transaction(accountId, amount));
		logger.info(clientId + " совершил транзакцию с карты " + cardNumber + " на сумму: " + amount);
	}

	@Override
	public void sendDoBlockCard(Long uuid, String cardNumber) throws IllegalArgumentException {

		if (!onLineClientDB.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: block operation from non-existent uuid detected");
			throw new IllegalArgumentException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer clientIdFromDB;
		try {
			clientIdFromDB = DBManager.getClientId(cardNumber);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new IllegalArgumentException(
					"Сбой в системе. Обратитесь в нашу службу поддержки");
		}
		Integer clientId = onLineClientDB.getClientId(uuid);
		if (clientIdFromDB != clientId) {
			logger.warn("SECURITY VIOLATION: block operation from fake uuid detected");
			throw new IllegalArgumentException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		
		DBManager.doBlockCard(cardNumber);
		logger.info(clientId + " заблокировал карту " + cardNumber);
	}

	@Override
	public List<String> getBlockedCards() throws IllegalArgumentException {
// TODO проверить, что uuid принадлежит админу
		List<String> result = DBManager.getBlockedCards();
		logger.info("admin requested blocked cards: " + result);
		return result;
	}

	@Override
	public void sendUnblocking(Long uuid, String cardNumber) throws IllegalArgumentException {
// TODO uuid - admin
		DBManager.doUnblockCard(cardNumber);
// TODO uuid -> idClient
		logger.info("админ разблокировал карту " + cardNumber);
	}

	@Override
	public List<CardTransactionInfo> getTransactions(Long uuid, String cardNumber) throws IllegalArgumentException {
// TODO check uuid -> owner of cardNumber
		List<CardTransactionInfo> transactions = DBManager.getTransactions(cardNumber);
		logger.info(uuid + " запросил транзакции " + transactions);
		return transactions;
	}

}
