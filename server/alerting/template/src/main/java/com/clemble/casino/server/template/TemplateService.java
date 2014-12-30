package com.clemble.casino.server.template;

import java.util.Map;

/**
 * Created by mavarazy on 12/30/14.
 */
public interface TemplateService {

    String produce(String template, Map<String, String> params);

}
