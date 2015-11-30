package giwi.server;

/**
 * Вспомогательные утилиты
 */

public class Utils {

	public static void pause(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
