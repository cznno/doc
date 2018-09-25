# HashTable

##特性

- 实现了Hash表
- 只允许非空key/value
- **初始容量**(initialCapacity) 和**加载因子**(loadFactor)会影响性能
  - 初始容量是哈希表在创建时的容量
    - the default load factor (.75) offers a good tradeoff between time and space costs
    - Higher values decrease the space overhead but increase the time cost to look up an entry (which is reflected in most `Hashtable` operations, including `get` and `put`).
  - 加载因子是哈希表在其容量自动增加之前可以达到多满的一种尺度
    - The initial capacity controls a tradeoff between wasted space and the need for `rehash` operations, which are time-consuming. No `rehash` operations will *ever* occur if the initial capacity is greater than the maximum number of entries the `Hashtable` will contain divided by its load factor. However, setting the initial capacity too high can waste space.
- synchronized
- fast-fail

## 实现

- 主要的元素是Entry的数组

```java
private static class Entry<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Entry<K,V> next;
}
```

- put

  ```java
  public synchronized V put(K key, V value) {
      // Make sure the value is not null
      if (value == null) {
          throw new NullPointerException();
      }
  
      // Makes sure the key is not already in the hashtable.
      Entry<?,?> tab[] = table;
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      @SuppressWarnings("unchecked")
      Entry<K,V> entry = (Entry<K,V>)tab[index];
      for(; entry != null ; entry = entry.next) {
          if ((entry.hash == hash) && entry.key.equals(key)) {
              V old = entry.value;
              entry.value = value;
              return old; // 重复的话返回值, 否则返回null
          }
      }
  
      // 主要就是这个
      addEntry(hash, key, value, index);
      return null;
  }
  
  private void addEntry(int hash, K key, V value, int index) {
      Entry<?,?> tab[] = table;
      if (count >= threshold) {
          // Rehash the table if the threshold is exceeded
          rehash();
  
          tab = table;
          hash = key.hashCode();
          /*
          https://stackoverflow.com/questions/9380670
          (hash & 0x7FFFFFFF) will result in a positive integer.
  		(hash & 0x7FFFFFFF) % tab.length will be in the range of the tab length.
          */
          index = (hash & 0x7FFFFFFF) % tab.length;
      }
  
      // Creates the new entry.
      @SuppressWarnings("unchecked")
      Entry<K,V> e = (Entry<K,V>) tab[index];
      tab[index] = new Entry<>(hash, key, value, e);
      count++;
      modCount++;
  }
  ```

- get

  ```java
  public synchronized V get(Object key) {
      Entry<?,?> tab[] = table;
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry<?,?> e = tab[index] ; e != null ; e = e.next) {
          if ((e.hash == hash) && e.key.equals(key)) {
              return (V)e.value;
          }
      }
      return null;
  }
  ```

##参考

https://docs.oracle.com/javase/10/docs/api/java/util/Hashtable.html

http://www.cnblogs.com/skywang12345/p/3310835.html#b2