package giwi.server;

/**
 * Транзакции выполняемые клиентом
 * При пополнении счёта сумма транзакции положительна,
 * при оплате счёта сумма транзакции отрицательная
 * account_id	# номер счёта
 * amount		# сумма транзакции
 * текущие дату и время проставляется автоматически 
 * при добавлении записи в БД   
 */

public class Transaction {

	private Integer account_id;
	private Integer amount;

//	private Transaction() {}
//	
	public Transaction(Integer account_id, Integer amount) {
		this.account_id = account_id;
		this.amount = amount;
	}
	
}
