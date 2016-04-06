package org.rossjohnson.xbox;

import java.util.Date;

/**
 * Created by ross on 4/5/16.
 */
public class NotifcationController {
	static void log(String message) {
		System.out.println(String.format("[%s] %s", new Date(), message));
	}
}
