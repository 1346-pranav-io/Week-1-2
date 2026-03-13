import java.util.*;
public class FlashSaleInventoryManager
{
    private Map<String, Integer> stock = new HashMap<>();
    private Map<String, LinkedHashMap<Integer, Integer>> waitingList = new HashMap<>();
    public FlashSaleInventoryManager(Map<String, Integer> initialStock)
    {
        stock.putAll(initialStock);
        for (String product : initialStock.keySet())
        {
            waitingList.put(product, new LinkedHashMap<>());
        }
    }
    public synchronized String checkStock(String productId)
    {
        int s = stock.getOrDefault(productId, 0);
        return s + " units available";
    }
    public synchronized String purchaseItem(String productId, int userId)
    {
        int s = stock.getOrDefault(productId, 0);
        if (s > 0)
        {
            stock.put(productId, s - 1);
            return "Success, " + (s - 1) + " units remaining";
        }
        else
        {
            LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);
            int position = queue.size() + 1;
            queue.put(userId, position);
            return "Added to waiting list, position #" + position;
        }
    }
    public static void main(String args[])
    {
        Map<String, Integer> initial = new HashMap<>();
        initial.put("IPHONE15_256GB", 100);
        FlashSaleInventoryManager manager = new FlashSaleInventoryManager(initial);
        System.out.println(manager.checkStock("IPHONE15_256GB"));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}