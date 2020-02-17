package shared;

import java.io.ObjectInputStream;
import shared.Buffer;

public class ObjectReciever extends Thread{

    private ObjectInputStream ois;
    private Buffer<Object> b;

    private boolean alive = true;

    public ObjectReciever(ObjectInputStream ois, Buffer<Object> b){
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
                Object o = ois.readObject();
                b.put(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}