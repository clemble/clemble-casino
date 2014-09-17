package com.clemble.casino.server.event;

import com.clemble.casino.json.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by mavarazy on 8/23/14.
 */
public class SystemEventParserTest {
    
    final private String GOAL_JUDGE_INVITATION_ACCEPTED_EVENT = "{\"type\":\"sys:goal:judge:invitation:accepted\",\"invitation\":{\"player\":\"2tKnc58CuA\",\"judge\":\"Vn0xb2joqu\",\"goalKey\":\"2tKnc58CuAA86\",\"goal\":\"Run 30K\",\"status\":\"pending\"}}";

    @Test
    public void testInvitationAcceptedEventParsing() throws IOException {
        ObjectMapper objectMapper = ObjectMapperUtils.OBJECT_MAPPER;
        SystemEvent invitationAccepted = objectMapper.readValue(GOAL_JUDGE_INVITATION_ACCEPTED_EVENT, SystemEvent.class);
        Assert.assertNotNull(invitationAccepted);
    }

}
