package giwi.server;

import org.apache.ibatis.annotations.Insert;

public interface TransactionMapper {

	@Insert("insert into transaction values (#{account_id}, #{amount}, NOW())")
	Integer storeTransaction(Transaction transaction);

}
