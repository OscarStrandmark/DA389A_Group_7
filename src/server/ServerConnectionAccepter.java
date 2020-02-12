package server;

import java.io.ObjectInputStream;
import java.util.Queue;

public class ServerConnectionAccepter extends Thread{

    private ObjectInputStream ois;
    private Queue q;

    private boolean alive = true;

    public ServerConnectionAccepter(ObjectInputStream ois, Queue q){ //TODO: Change to buffer
        start();
    }

    public void kill(){
        alive = false;
    }

    @Override
    public void run() {
        while(alive){
            try (Object o = ois.readObject()) {
                q.add(o)
            } catch (Exception e) {}
        }
    }
}