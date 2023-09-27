package es.kiwi.utils.thread;

import es.kiwi.model.user.pojos.ApUser;

public class AppThreadLocalUtils {

    private final static ThreadLocal<ApUser> APP_USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 存入线程
     * @param apUser
     */
    public static void setUser(ApUser apUser) {
        APP_USER_THREAD_LOCAL.set(apUser);
    }

    /**
     * 从线程中获取
     * @return
     */
    public static ApUser getUser() {
        return APP_USER_THREAD_LOCAL.get();
    }

    public static void clear() {
        APP_USER_THREAD_LOCAL.remove();
    }
}
