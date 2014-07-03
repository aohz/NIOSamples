package com.gft.async;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

public class CallbackDemo {

	public static void main(String[] args) throws InterruptedException, IOException {
            Path file = Paths.get("/home/administrator/tests/app.log");
		try (AsynchronousFileChannel readfile = AsynchronousFileChannel.open(
                        file,
			StandardOpenOption.READ
                )) {
			readAll(readfile, (ByteBuffer) ByteBuffer.allocateDirect(1000).put(new byte[1000]).flip(), 1000L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void readAll(final AsynchronousFileChannel ch, final ByteBuffer dst, long filePosition)
			throws InterruptedException {
                /*
                We need threads to wait until the set of operations being performed in other threads completes.
                */
		final CountDownLatch latch = new CountDownLatch(1);
                /*
                We use the completion handler to enforce that all bytes 
                are written or read when we perform I/O operations on an asynchronous channel.
                */
		ch.read(dst, filePosition, filePosition, new CompletionHandler<Integer, Long>() {
			public void completed(Integer bytesTransferred, Long filePosition) {
                            System.out.println("SUCCESS");
                            if (bytesTransferred > 0) {
				long p = filePosition + bytesTransferred;
				ch.read(dst, p, p, this);
                            } else {
                                //Let the awaiting thread proceed
				latch.countDown();
                            }
			}

			public void failed(Throwable exc, Long position) {
                            System.out.println("ERROR");
			}
		});

		latch.await();
	}
	
	static void writeFully(final AsynchronousFileChannel ch, final ByteBuffer src, long filePosition)
			throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		ch.write(src, filePosition, filePosition, new CompletionHandler<Integer, Long>() {
			public void completed(Integer bytesTransferred, Long filePosition) {
				if (src.hasRemaining()) {
					long newFilePosition = filePosition + bytesTransferred;
					ch.write(src, newFilePosition, newFilePosition, this);
				} else {
					latch.countDown();
				}
			}

			public void failed(Throwable exc, Long position) {
			}
		});
		latch.await();
	}

}