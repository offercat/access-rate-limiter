package com.github.offercat.access.rate.limiter.stater;

import com.github.offercat.access.rate.limiter.core.annotated.AccessLimitController;
import com.github.offercat.access.rate.limiter.core.annotated.QpsLimit;
import org.springframework.stereotype.Service;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月04日 12:04:38
 */
@Service
@AccessLimitController
public class TestLimitController {

    @QpsLimit(value = 3, blockWait = true, maxWaitTime = 5000)
    public String say(String userId, User user1, User user2, User user3) {
        return "say " + userId;
    }
}
