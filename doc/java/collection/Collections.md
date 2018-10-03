# Collections

java.util.Collections仅包含对集合进行操作或返回集合的静态方法。它也包含对集合进行操作的多态算法，“包装器（wrapper）”，它返回由指定集合生成的新集合。

如果提供给它们的集合或类对象为null ，则此类的方法都抛出`NullPointerException` 。

包含在这个类中的多态算法的文档通常包括对其实现的简要说明。只要遵守规范本身，实现者就可以随意替换其他算法。（例如，使用的算法`sort`不必是mergesort，但它必须是稳定的。）

此类中包含的“破坏性”算法是指，如果算法操作的集合不支持合适的mutation primitive（例如`set` 方法）就会抛出`UnsupportedOperationException`。如果调用对集合没有影响，则这些算法可能（但不是必须）抛出此异常。例如，在已经排序好的不可变列表上调用`sort`方法可能会也可能不会抛出`UnsupportedOperationException`。

## 方法

### shuffle(java.util.List<?>, java.util.Random)

使用指定的随机源对指定的列表的元素进行随机重新排列。假设随机源是公平的，所有排列都以相同的可能性发生。

该实现向后遍历列表，从最后一个元素到第二个元素，重复地将随机选择的元素交换到“当前位置”。从列表中从第一个元素到当前位置（包括第一个元素）的部分随机选择元素。

此方法以线性时间运行。如果指定的列表没有实现[`RandomAccess`](https://docs.oracle.com/javase/10/docs/api/java/util/RandomAccess.html)接口并且很大，则此实现会在将其重新排列之前将指定的列表转储到数组中，并将重排的数组转储回列表中。这避免了在适当的位置改组“顺序访问”列表所导致的二次行为。

```java
public static void shuffle(List<?> list, Random rnd) {
    int size = list.size();
    
   	// 实现RandomAccess或者list大小小于阈值，就直接循环 -> swap
    if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
        for (int i=size; i>1; i--)
            swap(list, i-1, rnd.nextInt(i));
    } else {
        // 列表转储到数组中
        Object arr[] = list.toArray();

        // Shuffle array
        for (int i=size; i>1; i--)
            swap(arr, i-1, rnd.nextInt(i));

        // Dump array back into list
        // instead of using a raw type here, it's possible to capture
        // the wildcard but it will require a call to a supplementary
        // private method
        ListIterator it = list.listIterator();
        for (Object e : arr) {
            it.next();
            it.set(e);
        }
    }
}

//交换两个元素的位置
public static void swap(List<?> list, int i, int j) {
    // instead of using a raw type here, it's possible to capture
    // the wildcard but it will require a call to a supplementary
    // private method
    final List l = list;
    l.set(i, l.set(j, l.get(i)));
}
```