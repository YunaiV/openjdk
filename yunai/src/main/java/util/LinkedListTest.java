package util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LinkedListTest {

    public static void main(String[] args) {
//        test01();
        test02();
    }

    public static void test01() {
        List<Integer> array = new LinkedList<>();
        array.removeAll(Arrays.asList(1, 2));
    }

    public static void test02() {
        List<Integer> array = new LinkedList<>();
//        System.out.println(array.equals(array));
//        array.hashCode();
//        array.equals(array);
        array.add(1);
        array.add(2);
        Iterator<Integer> iterator = array.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}
