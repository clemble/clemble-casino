package com.clemble.casino.server.event;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.Event;

/**
 * Created by mavarazy on 2/2/15.
 */
public class EventByTemplateSelector implements EventSelector {

    final private String template;

    public EventByTemplateSelector(String template) {
        this.template = template;
    }

    @Override
    public boolean filter(Event event) {
        return event instanceof TemplateAware && ((TemplateAware) event).getTemplate().equals(template);
    }
}
