package giwi.server;

import org.apache.ibatis.annotations.Insert;

public interface TransactionMapper {

	@Insert("insert into transaction values (#{client_id}, #{fromCard}, #{toCard}, #{amount}, NOW())")
	Integer storeTransaction(Transaction transaction);

}
