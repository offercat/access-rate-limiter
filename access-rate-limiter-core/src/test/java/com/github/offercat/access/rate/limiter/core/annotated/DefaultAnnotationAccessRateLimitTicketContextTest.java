package com.github.offercat.access.rate.limiter.core.annotated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultAnnotationAccessRateLimitTicketContextTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void refresh() throws InterruptedException {
        DefaultAnnotationAccessRateLimitContext context = new DefaultAnnotationAccessRateLimitContext("com.github.offercat.access.rate.limiter");
        context.refresh();
        System.out.println(context.getLimitPoints());

        User user = new User();
        user.setId(123);
        user.setUserId("dsfdsfdsf");

        User user1 = new User();
        user1.setId(456);
        user1.setUserId("fgdhdfghfg");

        user.setUser(user1);

        TestService testService = context.getProxy(new TestService(3));
        for (int i=0; i<50; i++) {
            int finalI = i;
            new Thread(() -> {
                String str = testService.say(String.valueOf(finalI), user, user, user);
                System.out.println(str);
            }).start();
        }

        Thread.sleep(10000);
    }
}