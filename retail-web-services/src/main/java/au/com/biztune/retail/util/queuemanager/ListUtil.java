package au.com.biztune.retail.util.queuemanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arash on 21/04/2016.
 */
public class ListUtil {
    public static <T> List<List<T>> chunks(List<T> bigList, int n){
        List<List<T>> chunks = new ArrayList<List<T>>();
        for (int i = 0; i<bigList.size(); i +=n) {
            chunks.add(new ArrayList<T>(
                bigList.subList(i, Math.min(bigList.size(), i + n))));
        }
        return chunks;
    }
}
