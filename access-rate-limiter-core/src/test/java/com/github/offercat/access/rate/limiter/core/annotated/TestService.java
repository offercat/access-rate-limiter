package com.github.offercat.access.rate.limiter.core.annotated;

import java.util.concurrent.TimeUnit;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月01日 13:20:39
 */
public class TestService {

    private String test;
    private int id;

    public TestService(int id) {
        this.id = id;
    }

//    public TestService(String test) {
//        this.test = test;
//    }

    public String say() {return "fail";}
    @AccessRateLimit(count = 3, blockWait = true, backupMethod = "this#say", limitFactorMethod = "com.github.offercat.access.rate.limiter.core.annotated.TestService#test")
    public String say(String userId, User user1,  User user2, User user3) {
        return "say " + userId;
    }

    public static String test() {
        return "4";
    }
}
