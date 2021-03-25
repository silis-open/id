/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*(c)http://gitee.com/silis/id*/
package App;

import java.security.SecureRandom;
import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

public class Id {
	private static final int LOW_ORDER_THREE_BYTES = 0x00ffffff;
	
	private static final char[] HEX_CHARS = new char[] {
      '0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	//当前机器的Id
	private static final int MACHINE_IDENTIFIER;
	//当前进程的Id
	private static final short PROCESS_IDENTIFIER;
	//带原子锁的计数器
	private static final AtomicInteger NEXT_COUNTER = new AtomicInteger(new SecureRandom().nextInt());
	
	static {
        try {
            MACHINE_IDENTIFIER = createMachineIdentifier();
            PROCESS_IDENTIFIER = createProcessIdentifier();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	private static int createMachineIdentifier() {
		// build a 2-byte machine piece based on NICs info
        int machinePiece;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                sb.append(ni.toString());
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    ByteBuffer bb = ByteBuffer.wrap(mac);
                    try {
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                    } catch (BufferUnderflowException shortHardwareAddressException) { //NOPMD
                        // mac with less than 6 bytes. continue
                    }
                }
            }
            machinePiece = sb.toString().hashCode();
        } catch (Throwable t) {
            // exception sometimes happens with IBM JVM, use random
            machinePiece = (new SecureRandom().nextInt());
            System.out.println("Failed to get machine identifier from network interface, using random number instead");
        }
        machinePiece = machinePiece & LOW_ORDER_THREE_BYTES;
        return machinePiece;
	}
	
	private static short createProcessIdentifier() {
        short processId;
        try {
            String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
            if (processName.contains("@")) {
                processId = (short) Integer.parseInt(processName.substring(0, processName.indexOf('@')));
            } else {
                processId = (short) java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
            }
        } catch (Throwable t) {
            processId = (short) new SecureRandom().nextInt();
			System.out.println("Failed to get process identifier from JMX, using random number instead");
        }

        return processId;
	}
	
	private final int timestamp;
    private final int machineIdentifier;
    private final short processIdentifier;
    private final int counter;
	
	/**
     * Create a new id.
     */
	private Id() {
        this((int) (new Date().getTime() / 1000), MACHINE_IDENTIFIER, PROCESS_IDENTIFIER, NEXT_COUNTER.getAndIncrement(), false);
    }
	
	private Id(final int timestamp, final int machineIdentifier, final short processIdentifier, final int counter, final boolean checkCounter) {
        if ((machineIdentifier & 0xff000000) != 0) {
            throw new IllegalArgumentException("The machine identifier must be between 0 and 16777215 (it must fit in three bytes).");
        }
        if (checkCounter && ((counter & 0xff000000) != 0)) {
            throw new IllegalArgumentException("The counter must be between 0 and 16777215 (it must fit in three bytes).");
        }
        this.timestamp = timestamp;
        this.machineIdentifier = machineIdentifier;
        this.processIdentifier = processIdentifier;
        this.counter = counter & LOW_ORDER_THREE_BYTES;
    }
	
	@Override
    public String toString() {
        return toHexString();
    }
	
	/**
     * Converts this instance into a 24-byte hexadecimal string representation.
     *
     * @return a string representation of the ObjectId in hexadecimal format
     */
    public String toHexString() {
      char[] chars = new char[24];
      int i = 0;
      for (byte b : toByteArray()) {
        chars[i++] = HEX_CHARS[b >> 4 & 0xF];
        chars[i++] = HEX_CHARS[b & 0xF];
      }
      return new String(chars);
    }
	
	/**
     * Convert to a byte array.  Note that the numbers are stored in big-endian order.
     *
     * @return the byte array
     */
    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        putToByteBuffer(buffer);
        return buffer.array();  // using .allocate ensures there is a backing array that can be returned
    }
	
	/**
      * Convert to bytes and put those bytes to the provided ByteBuffer.
      * Note that the numbers are stored in big-endian order.
      *
      * @param buffer the ByteBuffer
      * @throws IllegalArgumentException if the buffer is null or does not have at least 12 bytes remaining
      * @since 3.4
      */
    public void putToByteBuffer(final ByteBuffer buffer) {
		if(buffer == null) throw new IllegalArgumentException("buffer");
		if(buffer.remaining() < 12) throw new IllegalArgumentException("buffer");

        buffer.put(int3(timestamp));
        buffer.put(int2(timestamp));
        buffer.put(int1(timestamp));
        buffer.put(int0(timestamp));
        buffer.put(int2(machineIdentifier));
        buffer.put(int1(machineIdentifier));
        buffer.put(int0(machineIdentifier));
        buffer.put(short1(processIdentifier));
        buffer.put(short0(processIdentifier));
        buffer.put(int2(counter));
        buffer.put(int1(counter));
        buffer.put(int0(counter));
    }
	
	private static byte int3(final int x) {
        return (byte) (x >> 24);
    }

    private static byte int2(final int x) {
        return (byte) (x >> 16);
    }

    private static byte int1(final int x) {
        return (byte) (x >> 8);
    }

    private static byte int0(final int x) {
        return (byte) (x);
    }

    private static byte short1(final short x) {
        return (byte) (x >> 8);
    }

    private static byte short0(final short x) {
        return (byte) (x);
    }
	
	public static String generateString(){
		return new Id().toHexString();
	}
}