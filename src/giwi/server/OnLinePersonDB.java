package giwi.server;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.TreeMap;
import java.util.TreeSet;

import giwi.shared.SecurityViolationException;

public class OnLinePersonDB {

	private TreeSet<Integer> personSet;
	private TreeMap<Long, Integer> uuidMap;
	private SecureRandom secureRandom;
	
	public OnLinePersonDB() throws NoSuchAlgorithmException {
		personSet = new TreeSet<>();
		uuidMap = new TreeMap<>();
		secureRandom = SecureRandom.getInstance("SHA1PRNG");
	}
	
	public Long addPersonId(Integer personId) 
			throws SecurityViolationException 
	{
		if (isPersonIdExist(personId)) {
			throw new SecurityViolationException(
					"Пользователь с id=" + personId + " уже вошёл в систему");
		}
		Long uuid = secureRandom.nextLong();
		personSet.add(personId);
		uuidMap.put(uuid, personId);
		return uuid;
	}
	
	// возвращает id удалённой персоны
	public Integer removePersonId(Long uuid) 
			throws SecurityViolationException 
	{
		Integer personId = uuidMap.remove(uuid);
		if (null == personId ) {
			throw new SecurityViolationException(
					"Пользователь вышел из системы, не войдя в неё");
		};
		personSet.remove(personId);
		return personId;
	}
	
	public boolean isUuidExist(Long uuid) {
		return uuidMap.containsKey(uuid);
	}
	
	public Integer getId(Long uuid) {
		return uuidMap.get(uuid);
	}

	private boolean isPersonIdExist(Integer personId) {
		return personSet.contains(personId);
	}
}
