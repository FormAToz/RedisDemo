import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

public class RedisStorage {

    //объект для работы с Redis
    private RedissonClient redisson;

    //объект для работы с ключами
    private RKeys rKeys;

    //объект для работы с List
    private RList<String> users;

    private final static String KEY = "USERS";

    public void listKeys() {
        rKeys.getKeys().forEach(System.out::println);
    }

    public void listUsers() {
        users.readAll().forEach(System.out::println);
    }

    public void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        try {
            redisson = Redisson.create(config);
        }catch (RedisConnectionException ex) {
            System.out.println("Error connection to Redis");
            System.out.println(ex.getMessage());
        }

        rKeys = redisson.getKeys();
        users = redisson.getList(KEY);
        rKeys.delete(KEY);
    }

    public void shutdown() {
        redisson.shutdown();
    }

    //LPUSH
    public void addUser(int id) {
        users.add(String.valueOf(id));
    }

    public void removeUser(int id) {
        users.remove(String.valueOf(id));
    }

    public String getUser(int id) {
        var user = users.get(--id);
        return user != null ? user : null;
    }

    public void makeUserLast(int id) {
        var user = getUser(id);

        if (user == null) {
            System.out.println("Такого пользователя не существует");

        }else {
            users.remove(String.valueOf(id));
            users.add(String.valueOf(id));
        }
    }

    //LINDEX
    public void printUser(int id) {
        System.out.println("На экране показывется пользователь " + getUser(id));
    }
}
