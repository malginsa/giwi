package giwi.server;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import giwi.client.Account;
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
//			sqlSessionFactory.getConfiguration().addMapper(TransactionMapper.class);
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


	public static List<CardInfo> getCardInfo(Integer clientId) 
			throws IllegalArgumentException
	{
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			List<CardInfo> accounts = mapper.getCardInfo(clientId);
			if (accounts.isEmpty()) {
				throw new IllegalArgumentException("У Вас нет активных карт");
			}
			return accounts;
		} finally {
			session.close();
		}
	}


	
//	public static List<Account> getAccounts(int client_id)
//			throws IllegalArgumentException
//	{
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			AccountMapper mapper = session.getMapper(AccountMapper.class);
//			List<Account> accounts = mapper.getAccounts(client_id);
//			if (accounts.isEmpty()) {
//				throw new IllegalArgumentException("У Вас нет активных карт");
//			}
//			return accounts;
//		} finally {
//			session.close();
//		}
//	}
//
//	public static List<Account> getBlockedAccounts() {
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			AccountMapper mapper = session.getMapper(AccountMapper.class);
//			List<Account> accounts = mapper.getBlockedAccounts();
//			if (accounts.isEmpty()) {
//				throw new IllegalArgumentException("Заблокированных карт нет");
//			}
//			return accounts;
//		} finally {
//			session.close();
//		}
//	}
//
//	public static Integer getBalance(String fromCard) {
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			AccountMapper mapper = session.getMapper(AccountMapper.class);
//			Account account = mapper.getBalance(fromCard);
//			if (null == account) {
//				throw new IllegalArgumentException(
//					"Операция отклонена: неверный номер карты");
//			}
//			return account.getBalance();
//		} finally {
//			session.close();
//		}
//	}
//
//	public static void changeBalance(String fromCard, Integer amount) {
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			AccountMapper mapper = session.getMapper(AccountMapper.class);
//			Integer result = mapper.changeBalance(fromCard, amount); 
//			session.commit();
//			if (1 != result) {
//				throw new IllegalArgumentException(
//					"Операция отклонена: сбой в системе...");
//			}
//		} finally {
//			session.close();
//		}
//	}
//
//	public static void storeTransaction(Transaction transaction) {
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			TransactionMapper mapper = session.getMapper(TransactionMapper.class);
//			Integer result = mapper.storeTransaction(transaction);
//			session.commit();
//			if (1 != result) {
//				throw new IllegalArgumentException(
//					"Операция отклонена: транзакция не выполнена...");
//			}
//		} finally {
//			session.close();
//		}
//	}
//
//	public static void doBlockCard(String cardNumber) {
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			AccountMapper mapper = session.getMapper(AccountMapper.class);
//			Integer result = mapper.blockCard(cardNumber); 
//			session.commit();
//			if (1 != result) {
//				throw new IllegalArgumentException(
//					"Операция отклонена: сбой в системе...");
//			}
//		} finally {
//			session.close();
//		}
//	}
//
//	public static void doUnblockCard(String cardNumber) {
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			AccountMapper mapper = session.getMapper(AccountMapper.class);
//			Integer result = mapper.unblockCard(cardNumber); 
//			session.commit();
//			if (1 != result) {
//				throw new IllegalArgumentException(
//					"Операция отклонена: сбой в системе...");
//			}
//		} finally {
//			session.close();
//		}
//	}
//
}
