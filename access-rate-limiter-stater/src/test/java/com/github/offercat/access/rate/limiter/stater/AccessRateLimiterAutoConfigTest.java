package com.github.offercat.access.rate.limiter.stater;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class AccessRateLimiterAutoConfigTest {

    @Autowired
    TestLimitController testLimitController;

    @Test
    void springBootAnnotationAccessRateLimitContext() throws InterruptedException {
        User user = new User();
        user.setId(123);
        user.setUserId("dsfdsfdsf");

        User user1 = new User();
        user1.setId(456);
        user1.setUserId("fgdhdfghfg");

        user.setUser(user1);

        for (int i=0; i<100; i++) {
            if (i == 1) {
                Thread.sleep(3000);
            }
            int finalI = i;
            new Thread(() -> {
                String str = testLimitController.say(String.valueOf(finalI), user, user, user);
                System.out.println(str);
            }).start();
        }

        Thread.sleep(10000);
    }
}