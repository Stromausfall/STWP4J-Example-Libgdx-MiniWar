package net.matthiasauer.stwp4j.libgdx.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.badlogic.gdx.utils.Pool;

public class PoolsTest {

    @Test
    public void testGetNullArgument() {
        String errorMessage = "";
        
        try {
            Pools.get(null);
        } catch(IllegalArgumentException e) {
            errorMessage = e.getMessage(); 
        }

        assertEquals(
                "exception not thrown when passing null to get",
                "passed 'null' as argument",
                errorMessage);
    }

    @Test
    public void testPutNullClazzArgument() {
        String errorMessage = "";
        
        try {
            Pools.put(null, null);
        } catch(IllegalArgumentException e) {
            errorMessage = e.getMessage(); 
        }

        assertEquals(
                "exception not thrown when passing null to get",
                "passed 'null' as clazz argument",
                errorMessage);
    }

    @Test
    public void testPutNullPoolArgument() {
        String errorMessage = "";
        
        try {
            Pools.put(PoolsTest.class, null);
        } catch(IllegalArgumentException e) {
            errorMessage = e.getMessage(); 
        }

        assertEquals(
                "exception not thrown when passing null to get",
                "passed 'null' as pool argument",
                errorMessage);
    }

    @Test
    public void testPutAddTwice() {
        String errorMessage = "";
        
        try {
            Pool<Double> pool =
                    new Pool<Double>() {
                        @Override
                        protected Double newObject() {
                            return 3.5;
                        }
                    };
            
            Pools.put(Double.class, pool);
            Pools.put(Double.class, pool);
        } catch(IllegalArgumentException e) {
            errorMessage = e.getMessage(); 
        }

        assertEquals(
                "exception not thrown when passing null to get",
                "added pool of type '" + Double.class + "' twice",
                errorMessage);
    }

    @Test
    public void testGetReturnsNullIfNoPool() {
        assertEquals(
                "expected null to be returned !",
                null,
                Pools.get(PoolsTest.class));
    }

    @Test
    public void testPutAndGet() {
        Pool<Integer> pool =
                new Pool<Integer>() {
                        @Override
                        protected Integer newObject() {
                            return 35;
                        }
                };
            
        Pools.put(Integer.class, pool);

        assertEquals(
                "expected same pool to be returned that was put into the Pools class",
                pool,
                Pools.get(Integer.class));
    }
}
