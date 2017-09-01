package uk.ac.dswan01.kainoshearingtest.standalone.storage;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class CacheStorage {

    //Class is deprecated and no longer used, left for marking purposes only
    private CacheStorage() {}

    /**
     * Method to write a selected object to the applications cache
     *  Creates a fileOutputStream, passes it to an objectOutputStream, outputs the object then closes all streams.
     * @param context - The applications context
     * @param key - The key used to identify the object once it is saved
     * @param object - The object to be retrieved
     * @throws IOException
     */
    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
        context.getApplicationContext();
    }

    /**
     * Method to retrieve a selected object from the applications cache
     * @param context - The applications context
     * @param key - The key used to identify which object is to be retrievd
     * @return - Returns the etrieved object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }
}
