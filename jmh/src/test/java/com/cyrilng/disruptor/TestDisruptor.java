package com.cyrilng.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDisruptor {

    @Test
    public void runDisruptorPublishesAndHandlesEvents() throws Exception {
        final int bufferSize = 1024;
        final int eventsToPublish = 10;

        final Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

        final List<Long> received = new CopyOnWriteArrayList<>();

        // capture event values (primitive long) to avoid issues with event instance reuse
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> received.add(event.getValue()));
        disruptor.start();

        try {
            final RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
            final ByteBuffer bb = ByteBuffer.allocate(8);

            for (long l = 0; l < eventsToPublish; l++) {
                bb.putLong(0, l);
                ringBuffer.publishEvent((event, sequence, buffer) -> event.set(buffer.getLong(0)), bb);
            }

            // wait for consumer to process events (timeout to avoid hanging tests)
            final long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
            while (received.size() < eventsToPublish && System.nanoTime() < deadline) {
                Thread.sleep(10);
            }

            Assertions.assertEquals(eventsToPublish, received.size(), "should receive all published events");
            Assertions.assertEquals(eventsToPublish - 1, received.getLast().longValue(), "last received value");
        } finally {
            disruptor.shutdown();
        }
    }


    public static class LongEvent {
        private long value;

        public void set(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "LongEvent{" + "value=" + value + '}';
        }
    }
}
