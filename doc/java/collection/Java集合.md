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
