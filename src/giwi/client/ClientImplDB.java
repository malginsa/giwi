package giwi.client;

import com.google.gwt.core.client.GWT;

public class ClientImplDB {

	static Long uuid;
	static final GiwiServiceAsync giwiService = GWT.create(GiwiService.class);
	static String clientName;

}
