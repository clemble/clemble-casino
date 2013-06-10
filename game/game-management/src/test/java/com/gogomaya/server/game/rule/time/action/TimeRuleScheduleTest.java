package com.gogomaya.server.game.rule.time.action;

public class TimeRuleScheduleTest {

    final TimeRuleScheduler ruleScheduler = new TimeRuleScheduler(new TimeTaskFactory());
    //
    // @Test
    // public void testComparison() {
    // ruleScheduler.schedule(1, -1);
    // ruleScheduler.schedule(2, 100);
    // Assert.assertEquals(1, ruleScheduler.poll().getSession());
    // }
    //
    // @Test
    // public void testReScheduleComparison() {
    // ruleScheduler.schedule(1, -500);
    // ruleScheduler.schedule(2, -100);
    // ruleScheduler.schedule(1, 100);
    // Assert.assertEquals(2, ruleScheduler.poll().getSession());
    // }

}
