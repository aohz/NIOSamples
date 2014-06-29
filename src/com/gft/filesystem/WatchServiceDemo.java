
package com.gft.filesystem;

import static com.sun.glass.ui.android.Activity.shutdown;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;


public class WatchServiceDemo {
    public static void main(String[] args) {
        boolean shutdown = false;
        try{
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = FileSystems.getDefault().getPath("/usr/lib/samples");
            
            //Watch for modifications
            WatchKey key = dir.register(watcher, ENTRY_MODIFY);
            
            //Get next key and its events
            while(!shutdown){
                /*
                The take() method on the WatchService waits until a WatchKey is available
                As soon as a WatchKey is made available, the code polls that WatchKey for Watch-Events
                */
                key = watcher.take();
                for (WatchEvent<?> event: key.pollEvents()){
                    /*
                    other events we can monitor:
                    ENTRY_CREATE
                    ENTRY_DELETE
                    OVERFLOW
                    */
                    if (event.kind() == ENTRY_MODIFY){
                        System.out.println("Home dir changed!");
                    }
                }
                //Be ready for the next event
                key.reset();
            }
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }
    }
}


