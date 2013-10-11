package com.clemble.casino.json;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;



/**
 * Custom JSON annotation introspector, which ignores @JsonIgnore
 * 
 * @author mavarazy
 *
 */
public class CustomJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    /**
     * Generated 15
     */
    private static final long serialVersionUID = -7081978483743119569L;

    @Override
    protected boolean _isIgnorable(Annotated a) {
        return false;
    }

}
