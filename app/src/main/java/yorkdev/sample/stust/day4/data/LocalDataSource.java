package yorkdev.sample.stust.day4.data;


import yorkdev.sample.stust.MyApp;
import yorkdev.sample.stust.day3.room.MyDao;
import yorkdev.sample.stust.day3.room.MyDatabase;

public class LocalDataSource {
    public static MyDao generate() {
        return MyDatabase.getInstance(MyApp.getAppContext()).myDao();
    }
}
