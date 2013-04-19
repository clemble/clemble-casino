package com.gogomaya.server.json;

import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;


/**
 * Custom JSON annotation introspector, which ignores @JsonIgnore
 * 
 * @author mavarazy
 *
 */
public class CustomJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    protected boolean _isIgnorable(Annotated a) {
        return false;
    }

}
