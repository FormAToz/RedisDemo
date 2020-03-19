import java.util.Random;

public class RedisTest {

    private static final int USERS = 20;
    private static final int CASE_OF_PURCHASE = 10;

    public static void main(String[] args) throws InterruptedException {
        RedisStorage redisStorage = new RedisStorage();
        redisStorage.init();

        //создаем 20 пользователей
        for (int i = 1; i <= USERS; i++) {
            redisStorage.addUser(i);
        }

        while (true) {

            for (int i = 1; i <= USERS; i++) {
                redisStorage.printUser(i);
                Thread.sleep(500);

                if (i % CASE_OF_PURCHASE == 0) {
                    var userId = new Random().nextInt(USERS);
                    System.out.println("Пользователь " + userId + " оплатил услугу" );
                    redisStorage.makeUserLast(userId);
                    Thread.sleep(1000);
                }
            }
        }
    }
}
