package giwi.server;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import giwi.shared.CardInfo;

public class DBManager {

	private static SqlSessionFactory sqlSessionFactory;

	static 
	{
		try {
			Reader resourceReader = Resources.getResourceAsReader("config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceReader);
			sqlSessionFactory.getConfiguration().addMapper(ClientMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(AccountMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(TransactionMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(AdminMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isAdmin(String name, String password) 
	{
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AdminMapper mapper = session.getMapper(AdminMapper.class);
			Admin admin = mapper.getAdmin(name, password);
			return null != admin;
		} finally {
			session.close();
		}
	}
	
	public static Integer getClientId(String name, String password) 
		throws IllegalArgumentException
	{
		SqlSession session = sqlSessionFactory.openSession();
		try {
			ClientMapper mapper = session.getMapper(ClientMapper.class);
			Client client = mapper.getClient(name, password);
			if (null == client) {
				throw new IllegalArgumentException("Неверное имя или пароль");
			}
			return client.getId();
		} finally {
			session.close();
		}
	}

	public static Integer getClientId(String cardNumber) 
			throws IllegalArgumentException 
	{
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			Account account = mapper.getAccount(cardNumber);
			if (null == account) {
				throw new IllegalArgumentException(
						"Ошибка БД: У карты "+ cardNumber +" не найден Id клинта");
			}
			return account.getClientId();
		} finally {
			session.close();
		}
	}
	
	public static List<CardInfo> getCardInfo(Integer clientId) 
			throws IllegalArgumentException
	{
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			List<CardInfo> accounts = mapper.getCardInfo(clientId);
			if (accounts.isEmpty()) {
				throw new IllegalArgumentException("Карт не найдено");
			}
			return accounts;
		} finally {
			session.close();
		}
	}

	public static Card getCard(String cardNumber) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			Card card = mapper.getCard(cardNumber);
			return card;
		} finally {
			session.close();
		}
	}

	public static void changeBalance(Integer accountId, Integer amount) {
	SqlSession session = sqlSessionFactory.openSession();
	try {
		AccountMapper mapper = session.getMapper(AccountMapper.class);
		Integer result = mapper.changeBalance(accountId, amount); 
		session.commit();
		if (1 != result) {
			throw new IllegalArgumentException(
				"Операция отклонена: сбой в БД");
		}
	} finally {
		session.close();
	}
}

	public static void storeTransaction(Transaction transaction) {
	SqlSession session = sqlSessionFactory.openSession();
	try {
		TransactionMapper mapper = session.getMapper(TransactionMapper.class);
		Integer result = mapper.storeTransaction(transaction);
		session.commit();
		if (1 != result) {
			throw new IllegalArgumentException(
				"Транзакция не выполнена. Сбой в БД");
		}
	} finally {
		session.close();
	}
}

	public static Account getAccount(String cardNumber) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			Account account = mapper.getAccount(cardNumber);
			if (null == account) {
				throw new IllegalArgumentException(
						"Ошибка БД: Карта "+ cardNumber +" не имеет соответствующего счёта");
			}
			return account;
		} finally {
			session.close();
		}
	}

	public static void doBlockCard(String cardNumber) {
	SqlSession session = sqlSessionFactory.openSession();
	try {
		AccountMapper mapper = session.getMapper(AccountMapper.class);
		Integer result = mapper.blockCard(cardNumber); 
		session.commit();
		if (1 != result) {
			throw new IllegalArgumentException(
				"Операция отклонена: сбой в системе...");
		}
	} finally {
		session.close();
	}
}


	public static List<String> getBlockedCards() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			List<Card> cards = mapper.getBlockedCards();
			if (cards.isEmpty()) {
				throw new IllegalArgumentException("Заблокированных карт нет");
			}
			List<String> result = new ArrayList<>();
			for (Card card : cards) {
				result.add(card.getNumber());
			}
			return result;
		} finally {
			session.close();
		}
	}

	public static void doUnblockCard(String cardNumber) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			Integer result = mapper.unblockCard(cardNumber); 
			session.commit();
			if (1 != result) {
				throw new IllegalArgumentException(
					"Операция отклонена: сбой в системе...");
			}
		} finally {
			session.close();
		}
	}

//	public static CardInfo getCardInfo(String cardNumber) {
//	SqlSession session = sqlSessionFactory.openSession();
//	try {
//		AccountMapper mapper = session.getMapper(AccountMapper.class);
//		CardInfo cardInfo = mapper.getCardInfo(cardNumber);
//		if (null == cardInfo) {
//			throw new IllegalArgumentException("Информация о карте не найдена");
//		}
//		return cardInfo;
//	} finally {
//		session.close();
//	}
//}
//
}
