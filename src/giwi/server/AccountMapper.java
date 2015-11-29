package giwi.server;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

//import giwi.client.Account;
import giwi.shared.CardInfo;

public interface AccountMapper {

	@Select("select card.number, card.isblocked, account.balance from account, card where account.client_id = #{clientId} and account.id = card.account_id")
	List<CardInfo> getCardInfo(Integer clientId);

	@Select("select account.* from card, account where card.number = #{cardNumber} and account.id = card.account_id;")
	Account getAccount(String cardNumber);

	@Select("select * from card where number = #{cardNumber}")
	Card getCard(String cardNumber);

	// @return rows affected
	@Update("update account set balance = balance + #{param2} where id = #{param1}")
	Integer changeBalance(Integer param1, Integer param2);

	@Update("update card set isBlocked = TRUE where number = #{cardNumber}")
	Integer blockCard(String cardNumber);

	@Select("select * from card where isBlocked = TRUE")
	List<Card> getBlockedCards();

	@Update("update card set isBlocked = FALSE where number = #{cardNumber}")
	Integer unblockCard(String cardNumber);

//	@Select("select card.number, card.isblocked, account.balance from account, card where card.number = #{cardNumber} and account.id = card.account_id")
//	CardInfo getCardInfo(String cardNumber);
//
//	@Select("select * from account where client_id = #{client_id}")
//	List<Account> getAccounts(Integer client_id);
//
}
