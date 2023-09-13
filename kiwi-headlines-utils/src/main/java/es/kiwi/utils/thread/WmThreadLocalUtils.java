package es.kiwi.utils.thread;

import es.kiwi.model.wemedia.pojos.WmUser;

public class WmThreadLocalUtils {

    private final static ThreadLocal<WmUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 存入线程
     * @param wmUser
     */
    public static void setUser(WmUser wmUser) {
        WM_USER_THREAD_LOCAL.set(wmUser);
    }

    /**
     * 从线程中获取
     * @return
     */
    public static WmUser getUser() {
        return WM_USER_THREAD_LOCAL.get();
    }

    public static void clear() {
        WM_USER_THREAD_LOCAL.remove();
    }
}
