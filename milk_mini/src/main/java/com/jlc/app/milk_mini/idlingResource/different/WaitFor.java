package com.jlc.app.milk_mini.idlingResource.different;

import static com.jlc.app.milk_mini.utils.DoIt.sleep;

public class WaitFor {

    /**
     * 自定义的等待程序空闲
     * @param idle 空闲的条件
     * @param step 轮巡的时间步长 单位 ms
     * @param timeout 超时 单位 ms
     */
    public static void waitForIdle(Idle idle , long step , long timeout) {
        while (timeout > 0) {
            if (idle.isIdle()) {
                return;
            }
            sleep(step);
            timeout -= step;
        }
    }
    public static void waitForIdle(Idle idle) {
        waitForIdle(idle,500,5000);
    }

    public static void waitForIdle(SelfIdle idle , long step , long timeout) {

    }
}
