import java.util.*;

public class Cache {
	
	// A hashtable representing the cache, where entries are hashed by host name.
	private Hashtable<String, CacheEntry> cache;
	
	// An inner class representing a single cache entry.
	public class CacheEntry {
		
		// The hostname
		private String host;
		
		// The IP address
		private String ip;
		
		// The time this entry was cached
		private long time;
		
		/**
		 * The constructor
		 * @param host
		 * @param ip
		 */
		public CacheEntry(String host, String ip) {
			this.host = host;
			this.ip = ip;
			time = System.nanoTime();
		}

		/**
		 * @return the host
		 */
		public String getHost() {
			return host;
		}

		/**
		 * @param host the host to set
		 */
		public void setHost(String host) {
			this.host = host;
		}

		/**
		 * @return the ip
		 */
		public String getIp() {
			return ip;
		}

		/**
		 * @param ip the ip to set
		 */
		public void setIp(String ip) {
			this.ip = ip;
		}

		/**
		 * @return the time
		 */
		public long getTime() {
			return time;
		}

		/**
		 * @param time the time to set
		 */
		public void setTime(long time) {
			this.time = time;
		}
		
		/**
		 * Returns true if the entry has been in the cache for longer than 30 seconds.
		 * @return boolean
		 */
		public boolean isExpired() {
			if ((System.nanoTime() - getTime()) > 30 * 1000000000)
				return true;
			return false;
		}
	}
	
	/**
	 * The constructor
	 */
	public Cache() {
		cache = new Hashtable<String, CacheEntry>(50);
	}
	
	/**
	 * Adds an entry to the current cache
	 * @param host
	 * @param ip
	 */
	public void add(String host, String ip) {
		cache.put(host, new CacheEntry(host, ip));
	}
	
	/**
	 * Returns the host's IP if the record is cached and not expired. If an expired record for this host exists, the record is deleted.
	 * @param host
	 * @return
	 */
	public String getHost(String host) {
		CacheEntry entry = cache.get(host);
		if (entry != null && !entry.isExpired())
			return entry.getIp();
		else if (entry != null)
			cache.remove(host);
		return host;
	}

}
