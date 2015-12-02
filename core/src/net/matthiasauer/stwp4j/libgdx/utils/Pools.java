package net.matthiasauer.stwp4j.libgdx.utils;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Pool;

public class Pools {
    private static final Map<Class<?>, Pool<?>> pools = new HashMap<Class<?>, Pool<?>>();
    
    @SuppressWarnings("unchecked")
    public synchronized static <T> Pool<T> get(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("passed 'null' as argument");
        }
        
        return (Pool<T>) pools.get(clazz);
    }
    
    public synchronized static <T> void put(Class<T> clazz, Pool<T> pool) {
        if (clazz == null) {
            throw new IllegalArgumentException("passed 'null' as clazz argument");
        }
        
        if (pool == null) {
            throw new IllegalArgumentException("passed 'null' as pool argument");
        }
        
        if (get(clazz) != null) {
            throw new IllegalArgumentException("added pool of type '" + clazz + "' twice");
        }
        
        pools.put(clazz, pool);
    }
}
