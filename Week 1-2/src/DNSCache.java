import java.util.*;
class DNSEntry
{
    String domain;
    String ip;
    long expiry;
    DNSEntry(String domain, String ip, long ttl)
    {
        this.domain = domain;
        this.ip = ip;
        this.expiry = System.currentTimeMillis() + ttl * 1000;
    }
    boolean isExpired()
    {
        return System.currentTimeMillis() > expiry;
    }
}
public class DNSCache
{
    private int capacity;
    private Map<String, DNSEntry> cache;
    private long hits = 0;
    private long misses = 0;
    private long totalLookupTime = 0;
    private long requests = 0;
    public DNSCache(int capacity)
    {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true)
        {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest)
            {
                return size() > DNSCache.this.capacity;
            }
        };
    }
    public synchronized String resolve(String domain)
    {
        long start = System.nanoTime();
        DNSEntry entry = cache.get(domain);
        if (entry != null && !entry.isExpired())
        {
            hits++;
            requests++;
            totalLookupTime += System.nanoTime() - start;
            return "Cache HIT → " + entry.ip;
        }
        if (entry != null && entry.isExpired())
        {
            cache.remove(domain);
        }
        misses++;
        String ip = queryUpstream(domain);
        cache.put(domain, new DNSEntry(domain, ip, 300));
        requests++;
        totalLookupTime += System.nanoTime() - start;
        return "Cache MISS → " + ip;
    }
    private String queryUpstream(String domain)
    {
        return "172.217.14." + new Random().nextInt(255);
    }
    public String getCacheStats()
    {
        double hitRate = requests == 0 ? 0 : (hits * 100.0 / requests);
        double avgLookup = requests == 0 ? 0 : (totalLookupTime / 1e6) / requests;
        return "Hit Rate: " + String.format("%.2f", hitRate) + "%, Avg Lookup Time: " + String.format("%.2f", avgLookup) + "ms";
    }
    public static void main(String args[])
    {
        DNSCache dns = new DNSCache(5);
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("openai.com"));
        System.out.println(dns.getCacheStats());
    }
}