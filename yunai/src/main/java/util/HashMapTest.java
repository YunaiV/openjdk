package util;

import java.util.HashMap;
import java.util.Map;

public class HashMapTest {

    public static void main(String[] args) {
//        System.out.println(-1 >>> Integer.numberOfLeadingZeros(16 - 1));
//        System.out.println(10 << 1);

//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("test", new Object());
//        System.out.println(map);
//        test02();
//        test02();
//        test03();
//        System.out.println(178913281 % (16));
//        System.out.println((-10 & (64)) & 64);
    }

    public static void test01() {
        Map<String, Object> map = new HashMap<>(4, 0.75F);
        map.put("1", new Object());
        map.put("2", new Object());
        map.put("3", new Object());
        map.put("4", new Object());
        map.put("5", new Object());
    }

    public static void test02() {
        int n = 15;
        for (int i = 0; i < 100; i++) {
            System.out.println(((n - 1) & i) + "=" + (i % n));
        }
    }

    public static void test03() {
        Map<String, Object> map = new HashMap<>(5);
        map.put("1", new Object());
        map.put("2", new Object());
        map.put("3", new Object());
        map.put("4", new Object());
        map.put("5", new Object());
    }

}
