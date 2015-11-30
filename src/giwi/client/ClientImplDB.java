package giwi.client;

import com.google.gwt.core.client.GWT;

/**
 * Общие данные для виджетов в клиентской части  
 */

public class ClientImplDB {

	static Long uuid;
	static String clientName;
	static final GiwiServiceAsync giwiService = GWT.create(GiwiService.class);

}
