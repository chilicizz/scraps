package com.cyrilng.agrona;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.ControlledMessageHandler;
import org.agrona.concurrent.MessageHandler;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBufferDescriptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashSet;

class OneToOneRingBufferTests {
    private static final Logger logger = LoggerFactory.getLogger(OneToOneRingBufferTests.class);

    @Test
    public void runTest() {
        final int bufferLength = 4096 + RingBufferDescriptor.TRAILER_LENGTH;
        final UnsafeBuffer internalBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(bufferLength));
        final OneToOneRingBuffer ringBuffer = new OneToOneRingBuffer(internalBuffer);
        final MessageCapture capture = new MessageCapture();

        final UnsafeBuffer toSend = new UnsafeBuffer(ByteBuffer.allocateDirect(10));
        String testString = "0123456789";
        toSend.putStringWithoutLengthAscii(0, testString);

        for (int i = 0; i < 10000; i++) {
            for (int k = 0; k < 20; k++) {
                final boolean success = ringBuffer.write(1, toSend, 0, 10);
                if (!success) {
                    System.err.println("Failed to write!");
                }
            }
            ringBuffer.read(capture, 40);
        }

        Assertions.assertEquals(1, capture.receivedStrings.size());
        Assertions.assertTrue(capture.receivedStrings.contains(testString));
        Assertions.assertEquals(200000, capture.count);
        Assertions.assertNotEquals(0, ringBuffer.consumerPosition());
        Assertions.assertNotEquals(0, ringBuffer.producerPosition());
    }

    static class MessageCapture implements MessageHandler {

        private HashSet<String> receivedStrings = new HashSet<>();
        private int count = 0;

        @Override
        public void onMessage(final int msgTypeId, final MutableDirectBuffer buffer, final int index, final int length) {
            receivedStrings.add(buffer.getStringWithoutLengthAscii(index, length));
            count++;
        }

    }

    static class ControlledMessageCapture implements ControlledMessageHandler {

        private HashSet<String> receivedStrings = new HashSet<>();
        private int count = 0;

        @Override
        public ControlledMessageHandler.Action onMessage(final int msgTypeId,
                                                         final MutableDirectBuffer buffer,
                                                         final int index,
                                                         final int length) {
            receivedStrings.add(buffer.getStringWithoutLengthAscii(index, length));
            count++;
            return Action.COMMIT;
        }

    }
}