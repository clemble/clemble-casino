package com.gogomaya.server.game.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.SpecificationName;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { GameManagementSpringConfiguration.class })
public class GameTableQueueTest {

    final private int SIZE = 1000;

    @Inject
    GameTableQueue tableQueue;

    GameSpecification specification;

    Random random = new Random();

    @Test
    public void queueConsumption() throws InterruptedException, ExecutionException {
        this.specification = new GameSpecification().setName(new SpecificationName().setName("name").setGroup("group"));
        // Step 1. Generating random List
        List<Callable<Long>> queue = new ArrayList<Callable<Long>>(SIZE * 2);
        for (int i = 0; i < SIZE; i++) {
            queue.add(new QueuePopulator(i));
            queue.add(new QueueConsumer());
        }
        // Step 2. Shuffling to exclude guaranteed results
        Collections.shuffle(queue);
        ExecutorService executorService = Executors.newFixedThreadPool(SIZE * 2);
        // Step 3. Running all services at once
        try {
            List<Future<Long>> executionResults = executorService.invokeAll(queue);
            Set<Long> identifiers = new HashSet<Long>();
            for (Future<Long> result : executionResults) {
                long tableId = result.get();
                if (identifiers.contains(tableId)) {
                    identifiers.remove(tableId);
                } else {
                    identifiers.add(tableId);
                }
            }
            Assert.assertEquals(identifiers.size(), 0);
        } finally {
            executorService.shutdown();
        }

    }

    private class QueuePopulator implements Callable<Long> {
        private final long tableId;

        public QueuePopulator(long tableId) {
            this.tableId = tableId;
        }

        @Override
        public Long call() {
            // Step 1. Adding to the table Queue
            tableQueue.add(tableId, specification);
            return tableId;
        }

    }

    private class QueueConsumer implements Callable<Long> {

        @Override
        public Long call() {
            // Step 1. Pooling table from specification
            Long polledTable = tableQueue.poll(specification);
            while (polledTable == null) {
                // Step 2. In case of a failure try to poll again in timeout
                try {
                    Thread.sleep(random.nextInt(100));
                } catch (InterruptedException e) {
                }
                polledTable = tableQueue.poll(specification);
            }
            // Step 3. Adding it back
            tableQueue.add(polledTable, specification);
            // Step 4. Trying to poll again
            polledTable = tableQueue.poll(specification);
            while (polledTable == null) {
                // Step 2. In case of a failure try to poll again in timeout
                try {
                    Thread.sleep(random.nextInt(100));
                } catch (InterruptedException e) {
                }
                polledTable = tableQueue.poll(specification);
            }
            return polledTable;
        }

    }
}
