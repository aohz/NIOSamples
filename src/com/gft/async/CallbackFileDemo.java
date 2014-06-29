
package com.gft.async;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CallbackFileDemo {
    public static void main(String[] args) {
        try{
            Path file = Paths.get("/usr/lib/log.txt");
            //Open asynchronous file
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(file);
            /*
            A long operation:
            Start to read 100,000 bytes in the above file
            */
            ByteBuffer buffer = ByteBuffer.allocate(100_000);
            
            /*
            Read from channel.
            The CompletionHandler object is the callback method
            handling the result with two possibilities:
            completed -> success
            failed -> error
            */
            channel.read(buffer, 0, buffer,
                new CompletionHandler<Integer, ByteBuffer>(){
                    //Complete reading callback
                    public void completed(Integer result, ByteBuffer attachment){
                        System.out.println("Bytes read [" + result + "]");
                    }

                    public void failed(Throwable exception, ByteBuffer attachment){
                        System.out.println(exception.getMessage());
                    }
                });
        }catch(IOException e){
            System.out.println(e.getMessage());
        } 
    }
}
