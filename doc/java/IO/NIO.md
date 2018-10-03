# JAVA NIO

http://ifeve.com/overview/

## 核心组件

### Channel

#### 所有的 NIO 的 I/O 操作都是从 Channel 开始的. 一个 channel 类似于一个 stream.

#### 对比

##### 我们可以在同一个 Channel 中执行读和写操作, 然而同一个 Stream 仅仅支持读或写.

##### Channel 可以异步地读写, 而 Stream 是阻塞的同步读写.

##### Channel 总是从 Buffer 中读取数据, 或将数据写入到 Buffer 中.

#### 实现

##### FileChannel, 文件操作

###### 使用

####### 打开

######## 使用一个InputStream、OutputStream或RandomAccessFile来获取一个FileChannel实例

####### 分配buffer

####### 读取

######## read()

######### 如果返回-1，表示到了文件末尾

####### 写

######## write (ByteBuffer src)

######## write(ByteBuffer[] srcs)

######## write(ByteBuffer[] srcs, int offset, int length)

####### 关闭

######## close()

###### 方法

####### transferFrom()

######## 将数据从源通道传输到FileChannel中

####### transferTo()

######## 将数据从FileChannel传输到其他的channel中

####### position()

####### size()

####### truncate()

######## 截取文件

####### force()

######## 将通道里尚未写入磁盘的数据强制写到磁盘上

##### DatagramChannel, UDP 操作

##### SocketChannel, TCP 操作

###### 使用

####### 打开

######## open()

######## connect()

######## finishConnect()

####### 读

######## read()

####### 写

######## write()

####### 非阻塞模式

####### 关闭

######## close()

##### ServerSocketChannel, TCP 操作, 使用在服务器端

###### 使用

####### 打开

######## open()

######## connect()

######## finishConnect()

####### 监听进入的连接

######## serverSocketChannel.accept();

####### 读

######## read()

####### 写

######## write()

####### 非阻塞模式

######## configureBlocking(false)

####### 关闭

######## close()

####### 接收

######## receive()

####### 发送

######## send()

####### 连接到特定的地址

######## 可以将DatagramChannel“连接”到网络中的特定地址的。由于UDP是无连接的，连接到特定地址并不会像TCP通道那样创建一个真正的连接。而是锁住DatagramChannel ，让其只能从特定地址收发数据。

### Buffer

#### 缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。

#### 三个属性

##### capacity

###### 作为一个内存块，Buffer有一个固定的大小值，也叫“capacity”.你只能往里写capacity个byte、long，char等类型。一旦Buffer满了，需要将其清空（通过读数据或者清除数据）才能继续写数据往里写数据

##### position

###### 当你写数据到Buffer中时，position表示当前的位置。初始的position值为0.当一个byte、long等数据写到Buffer后， position会向前移动到下一个可插入数据的Buffer单元。position最大可为capacity – 1.

###### 当读取数据时，也是从某个特定位置读。当将Buffer从写模式切换到读模式，position会被重置为0. 当从Buffer的position处读取数据时，position向前移动到下一个可读的位置。

##### limit

###### 在写模式下，Buffer的limit表示你最多能往Buffer里写多少数据。 写模式下，limit等于Buffer的capacity。

###### 读模式时， limit表示你最多能读到多少数据。因此，当切换Buffer到读模式时，limit会被设置成写模式下的position值。换句话说，你能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position）

#### 实现

##### ByteBuffer

###### MappedByteBuffer

##### CharBuffer

##### DoubleBuffer

##### FloatBuffer

##### IntBuffer

##### LongBuffer

##### ShortBuffer

#### 方法

##### allocate()

###### 要想获得一个Buffer对象首先要进行分配

###### ByteBuffer buf = ByteBuffer.allocate(48);

##### allocateDirect()

##### put()

###### 写数据到Buffer

##### flip()

###### 将Buffer从写模式切换到读模式

###### 将position设回0，并将limit设置成之前position的值

##### get()

###### 读取数据

###### byte aByte = buf.get();

##### rewind()

###### 将position设回0

##### clear()

###### position将被设回0，limit被设置成 capacity的值

###### Buffer 被清空了。Buffer中的数据并未清除

###### 清除未读的数据

##### compact()

###### 将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面

###### limit属性依然像clear()方法一样，设置成capacity

###### 不会覆盖未读的数据

##### mark()

###### 标记Buffer中的一个特定position

##### reset()

###### 恢复到这个position

### Selector

#### 能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件

#### 方法

##### open()

## 对比 OIO

### IO 基于流(Stream oriented), 而 NIO 基于 Buffer (Buffer oriented)

#### OIO 是面向字节流或字符流的

##### 流式的方式顺序地从一个 Stream 中读取一个或多个字节, 因此我们也就不能随意改变读取指针的位置

#### NIO引入了 Channel 和 Buffer

##### 首先需要从 Channel 中读取数据到 Buffer 中, 当 Buffer 中有数据后, 我们就可以对这些数据进行操作了. 不像 IO 那样是顺序操作, NIO 中我们可以随意地读取任意位置的数据

