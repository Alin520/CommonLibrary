package com.alin.commonlibrary.util.collection;



import java.util.HashMap;
import java.util.Map;


/**
 * ================================================
 * 作    者   ： hailinhe
 * github地址 ： https://github.com/Alin520/Mvp-Rxjava-Retrofit
 * CSDN地址   ： http://blog.csdn.net/hailin123123/article/details/78643330
 * 创建时间    ： 2018/7/30 17:40
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class NoEmptyHashMap<K,V> extends HashMap<K, V> {

    public NoEmptyHashMap() {
        super();
    }

    public NoEmptyHashMap(final Map<K,V> map) {
        super();
        putAll(map);
    }

    @Override
    public V put(final K key, final V value) {
        if (Preconditions.isBlank(key) || Preconditions.isBlank(value)) {
            return null;
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
