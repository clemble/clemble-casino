package com.clemble.server.tag.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 2/3/15.
 */
public class TagApplicationInitializer extends AbstractWebApplicationInitializer {

    public TagApplicationInitializer() {
        super(TagSpringConfiguration.class);
    }

}
