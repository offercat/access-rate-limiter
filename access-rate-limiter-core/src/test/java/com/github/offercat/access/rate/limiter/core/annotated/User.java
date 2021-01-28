package com.github.offercat.access.rate.limiter.core.annotated;

import lombok.Data;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月01日 20:45:20
 */
@Data
public class User {

    private int id;
    private String userId;
    private User user;
}
