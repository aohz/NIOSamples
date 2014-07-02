package com.gft.async;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;



public class AsyncSocketDemo {

    private static void clientStart() {
        try {
            InetSocketAddress hostAddress = new InetSocketAddress(InetAddress.getByName("localhost"),50000);
            AsynchronousSocketChannel clientSocketChannel = AsynchronousSocketChannel.open();
            
            
            System.out.println("connecting...");
            Future<Void> connectFuture = clientSocketChannel.connect(hostAddress);
            while(!connectFuture.isDone()){
                //Execute other useful logic in the MAIN thread while the reading is not done
            }
            // Wait until connection is done.
            if(connectFuture.isDone()){
                connectFuture.get(5, TimeUnit.SECONDS);
                System.out.println("CONNECTED!");
            }
            
            
            OutputStream os = Channels.newOutputStream(clientSocketChannel);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            for (int i = 0; i < 5; i++) {
                oos.writeObject("Look at me " + i);
                Thread.sleep(1000);
            }
            oos.writeObject("EOF");
            oos.close();
            clientSocketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void serverStart() {
        try {
            System.out.println("In serverStart");
            InetSocketAddress hostAddress = new InetSocketAddress(InetAddress.getByName("localhost"), 50000);
            
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open().bind(hostAddress);
            Future<AsynchronousSocketChannel> serverFuture = serverSocketChannel.accept();
            
            final AsynchronousSocketChannel clientSocket = serverFuture.get();
            if ((clientSocket != null) && (clientSocket.isOpen())) {
                InputStream connectionInputStream = Channels.newInputStream(clientSocket);
                ObjectInputStream ois = null;
                ois = new ObjectInputStream(connectionInputStream);
                while (true) {
                    Object object = ois.readObject();
                    if (object.equals("EOF")) {
                        clientSocket.close();
                        break;
                    }
                    
                }
                ois.close();
                connectionInputStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                serverStart();
            }
        });
        serverThread.start();
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                clientStart();
            }
        });
        clientThread.start();
    }

}
