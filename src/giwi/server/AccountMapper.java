package giwi.server;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import giwi.shared.Account;

public interface AccountMapper {

	@Results({
	     @Result(property = "client_id", column = "client_id"),
	     @Result(property = "cardNumber", column = "cardNumber"),
	     @Result(property = "balance", column = "balance"),
	     @Result(property = "isBlocked", column = "isBlocked"),
	        })
	
	@Select("select * from account where client_id = #{client_id}")
	List<Account> getAccounts(Integer client_id);

	@Select("select * from account where cardNumber = #{fromCard}")
	Account getBalance(String fromCard);
	
	// update account set balance = balance - amount where cardNumber = fromCard;
	@Update("update account set balance = balance - #{param1} where cardNumber = #{param2}")
	Integer decreaseBalance(String param1, Integer param2);
	
}
