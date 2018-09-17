# Java集合

按照[Oracle的文档](https://docs.oracle.com/javase/tutorial/collections/index.html)，Java Collections Framework的核心元素包括：接口，实现和算法

[TOC]



## 接口

![](https://docs.oracle.com/javase/tutorial/figures/collections/colls-coreInterfaces.gif)

### Set

- 不包含重复元素的集合，抽象了数学意义上的集合（无序性，互异性和确定性）
- 主要的三个实现
  - HashSet：将其元素存储在哈希表中，是性能最佳的实现; 但不能保证顺序
  -  TreeSet：将其元素存储在红黑树中，根据其值对其元素进行排序; 比HashSet慢得多
  - LinkedHashSet：以双向链表关联的Hash Table实现，能够根据它们插入集合（插入顺序）的顺序对其元素进行排序。开销比HashSet略高

### List

- 有序集合（序列）
- 可以包含重复的元素
- 主要的操作
  - Positional access（根据列表中的数字位置操纵元素）：例如`get`， `set`，`add`，`addAll`，和`remove`
  - Search ：搜索列表中的指定对象并返回其数字位置，包括 `indexOf`和`lastIndexOf`
  - Iteration：利用列表的顺序性遍历
  - Range-view：执行任意范围操作，`如sublist`
- 两个主要实现
  - ArrayList：通常情况下性能更好
  - LinkedList：大量add/remove下性能更好

### Queue

- 队列通常（但不一定）以FIFO（先进先出）方式对元素进行排序
- 优先级队列会按照comparator或者元素的自然顺序排序

### Deque

- 双向队列
- 既可以FIFO，也可以LIFO；两端都可以insert, retrieve and remove

### Map

- 不属于真正的Collection（但是是Java Collections Framework的成员）
- 键值存储，不能有重复的键，每个键最多有一个值（键值对）
- 三个主要实现
  - HashMap
  - TreeMap
  - LinkedHashMap

### SortedSet

- 按照升序排列的Set

### SortedMap

- 键按照升序排列的Map

## 实现

源码基于jdk 10, 实现按照oracle的文档做了分类

* **通用实现**是最常用的实现，专为日常使用而设计

| 接口    | 哈希表实现 | 可调整大小的数组实现 | 树实现    | 链接列表实现 | 哈希表+链表实现 |
| ------- | ---------- | -------------------- | --------- | ------------ | --------------- |
| Set   | HashSet  |                      | TreeSet |              | LinkedHashSet |
| List  |            | ArrayList          |           | LinkedList |                 |
| Queue |            |                      |           |              |                 |
| Deque |            | ArrayDeque         |           | LinkedList |                 |
| Map   | HashMap  |                      | TreeMap |              | LinkedHashMap |

- **专用实现**旨在用于特殊情况，并显示非标准性能特征，使用限制或行为。
- **并发实现**旨在支持高并发性，通常以牺牲单线程性能为代价。这些实现是java.util.concurrent包的一部分。
- **包装器实现**与其他类型的实现（通常是通用实现）结合使用，以提供增加或限制的功能。
- **便利实现**是通常通过静态工厂方法提供的小型实现，为特殊集合（例如，单例集）的通用实现提供方便，有效的替代方案。
- **抽象实现**是骨架实现，有助于构建自定义实现 （Ref [自定义集合实现](https://docs.oracle.com/javase/tutorial/collections/custom-implementations/index.html)）

### Set

####HashSet

##### 特性:

- 没有重复元素
- 无序
-  add, remove, contains和size的时间复杂度为O(1) 
- 迭代速度和大小成比例
- 线程不安全, 可以在外部使用synchronized或者使用Collections#synchronizedSet方法包装
- 迭代器是快速失败的：如果在创建迭代器之后的任何时候修改了set，除了通过迭代器自己的`remove` 方法之外，迭代器抛出一个[`ConcurrentModificationException`](https://docs.oracle.com/javase/8/docs/api/java/util/ConcurrentModificationException.html)

##### 实现:

- 内部是一个HashMap 

  ```java
  private transient HashMap<E,Object> map;
  public HashSet() {
      map = new HashMap<>();
  }
  ```

- 使用了一个dummy Object作为value

  ```java
  private static final Object PRESENT = new Object();
  
  public boolean add(E e) {
      return map.put(e, PRESENT)==null;
  }
  
  public boolean remove(Object o) {
      return map.remove(o)==PRESENT;
  }
  ```

- iterator, size, isEmpty, contains, add, remove等用的都是HashMap的对应方法

#### LinkedHashSet

#####特性:

- 有序, 保持插入的顺序(重复插入不改变顺序)
- 常用操作复杂度为O(1)
- 线程不安全, 可以在外部使用synchronized或者使用Collections#synchronizedSet方法包装
- 快速失败

#####实现:

- 内部是LinkedHashMap

- 继承了HashSet, HashSet提供了构造方法

  ```java
  HashSet(int initialCapacity, float loadFactor, boolean dummy) {
      map = new LinkedHashMap<>(initialCapacity, loadFactor);
  }
  
  public LinkedHashSet() {
      super(16, .75f, true);
  }
  ```

#### TreeSet

#####特性:

- 按照元素的CompareTo方法或者在TreeSet构造函数中传入的Comparator进行排序, 没有的话会抛出java.lang.ClassCastException
- add, remove, contains和next的复杂度是O(log n)
- size的复杂度是O(1)
- 线程不安全, fail fast

##### 实现:

- 内部是TreeMap (NavigableMap/SortedMap)

  ```java
  private transient NavigableMap<E,Object> m;
  
  TreeSet(NavigableMap<E,Object> m) {
      this.m = m;
  }
  public TreeSet(Comparator<? super E> comparator) {
      this(new TreeMap<>(comparator));
  }
  public TreeSet(SortedSet<E> s) {
      this(s.comparator());
      addAll(s);
  }
  ```

- add, remove等方法也是调用的NavigableMap的对应方法

  ```java
  public boolean add(E e) {
      return m.put(e, PRESENT)==null;
  }
  public boolean remove(Object o) {
      return m.remove(o)==PRESENT;
  }
  ```

### List

#### ArrayList

##### 特性:

- 可调整大小的数组实现
- 允许所有元素，包括 `null`
- add, get和next的复杂度是O(1)
- remove和contains的复杂度是O(n)
- 线程不安全

##### 实现:

- 内部存储其实是一个array

  ```java
  transient Object[] elementData;
  
  public ArrayList() {
      this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;  //Object[], 空数组
  }
  
  public ArrayList(int initialCapacity) {
      if (initialCapacity > 0) {
          this.elementData = new Object[initialCapacity];
      } else if (initialCapacity == 0) {
          this.elementData = EMPTY_ELEMENTDATA; 
          //Object[], 空数组, 和DEFAULTCAPACITY_EMPTY_ELEMENTDATA一样
      } else {
          throw new IllegalArgumentException("Illegal Capacity: "+
                                             initialCapacity);
      }
  }
  ```

- add

  ```java
  public boolean add(E e) {
      modCount++;
      add(e, elementData, size);
      return true;
  }
  
  /**
    * This helper method split out from add(E) to keep method
    * bytecode size under 35 (the -XX:MaxInlineSize default value),
    * which helps when add(E) is called in a C1-compiled loop.
    */
  private void add(E e, Object[] elementData, int s) {
      if (s == elementData.length)
          elementData = grow();
      elementData[s] = e;
      size = s + 1;
  }
  
  
  /**
   * Increases the capacity to ensure that it can hold at least the
   * number of elements specified by the minimum capacity argument.
   *
   * @param minCapacity the desired minimum capacity
   * @throws OutOfMemoryError if minCapacity is less than zero
   */
  /* 
    扩容的时候用Arrays#copyOf把array复制了一遍, 用到了System#arraycopy,时间复杂度是O(n)
    所以add的复杂度是O(n) 
  */
  private Object[] grow(int minCapacity) {
      return elementData = Arrays.copyOf(elementData,
                                         newCapacity(minCapacity));
  }
  
  private Object[] grow() {
      return grow(size + 1);
  }
  ```

- get, set

  ```java
  // 基本上就是对elementData这个数组的操作
  public E get(int index) {
      Objects.checkIndex(index, size); // 检查index是不是在0-size内
      return elementData(index);
  }
  public E set(int index, E element) {
      Objects.checkIndex(index, size);
      E oldValue = elementData(index);
      elementData[index] = element;
      return oldValue;
  }
  ```

- remove

  ```java
  // 按值删除的话会循环查找, 只删除第一个
  // 不论是按index还是按值删除都会调用
  private void fastRemove(Object[] es, int i) {
      modCount++;
      final int newSize;
      if ((newSize = size - 1) > i)
          // 将index后的数组往前移一个
          System.arraycopy(es, i + 1, es, i, newSize - i);
      es[size = newSize] = null;
  }
  ```

- clear

  ```java
  /* 
    复杂度也是O(n), 为什么要循环?
    https://stackoverflow.com/questions/3823398/
    https://stackoverflow.com/questions/18370780/
    经测试如果用list = new ArrayList()替换的话, 速度会快很多:见CollectTest.java
  */
  public void clear() {
      modCount++;
      final Object[] es = elementData;
      for (int to = size, i = size = 0; i < to; i++)
          es[i] = null;
  }
  ```











### 参考

[Big O notation for java's collections](https://gist.github.com/FedericoPonzi/8d5094dbae33cbb94536a73f62d1c1a0)

[Runtime Complexity of Java Collections](https://gist.github.com/psayre23/c30a821239f4818b0709)

