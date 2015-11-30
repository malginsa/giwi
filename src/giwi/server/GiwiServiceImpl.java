package giwi.server;

import giwi.client.GiwiService;
import giwi.shared.CardInfo;
import giwi.shared.TransactionInfo;
import giwi.shared.PersonInfo;
import giwi.shared.SecurityViolationException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GiwiServiceImpl extends RemoteServiceServlet implements GiwiService {

	private final Logger logger = LogManager.getLogger(GiwiServiceImpl.class.getName());

	OnLinePersonDB onLineClients;
	OnLinePersonDB onLineAdmins;
	
	{
		try {
			onLineClients = new OnLinePersonDB();
			onLineAdmins = new OnLinePersonDB();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public PersonInfo signIn(String name, String password) 
			throws IllegalArgumentException, SecurityViolationException 
	{
		if (DBManager.isAdmin(name, password)) {
			Integer adminId = DBManager.getAdminId(name, password);
			Long uuid;
			try {
				uuid = onLineAdmins.addPersonId(adminId);
			} catch (SecurityViolationException e) {
				logger.info("admin " + name + e.getMessage());
				throw e;
			}
			logger.info("admin " + name + " signed in");
			return new PersonInfo(uuid, PersonInfo.Status.ADMIN);
		} else {
			Integer clientId = DBManager.getClientId(name, password);
			Long uuid;
			try {
				uuid = onLineClients.addPersonId(clientId);				
			} catch (SecurityViolationException e) {
				logger.info("client " + name + e.getMessage());
				throw e;
			}
			logger.info("client " + name + " signed in");
			return new PersonInfo(uuid, PersonInfo.Status.CLIENT);
		}
	}

	@Override
	public void signOut(Long uuid) throws SecurityViolationException {
		if (onLineClients.isUuidExist(uuid)) {
			Integer id;
			try {
				id = onLineClients.removePersonId(uuid);
			} catch (SecurityViolationException e) {
				logger.info("uuid=" + uuid + e.getMessage());
				return;
			}
			logger.info(DBManager.getClientName(id) + " signed out");
			Utils.pause(2_000); // Имитация задержки при запросе к БД
			return;
		}
		if (onLineAdmins.isUuidExist(uuid)) {
			Integer id;
			try {
				id = onLineAdmins.removePersonId(uuid);
			} catch (SecurityViolationException e) {
				logger.info("uuid=" + uuid + e.getMessage());
				return;
			}
			logger.info("admin " + DBManager.getAdminName(id) + " signed out");
			Utils.pause(2_000); // Имитация задержки при запросе к БД
			return;
		}
		logger.warn("SECURITY VIOLATION: Sign Out from non-existent uuid detected");
		throw new SecurityViolationException(
				"Обратитесь в нашу службу безопасности за разъяснениями");
	}

	@Override
	public List<CardInfo> getCardInfo(Long uuid) 
			throws IllegalArgumentException, SecurityViolationException
	{
		// Проверка наличия uuid в базе сессий
		if (!onLineClients.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: request from non-existent uuid detected");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer clientId = onLineClients.getId(uuid);
		List<CardInfo> cardInfo = DBManager.getCardInfo(clientId);
		String name = DBManager.getClientName(clientId);
		logger.info("client " + name + " requested CardInfo: " + cardInfo);
		Utils.pause(1_000); // Имитация задержки при запросе к БД
		return cardInfo;
	}


	@Override
	public void sendTransaction(Long uuid, String cardNumber, Integer amount) 
			throws IllegalArgumentException, SecurityViolationException 
	{
		if (!onLineClients.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: transaction from non-existent uuid detected");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer clientId = onLineClients.getId(uuid);
		String name = DBManager.getClientName(clientId);
		Integer clientIdByCard;
		
		// Проверка наличия карты
		try {
			clientIdByCard = DBManager.getClientId(cardNumber);
		} catch (Exception e) {
			logger.warn("SECURITY VIOLATION: transaction by client " + name + " with fake card number detected");
			logger.warn(e.getMessage());
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		
		// Проверка принадлежности карты клиенту
		if (clientIdByCard != clientId) {
			logger.warn("SECURITY VIOLATION: transaction by client " + name + " with fake card number detected");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		
		// Попытка обхода на клиентской стороне проверки списания с отрицательным балансом
		Account account = DBManager.getAccount(cardNumber);
		Integer balance = account.getBalance();
		if (balance < amount) {
			logger.warn("SECURITY VIOLATION: client " + name + " is trying to withdrawal from card " + cardNumber + "with negative balance in summary");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}

		// Проветка обхода на клиентской стороне проверки списания с заблокированной карты
		Card card = DBManager.getCard(cardNumber);
		Boolean isBlocked = card.getIsBlocked();
		if (isBlocked) {
			logger.warn("SECURITY VIOLATION: client " + name + " is trying to withdraw from blocking card " + cardNumber);
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		
		Integer accountId = account.getId();
		DBManager.changeBalance(accountId, amount);
		logger.info("клиент " + name + " пополнил карту " + cardNumber + " на сумму " + amount);
		DBManager.storeTransaction(new Transaction(accountId, amount));
		logger.info("клиент " + name + " совершил транзакцию с карты " + cardNumber + " на сумму: " + amount);
	}

	@Override
	public void sendDoBlockCard(Long uuid, String cardNumber) 
			throws IllegalArgumentException, SecurityViolationException {

		if (!onLineClients.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: block card operation from non-existent uuid detected");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer clientId = onLineClients.getId(uuid);
		String name = DBManager.getClientName(clientId);
		Integer clientIdByCard;
		
		// Проверка наличия карты
		try {
			clientIdByCard = DBManager.getClientId(cardNumber);
		} catch (Exception e) {
			logger.warn("SECURITY VIOLATION: block card operation by client " + name + " with fake card number detected");
			logger.warn(e.getMessage());
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		
		// Проверка принадлежности карты клиенту
		if (clientIdByCard != clientId) {
			logger.warn("SECURITY VIOLATION: block card operation by client " + name + " with fake card number detected");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		
		DBManager.doBlockCard(cardNumber);
		logger.info("клиент " + name + " заблокировал карту " + cardNumber);
	}

	@Override
	public List<String> getBlockedCards(Long uuid) 
			throws IllegalArgumentException, SecurityViolationException 
	{
		if (!onLineAdmins.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: list of blocked cards query from non-existent uuid detected");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer adminId = onLineAdmins.getId(uuid);
		String name = DBManager.getAdminName(adminId);
		List<String> result;
		try {
			result = DBManager.getBlockedCards();
		} catch (IllegalArgumentException e) {
			this.signOut(uuid);
			throw e;
		}
		logger.info("admin " + name + " requested blocked cards: " + result);
		return result;
	}

	@Override
	public void sendDoUnblock(Long uuid, String cardNumber) 
			throws IllegalArgumentException, SecurityViolationException 
	{
		if (!onLineAdmins.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: unblock operation from non-existent uuid detected");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer adminId = onLineAdmins.getId(uuid);
		String name = DBManager.getAdminName(adminId);
		DBManager.doUnblockCard(cardNumber);
		logger.info("админ " + name + " разблокировал карту " + cardNumber);
	}

	@Override
	public List<TransactionInfo> getTransactions(Long uuid, String cardNumber) 
			throws IllegalArgumentException, SecurityViolationException 
	{
		if (!onLineClients.isUuidExist(uuid)) {
			logger.warn("SECURITY VIOLATION: block operation from non-existent uuid detected");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		Integer clientId = onLineClients.getId(uuid);
		String name = DBManager.getClientName(clientId);
		Integer clientIdByCard;
		
		// Проверка наличия карты
		try {
			clientIdByCard = DBManager.getClientId(cardNumber);
		} catch (Exception e) {
			logger.warn("SECURITY VIOLATION: query transactions by client " + name + " with fake card number detected");
			logger.warn(e.getMessage());
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}
		
		// Проверка принадлежности карты клиенту
		if (clientIdByCard != clientId) {
			logger.warn("SECURITY VIOLATION: query transactions by client " + name + " with fake card number detected");
			throw new SecurityViolationException(
					"Отклонено. Обратитесь в нашу службу безопасности за разъяснениями");
		}

		List<TransactionInfo> transactions = DBManager.getTransactions(cardNumber);
		logger.info("клиент " + name + " запросил транзакции " + transactions);
		return transactions;
	}

}
