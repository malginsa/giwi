package giwi.server;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import giwi.shared.Account;

public class DBManager {

	private static SqlSessionFactory sqlSessionFactory;

	static 
	{
		try {
			Reader resourceReader = Resources.getResourceAsReader("config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceReader);
			sqlSessionFactory.getConfiguration().addMapper(ClientMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(AccountMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
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

	public static List<Account> getAccounts(int client_id)
			throws IllegalArgumentException
	{
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			List<Account> accounts = mapper.getAccounts(client_id);
			if (accounts.isEmpty()) {
				throw new IllegalArgumentException("У Вас нет карт");
			}
			return accounts;
		} finally {
			session.close();
		}
	}

	public static Integer getBalance(String fromCard) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			Account account = mapper.getBalance(fromCard);
			if (null == account) {
				throw new IllegalArgumentException(
					"Операция отклонена: неверный номер карты");
			}
			return account.getBalance();
		} finally {
			session.close();
		}
	}

	public static void decreaseBalance(String fromCard, Integer amount) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			AccountMapper mapper = session.getMapper(AccountMapper.class);
			Integer result = mapper.decreaseBalance(fromCard, amount);
			if (0 == result) {
				throw new IllegalArgumentException(
					"Операция отклонена: сбой в системе...");
			}
		} finally {
			session.close();
		}
	}

}
