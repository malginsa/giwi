package giwi.server;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import giwi.shared.Account;

public interface AccountMapper {
	
	@Select("select * from account where client_id = #{client_id}")
	List<Account> getAccounts(Integer client_id);

	@Select("select * from account where cardNumber = #{fromCard}")
	Account getBalance(String fromCard);
	
	// @return rows affected
	@Update("update account set balance = balance + #{param2} where cardNumber = #{param1}")
	Integer changeBalance(String param1, Integer param2);

	@Update("update account set isBlocked = TRUE where cardNumber = #{cardNumber}")
	Integer blockCard(String cardNumber);

	@Select("select * from account where isBlocked = TRUE")
	List<Account> getBlockedAccounts();

	@Update("update account set isBlocked = FALSE where cardNumber = #{cardNumber}")
	Integer unblockCard(String cardNumber);

}
