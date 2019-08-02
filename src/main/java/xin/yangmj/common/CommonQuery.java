package xin.yangmj.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonQuery {

    public static HashMap<Object, Object> setCurrentNum(List<Map> listNumCount){
        HashMap<Object, Object> hashMap = new HashMap<>();
        for(int i = 0;i<listNumCount.size() ;i++){
            Map map = listNumCount.get(i);
            Object key = map.get("orderId");
            Object value = map.get("countNumFore");
            hashMap.put(key,value);
        }
        return hashMap;
    }
}