### IO 操作是阻塞的, 而 NIO 操作是非阻塞的

#### Stream 操作都是阻塞的, 例如我们调用一个 read 方法读取一个文件的内容, 那么调用 read 的线程会被阻塞住, 直到 read 操作完成

#### NIO 的非阻塞模式允许我们非阻塞地进行 IO 操作. 例如我们需要从网络中读取数据, 在 NIO 的非阻塞模式中, 当我们调用 read 方法时, 如果此时有数据, 则 read 读取并返回; 如果此时没有数据, 则 read 直接返回, 而不会阻塞当前线程

### IO 没有 selector 概念, 而 NIO 有 selector 概念.

#### 通过 Selector, 一个线程可以监听多个 Channel 的 IO 事件, 当我们向一个 Selector 中注册了 Channel 后, Selector 内部的机制就可以自动地为我们不断地查询(select) 这些注册的 Channel 是否有已就绪的 IO 事件(例如可读, 可写, 网络连接完成等)

#### 如果你的应用打开了多个连接（通道），但每个连接的流量都很低，使用Selector就会很方便。

##### 例如，在一个聊天服务器中

## how it works

### 基本上，所有的 IO 在NIO 中都从一个Channel 开始。Channel 有点象流。 数据可以从Channel读到Buffer中，也可以从Buffer 写到Channel中

### 使用Buffer读写数据

#### 写入数据到Buffer

##### 从Channel写到Buffer

###### int bytesRead = inChannel.read(buf);

##### put()方法

###### buf.put(127);

#### 调用flip()方法

#### 从Buffer中读取数据

##### 从Buffer读取数据到Channel

###### int bytesWritten = inChannel.write(buf);

##### get()方法

###### byte aByte = buf.get();

#### 调用clear()方法或者compact()方法

##### clear()方法会清空整个缓冲区

##### compact()方法只会清除已经读过的数据

##### 任何未读的数据都被移到缓冲区的起始处

##### 新写入的数据将放到缓冲区未读数据的后面

### Scatter/Gather

#### 用于需要将传输的数据分开处理的场合

##### 传输一个由消息头和消息体组成的消息，你可能会将消息体和消息头分散到不同的buffer中

#### scatter

##### 在读操作时将读取的数据写入多个buffer中

##### Channel将从Channel中读取的数据“分散（scatter）”到多个Buffer中

#### gather

##### 在写操作时将多个buffer的数据写入同一个Channel

##### Channel 将多个Buffer中的数据“聚集（gather）”后发送到Channel

### Selector

#### 创建

##### Selector selector = Selector.open();

#### 向Selector注册通道

##### 非阻塞模式

###### channel.configureBlocking(false);

##### SelectionKey key = channel.register(selector,Selectionkey.OP_READ);

##### register的事件

###### SelectionKey.OP_CONNECT

###### SelectionKey.OP_ACCEPT

###### SelectionKey.OP_READ

###### SelectionKey.OP_WRITE

##### 多个事件

###### SelectionKey.OP_READ | SelectionKey.OP_WRITE

##### SelectionKey

###### interest集合

####### 是你所选择的感兴趣的事件集合

####### interestOps

###### ready集合

####### 是通道已经准备就绪的操作的集合

####### readyOps

####### selectionKey.isAcceptable();

####### selectionKey.isConnectable();

####### selectionKey.isReadable();

####### selectionKey.isWritable();

###### Channel

###### Selector

####### int select()

######## 阻塞到至少有一个通道在你注册的事件上就绪了

####### int select(long timeout)

######## 超时

####### int selectNow()

######## 不会阻塞，不管什么通道就绪都立刻返回

######## 没有则返回0

####### selectedKeys()

####### selectedKeySet()

####### remove()

######## Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除。

####### channel()

######## 返回的通道需要转型成你要处理的类型

####### wakeUp()

######## 某个线程调用select()方法后阻塞了，即使没有通道已经就绪，也有办法让其从select()方法返回。只要让其它线程在第一个线程调用select()方法的那个对象上调用Selector.wakeup()方法即可。阻塞在select()方法上的线程会立马返回。

######## 如果有其它线程调用了wakeup()方法，但当前没有线程阻塞在select()方法上，下个调用select()方法的线程会立即“醒来（wake up）”。

####### close()

######## 用完Selector后调用其close()方法会关闭该Selector，且使注册到该Selector上的所有SelectionKey实例无效。通道本身并不会关闭。

###### 附加的对象（可选）

####### attach()

## Pipe

### Java NIO 管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取。

### 使用

#### 创建管道

##### Pipe.open()

#### 向管道写数据

##### Pipe.SinkChannel sinkChannel = pipe.sink();

##### sinkChannel.write(buf);

#### 从管道读取数据

##### Pipe.SourceChannel sourceChannel = pipe.source();

##### ByteBuffer buf = ByteBuffer.allocate(48);

##### int bytesRead = sourceChannel.read(buf);

## 系统调用

### select

### pselect

### poll

### epoll
