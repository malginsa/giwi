package giwi.server;

import org.apache.ibatis.annotations.Insert;

/**
 * Мэппер таблицы транзакций.
 * Транзакции, выполняемые клиентом, сохраняются в БД
 * При пополнении счёта сумма транзакции положительна,
 * при оплате - сумма транзакции отрицательная
 *  
 * create table transaction (
 * account_id integer,  # идентификатор счёта
 * amount integer,		# сумма транзакции
 * date date,				# дата транзакции
 * times time				# время транзакции
 * );
 */

public interface TransactionMapper {

	@Insert("insert into transaction values (#{account_id}, #{amount}, NOW(), NOW())")
	Integer storeTransaction(Transaction transaction);

}
