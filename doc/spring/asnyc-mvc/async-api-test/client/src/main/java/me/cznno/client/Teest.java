package me.cznno.client;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author cznno
 * Date: 2019/2/20
 */
public class Teest {
    public static void main(String[] args) {
        Consumer<String> consumer = s -> System.out.println(s);

        Runnner runnner = s -> System.out.println(s);
    }
}
