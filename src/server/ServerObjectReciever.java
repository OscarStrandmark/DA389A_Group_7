package server;

import java.io.ObjectInputStream;
import shared.Buffer;

public class ServerObjectReciever extends Thread{

    private ObjectInputStream ois;
    private Buffer<Object> b;

    private boolean alive = true;

    public ServerObjectReciever(ObjectInputStream ois, Buffer<Object> b){
        this.ois = ois;
        this.b = b;
        start();
    }

    public void kill(){
        alive = false;
    }

    @Override
    public void run() {
        while(alive){
            try {
                Object o = (Object) ois.readObject();
                b.put(o);
            } catch (Exception e) {
                System.err.println("SERVER");
                e.printStackTrace();
            }
        }
    }
}