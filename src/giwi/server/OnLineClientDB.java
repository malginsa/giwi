package giwi.server;

import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

public class OnLineClientDB {

	private TreeSet<Integer> clientSet;
	private TreeMap<Long, Integer> uuidMap;
	private Random random;
	
	public OnLineClientDB() {
		clientSet = new TreeSet<>();
		uuidMap = new TreeMap<>();
		random = new Random();
	}
	
	public Long addClienId(Integer clientId) throws IllegalArgumentException {
		if (isClientIdExist(clientId)) {
			GiwiServiceImpl.logger.warn("Клиент id=" + clientId + " уже вошёл в систему");
			throw new IllegalArgumentException("Клиент с id=" + clientId + " уже вошёл в систему");
		}
		Long uuid = random.nextLong();
		clientSet.add(clientId);
		uuidMap.put(uuid, clientId);
		return uuid;
	}
	
	public void deleteClientId(Long uuid) {
		Integer clientId = uuidMap.remove(uuid);
		if (null == clientId ) {
			GiwiServiceImpl.logger.warn("Клиент вышел из системы, не войдя в неё");
		};
		clientSet.remove(clientId);
		return;
	}
	
	public boolean isUuidExist(Long uuid) {
		return uuidMap.containsKey(uuid);
	}
	
	public Integer getClientId(Long uuid) {
		return uuidMap.get(uuid);
	}

	private boolean isClientIdExist(Integer clientId) {
		return clientSet.contains(clientId);
	}
}
