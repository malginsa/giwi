package giwi.shared;

// Этот класс используется для того, чтобы при авторизации передать
// из серверной части в клиентскую статус персоны (клиент или администратор)
// и его uuid 

import java.io.Serializable;

public class PersonInfo implements Serializable{

	private static final long serialVersionUID = 5621635506413600298L;

	private Long uuid;
	private Boolean isAdmin;

	private Status status;
	
	public static enum Status {ADMIN, CLIENT}
	
	private PersonInfo() {}
	
	public PersonInfo(Long uuid, Status status) {
		this.uuid = uuid;
		this.status = status;
	}

	public Long getUuid() {
		return uuid;
	}

	public Status getStatus() {
		return status;
	}

}
