package com.gft.async;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class FutureFileDemo {
    public static void main(String[] args) {
        try{
            Path file = Paths.get("/usr/lib/log.txt");
            //Open file asynchronously
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(file);
            /*
            A long operation:
            Start to read 100,000 bytes in the above file
            */
            ByteBuffer buffer = ByteBuffer.allocate(100_000);
            Future<Integer> result = channel.read(buffer, 0);
            /*
            In the sample artificially we made sure the result would be finished
            (by using isDone() ). Normally the result would either be finished (and the main
            thread would continue), or it would wait until the background I/O is complete.
            */
            while(!result.isDone()){
                //Execute other logic in the MAIN thread
            }
            //Get the result from the async operation
            Integer bytesRead = result.get();
            System.out.println("Bytes read [" + bytesRead + "]");
        }catch (IOException | ExecutionException | InterruptedException e){
            System.out.println(e.getMessage());
        }
    }
}


