package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayListTest {

    public static void main(String[] args) {
//        test01();
        test02();
    }

    public static void test01() {
        List<Integer> array = new ArrayList<>(0);
        array.add(1);
    }

    // 需要在 JDK8 版本执行，因为 JDK9 已经修复该问题
    public static void test02() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Object[] array = list.toArray(); // JDK8 返回 Integer[] 数组，JDK9+ 返回 Object[] 数组。
        System.out.println("array className ：" + array.getClass().getSimpleName());

        // 此处，在 JDK8 和 JDK9+ 表现不同，前者会报 ArrayStoreException 异常，后者不会。
        array[0] = new Object();
    }

}
