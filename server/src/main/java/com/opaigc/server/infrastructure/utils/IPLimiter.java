package com.opaigc.server.infrastructure.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/10
 */

public class IPLimiter {
	private final ConcurrentHashMap<String, AtomicInteger> ipCounter;
	private final ConcurrentHashMap<String, Long> ipTimeStamps;

	private final int limit;
	private final long window;

	public IPLimiter(int limit, long window) {
		this.ipCounter = new ConcurrentHashMap<>();
		this.ipTimeStamps = new ConcurrentHashMap<>();
		this.limit = limit;
		this.window = window;
	}

	public boolean isAllowed(String ip) {
		long currentTimeMillis = System.currentTimeMillis();
		AtomicInteger count = ipCounter.get(ip);
		Long lastTimeMillis = ipTimeStamps.get(ip);

		if (lastTimeMillis != null && (currentTimeMillis - lastTimeMillis) > window) {
			ipCounter.remove(ip);
			ipTimeStamps.remove(ip);
			return isAllowed(ip);
		}

		if (count == null) {
			ipCounter.put(ip, new AtomicInteger(1));
			ipTimeStamps.put(ip, currentTimeMillis);
			return true;
		}

		if (count.get() < limit) {
			count.incrementAndGet();
			return true;
		}

		return false;
	}
}
