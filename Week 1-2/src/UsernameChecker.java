import java.util.*;
public class UsernameChecker
{
    private Map<String, Integer> users = new HashMap<>();
    private Map<String, Integer> attempts = new HashMap<>();
    public UsernameChecker(Map<String, Integer> existingUsers)
    {
        users.putAll(existingUsers);
    }
    public boolean checkAvailability(String username)
    {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !users.containsKey(username);
    }
    public List<String> suggestAlternatives(String username)
    {
        List<String> suggestions = new ArrayList<>();
        if (!users.containsKey(username)) return suggestions;
        for (int i = 1; i <= 5; i++)
        {
            String candidate = username + i;
            if (!users.containsKey(candidate)) suggestions.add(candidate);
        }
        String dotVariant = username.replace("_", ".");
        if (!users.containsKey(dotVariant)) suggestions.add(dotVariant);
        return suggestions;
    }
    public String getMostAttempted()
    {
        String result = null;
        int max = 0;
        for (Map.Entry<String, Integer> entry : attempts.entrySet())
        {
            if (entry.getValue() > max)
            {
                max = entry.getValue();
                result = entry.getKey();
            }
        }
        return result;
    }
    public void registerUser(String username, int userId)
    {
        users.put(username, userId);
    }
    public static void main(String args[])
    {
        Map<String, Integer> existing = new HashMap<>();
        existing.put("john_doe", 1);
        existing.put("admin", 2);
        UsernameChecker checker = new UsernameChecker(existing);
        System.out.println(checker.checkAvailability("john_doe"));
        System.out.println(checker.checkAvailability("jane_smith"));
        System.out.println(checker.suggestAlternatives("john_doe"));
        System.out.println(checker.getMostAttempted());
    }
}