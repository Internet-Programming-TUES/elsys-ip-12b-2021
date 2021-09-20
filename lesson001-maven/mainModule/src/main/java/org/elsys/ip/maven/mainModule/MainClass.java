package org.elsys.ip.maven.mainModule;

import com.google.common.collect.ImmutableSet;
import lombok.val;
import maven.subModule.ClassInModule;

public class MainClass {
    public static void main(String[] args) {
        ImmutableSet<String> set1 = ImmutableSet.of("1", "2", "3");
        ImmutableSet<String> set2 = ImmutableSet.of("2", "1", "3");
        System.out.println(set1.equals(set2));

        val product = new Product("1", 1.0, "product");
        System.out.println(product);

        System.out.println(new NoToString());

        val classInModule = new ClassInModule();
        System.out.println(classInModule.getString());
    }
}
