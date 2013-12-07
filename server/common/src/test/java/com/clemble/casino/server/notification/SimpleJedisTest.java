package com.clemble.casino.server.notification;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: mavarazy
 * Date: 05/12/13
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class SimpleJedisTest {

    @Test
    public void scriptTest() {
        Jedis jedis = new Jedis("127.0.0.1");

        jedis.set("OneKey", "A");
        jedis.set("SKey", ":");
        try {
            String shaScript = jedis.scriptLoad(
                    " for i = 1,table.getn(KEYS) do " +
                        " local state = redis.call('get', KEYS[i])" +
                        " if (state ~= ':') then " +
                            " redis.log(redis.LOG_DEBUG, ARGV[1] .. ' > ' .. KEYS[i] .. ' is in ' .. state); " +
                            " return 'false'" +
                        " end " +
                    " end " +
                    " redis.log(redis.LOG_DEBUG, 'Starting ' .. ARGV[1]) " +
                    " for i = 1,table.getn(KEYS) do " +
                        " redis.call('set', KEYS[i], ARGV[1])" +
                    " end " +
                    " return 'true'");
            Assert.assertFalse(Boolean.valueOf((String) jedis.evalsha(shaScript, ImmutableList.<String>of("OneKey", "SKey"), ImmutableList.<String>of("A:A"))));

            jedis.set("OneKey", ":");
            Assert.assertTrue(Boolean.valueOf((String) jedis.evalsha(shaScript, ImmutableList.<String>of("OneKey", "SKey"), ImmutableList.<String>of("A:A"))));
            Assert.assertEquals(jedis.get("OneKey"), "A:A");
            Assert.assertEquals(jedis.get("SKey"), "A:A");
        } finally {
            jedis.del("OneKey");
            jedis.del("SKey");
        }
    }
}
