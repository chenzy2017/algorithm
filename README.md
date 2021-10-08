复习网址:
1. concurrenthashmap
   https://segmentfault.com/a/1190000024432650
   https://segmentfault.com/a/1190000024439085?utm_source=sf-similar-article
2. juc里的cas乐观锁
   https://segmentfault.com/a/1190000023839912
3. 可重入锁之-synchronized和ReentrantLock
   https://juejin.cn/post/6894798770685509639
4. synchronized原理+锁升级
   https://blog.csdn.net/Kurry4ever_/article/details/109557532
   https://www.cnblogs.com/aspirant/p/11470858.html
5. jvm调优
   https://zhuanlan.zhihu.com/p/363961261
6. canal解决mq消息顺序问题
   https://www.cxyzjd.com/article/m0_48043632/111642237
7. apollo原理
   http://c.biancheng.net/view/5482.html
   8.一行一行源码分析AQS
   https://www.javadoop.com/post/AbstractQueuedSynchronizer




# java基础
1. 对list<String>去重
    1. 用set;
    2. java8里的stream.distinct()方法;
       重写equals()方法和hashCode()方法, 再用list.contains方法比较
    3. 利用Iterator遍历，new list contains(string), remove方法移除去重
    4. hashmap的key不可重复
2. 为什么重写equals方法必须重写hashcode?  
   因为java约定, 两个相等的对象必须具有相等的散列码;如果不重写, 会导致对象相等, 但是有可能两个的散列码不相等;
3. base理论:是cap中的AP的一个扩展,指的是三个状态:
    1. 基本可用是指，通过支持局部故障而不是系统全局故障来实现的；
    2. Soft State表示状态可以有一段时间不同步；
    3. 最终一致，最终数据是一致的就可以了，而不是实时保持强一致
4. 分布式锁实现方式:
    1. 数据库;(1.乐观锁/悲观锁 2.创建一个表,利用表的唯一性约束, 插入成功的线程拿到锁 ) 2. redis 3. zk;
5. 线程安全与不安全怎么理解?
   线程安全，是指当多个线程访问同一个变量时，该变量不会因为多线程访问产生意想不到的问题;
   若每个线程中对全局变量、静态变量只有读操作，而无写操作，一般来说，这个全局变量是线程安全的；若有多个线程同时执行写操作，一般都需要考虑线程同步，否则的话就可能影响线程安全。
   分为失效问题和同步问题, 可以通过volatile解决缓存失效问题, 通过synchronized解决同步问题;
6. CountDownLatch, 简称:倒计时器;
   等待其他任务执行完毕之后才能执行;
   和join区别?
   两者都能够实现阻塞线程等待完成后，再继续进行后续逻辑;区别是:join必须等待子线程全部执行结束才行, 而CountDownLatch只需要等待子线程中某一步操作结束, 他的任务控制粒度更细
   6.0 CountDownLatch底层怎么实现的?倒计数器应用场景是什么?
   应用场景:(1.同时启用多个线程;2.等待多个线程完成各自任务后再开始执行自己的任务)
   1.火箭发射前, 所有零件都要进行检查, 并发检查所有零件, 都返回没问题, 才能发送火箭;
   2.出去旅游, 导游等所有人都集合后才会出发;
   3.在酒店吃饭, 服务员会等所有人都到后才上菜;
   实现原理:底层基于AQS实现,在数值大于0之前让调用await()方法的线程堵塞住，数值为0是则会放开所有阻塞住的线程
    1. 构造方法: 内部也是有个Sync类继承了AQS, CountDownLatch类的构造方法就是调用Sync类的构造方法,setState()方法设置AQS中state的值;
    2. await(): 该方法是使调用的线程阻塞住，直到state的值为0就放开所有阻塞的线程;调用到AQS中的acquireSharedInterruptibly()方法，先判断下是否被中断，接着调用了tryAcquireShared()方法;
       tryAcquireShared(),该方法在AQS是需要子类实现的，可以看到实现的逻辑就是判断state值是否为0，是就返回1，不是则返回-1。
    3. countDown(): 这个方法会对state值减1，会调用到AQS中releaseShared()方法，目的是为了调用doReleaseShared()方法;这个是AQS定义好的释放资源的方法，而tryReleaseShared()则是子类实现的，
       是一个自旋CAS操作，每次都获取state值，如果为0则直接返回，否则就执行减1的操作，失败了就重试，如果减完后值为0就表示要释放所有阻塞住的线程了，也就会执行到AQS中的doReleaseShared()方法。
       6.1 ReentrantLock通过什么方式来实现锁的?
       ReentrantLock通过重写锁获取和锁释放这两个方法实现了公平锁和非公平锁,就是aqs的tryAcquire,tryRelease;
       和CountDownLatch一样,内部有个sync类继承了aqs;
       6.2 介绍下AQS类?AQS是什么东西?怎么保证线程安全的?
       AQS(AbstractQueuedSynchronizer)是一个抽象同步队列, AQS的基本原理: 就是当一个线程请求共享资源的时候会判断是否能够成功操作这个共享资源，如果可以就会把这个共享资源设置为锁定状态，
       如果当前共享资源已经被锁定了，那就把这个请求的线程阻塞住，也就是放到队列中等待;
       AQS 由线程状态/队列状态 state 和 线程等待队列组成;AQS 加锁解锁的过程实际上就是对线程状态的修改和等待队列的出入队列操作
       AQS中有一个被volatile声明的变量state用来表示同步状态(>0表示已经获取了锁, =0表示释放了锁)(而对于不同的锁，state 也有不同的值：独享锁中 state =0 代表释放了锁，state = 1 代表获取了锁。共享锁中 state 即持有锁的数量。可重入锁 state 即代表重入的次数。读写锁比较特殊，因 state 是 int 类型的变量，为 32 位，所以采取了中间切割的方式，高 16 位标识读锁的数量 ，低 16 位标识写锁的数量.);

   AQS分为独占式/共享式两种实现方式:
   独占式:
   1.获取资源的时候会调用acquire()方法,这里面会调用tryAcquire()方法去设置state变量,如果失败的话就把当前线程放入一个Node中存入队列
   2.释放资源的时候是调用realase()方法,会调用tryRelease()方法修改state变量，调用成功后会去唤醒队列中Node里的线程，unparkSuccessor()方法就是判断当前state变量是否符合唤醒的标准，如果合适就唤醒，否则继续放回队列;
   (其中, tryAcquire()和tryRelease()需要子类自己实现, 可以通过cas实现)
   共享式:
   1.获取资源会调用acquireShared()方法，会调用tryAcquireShared()操作state变量，如果成功就获取资源，失败则放入队列
   2.释放资源是调用releaseShared()方法，会调用tryReleaseShared()设置state变量，如果成功就唤醒队列中的一个Node里的线程，不满足唤醒条件则还放回队列中
   (其中, tryAcquireShared()tryReleaseShared()需要子类自己实现)
   两者区别:同一时刻能否有多个线程同时获取到同步状态。 共享式访问资源时，同一时刻其他共享式的访问会被允许。独占式访问资源时，同一时刻其他访问均被阻塞。
   条件变量Condition在AQS中的原理(线程间通信Condition):
   ReentrantLock lock = new ReentrantLock();
   Condition condition = lock.newCondition();
   lock.newCondition()其实是new一个AQS中ConditionObject内部类的对象出来，这个对象里面有一个队列(双向链表),当调用await()方法的时候会存入一个Node节点到这个队列中，并且调用park()方法阻塞当前线程，释放当前线程的锁。而调用singal()方法则会移除内部类中的队列头部的Node，然后放入AQS中的队列中等待执行机会
   同样的，AQS并没有实现newCondition()方法，也是需要子类自己去实现

   公平锁和非公平锁不同之处在于,非公平锁不会先判断AQS队列中是否有等候的节点,而是直接cas试着获取一次锁,如果获取不到,则和公平锁一样尾插队列
   “非公平”即体现在这里，如果占用锁的线程刚释放锁，state置为0，而排队等待锁的线程还未唤醒时，新来的线程就直接抢占了该锁，那么就“插队”了
   (请注意此处的非公平锁是指新来的线程跟队列头部的线程竞争锁，队列其他的线程还是正常排队，百度面试题)

   进入阻塞队列排队的线程会被挂起，而唤醒的操作是由前驱节点完成的。当占用锁的线程结束，调用 unlock() 方法，此时 AQS 会去队列里唤醒排在最前面的节点线程。
   6.3 countDownLatch-倒计时器,CyclicBarrier-栅栏,Semaphore-信号量-,BlockingQueue-阻塞队列 区别和联系?   
   Semaphore, 信号量: 可以用来实现限流, 并发访问某个资源, 等待资源释放才能访问;
   BlockingQueue 阻塞队列, 另外也可以通过ReentrantLock 和 Condition实现;(Condition里的await和signal类似Object类的wait和notify)
   public class BoundedBuffer {
   final ReentrantLock lock = new ReentrantLock();
   final ConditionObject notFull = (ConditionObject) lock.newCondition();
   final ConditionObject notEmpty = (ConditionObject) lock.newCondition();
   final Object[] items = new Object[100];
   int putptr, takeptr, count;
   public void put(Object x) throws InterruptedException {
   lock.lock();
   try {
   // 当数组满了
   while (count == items.length) {
   // 释放锁，等待
   notFull.await();
   }
   // 放入数据
   items[putptr] = x;
   // 如果到最后一个位置了,下标从头开始,防止下标越界
   if (++putptr == items.length) {
   // 从头开始
   putptr = 0;
   }
   // 对 count ++ 加加
   ++count;
   // 通知 take 线程,可以取数据了,不必继续阻塞
   notEmpty.signal();
   } finally {
   lock.unlock();
   }
   }
   public Object take() throws InterruptedException {
   lock.lock();
   try {
   // 如果数组没有数据,则等待
   while (count == 0) {
   notEmpty.await();
   }
   // 取数据
   Object x = items[takeptr];
   // 如果到数组尽头了,就从头开始
   if (++takeptr == items.length) {
   // 从头开始
   takeptr = 0;
   }
   // 将数量减1
   --count;
   // 通知阻塞的 put 线程可以装填数据了
   notFull.signal();
   return x;
   } finally {
   lock.unlock();
   }
   }

   CyclicBarrier的计数器更像一个阀门，需要所有线程都到达，然后继续执行，计数器递增，提供reset功能，可以多次使用;
   它是 ReentrantLock 和 Condition 的组合使用
   场景:可以用于多线程计算数据，最后合并计算结果

   Semaphore[ˈseməfɔː(r)]（信号量）用来控制并发访问的线程数量;(和CountDownLatch一样, 内部也是有个Sync类继承了AQS, 有公平锁和非公平锁)
   场景:限流算法之---令牌桶/停车场提示牌/洗脚城;技师数量就是信号, 即并发数;顾客就是线程;
   信号量用完以后，后续使用acquire()方法请求信号的线程便会加入阻塞队列挂起

6.4 CountDownLatch和CyclicBarrier的区别?
1.CountDownLatch是线程组之间的等待，即主线程等待N个线程完成某件事情之后再执行;
而CyclicBarrier则是线程组内的等待，即每个线程相互等待，即N个线程都被拦截之后，然后依次执行。
2.CountDownLatch是减计数方式，而CyclicBarrier是加计数方式。
3.CyclicBarrier可以调用reset()复用;
7. synchronized和Lock(ReentrantLock)区别?
    1. synchronized是Java语言的关键字, Lock是一个接口;
    2. synchronized是自动释放锁, 程序执行完或者线程发生异常都会自动释放锁; 而lock必须要在finally中手动释放锁, 否则会造成死锁;
    3. lock.lockInterruptibly方法(相当于一个超时设为无限的tryLock方法)能够中断等待中线程的锁, 使用synchronized或者reentrantLock.lock()时，等待的线程会一直等待下去，不能够响应中断；
    4. 通过tryLock能够知道有没有成功获取锁, 而synchronized做不到;
       而且如果reentrantLock.tryLock(long timeout, TimeUnit unit)，那么如果线程在等待时被中断，将抛出一个InterruptedException异常, 即可打破死锁
    5. Lock提供了读写分离的锁, 能够提高多个线程进行读操作的效率;
    6. ReenTrantLock可以指定是公平锁还是非公平锁(默认)。而synchronized只能是非公平锁。所谓的公平锁就是先等待的线程先获得锁。
       7.0 使用synchronized 实现ReentrantLock?
       1.lock和unlock方法都用synchronized修饰;
       2.lock判断是否有线程占用, 若有则await()等待;unlock里判断当前占用锁是不是我想释放的这个线程id,若是, 则释放,并调用notify();
       7.1 ReentrantReadWriteLock与StampedLock区别?
       ReentrantReadWriteLock悲观读, 读的时候拒绝写,可重入锁,无法解决ABA问题;
       StampedLock乐观读,不可重入锁,通过版本号解决ABA问题;(StampedLockd的内部实现是基于CLH锁的，一种自旋锁，保证没有饥饿且FIFO。)
       (普通自旋锁缺点:锁饥饿:某个线程一直被插队; 竞争激烈时,会占用cpu做无用功)
       可重入锁的意思是: 方法a是synchronized方法, 在a中调用synchronized方法b, 可重入锁不需要再申请获得锁, 不可重入锁可能会因此导致死锁;
       当并发量大且读远大于写的情况下使用StampedLock;
8. volatile使用场景?
   多线程下的状态标记量和双重检查(比如单例的double-check)
    1. 在两个或者更多的线程需要访问的成员变量(成为共享变量)上使用volatile
       注意: volatile不影响读的性能, 由于使用volatile屏蔽掉了JVM中必要的代码优化，所以在效率上比较低
9. volatile和synchronized区别
   1、volatile不会进行加锁操作
   2、volatile变量作用类似于同步变量读写操作
   3、volatile无法保证原子性, 不如synchronized安全
10. java多线程方式: 1.继承Thread类 2.实现Runnable接口 3. 使用ExecutorService、Callable、Future
11. 暂停线程方式: 四种
    应用场景: 假设从网络下载一个100M的文件，如果网速很慢，我等得不耐烦，想在下载过程中点“取消”，这时，就需要中断下载线程的功能。
    1. join: (只有调用它的线程会被阻塞) 调用子线程并且等待子线程结束后，主线程才能继续运行
    2. sleep: (不会释放锁)使当前正在执行的线程休眠millis秒, 线程处于阻塞状态
    3. yield: (让出CPU的使用权，使线程从运行态直接进入就绪状态) 当前正在执行的线程暂停一次，允许其他线程执行，不阻塞，如果没有其他等待执行的线程，这个时候当前线程就会马上恢复执行;
    4. stop : 强制线程停止工作, 已弃用(为什么弃用, 因为它本身是不安全的, 停掉一个线程将会导致 ThreadDeath异常传播到堆中, 并且立即释放该线程所持有的所有的锁, 会导致数据不一致的风险);
       关于如何正确停止线程:
        1. 使用violate boolean变量来标识线程是否停止
        2. 停止线程时，需要调用停止线程的interrupt()方法，因为线程有可能在wait()或sleep(), 提高停止线程的即时性
           (但是调用interrupt方法是在当前线程中打了一个停止标志，并不是真的停止线程。)
    5. interrupt: thread.interrupted() 测试当前线程是否已经中断---- 该方法调用后会将中断标示位清除，即第二次调用返回false;
       isInterrupted()  测试当前线程是否已经中断----- 线程的中断状态 不受该方法的影响。, 多次调用都返回true;
       interrupt- ---- 中断线程;
       应用场景:
12. ThreadLocal?
    1.在当前线程中，任何一个点都可以访问到ThreadLocal的值
    2.只能被当前线程访问
    不会出现内存泄露, 内部通过ThreadLocalMap实现, 因为是弱引用(jvm垃圾回收器 一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存);
    强引用就是new()
13. netty框架有啥好处?(netty能做什么?)(netty的应用场景?)
    1.使用Netty你就可以定制编解码协议，实现自己的特定协议的服务器。Netty就是基于Java NIO技术封装的一套框架
    2.Dubbo 协议默认使用 Netty 作为基础通信组件，用于实现各进程节点之间的内部通信
    3.RocketMQ 的消息生产者和消息消费者之间异步通信
    13.1 netty理解?
    事件驱动模型, boss线程负责把事件放入队列中, 每个端口对应一个boss线程;
    work线程消费事件队列中的事件，调用对应的handler处理事件。
14. java, BIO和NIO有什么区别?
    BIO:Blocking IO, 阻塞io;
    Accept是阻塞的，只有新连接来了，Accept才会返回，主线程才能继续
    Read是阻塞的，只有请求消息来了，Read才能返回，子线程才能继续处理
    Write是阻塞的，只有客户端把消息收了，Write才能返回，子线程才能继续读取下一个请求
    NIO:NoneBlocking IO, 非阻塞IO;高性能，吞吐量更高;通过事件驱动实现非阻塞io;
15. NIO的bug?netty是怎么解决这个bug的?
    epoll 空轮询;
16. synchronized锁的升降级?
    锁的升级、降级，就是JVM优化 synchronized运行的机制，当JVM检测到不同的竞争状况时，会自动切换到适合的锁实现，这种切换就是锁的升级、降级。
    无锁(标志位:01)->偏向锁(01)->轻量级锁(00)->重量级锁(10)->GC标记(11)
    其中偏向锁和轻量级锁, 要先说下对象在内存中的布局, 分为三块区域：对象头、实例数据和对齐填充;
    对象头中的mark word存储的是对象的hashcode, 锁状态标志, GC分代年龄,线程持有的锁,偏向线程id,偏向时间戳等;
    锁升级具体过程:
    当没有竞争出现时，默认会使用偏向锁。JVM 会利用 CAS 操作（compare and swap），在对象头上的 Mark Word 部分设置线程 ID，以表示这个对象偏向于当前线程，所以并不涉及真正的互斥锁。这样做的假设是基于在很多应用场景中，大部分对象生命周期中最多会被一个线程锁定，使用偏向锁可以降低无竞争开销。如果有另外的线程试图锁定某个已经被偏向过的对象，JVM 就需要撤销（revoke）偏向锁，并切换到轻量级锁实现。轻量级锁依赖 CAS 操作 Mark Word 来试图获取锁，如果重试成功，就使用普通的轻量级锁；否则，进一步升级为重量级锁。
    锁降级:可以认为--锁是不能降级的, 只有一种情况是可以的:
    当 JVM 进入安全点（SafePoint）的时候，会检查是否有闲置的 Monitor，然后试图进行降级
    也就是说jvm进入垃圾回收的STW 阶段(stop-the-world);
    16.1 什么是锁膨胀?
    首先说一下synchronize锁的优化策略:
    自旋锁--->优化后是:自适应自旋锁:锁默认自旋10次,如果一个锁在10次内获得了锁, 则下次可以设置成最多自旋20次;反之,减少自旋次数,反正他也拿不到锁;
    锁粗化--->优化后:当系统发现频繁的加锁解锁(MutexLock重量级锁,重量级锁涉及到线程阻塞),线程的阻塞和唤醒从用户态进入内核态，开销很大, 于是将多个连续的加锁、解锁操作连接在一起，扩展成一个范围更大的锁;
    锁消除--->通过逃逸分析发现没有别的线程会来抢占锁（别的线程没有临界量的引用），而“自作多情”地给自己加上了锁。有可能虚拟机会直接去掉这个锁
    偏向锁和轻量级锁--->>>最高效的是偏向锁，尽量使用偏向锁，如果不能（发生了竞争）就膨胀为轻量级锁
    偏向锁的具体过程:当一个进程访问同步块并且获得锁的时候，会在对象头和栈帧的锁记录里面存储取得偏向锁的线程ID;下一次有线程尝试获取锁的时候，首先检查这个对象头的MarkWord是不是储存着这个线程的ID。如果是，那么直接进去而不需要任何别的操作;如果不是，那么分为两种情况: 1. 对象锁的标志位为00，说明发生了竞争，偏向锁已经膨胀为轻量级锁，这时使用CAS操作尝试获得锁;
    2. 对象锁的标志位为01，说明还是偏向锁不过请求的线程不是原来那个了。这时只需要使用CAS尝试更新下对象头中markword的线程id;
    如果cas失败:说明别的线程竞争并得到了锁, 则判断下那个线程是不是活动线程, 如果不是活动线程:则将对象头的markword里的线程id清空; 如果是活动线程:先遍历栈帧里面的锁记录，让这个线程的偏向锁变为无锁状态，然后恢复线程;
    如果cas失败了, 去查看MarkWord里线程id的值, 有两种可能:1. 指向当前线程的指针; 2. 别的值;
    如果是1, 则说明发生了重入, markword的值已经被修改了新的值了, 但是这个指针还是指向这个线程的, 直接当做成功获得锁处理; 如果是2, 则说明发生了竞争, 锁会膨胀为一个重量级锁(标志位10), 后面等待的线程将会进入阻塞状态;
    解锁过程:(那个拿到锁的线程)用CAS把MarkWord换回到原来的对象头,如果成功,那么没有竞争发生,释放锁成功.
    如果失败,表示存在竞争（之前有线程试图通过CAS修改MarkWord），这时要释放锁的同时,唤醒阻塞的线程。
    逃逸分析:举例子,StringBuffer和Vector 都是用 synchronized 修饰的, 当Java虚拟机判断对象只有当前线程在使用，就会移除该对象的同步锁;
17. transient: 不需要序列化的属性前添加关键字transient;只能修饰变量，而不能修饰方法和类; 这个字段的生命周期仅存于调用者的内存中而不会写到磁盘里持久化
18. CLH锁?
    AQS是JUC的核心, CLH锁的原理和思想是AQS的基础, clh是一种基于链表的可扩展、高性能、公平的自旋锁，申请线程只在本地变量上自旋，它不断轮询前驱的状态，如果发现前驱释放了锁就结束自旋。
    一个节点代表一个线程, 不可重入的独占锁;因为clh使用locked字段(true/false)来代表是否占有锁(false没有锁/释放锁);
    加锁的过程: 每一个节点都有locked字段标示锁的状态, tailNode指向最后一个节点,每次线程进来的时候, 判断最后一个线程的locked是否是true, 是->将自己的locked也设置为true, 跟着自旋等待;
    释放锁: 将当前节点的locked状态设置为false. 同时由于当前节点被后面一个线程的preNode指向着, 所以
    (CLH锁原理：锁维护着一个等待线程队列，所有申请锁且失败的线程都记录在队列. 用以判断当前线程是否已经释放锁。当一个线程试图获取锁时，循环判断所有的前序节点是否已经成功释放锁。)
    每一个进程都有一个各自对应的队列结点， 但是队列中的结点相互之间其实都没有指针关联，整个队列的前驱后继关系依赖于每个进程的私有指针维系
19. aqs在clh基础上做了哪些优化?
    1.用阻塞等待替换了自旋操作,不能主动感知到前驱节点状态变化的信息, 需要显示的通知下一个节点解除阻塞;
    2.改用waitStatus, 使锁可重入;
    3.引入了头结点和尾节点,将队列改成双向链表;
20. CAS和Synchronized适用场景?
    CAS适用于读多写少的情况（冲突较少）;
    synchronized适用于写比较多的情况下（多写场景，冲突一般较多）
21. CAS有什么问题，性能开销，开销在什么地方?
    三大问题:ABA问题;循环时间长性能开销大;只能保证一个共享变量的原子操作;
    性能开销在什么地方?
    如果CAS不成功，则会原地自旋，如果长时间自旋会给CPU带来非常大且没必要的开销。
22. juc下面的类你用过哪些?
    1.atomic包下的原子变量类;
    2.locks包下的ReentantLock,ReentrantReadWriteLock,StampedLock
    3.容器类:ConcurrentHashMap;
    4.同步工具类:CountDownLatch倒计时器;CyclicBarrier栅栏;Semaphore信号量;
    5.Executor框架相关:ThreadPoolExecutor创建线程池;
    6.阻塞队列实现类:ArrayBlockingQueue、LinkedBlockingQueue、PriorityBlockingQueue



# hashmap
- 数据结构  
  数据+链表,1.8之后新增红黑树,查询性能从o(n)提升到o(lgn)
- 允许key和value为null
- hashmap初始容量默认为16, 负载因子是0.75, 通过位运算每次扩容是2的n次方
- 1.8中优化点:新增了红黑树,从头插法改为尾插法,1.7先扩容再插入, 1.8先插入再扩容
  头插法和尾插发优缺点:因为1.7的头插法在并发情况下会产生环形链(a线程插入1->2, b线程扩容2>1);
- 扩容的时候为什么 1.8 不用重新 hash 就可以直接定位原节点在新数据的位置呢?
  因为扩容是扩为原来的两倍, 就是掩码最高位多了一个1, &运算, 两种情况:a.原数据hashcode最高位为0, 位置不变;b.为1,则位置=原来位置+原数组容量;
- hash函数计算过程: hash 函数是先拿到通过key的hashcode，是 32 位的 int 值，然后让 hashcode 的高 16 位和低 16 位进行异或操作。
- 为什么这样设计hash函数: 1.通过扰乱函数尽可能降低hash碰撞，使其分散越好；2.算法一定要尽可能高效，因为这是高频操作, 因此采用位运算；

hashmap的put过程:
1. 判断数组是否为空，为空进行初始化;
2. 不为空，计算 k 的 hash 值，通过(n - 1) & hash计算应当存放在数组中的下标 index;
3. 查看 table[index] 是否存在数据，没有数据就构造一个 Node 节点存放在 table[index] 中；
4. 存在数据，说明发生了 hash 冲突(存在二个节点 key 的 hash 值一样), 继续判断 key 是否相等，相等，用新的 value 替换原数据(onlyIfAbsent 为 false)；
5. 如果不相等，判断当前节点类型是不是树型节点，如果是树型节点，创造树型节点插入红黑树中；
6. 如果不是树型节点，创建普通 Node 加入链表中；判断链表长度是否大于 8， 大于的话链表转换为红黑树；
7. 插入完成之后判断当前节点数是否大于阈值，如果大于开始扩容为原数组的二倍
- 线程安全:  
  1.7会造成环形链,数据丢失,数据覆盖的问题;  
  1.8会有数据覆盖的问题, 也有死循环的问题(1.是在链表转换为树的时候for循环一直无法跳出 2.balanceInsertion平衡树的时候);  
  put的时候可能会造成数据不一致(eg:a线程判断数组为空,线程被挂起,b线程也判断为空,并往里设置值1,a线程恢复,将值覆盖为2;)  
  resize而引起死循环
- 为什么hashmap1.8使用红黑树不用二叉平衡树
    - CurrentHashMap中是加锁了的，实际上是读写锁，如果写冲突就会等待，如果插入时间过长必然等待时间更长，而红黑树相对AVL树他的插入更快
    - avl树更加平衡, 适合使用查询多的;
    - 插入数据, avl的旋转次数比红黑树更多;
- 为什么redis的zset使用跳表不用红黑树
    1. 跳表实现更简单
    2. zset有区间查询, 跳表的效率更高
- 说说你对红黑树的见解？
    - 每个节点非红即黑
        - 根节点总是黑色的
    - 如果节点是红色的，则它的子节点必须是黑色的（反之不一定）
        - 每个叶子节点都是黑色的空节点（NIL节点）
        - 从根节点到叶节点或空子节点的每条路径，必须包含相同数目的黑色节点（即相同的黑色高度）
- 怎么解决hashmap不安全?
    1. hashtable, 通过synchronized 关键字锁住整个数组,锁粒度大;
    2. Collections.synchronizedMap, 通过传入 Map 封装出一个 SynchronizedMap 对象, 内部是通过对象锁实现;
    3. ConcurrentHashMap分段锁;


# concurrenthashmap
1. currentHashMap的key和value都不允许为null(因为不知道是key为null还是value是null)
1. 大量的利用了volatile(禁止指令重排序,保证内存可见性,无法保证原子性),CAS等乐观锁技术来减少锁竞争对于性能的影响
   volatile是通过将汇编指令lock当做是一个内存屏障来实现的;
   lock指令保证了以下几点:
   1. 重排序时不能把后面的指令重排序到内存屏障之前的位置
   2. 将本处理器的缓存写入内存
   3. 如果是写入动作，会导致其他处理器中对应的缓存无效。
   内存屏障保证前面的指令先执行，所以这就保证了禁止了指令重排啦，同时内存屏障保证缓存写入内存和其他处理器缓存失效，这也就保证了可见性
   而synchronized(是不能保证指令重排的) 锁方法的时候是 通过 jvm 层指令monitorenter和monitorexit两个指令，可以保证被synchronized修饰的代码在同一时间只能被一个线程访问，
   即可保证 CPU 时间片不会 在多个线程间切换，也就能保证原子性。
   synchronized在锁代码块的时候, 是通过ACC_SYNCHRONIZED标记, jvm在方法访问标识符(flags)中加入ACC_SYNCHRONIZED来实现同步;
2. ConcurrentHashMap保证线程安全的方案是：  
   JDK1.8：synchronized+CAS+HashEntry+红黑树  
   JDK1.7：ReentrantLock+Segment+HashEntry
3. Segment继承ReentrantLock, 是一种可重入锁，是一种数组和链表的结构，一个Segment中包含一个HashEntry数组(用volatile修饰, 保证了多线程下的可见性), 每个HashEntry又是一个链表结构，因此在ConcurrentHashMap查询一个元素的过程需要进行两次Hash操作，如下所示：
    - 第一次Hash定位到Segment
    - 第二次Hash定位到元素所在的链表的头部
4. 但在JDK1.8中摒弃了Segment分段锁的数据结构，基于CAS(乐观锁)操作保证数据的获取以及使用synchronized关键字对Node（首结点）（实现 Map.Entry) 加锁来实现线程安全，这进一步提高了并发性
5. 为什么使用synchronized替换可重入锁ReentrantLock?
    - jdk1.6中, 对 synchronized 锁的实现引入了大量的优化，并且 synchronized 有多种锁状态，会从无锁 -> 偏向锁 -> 轻量级锁 -> 重量级锁一步步转换, 所以性能差不多
    - 减少内存开销 。假设使用可重入锁来获得同步支持，那么每个节点都需要通过继承 AQS来获得同步支持。但并不是每个节点都需要获得同步支持的,只有链表的头节点（红黑树的根节点）需要同步，这无疑带来了巨大内存浪费
5. 描述ConcurrentHashMap的put操作步骤
    1. 如果没有初始化，就调用 initTable() 方法来进行初始化；
    2. 如果没有 hash 冲突就直接 CAS 无锁插入；
    3. 如果需要扩容，就先进行扩容；
    4. 如果存在 hash 冲突，就加锁来保证线程安全，两种情况：一种是链表形式就直接遍历到尾端插入，一种是红黑树就按照红黑树结构插入；
    5. 如果该链表的数量大于阀值8，就要先转换成红黑树的结构，break 再一次进入循环
    6. 如果添加成功就调用 addCount() 方法统计 size，并且检查是否需要扩容。
7. 为什么链表 >8转化为红黑树, 为什么<=6退化为链表;
   在 hash 函数设计合理的情况下，发生 hash 碰撞 8 次的几率为百万分之 6，概率说话。。因为 8 够用了，至于为什么转回来是 6，因为如果 hash 碰撞次数在 8 附近徘徊，会一直发生链表和红黑树的转化，为了预防这种情况的发生。
8. 有序的hashmap有: LinkedHashMap 和 treemap, LinkedHashMap 怎么实现有序的?
   LinkedHashMap 内部维护了一个单链表，有头尾节点，同时 LinkedHashMap 节点 Entry 内部除了继承 HashMap 的 Node 属性，还有 before 和 after 用于标识前置节点和后置节点。可以实现按插入的顺序或访问顺序排序。
9. treemap的默认排序是什么?
   按照key的字典顺序升序(注意:子串<父串;按照ascii排序);或者实现Comparator接口, 自定义排序规则;



# 线程池
1. 线程池有哪些核心参数
    1. 核心线程数：corePoolSize ,线程池中活跃的线程数  
       allowCoreThreadTimeOut的值是控制核心线程数是否在没有任务时是否停止活跃的线程，
       当它的值为true时，在线程池没有任务时，所有的工作线程都会停止
    2. 最大线程数：maximumPoolSize
    3. 多余线程存活时长：keepAliveTime, 多余线程数 = 最大线程数 - 核心线程数
       如果在这个时间范围内，多余线程没有任务需要执行，则多余线程就会停止
    4. 多余线程存活时间的单位：TimeUnit
    5. 任务队列：workQueue
       线程池的任务队列，使用线程池执行任务时，任务会先提交到这个队列中，然后工作线程取出任务进行执行，当这个队列满了，线程池就会执行拒绝策略。
    6. 线程工厂：threadFactory
       创建线程池的工厂，线程池将使用这个工厂来创建线程池，自定义线程工厂需要实现ThreadFactory接口。
    7. 拒绝执行处理器（也称拒绝策略）：handler
       当线程池无空闲线程，并且任务队列已满，此时将线程池将使用这个处理器来处理新提交的任务。
2. 线程池有哪些拒绝策略(0到9)
    - AbortPolicy         --(0,1) 当任务添加到线程池中被拒绝时，它将抛出 RejectedExecutionException 异常。
    - CallerRunsPolicy    --(全部执行完) 当任务添加到线程池中被拒绝时，会在线程池当前正在运行的Thread线程池中处理被拒绝的任务。
    - DiscardOldestPolicy --(0,9) 当任务添加到线程池中被拒绝时，丢弃队列最后面的任务，然后将被拒绝的任务添加到等待队列后面
    - DiscardPolicy       --(0,1) 当任务添加到线程池中被拒绝时，线程池将丢弃被拒绝的任务
3. 线程池工作流程/原理
    - 如果workerCount < corePoolSize，则创建并启动一个线程来执行新提交的任务。
    - 如果workerCount >= corePoolSize，且线程池内的阻塞队列未满，则将任务添加到该阻塞队列中。
    - 如果workerCount >= corePoolSize && workerCount < maximumPoolSize，且线程池内的阻塞队列已满，则创建并启动一个线程来执行新提交的任务。
    - 如果workerCount >= maximumPoolSize，并且线程池内的阻塞队列已满, 则根据拒绝策略来处理该任务, 默认的处理方式是直接抛异常。
    - 如果线程池中线程数超过corePoolSize，且线程空闲下来时，超过空闲时间 就会被销毁，直到线程数==corePoolSize, 如果设置allowCoreThreadTimeOut=true,那么超过keepAliveTime时，低于corePoolSize数量的线程空闲时间达到keepAliveTime也会销毁  
      ![avatar](../picture/线程池/线程池任务调度流程.png)
4. 线程池的阻塞队列    
   ![avatar](../picture/线程池/线程池阻塞队列实现方式.png)
5. 什么情况下使用线程池  
   T1 创建线程时间，T2 在线程中执行任务的时间，T3 销毁线程时间
   T1+T3>T2



# jvm
1. 线上频繁fullgc,怎么处理?
    - 特征:
        1. 线上多个线程的 CPU 都超过了 *** jstack 命令可以看到这些线程主要是垃圾回收线程。
        2. 通过 jstat 命令监控 GC 情况，可以看到 Full GC 次数非常多，并且次数在不断增加。
    - 排查步骤:
        1. 找到cpu占用过高的进程(top)
        2. 找到进程下占用过高的线程(top -Hp 进程id )
        3. jstack查找nid=线程id, 是否由于gc频繁导致的(日志有VM Thread代表垃圾回收)
        4. 可以查看 GC 的情况(jstat -gcutil 进程id 1000 10), 如果gc频繁,基本确定是由于内存溢出导致的;
        5. Dump出内存日志(jmap dump:format=b,file=a.dmp pid), 用mat工具查看是哪个对象比较消耗内存,
           如果日志里面没有哪个对象耗内存, 再查看下是不是由于手动调用system.gc(),
           如果是, 可以添加-XX:+DisableExplicitGC来禁用JVM 对显示 GC 的响应;
2. 分析cpu过高
    1. 查看该进程中有哪些线程 CPU 过高(top -Hp)
       这样我们就能得到 CPU 消耗比较高的线程 id。
    2. 接着通过该线程 id 的十六进制表示在 jstack 日志中查看当前线程具体的堆栈信息。
       在这里我们就可以区分导致 CPU 过高的原因具体是 Full GC 次数过多还是代码中有比较耗时的计算了。
    3. 如果是 Full GC 次数过多，那么通过 jstack 得到的线程信息会是类似于 VM Thread 之类的线程
       而如果是代码中有比较耗时的计算，那么我们得到的就是一个线程的具体堆栈信息。
3. 分析接口偶发性耗时长,并给出解决方案
    1. 先用jmeter压测;
    2. 找到卡住的那些线程, dump堆栈日志, 在日志中找打印出 TIMED_WAITING 相同日志的地方
5. 谈谈你对jvm了解?
   jvm内存分为: 5个区域;
   堆区、方法区: 这两个区域的数据共享  
   虚拟机栈、本地方法栈、程序计数器: 这三个区域的数据私有隔离，不可共享
   方法区主要是存储类的元数据的;
   堆区:Eden区、From Survivor、To Survivor三个区域，比例是8:1:1
6. jvm新生代内存占比为什么是8:1:1?为什么三个区,两个区有什么危害?
   新生代GC算法使用的是复制回收算法;当新生代内存使用达到90%时触发Minor GC, 将伊甸区+fromSurvivor存活的对象放入To Survivor;
   始终保持着其中一个S区是空留的，保证GC的时候复制存活的对象有个存储的地方;
   这样做的好处是高效, 因为只需复制少量存活的对象，比标记和标记整理高效多;
   (注意：如何判断对象存活可用，通过可达性分析法来判断:以GCRoot对象做起点，从这些节点向下搜索, 如果一个对象在引用链上(搜索所走过的路径称为引用链)，说明是可达的,不能被清除;
   GCRoots的对象包括下面几种：
   1.虚拟机栈和本地方法栈中局部变量表中引用的对象
   2.方法区中常量和类静态属性引用的对象
   当对象在 Eden 出生后，在经过一次 Minor GC 前，会标记不需要清除的对象，然后将标记的对象复制到ToSurvivor区，然后清理所使用过的Eden以及fromSurvivor区，并且将这些存活的对象的年龄设置为1，（当对象的年龄达到某个值时 ( 默认是 16 岁，CMS默认6岁，可以通过参数 -XX:MaxTenuringThreshold设置)，才会被送到老年代。补充:对于一些较大的对象(即s区放不下 ) 则是直接进入到老年代。
   设置两个Survivor区最大的好处就是解决了碎片化;如果只设置一个s,第二次minorGC时,伊甸区和原s区都有存活对象,导致前面会有内存不连续;
   6.1 判断是否为垃圾对象的算法?
   1.引用计数法(效率高, 计数器为0则为垃圾对象, 但是无法解决互相引用的对象)
   2.可达性分析
7. 垃圾回收算法有哪些?
   标记-清除算法:缺点:效率不高，无法清除垃圾碎片;
   复制回收算法: 年轻代所使用的gc算法;
   标记-整理算法: 标记无用对象, 让有用的都移动到一端, 清理剩下的, 优点:不会产生内存碎片;
   分代算法：根据对象存活周期的不同将内存划分为几块，一般是新生代和老年代，新生代基本采用复制算法，老年代采用标记整理算法。
8. JVM 有哪些垃圾回收器?
   (不推荐)Serial收集器（复制算法): 新生代单线程收集器;
   (不推荐)ParNew收集器 (复制算法): 新生代收并行集器,Serial并发版本;
   Parallel Scavenge[ska.vnj]收集器 (复制算法): 新生代并行收集器, 高吞吐量

   (不推荐)Serial Old收集器 (标记-整理算法): 老年代单线程收集器
   Parallel Old收集器 (标记-整理算法)： 老年代并行收集器，高吞吐量
   CMS(Concurrent Mark Sweep)收集器（标记-清除算法）： 老年代并发收集器，以牺牲吞吐量为代价来获得最短回收停顿时间的垃圾回收器;若干次Gc后进行一次碎片整理;

   G1(Garbage First)收集器 (标记-整理算法): 同时满足高吞吐量以及低延迟;
   Java堆并行收集器，G1收集器是JDK1.7提供的一个新收集器，G1收集器基于“标记-整理”算法实现，也就是说不会产生内存碎片。此外，G1收集器不同于之前的收集器的一个重要特点是：G1回收的范围是整个Java堆;
   G1重要特性: 可预测的停顿时间模型,根据优先级列表在规定时间内回收价值最大的Region
9. jdk8 默认的垃圾回收器是Parallel Scavenge + Parallel Old;
   jdk1.9 默认垃圾收集器G1, 并且cms被标记为废弃;
   在小内存(低于6Gb)应用上CMS的表现大概率会优于G1,大于等于8Gb使用G1;
10. G1使用场景:
    1. 大内存(堆大小>=6GB)、多处理器的机器;
    2. 运行过程产生大量碎片需要压缩;
    3. 可控的GC停顿周期，防止高并发下的雪崩;
11. jvm调优:
    1. Xmx和Xms配置成一样以避免每次gc后JVM重新分配内存.
        1. 空余堆内存小于40%时，JVM就会增大堆直到-Xmx.
        2. 空余堆内存大于70%时，JVM会减少堆直到 -Xms.
           xms: JVM初始分配的堆内存; xmx:JVM最大允许分配的堆内存
    2. Xmn256m ：年轻代内存大小，整个JVM内存=年轻代+年老代
    3. NewRatio: 年轻代(包括Eden和两个Survivor区)与年老代的比值;
       Xms=Xmx并且设置了Xmn的情况下，该参数不需要进行设置
    4. DisableExplicitGC: 禁用手动gc;
       总结: GC的性能有2个方面的指标：吞吐量throughput 和暂停时长pause（gc发生时app对外显示的无法响应）



# 设计模式
1. 单例模式:
    1. 懒汉式;

```
        private static Singleton instance;  
        private Singleton (){}  
        public static synchronized Singleton getInstance() {  
        if (instance == null) {  
            instance = new Singleton();  
        }  
        return instance;  
        } 
```
    2. 饿汉式;
```
        private static Singleton instance = new Singleton();  
        private Singleton (){}  
        public static Singleton getInstance() {  
        return instance;  
        }
```
    3. 双重校验锁;
```
        private volatile static Singleton singleton;  
        private Singleton (){}  
        public static Singleton getSingleton() {  
        if (singleton == null) {  
            synchronized (Singleton.class) {  
            if (singleton == null) {  
                singleton = new Singleton();  
            }  
            }  
        }  
        return singleton;  
        }  
```
    4.静态内部类 5.枚举
2. 策略模式
    1. 策略枚举
    2. 策略接口
    3. 策略处理器,在Servlet运行的时候执行,将所有策略对象加载到map中;
    4. 策略实现类
3. 策略模式和模板模式区别
    - 策略模式:注重多套算法多套实现
    - 模板模式:一般只针对一套算法
4. 适配器模式, 责任链模式, 代理模式?
   责任链模式:使用场景工作流中审批流程;
   在责任链模式中，客户只需要将请求发送到责任链上即可，无须关心请求的处理细节和请求的传递过程，请求会自动进行传递。所以责任链将请求的发送者和请求的处理者解耦了。
   适配器模式:适配器继承已有的对象，实现想要的目标接口。
   策略模式优于适配器模式;
   代理模式:
   1、和适配器模式的区别：适配器模式主要改变所考虑对象的接口，而代理模式不能改变所代理类的接口。 2、和装饰器模式的区别：装饰器模式为了增强功能，而代理模式是为了加以控制。
   代理模式使用场景: 1. 延迟初始化 （虚拟代理）。 如果你有一个偶尔使用的重量级服务对象， 一直保持该对象运行会消耗系统资源时， 可使用代理模式
   2. 保护目标对象, 在代理层将满足条件的请求传递给服务对象;
   适配器模式能为被封装对象提供不同的接口， 代理模式能为对象提供相同的接口， 装饰模式则能为对象提供加强的接口。


# mysql
1. 索引失效-----Extra中没有出现using index
    1. 违反最左前缀法则(abc索引, ac:a生效, c不生效)
    2. 在索引列上做任何操作(计算、函数)
    3. 索引范围条件右边的列(> < between)
    4. 尽量使用覆盖索引(少用select *, 能减少回表,减少树的搜索次数)--回表是指在非主键索引树上查询, 拿到id再回到主键索引树上查询
    5. 使用不等于（!=、<>, NOT IN不行）
    6. like以通配符开头（'%abc'）
    7. varchar不加单引号索引失效
    8. or连接(俩个查询条件字段中有一个没有索引)
    9. order by(后面既有desc,又有asc;  后面两个字段不遵循最左前缀法则;  含非索引字段)
    10. group by一样  
        <br/>
2. 如何选择合适的列创建索引  
   索引会提高查询速度, 但是会降低更新表的速度, 因为更新表时还需要更新索引;
   索引还要占磁盘空间
   3要 3不要
    - 3要:
        1. 频繁作为查询条件的字段应该创建索引
        2. 查询中与其它表关联的字段，外键关系建立索引
        3. 在经常需要根据范围进行搜索的列上创建索引，因为索引已经排序，其指定的范围是连续的；这样查询可以利用索引的排序，加快排序查询时间；
    - 3不要:
        1. 唯一性很差的字段不合适做索引，如性别(eg:有10条数据, 如果建立了索引, 需要先在非主键索引表中找到6个女生, 再回表扫描6行数据, 性能还不如不用)
        2. 更新频繁的字段不适合，耗时且影响性能
        3. Where条件里用不到的字段不创建索引  
           <br/>
3. delete会有什么隐患? 怎么优化
    - 隐患:
        1. 因为delete+where 删除记录数据库空间不减少, 会产生大量碎片，影响磁盘IO；---->解决办法:使用 OPTIMIZE TABLE 整理数据文件的碎片,
           注意，在OPTIMIZE TABLE运行过程中，MySQL会锁定表, 在半夜进行
    - 优化:
        1. where条件用索引
        2. 删除三个月以前的数据正确操作(晚上没有业务场景):
            1. 先创建临时表tmp
            2. 将需要的数据保留到tmp表中,然后通过rename将当前业务表替换为bak表,再将tmp表替换为业务表, 最后删除bak表;
        3. 每隔一段时间执行一次 OPTIMIZE TABLE  
           <br/>
4. 事务隔离级别  
   事务具有原子性（Atomicity）、一致性（Consistency）、隔离性（Isolation）、持久性
    1. Read Uncommited(RU)：读未提交，一个事务可以读到另一个事务未提交的数据！
       为了解决下图中问题:  
       ![avatar](../picture/java基础/读未提交.png)
    2. Read Committed (RC)：读已提交，一个事务总是可以读到另一个事务已提交的最新数据!
       为了解决下图中问题:
       ![avatar](../picture/java基础/读已提交.png)
    3. Repeatable Read (RR):可重复读(默认隔离级别)，a开启事务, 在a提交事务之前, 不会读取到别的事务提交的数据.
       加入间隙锁(gap lock)，解决了不可重复读问题,解决了在读情况下的幻读问题;
       ![avatar](../picture/java基础/可重复读.png)
    4. Serializable:串行化，该级别下读写串行化，即:读的时候加共享锁，也就是其他事务可以并发读，但是不能写。写的时候加排它锁，其他事务不能并发写也不能并发读。
       且所有的select语句后都自动加上lock in share mode，即使用了共享锁。是当前读，而不是快照读。  
       ![avatar](../picture/java基础/幻读.png)
       Next-Key锁(行锁+间隙锁)解决幻读问题,
       其中有索引,则在这条记录的两边，也就是(负无穷,10]、(10,30]这两个区间加了间隙锁;
       如果没有索引, 则在整个表加上间隙锁(即看起来就是表锁)
       <br/>
       4.1 MySQL 中是如何实现事务隔离的?(或者提问:如何实现可重复读？)
       通过mvcc实现可重复读,
       可重复读的核心是一致性读，而事务更新数据的时候，只能使用当前读，如果当前记录的行锁被其他事务占用，就需要进入锁等待。
       MVCC:多版本并发控制, 为了解决 读写互相不阻塞;
       原理:按时间先后顺序创建事务id; 根据不同的事务生成不同快照，快照学名叫做一致性视图;
       读提交是每次执行sql的时候都重新生成一次快照, 可重复读是在事务开始的时候生成一个当前事务全局性的快照;
       怎么实现mvcc: 通过在每行记录后面保存两个隐藏的列来实现的(行数据的最近一次修改的事务id和 指向该行回滚段的指针)
       读未提交没有锁,串行化:读+共享锁,写+排它锁;
       4.2 解决mysql的并发写问题?
       有索引, 直接加行锁;
       没有索引, 为所有行加锁后, 再过滤，发现不满足的行就释放锁，最终只留下符合条件的行(一锁一释放会消耗性能);
       4.3 解决幻读问题?
       我先简单介绍下innodb的行锁: 有三种
       Record Lock: 行锁
       Gap Lock：间隙锁, 锁定一个范围，但不包括记录本身。GAP锁的目的，是为了防止同一事务的两次「当前读」，出现幻读的情况
       Next-Key Lock：前两个锁的加和，锁定一个范围，并且锁定记录本身。对于行的查询，都是采用该方法，主要目的是解决幻读的问题;
       间隙锁;B+索引树是有序的;
5. 事务和数据库锁的关系?
    1. MyISAM引擎(默认是Fulltext索引):表级锁;  innoDB:行级锁，它也支持表级锁;
    2. 读操作可以分成两类：快照读 (snapshot read)与当前读 (current read)。
        - 快照读，读取的是记录的可见版本 (有可能是历史版本)，不用加锁
        - 当前读，读取的是记录的最新版本，并且，当前读返回的记录前，都会加上锁，保证其他事务不会再并发修改这条记录
    3. 锁类型
        - 共享锁(S锁):假设事务T1对数据A加上共享锁，那么事务T2可以读数据A，不能修改数据A。
        - 排他锁(X锁):假设事务T1对数据A加上共享锁，那么事务T2不能读数据A，不能修改数据A。  
          我们通过update、delete等语句加上的锁都是行级别的锁。只有LOCK TABLE … READ和LOCK TABLE … WRITE才能申请表级别的锁。
    4. 当执行select 的时候 默认是不加锁的 （快照读） （这种说法在隔离级别为Serializable中不成立）  
       如果想要对某个行数据加锁需要 执行如下：  
       select * from table where num = 200 lock in share mode 共享锁  
       select * from table where num = 200 for update 行级锁  
       这是通过显示加锁实现的,当执行update,insert,delete的时候 默认是加行锁的
6. mybatis操作流程
    1. 先读取mybatis-config.xml全局配置;将配置信息转化为流;将流里面信息取出来放入sqlessionfactory
       mybatis一级缓存默认是开启的,默认创建的是cacheExector,本地缓存，sqlSession级别的缓存;
    2. 一级缓存失效的4种情况:
        1. sqlSession不同。
        2. sqlSession相同，查询条件不同。因为缓存条件不同，缓存中还没有数据。
           3. sqlSession相同，在两次相同查询条件中间执行过增删改操作。（因为中间的增删改可能对缓存中数据进行修改，所以不能用）
           4. sqlSession相同，手动清空了一级缓存。
    3. 二级缓存：全局缓存；基于namespace级别的缓存
       二级缓存----->一级缓存-------->数据库。
7. a系统给b系统一条sql, 怎么判断是正确的sql?
   语法引擎检测, 再改写sql
8. buffer pool 缓冲策略:
    1. 改版后的LRU算法(引发两个问题:预读失效,缓冲池污染)
       解决预读失效: 将整个缓冲池分为新生代(70%)和老年代(30%);
       污染: 描述: 当某一个sql查询大量数据, 导致缓冲池的大量数据被替换, mysql性能下降;
       解决缓冲池污染问题: 新增老生代停留时间窗口, 只有满足“被访问”并且“在老生代停留时间”大于T(默认是1秒)，才会被放入新生代头部；
9. 索引不用hash,用B+原因?
   因为hash不支持范围查找, B+树的叶子节点都有下一个节点的指针有最左匹配原则,
   b+树特征:
   1.所有数据都保存在叶子节点, 所以一次io次数更少;
   2.每个叶子节点组成一个有序链表,在范围查询的时候, 只需要查到最小的那个值, 然后通过叶子节点依次往右边遍历即可;
10. 为什么不用b树?
    1.因为b树的非叶子节点也存储数据, b+树的非叶子节点没有存储数据, 所以b+树可以存储更多的数据;(eg: 每个节点能存16KB,id是8B+指针是6B=一个节点14B大小, 节点个数:16KB/14B= 1170, 高度=2就是1万8千条, 高度=3就是2000万条)
    2.因为b树的叶子节点之间没有指针相连,所以范围查找的时候还需要多次使用中序遍历, 而b+树是有序链表;
    10.1 为什么不用avl树?
    1.avl树高度太高, 磁盘io次数过多;
    2.avl树范围查找多n步中序遍历;
    3.avl和红黑树 没能充分利用好磁盘预读功能 提供的数据, 逻辑上很近的父子节点, 可能相差较远;
11. sql调优?sql效率分析?如何分析耗时sql,怎么解决?优化慢sql?
    1. 首先分析索引失效;
    2. 非固定长度字段使用varchar代替char;
    3. limit m,n要慎重(越往后面翻页即m越大的情况下SQL的耗时会越来越长)
       ---正确做法是:经过我测试,当m大于1000时,先用覆盖索引查出id，然后通过id跟原表进行Join关联查询
    4. 使用覆盖索引
    5. 用where字句替换HAVING字句(HAVING只会在检索出所有记录之后才对结果集进行过滤);
12. 数据持久化存储磁盘里，磁盘的最小单元是扇区，一个扇区的大小是 512个字节;
    mysql存储数据的最小单元是页, 大约16K;(也就是说, 根据磁盘预读, 每次至少)
    文件系统的最小单元是块，一个块的大小是 4K;
13. 在三个字段上分别建立索引和建立一个联合索引有什么区别?
    联合索引:从最左匹配原则展开描述;
    三个字段上分别建立索引:会占用更多存储空间, 而且查询where后 跟三个字段, 根据mysql优化器的优化策略, 可能只有一个索引生效, 也可能多个生效;
14. 怎么让mysql强制走索引?
    select id from table FORCE INDEX(**) where ;
15. 主键和唯一索引的区别?
    1. 主键一定会创建一个唯一索引，但是有唯一索引的列不一定是主键；
    2. 主键不允许为空值，唯一索引列允许空值；
    3. 一个表只能有一个主键，但是可以有多个唯一索引；
    4. 主键可以被其他表引用为外键，唯一索引列不可以；
    5. 主键是一种约束，而唯一索引是一种索引
16. 如何让 like %abc 走索引查询？
    我们知道如果要让 like 查询要走索引，查询字符不能以通配符（%）开始，如果要让 like %abc 也走索引，可以使用 REVERSE() 函数来创建一个函数索引:
    select * from t where reverse(f) like reverse(’%abc’);
17. 联合索引的作用是什么？
    1. 可以减少一部分不必要磁盘开销; eg:建了一个 key(a,b,c) 的联合索引，那么实际等于建了 key(a)、key(a,b)、key(a,b,c) 等三个索引
    2. 使用覆盖索引减少回表, 减少io操作,提升数据库查询的性能
18. 唯一索引和普通索引哪个性能更好？
    1. 查询差不多, 都是从索引树中进行查询;
    2. 更新: 唯一索引更慢，因为唯一索引需要先将数据读取到内存中，再在内存中进行数据的唯一效验，所以执行起来要比普通索引更慢
19. 以下 or 查询有什么问题吗？该如何优化？
    select * from t where num=10 or num=20;
    答：如果使用 or 查询会使 MySQL 放弃索引而全表扫描，可以改为：
    select * from t where num=10 union select * from t where num=20;
20. mysql的事务提交是两阶段提交的, 比如update时两段提交?为什么要用两段提交?
    我先说一下update数据的流程;
    1.innodb将记录a加载进buffer pool;
    2.将a的old值写入undo log, 便于rollback;
    3.执行器更新内存中的数据;(此时该数据页/缓存页修改为脏页)
    4.准备两阶段提交--->写redo log(prepare阶段)
    5.写binlog;
    6.commit;
    为什么用两阶段提交?
    答:为了让redo log和binlog日志之间的逻辑一致(还需要合理地设置redolog和binlog的fsync的时机)
    fsync配置?
    sync_binlog = 1 问题: 这个参数控制binlog的落盘时机，并且公司线上数据库的该参数一定被设置成了1;表示当事务提交时会将binlog落盘(fsync磁盘);
    注意注意!!! 这里事务提交是在step1阶段; (最后binlog 阶段也会fsync一次磁盘, 一共两次, 目的:两个
    其一：redo log和binlog都是顺序写，顺序写比数据页的随机写节约时间，性能更高;
    其二：组提交机制使得我们不用每个事务都进行写磁盘操作，而是将多个写操作放在一个组里面，这样可以大幅度降低磁盘的IO)
    总结:不论mysql什么时刻crash，最终是commit还是rollback完全取决于MySQL能不能判断出binlog和redolog在逻辑上是否达成了一致。只要逻辑上达成了一致就可以commit，否则只能rollback。
    20.1 如何判断binlog和redolog是否达成了一致?
    当MySQL写完redolog并将它标记为prepare状态时,此时会在redolog中记录一个XID,它全局唯一的标识着这个事务。
    而当你设置`sync_binlog=1`时，做完了上面第一阶段写redolog后，mysql就会对应binlog并且会直接将其刷新到磁盘中;
    只要这个XID和redolog中记录的XID是一致的，MySQL就会认为binlog和redolog逻辑上一致。就上面的场景来说就会commit，而如果仅仅是rodolog中记录了XID，binlog中没有，MySQL就会RollBack;
21. redo log数据怎么刷到磁盘的，什么时候刷

    1.log buffer空间不足时
    2.事务提交时
    3.后台线程大约每秒刷新redo log到磁盘
    4.正常关闭服务器时
    5.checkpoint时
    mysql数据库采用WAL(write-ahead logging)，先写日志，再写磁盘。目的是为了减少磁盘写;
    也就是当数据库进行更新的时候，innodb引擎先把记录写到redo log，此时进入第一阶段prepare; 再写入binlog, 提交事务，commit;
    同时innodb引擎会在适当时候更新到磁盘。(当redo log空间不够时则先将redo log内容写到磁盘再继续工作)
    redo log:物理日志，记录每个数据页做什么操作, 固定大小4G,循环覆盖写;
22. redo log 和binlog有什么不同点/区别?
    有三点区别:
    1.redo log 是 InnoDB 引擎特有的；binlog 是 MySQL 的 Server 层实现的，所有引擎都可以使用。
    2.redo log 是物理日志，记录的是“在某个数据页上做了什么修改”；binlog 是逻辑日志，记录的是这个语句的原始逻辑sql”。
    3.redo log 是循环写的，空间固定会用完；binlog 是可以追加写入的,并不会覆盖以前的日志。
23. redo log是为了解决什么问题?
    为了解决性能问题:
    1.因为Innodb是以页为单位进行磁盘交互的，而一个事务很可能只修改一个数据页里面的几个字节，这个时候将完整的数据页刷到磁盘的话，太浪费资源了！
    2.一个事务可能涉及修改多个数据页，并且这些数据页在物理上并不连续，使用随机IO写入性能太差！
    所以用redo log记录事务对数据页做了哪些修改



# redis
1. redis数据类型:string,hash,list(异步队列),set,zset(延时队列,限流器):拿时间戳作为score,用zrangebyscore指令获取N秒之前的数据
1. redis缓存穿透:一直访问缓存和数据库没有的数据,解决办法:布隆过滤器;
   缓存击穿:单一一个key在高并发时失效, 导致全部访问到数据库了, 解决办法:1.热点数据永不失效;2.互斥锁, 没拿到锁就sleep一会;
   缓存雪崩:大量的key在同一时刻失效, 解决办法:过期时间后面加一个随机毫秒数;
1. redis使用场景
    1. 缓存
    2. 排行榜
    3. 计数器
    4. 分布式会话管理session
    5. 分布式锁
2. redis的优点是什么？  
   ①读写性能优异，②持久化，③数据类型丰富，④单线程，⑤数据自动过期，⑥发布订阅，⑦分布式
4. redis做分布式锁(redisson封装的RedLock)/zk分布式锁(使用curator封装的InterProcessMutex)
    - 如果是用的阿里云的产品,或者单机的redis: redission.lock();
        1. 默认30秒过期,会有一个异步线程(看门狗)每10秒会给锁续命;
        2. setnxex,是通过lua实现的,达到了原子性;
        3. finally.unlock通过lock生成的唯一线程名释放锁;
    - redis是集群:
      redlock红锁,或者通过zk集群做分布式锁(是基于临时节点的有序性和节点的监听机制完成)
      但是zk并发没有redis高, 因为:
        1. Zk基于Zab协议，需要一半的节点ACK，才算写入成功;Redis基于内存，只写Master就算成功;
        2. Zk由于有通知机制，获取锁的过程，添加一个lisener就可以了。避免了轮询，性能消耗较小,
           Redis并没有通知机制，它只能使用类似CAS的轮询方式去争抢锁，较多空转，会对客户端造成压力(cas会引发ABA问题)
        3. 在cap原理中,zk是强一致性的,倾向于cp,redis是ap
        4. 每次在创建锁和释放锁的过程中，都要动态创建、销毁临时节点来实现锁功能, 都只能在leader执行
5. 判断zset里的长度
    1. ZCARD key: 获取有序集合的成员数
    2. ZLEXCOUNT key min max: 在有序集合中计算指定字典区间内成员数量
       5.1 zset了解
    1. 通过ziplist压缩列表和skipList跳表实现;
    2. 满足以下条件用ziplist,否则用skiplist;
        - 服务器属性server.zset_max_ziplist_entries 的值大于 0
        - 新添加的元素的member长度小于服务器属性server.zset_max_ziplist_value的值（默认64）
    3. ziplist满足以下条件将转化为dict+skiplist
        - ziplist所保存的元素超过server.zset_max_ziplist_entries 的值（默认值为 128 ）
        - 新添加元素的 member 的长度大于 server.zset_max_ziplist_value 的值（默认值为 64）
    4. 为什么要转化为skiplist?
       因为ziplist 是一个紧挨着的存储空间，但是当容量大到一定程度,扩容就是影响他的性能的主要原因之一
    5. 为什么不用avl树或者红黑树?
        1. avl树进行范围查找的时候, 需要多n步中序遍历
        2. 平衡树的插入和删除可能引发子树的平衡,而skiplist只需要修改相邻节点的指针;
        3. 从内存占用上来说，skiplist比平衡树更灵活一些。一般来说，平衡树每个节点包含2个指针（分别指向左右子树），
           而skiplist每个节点包含的指针数目平均为1/(1-p),具体取决于参数p的大小。如果像Redis里的实现一样，取p=1/4，那么平均每个节点包含1.33个指针，比平衡树更有优势
    6. skiplist如何解决插入节点维持每层2:1的关系?
       每次插入一个数据时, 随机出一个层数,这样就不会影响其它节点的层数,插入操作只需要修改插入节点前后的指针，
       而不需要对很多节点都进行调整
    7. skiplist查找平均时间复杂度为O(logn)。
    8. ziplist 转换为dict+skiplist;
       其中, dict负责zscore这类查询score的方法;
6. redis过期时间到了后, 会自动删除吗?
   过期键的三种删除策略:redis使用的过期键值删除策略是惰性删除+定期删除
   （1）：立即删除(会短时间内占用大量cpu)。在设置键的过期时间时，创建一个回调事件，当过期时间达到时，由时间处理器自动执行键的删除操作。
   （2）：惰性删除(会在一段时间内浪费内存)。键过期了就过期了，不管。每次从dict字典中按key取值时，先检查此key是否已经过期，如果过期了就删除它，并返回nil，如果没过期，就返回键值。
   （3）：定时删除。每隔一段时间(默认100ms)，对expires字典进行检查(随机检测)，删除里面的过期键。
7. redis内存淘汰机制?  
   1）noeviction： 默认, 不删除，直接返回报错信息。
   2）allkeys-lru：移除最久未使用（最长时间未被使用）使用的key。推荐使用这种。
   3）volatile-lru：在设置了过期时间的key中，移除最久未使用的key。
   4）allkeys-random：随机移除某个key。
   5）volatile-random：在设置了过期时间的key中，随机移除某个key。
   6）volatile-ttl： 在设置了过期时间的key中，移除准备过期的key。
   7）allkeys-lfu：移除最近最少使用的key(使用次数最少)。
   8）volatile-lfu：在设置了过期时间的key中，移除最近最少使用的key。
   LRU: 使用频率最少; LFU: 使用次数最少
8. 如果用keys一次性查10万个key, 会有什么问题？
   redis是单线程的,keys指令会导致线程阻塞一段时间，线上服务会停顿，直到指令执行完毕，服务才能恢复;
   解决办法:用scan,scan指令可以无阻塞的提取出指定模式的key列表,但是有会重复, 需要在客户端去重; 每次查询出一万数据批量删除的时候, 需要注意scan并不一定每次都是删一万, 需要记录下上次删除后游标位置,下次删除将这个值传过去;
9. redis持久化: RDB做镜像全量持久化,AOF做增量持久化;一般是两者配合使用
   优先加载AOF文件;
10. rdb的原理是:通过fork创建子进程,并且cow(copy on write): 父子进程共享数据段，父进程继续提供读写服务，父进程新写的数据会复制到子进程中;
11. Redis主从复制的工作原理？
    1. 一个Slave实例，无论是第一次连接还是重连到Master，它都会发出一个SYNC命令；
    2. 当Master收到SYNC命令之后，会做两件事：
       (a) Master执行BGSAVE，即在后台保存数据到磁盘（rdb快照文件）；
       (b) Master同时将新收到的写入和修改数据集的命令存入缓冲区（非查询类）；
       3.当Master在后台把数据保存到快照文件完成之后，Master会把这个快照文件传送给Slave，而Slave则把内存清空后，加载该文件到内存中；
       4.而Master也会把此前收集到缓冲区中的命令，通过Reids命令协议形式转发给Slave，Slave执行这些命令，实现和Master的同步；
       5.Master/Slave此后会不断通过异步方式进行命令的同步，达到最终数据的同步一致；
12. redis-cluster了解(redis集群模式是什么样的架构?):
    Redis-Cluster采用无中心结构，每个节点保存数据和整个集群状态,每个节点都和其他所有节点连接。
    redis cluster 为了保证数据的高可用性，加入了主从模式，一个主节点对应一个或多个从节点，主节点提供数据存取，从节点则是从主节点拉取数据备份，当这个主节点挂掉后，就会有这个从节点选取一个来充当主节点，从而保证集群不会挂掉
    1. Redis Cluster中节点负责存储数据，记录集群状态，集群节点能自动发现其他节点，检测出节点的状态，并在需要时剔除故障节点，提升新的主节点
    2. Redis Cluster中所有节点通过PING-PONG机制彼此互联，使用一个二级制协议(Cluster Bus) 进行通信，优化传输速度和带宽。发现新的节点、发送PING包、特定情况下发送集群消息，集群连接能够发布与订阅消息。
    3. 客户端和集群中的节点直连，不需要中间的Proxy层。理论上而言，客户端可以自由地向集群中的所有节点发送请求，但是每次不需要连接集群中的所有节点，只需要连接集群中任何一个可用节点即可。
       当客户端发起请求后，接收到重定向（MOVED\ASK）错误，会自动重定向到其他节点，所以客户端无需保存集群状态。不过客户端可以缓存键值和节点之间的映射关系，这样能明显提高命令执行的效率。
    4. redis-cluster数据会丢失吗，为什么？
        - 异步复制导致的数据丢失：因为master -> slave的复制是异步的，所以可能有部分数据还没复制到slave，master就宕机了，此时这些部分数据就丢失了。
        - 脑裂导致的数据丢失：某个master所在机器突然脱离了正常的网络，跟其他slave机器不能连接，但是实际上master还运行着，此时哨兵可能就会认为master宕机了，然后开启选举，将其他slave切换成了master。这个时候，集群里就会有两个master，也就是所谓的脑裂。此时虽然某个slave被切换成了master，但是可能client还没来得及切换到新的master，还继续写向旧master的数据可能也丢失了。因此旧master再次恢复的时候，会被作为一个slave挂到新的master上去，自己的数据会清空，重新从新的master复制数据。
          怎么解决redis脑裂?
          min-slaves-to-write 3 (表示连接到master的最少slave数量)
          min-slaves-max-lag 10 (表示slave连接到master的最大延迟时间)
          不满足上面两个条件, master就会拒绝写请求, 即使发生了脑裂, 原先的master节点接收到客户端的写入请求也会拒绝;
    5. Redis集群的节点不可用后，在经过集群半数以上Master节点与故障节点通信超过cluster-node-timeout时间后，认为该节点故障，从而集群根据自动故障机制，将从节点提升为主节点。这时集群恢复可用。
    6. redis-cluster如果某一个主节点和他所有的从节点都下线的话(或者只有主节点, 没有从节点, 主节点宕机; 或者超过半数主节点宕机),redis集群就会停止工作了。
       redis集群不保证数据的强一致性，在特定的情况下，redis集群会丢失数据;
13. redis-cluster 数据分片
    Redis-Cluster在设计中没有使用一致性哈希, 而是使用数据分片（Sharding）引入哈希槽（hash slot）来实现；
    一个 Redis-Cluster包含16384（0~16383）个哈希槽，存储在Redis Cluster中的所有键都会被映射到这些slot中，集群中的每个键都属于这16384个哈希槽中的一个，
    集群使用公式slot=CRC16（key）/16384来计算key属于哪个槽，其中CRC16(key)是指循环校验码; 获取数据的时候, 也是通过这个算法得到槽点, 然后获取数据;
14. redis-cluster新增/删除主节点:
    从各个节点的前面各拿取一部分slot, 删除也是一样,先迁移数据, 再删除;
15. redis-cluster优缺点:
    高可用性，部分节点不可用时，集群仍可用。通过增加Slave做standby数据副本，能够实现故障自动failover，节点之间通过gossip协议交换状态信息，用投票机制完成Slave到Master的角色提升
16. redis cluster为什么没有使用一致性hash算法，而是使用了哈希槽预分片？
    缓存热点问题：一致性哈希算法在节点太少时，容易因为数据分布不均匀而造成缓存热点的问题。一致性哈希算法可能集中在某个hash区间内的值特别多，会导致大量的数据涌入同一个节点，造成master的热点问题(如同一时间20W的请求都在某个hash区间内)
17. Redis哨兵机制的原理?
    通过sentinel模式启动redis后，自动监控master/slave的运行状态，基本原理是：心跳机制+投票裁决。每个sentinel会向其它sentinal、master、slave定时发送消息，以确认对方是否活着，如果发现对方在指定时间内未回应，则暂时认为对方宕机。若哨兵群中的多数sentinel都报告某一master没响应，系统才认为该master真正宕机，通过Raft投票算法，从剩下的slave节点中，选一台提升为master，然后自动修改相关配置。
    17.1 选举算法有哪些? 了解raft投票算法吗?
    有三种:基于序号选举的算法(Bully算法---MongoDB集故障转移);多数派投票选举算法(Raft----k8s的选举,ZAB)
    - bully:长者为大, 在存活的节点中, 选择myid最大的节点作为主节点;选举过程会使用三种消息:1.Election消息,用于发起选举;2.Alive消息,对Election消息的应答;
      3.Victory消息，竞选成功的主节点向其他节点发送的宣誓主权的消息。
      选举过程:
      1.集群中每个节点判断自己的 ID 是否为当前活着的节点中 ID 最大的，如果是，则直接向其他节点发送 Victory 消息，宣誓自己的主权；
      2.如果自己不是当前活着的节点中 ID 最大的，则向比自己 ID 大的所有节点发送 Election 消息，并等待其他节点的回复；
      3.若在给定的时间范围内，本节点没有收到其他节点回复的 Alive 消息，则认为自己成为主节点，并向其他节点发送 Victory 消息，宣誓自己成为主节点；
      若接收到来自比自己 ID 大的节点的 Alive 消息，则等待其他节点发送 Victory 消息；
      4.若本节点收到比自己 ID 小的节点发送的 Election 消息，则回复一个 Alive 消息，告知其他节点，我比你大，重新选举。
      总结:bully优点是，选举速度快、算法复杂度低、简单易实现; 缺点, 当新节点加入,如果myid最大, 会切主;
    - raft: 民主投票,少数服从多数; 节点类型有三种;
      Leader，即主节点，同一时刻只有一个 Leader，负责协调和管理其他节点；
      Candidate，即候选者，每一个节点都可以成为 Candidate，节点在该角色下才可以被选为新的 Leader；
      Follower，Leader 的跟随者，不可以发起选举。
      选举过程:
      1.初始化时，所有节点均为 Follower 状态。
      2.开始选主时，所有节点的状态由 Follower 转化为 Candidate，并向其他节点发送选举请求。
      3.其他节点根据接收到的选举请求的先后顺序，回复是否同意成为主。这里需要注意的是，在每一轮选举中，一个节点只能投出一张票。
      4.若发起选举请求的节点获得超过一半的投票，则成为主节点，其状态转化为 Leader，其他节点的状态则由 Candidate 降为 Follower。Leader 节点与 Follower 节点之间会定期发送心跳包，以检测主节点是否活着。
      总结:Raft选举速度快、算法复杂度低、易于实现的优点；
      缺点是，它要求系统内每个节点都可以相互通信，且需要获得过半的投票数才能选主成功，因此通信量大。该算法选举稳定性比 Bully 算法好，这是因为当有新节点加入或节点故障恢复后，会触发选主，但不一定会真正切主，除非新节点或故障后恢复的节点获得投票数过半，才会导致切主。

17.2 redis的投票机制?
投票过程是集群中所有master参与,如果半数以上master节点与master节点通信超时(cluster-node-timeout),认为当前master节点挂掉;
选举的依据依次是：网络连接正常->5秒内回复过INFO命令->10*down-after-milliseconds内与主连接过的->从服务器优先级->复制偏移量->myid较小的。选出之后通过slaveif no ont将该从服务器升为新主服务器
18. redis分布式锁在集群架构上会有什么问题?怎么解决?
    redis集群环境涉及到异步主从复制问题;当主节点设置成功后会立即返回客户端成功, 这时会出现主从数据不一致问题;
    解决办法: 使用redlock红锁;官方默认5个节点, 超过半数成功获取锁才会返回客户端成功,
19. redlock获取锁过程
    1.获取当前时间戳
    2.client尝试按照顺序使用相同的key,value获取所有redis服务的锁，在获取锁的过程中的获取时间比锁过期时间短很多，这是为了不要过长时间等待已经关闭的redis服务。并且试着获取下一个redis实例。
    比如：TTL为5s,设置获取锁最多用1s，所以如果一秒内无法获取锁，就放弃获取这个锁，从而尝试获取下个锁
    3.client通过获取所有能获取的锁后的时间减去第一步的时间，这个时间差要小于TTL时间并且至少有3个redis实例成功获取锁，才算真正的获取锁成功
    4.如果成功获取锁，则锁的真正有效时间是 TTL减去第三步的时间差 的时间；比如：TTL 是5s,获取所有锁用了2s,则真正锁有效时间为3s(其实应该再减去时钟漂移);
    5.如果客户端由于某些原因获取锁失败，在所有redis实例上执行释放锁命令; 因为可能已经获取了小于3个锁，必须释放，否则影响其他client获取锁



# spring
1. spring的bean是单例的
2. spring boot和 spring cloud区别
   Spring boot 是 Spring 的一套快速配置脚手架,基于spring boot 快速开发微服务, 理念:默认大于配置;
   Spring Cloud是一个基于Spring Boot实现的云应用开发工具,是一个全局的服务治理框架, 离不开spring boot;
3. spring使用哪些设计模式?
   1.简单工厂(BeanFactory,由一个工厂类根据传入的参数，动态决定应该创建哪一个产品类)
   2.工厂方法(eg:sqlSessionFactoryBean,spring会在使用getBean(), 会自动调用该bean的getObject()方法，所以返回的不是factory这个bean，而是这个bean.getOjbect()方法的返回值)
   3.单例模式(依赖注入Bean,采用double-check-singleton)
   4.适配器模式
   5.装饰器模式
   6.代理模式
   7.观察者模式
   8.策略模式(针对不同的底层资源，Spring 将会提供不同的 Resource 实现类，分别负责不同的资源访问逻辑(UrlResource/FileSystemResource/InputStreamResource/ByteArrayResource))
   9.模版方法模式(代码复用，减少重复代码。除了子类要实现的特定方法，其他方法及方法调用顺序都在父类中预先写好了。eg:JDBCTemplate、RedisTemplate)
4. spring aop: 开发代码一般是自上而下的,但是有时候需要解决一些横切性的问题, 比如权限校验, 比如访问节点调用时间, 与我们业务代码无关, 解决办法就是apo;
   jdk动态代理, cglib动态代理; Spring没有默认代理这一说法, 取决于被代理对象是不是接口，是接口->jdk, 不是接口->cglib
   为什么这么设计?
5. spring ioc: 控制反转; ioc是一种思想, IoC 容器控制了对象;即由IoC容器帮对象找相应的依赖对象并注入，而不是由对象主动去找。
   对于spring框架来说，就是由spring来负责控制对象的生命周期和对象间的关系;
   传统应用程序是由我们自己在对象中主动控制去直接获取依赖对象;而反转则是由容器来帮忙创建及注入依赖对象;
6. spring DI: 依赖注入; 由容器动态的将某个依赖关系注入到组件之中, 提升组件重用的频率;
   IoC的一个重点是在系统运行中，动态的向某个对象提供它所需要的其他对象。这一点是通过DI（Dependency Injection，依赖注入）来实现的;
   DI的实现方式是反射, 它允许程序在运行的时候动态的生成对象、执行对象的方法、改变对象的属性;
   总结: ioc的好处: 没有ioc的时候, a要使用b对象, 必须要在a中new一个b, A就对B产生了依赖, 高度耦合;有了ioc之后, a和b对象的创建都由spring完成, a不需要关心怎么创建对象,什么时候创建好对象, 耦合度降低;
   7.spring事务:
   支持两种方式的事务管理:编程式事务管理(手动commit事务)/声明式事务管理(aop注解)
   5种隔离级别(default(使用mysql默认的隔离级别)/读未提交/读提交/不可重复读/串行化), 7种传播行为(required/requires_new/nested/mandatory/supports/not supported/never)
   Class A {
   @Transactional(propagation=propagation.xxx)
   public void aMethod {
   //do something
   B b = new B();
   b.bMethod();
   }
   }
   Class B {
   @Transactional(propagation=propagation.xxx)
   public void bMethod {
   //do something
   }
   }
    1. 当AB都是required,AB为同一个事务, 任何一处回滚, 整个事务均回滚;
    2. 当A是required,B是requires_new,b是新事务, a的回滚不会导致b回滚; 但是反过来, b的回滚不会导致a回滚;
    3. 当A是requires,B是nested(嵌套,一起提交,子事务先提交/父事务再提交), a的回滚会导致b回滚; 但是反过来, b的回滚不会导致a回滚;
    4. (使用的很少)抛异常: mandatory, a有事务, b就加入; a没有事务, b会抛异常;
       5/6/7. (没有事务): supports(如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行)/
       not supported(以非事务方式运行，如果当前存在事务，则把当前事务挂起)/never(以非事务方式运行，如果当前存在事务，则抛出异常);
       8.Spring事物传播级别NESTED和REQUIRES_NEW的区别
       注意:在同一个service类中定义的两个方法,内层REQUIRES_NEW并不会开启新的事务(因为调用的对象不是代理对象);
       在不同的service中定义的两个方法， 内层REQUIRES_NEW会开启新的事务，并且二者独立，事务回滚互不影响
       相同点:REQUIRES_NEW执行到B时，A事物被挂起，B会新开了一个事务进行执行，B发生异常后，B中的修改都会回滚，然后外部事物继续执行;
       NESTED执行到B时，会创建一个savePoint，如果B中执行失败，会将数据回滚到这个savePoint;
       不同点:REQUIRES_NEW如果B正常执行，则B中的数据在A提交之前已经完成提交，其他线程已经可见其修改，这就意味着可能有脏数据的产生；同时，如果接下来A的其他逻辑发生了异常，A回滚，但是B已经完成提交，不会回滚了
       NESTED如果B正常执行，此时B中的修改并不会立即提交，而是在A提交时一并提交，如果A回滚则B也会回滚,就可以避免脏数据产生
       REQUIRES_NEW使用场景:审计工作, 不管成功或者失败, 都需要记录下审计结果, 且审计步骤不影响主流程;
9. spring cloud熔断怎么感应到服务需要熔断, 怎么恢复熔断? 过程是怎样的?
   在Spring Cloud框架里，熔断机制通过Hystrix实现;Hystrix会监控微服务间调用的状况，当失败的调用到一定阈值，默认是5秒内20次调用失败，就会启动熔断机制,
   熔断后回调一个相应的本地降级处理方法, 从而实现服务降级;
   熔断机制的注解是@HystrixCommand, @HystrixCommand仅当类的注解为@Service或@Component时才会发挥作用


10. 微服务之间的调用可以通过两种方式，一个是RestTemplate，另一个是Feign, 两种方式有什么区别?
    默认情况下FeignClient中默认情况下是禁用Hystrix的

# apollo
1. apollo能做到端口的热加载吗?
   redis端口可以吗?
   redis连接池加载过程?
2. apollo主要通过长连接更新配置,另外还有一个长轮询(默认5分钟),防止推送机制失效导致配置不更新
3. 为什么不使用消息系统？太复杂,没必要引入一个mq
   为什么不用 TCP 长连接？对网络环境要求高，容易推送失败, 且有双写问题。
   为什么使用 HTTP 长轮询？性能足够，结合 Servlet3 的异步特性，能够维持万级连接（一个客户端只有一个长连接）。直接使用 Servlet 的 HTTP 协议，比单独用 TCP 连接方便。HTTP 请求/响应模式(hold住30秒)，保证了不会出现双写的情况。最主要还是简单，性能暂时不是瓶颈。(能保证1万个客户端的连接)
4. apollo和spring cloud config比较? apollo优缺点
    1. 有配置界面, 且能方便看到有哪些客户端在使用;
    2. 有版本控制,支持回滚;
    3. 支持灰度发布;
    4. 权限力度更细: 支持修改权限和发布权限分离


5. 半年内有学习过新技术吗?
   seata, 和sentinel
6. 做了比较有成就感的项目, 介绍下?
7. 在做项目过程中, 有遇到什么困难吗? 难解决的bug? 最后你是怎么解决的?



# zk
1. zk的应用场景:
    1. 分布式注册中心, 对服务进行管理;
    2. 分布式协调调度, 通过watch机制, a系统某个节点状态发生改变，b系统可以得到通知;
    3. 分布式锁，利用Zookeeper创建临时顺序节点的特性
1. 描述zookeeper集群架构?
    1. zookeeper集群是一主多从的模型，节点分成三种角色：leader、follower和observer。leader负责写、follower和observer负责读
    2. 采用zab原子广播协议来实现数据最终一致性;
       zab协议特点:
       zk采用zab协议(Zookeeper Atomic Broadcast)进行选举, :
        - follower节点上所有的写请求都转发给leader;
        - 写操作严格有序;
        - ZooKeeper使用自定义的两阶段提交协议来保证各个节点的事务一致性;(改编后:只需要超过半数的参与者回复yes)  
          总结: ZAB 协议确保那些已经在 Leader 提交的事务最终会被所有服务器提交。
          ZAB 协议确保丢弃那些只在 Leader 提出/复制，但没有提交的事务。
2. zk的leader挂掉了怎么办（单点故障）
    2. zookeeper集群的状态分为两种：正常状态和异常状态。也就是有leader（能提供服务）和没有leader（进入选举）
        - 广播模式:就是指zookeeper正常工作的模式
            1. leader从客户端或者follower那里收到一个写请求, leader生成一个新的事务并为这个事务生成一个唯一的Zxid，
               然后把这个proposal放入到一个FIFO的队列中，按照FIFO的策略发送给所有的Follower;
            2. follower节点将收到的提议以事务日志的形式写入到本地磁盘中, 写入成功后发送ack给leader
            3. leader收到超过半数的follower的ack之后，就会发送commit命令给所有follower, 告诉他们可以提交刚才的proposal;
            4. 当follower收到commit请求时，会判断该事务的Zxid是不是比历史队列中的任何事务的Zxid都小，如果是则commit，如果不是则等待比它更小的事务的commit
        - 恢复模式:当leader故障之后, follower节点都会把自己的状态修改为LOOKING状态，然后重新进入选举流程;

            2. 正常模式下的几个步骤，每个步骤都有可能因为leader故障而中断。但是恢复过程只与leader有没有commit有关;首先看第一个步骤，把事务发送出去。
            3. 如果事务没有发出去，所有follower都没有收到这个事务，leader故障了。所有的follower都不知道这个事务的存在，根据心跳检测机制，follower发现leader故障，重新选出一个leader。会根据每个节点Zxid来选择，谁的Zxid最大，表示谁的数据最新，自然会被选举成新的leader。如果Zxid都一样，表示在follower故障之前，所有的follower节点数据完全一致，此时选择myid最大的节点成为新的leader，因为有一个固定的选举标准会加快选举流程。新的leader选出来之后，所有节点的数据本身就是一致的，此时就可以对外提供服务。
               假设新的leader选出来之后，原来的leader又恢复了，此时原来的leader会自动成为follower，之前的事务即使重新发送给新的leader，因为新的leader已经开启了新的纪元，而原先的leader中Zxid还是旧的纪元，自然会被丢弃。并且该节点的Zxid也会更新成新的纪元。
               纪元的意思就是标识当前leader是第几任leader，相当于改朝换代时候的年号。
            4. 如果在leader故障之前已经commit，zookeeper依然会根据Zxid或者myid选出数据最新的那个follower作为新的leader。新leader与follower建立FIFO的队列， 先将自身有而follower缺失的事务发送给它，再将这些事务的commit命令发送给 follower，这便保证了所有的follower都保存了所有的事务、所有的follower都处理了所有的消息。
               那选举之后又是怎样进行数据同步的？
        1. 缓存队列中min zxid < 当非leader服务器最后处理的zxid(PeerLastZxid) < 缓存队列max zxid;
           就说明Learner服务器还没有完全同步最新的数据, 这时候进行diff差异化同步(将PeerLastZxid到max zxid之间的proposal发送给新leader);
        2. 如果原leader生成的proposal还没发送出去就宕机了, 则TRUNC回滚, 丢弃掉这条数据, 再执行diff差异化同步;
        3. 全量同步 SNAP同步 在两种情况下会发生：
           1.非leader上的PeerLastZxid小于minCommittedLog
           2.Leader服务器上没有提议缓存队列，并且PeerLastZxid不等于Leader的最大ZXID
           这两种场景下，Leader将会发送SNAP命令，把全量的数据都发送给Learner进行同步。
3. 两阶段提交是用来解决分布式事务的,操作步骤?
    1. 协调者询问所有的参与者是不是可以提交了
    2. 参与者回复yes or no
    3. 协调者收到所有的yes之后执行commit否则执行rollback
    4. 参与者执行完成后回复ACK
4. 有了解过Paxos算法吗?[ˈpæksoʊs]
   共识算法的一种, 用来保证分布式数据一致性的;原理是两阶段提交的变种, 可以解决拜占庭将军问题;
   共识算法有:pow,paxos,raft等等;
    1. (无敌)Paxos算法是基于消息传递且具有高度容错特性的一致性算法，是目前公认的解决分布式一致性问题最有效的算法之一。
    2. 三种角色Proposer,Acceptor,Learners
    3. zab是paxos的一种简化版本
5. zk所以没有采用Paxos是因为Paxos保证不了业务逻辑顺序(eg:a导致了b,但是消息一起发送的,需要确保a必须在b之前执行)
6. TCP具体是通过怎样的方式来保证数据的顺序化传输呢？
    1. 主机每次发送数据时，TCP就给每个数据包分配一个序列号,并且等待一段时间, 等分机的ack确认;
    2. 如果发送主机在一个特定时间内 没有收到接收主机的确认，则重发。
    3. 分机利用序列号对接收的数据进行确认，以便检测对方发送的数据是否有丢失或者乱序等，
    4. 分机一旦收到已经顺序化的数据，它就将这些数据按正确的顺序重组成数据流并传递到高层进行处理。
7. 你们系统是怎么实现分布式事务
    1. 2PC----XA两阶段提交(不可取);
        1. XA 它包含两个部分：事务管理器和本地资源管理器;
        2. 是一种尽量保证强一致性的分布式事务，是同步阻塞的，而同步阻塞就会出现 资源锁定时间长问题，总体而言效率低，并且存在单点故障问题，在极端条件下存在数据不一致的风险;
           所以不适合解决微服务事务问题
        3. 问题其实就出在每个参与者自身的状态只有自己和协调者知道，因此新协调者无法通过在场的参与者的状态推断出挂了的参与者是什么情况。
    2. TCC---基于业务的分布式事务;
       难点在于业务上的定义，对于每一个操作你都需要定义三个动作分别对应Try - Confirm - Cancel
       因此 TCC 对业务的侵入较大和业务紧耦合，需要根据特定的场景和业务逻辑来设计相应的操作。
       TCC可以跨数据库、跨不同的业务系统来实现事务
    3. 本地消息表---最终一致性，容忍了数据暂时不一致的情况
       将业务的执行和将消息放入消息表中的操作放在同一个事务中，这样就能保证消息放入本地表中业务肯定是执行成功的。
       然后再去调用下一个操作，如果下一个操作调用成功了, 消息表的消息状态直接改成已成功。
       如果调用失败，写一个后台任务定时去读取本地消息表，筛选出还未成功的消息再调用对应的服务，服务更新成功了再变更消息的状态。
    4. RocketMQ的消息事务
    5. 使用阿里的开源框架seata
       基于AT模式
    2. 3pc----三阶段提交;
       相比于 2PC 它在参与者中也引入了超时机制，并且新增了一个阶段使得参与者可以利用这一个阶段统一各自的状态。
       3PC 包含了三个阶段，分别是准备阶段、预提交阶段和提交阶段
       总结:3PC 相对于 2PC,做了一定的改进：引入了参与者超时机制，并且增加了预提交阶段使得故障恢复之后协调者的决策复杂度降低，
       但整体的交互过程更长了，性能有所下降，并且还是会存在数据不一致问题。因此一般都需要有定时扫描补偿机制
8. zk为什么叫cp?
   因为zk的leader挂掉后再进行选举的过程中, 整个zk集群服务不可用;
9. 哪个地方体现出cp了?
    1. Follower通过队列和zxid等顺序标识保证请求的顺序处理，不一致的follower重新同步Leader
    2. zookeeper在进行数据的同步时，不允许客户端读写，程序堵塞。
    3. 原子性：更新操作要么成功要么失败，没有中间状态
10. zk的leader挂掉了, 整个服务不可用, Dubbo为什么用zk做注册中心?
    调用服务不会报错, 因为启动dubbo时，消费者会从zk拉取注册的生产者的地址接口等数据，缓存在本地;
    每次调用时，按照本地存储的地址进行调用, 但是无法注册新服务
    zk宕掉后, 生产者和消费者会不停的重连zk
    10.1 Dubbo在安全机制方面是如何解决的?
    Dubbo通过Token令牌防止用户绕过注册中心直连,然后在注册中心上管理授权.Dubbo还提供服务黑白名单,来控制服务所允许的调用方
11. zookeeper 如何保证半数提交后剩下的节点上最新的数据呢？
    剩下的节点，会进行版本比对，发现版本不一致的话，会更新节点的数据。
12. Zookeeper中数据存储于内存之中，这个数据节点就叫做Znode，他是一个树形结构
    Znode分为持久节点、临时节点、顺序节点
13. zk怎么实现分布式锁?如何加锁, 如何释放, 下一个线程如何拿到?怎么保证只有一个线程抢到锁设置节点?
    基于节点去实现各种分布式锁;创建顺序的临时节点,只有第一个节点能拿到锁,能避免羊群效应, 每个节点都只需要监听前一个节点,
    释放锁就是删除节点,当监听到前一个节点被删除后, 按照顺序下一个节点获取到锁,通过zk的watch机制, 监听前一个节点是否存在, 存在就await;
    一旦zk挂掉, 那这台zk创建的临时节点自动删除掉, 从而避免死锁;
    zk的分布式锁是公平的, 且是可重入的, 是clh锁的变种, 只不过释放锁后节点通知不是本地，而是利用ZooKeeper的Watch机制远程通知
14. zk作注册中心时, 里面存了哪些数据, 里面的数据结构是怎么样的? 让你来实现注册中心, 里面会存哪些数据?
    zk存的是节点信息;节点类型分4类:持久化节点,持久化顺序节点,临时节点,临时顺序节点;
    zk进行服务注册时,实际创建了一个临时Znode节点,里面存储服务ip,port,服务名称,调用协议;
    zk数据结构是类似文件系统的树形结构, 节点类型有持久节点和临时节点



# mq(kafka,rocketmq)
1. mq使用的场景有哪些?
    1. 异步处理(记录用户操作日志)
    2. 解耦
    3. 流量削峰
    4. es日志采集
    5. 消息通讯
2. Broker:每个broker对应一个kafka实例(服务器);
   Topic：消息的主题，可以理解为消息的分类，kafka的数据就保存在topic。在每个broker上都可以创建多个topic;
   Partition：Topic的分区，每个topic可以有多个分区，分区的作用是做负载，提高kafka的吞吐量。同一个topic在不同的分区的数据是不重复的，
   partition的表现形式就是一个一个的文件夹
   Replication:每一个分区都有多个副本，副本的作用是做备胎。当主分区（Leader）故障的时候会选择一个备胎（Follower）上位，成为Leader。
   在kafka中默认副本的最大数量是10个，且副本的数量不能大于Broker的数量。
   Consumer Group：将多个消费组组成一个消费者组，在kafka的设计中同一个分区的数据只能被消费者组中的某一个消费者消费。
   同一个消费者组的消费者可以消费同一个topic的不同分区的数据，这也是为了提高kafka的吞吐量！
3. kafka默认是自动提交offset;
4. Kafka 使用 Compact 策略来删除offset topic中的过期消息，避免该topic无限期膨胀.
5. 生产出现kafka日志占用过多磁盘;
   一般是因为log clear线程宕掉了;
6. kafka是不是离不开zk?
   是的, kafka集群依赖zookeeper来保存集群的的元数据信息(eg:topic,borker实例列表)，来保证系统的可用性
   同时, zk还用来管理分区与消费者的关系:
7. kafka副本同步策略?
   kafka副本同步策略有两种:1.半数以上完成同步,就发ack;2.全部同步完成,发ack;
   kafka默认选择第二种方式全同步;因为半数同步,n个节点故障时, 需要2n+1个副本, 会造成大量数据冗余;全同步时只需要n+1个副本;
   原因:举个例子: 全同步时, 集群5个节点, 我们容忍4个节点宕机, 那我们的副本数就至少要有5个, 这样, 当其他4个副本宕机时, 剩下的这个节点就有完整的数据可以成为leader继续工作;
   半数同步时, 集群5个节点, 半数同步机制, 会导致5个节点,只要3个节点返回ack, broker就会发送ack, 此时也是4个节点故障, 只有三个正确的副本, 宕机4台, 就无法保证我们数据不丢失;
   也就是说至少需要9个副本, 这样才能保证半数时有5个正确的副本;
8. kafka怎么保证消息不丢失?
   1.生产者: 同步方式: 调用send(msg,callback)能避免由于网络波动导致消息发送失败;
   异步方式: 配置block.on.buffer.full = true, 异步方式缓冲区满了，就阻塞在那，等着缓冲区可用，不能清空缓冲区(一旦清空，数据就丢失了)
   2.消费者: kafka通过先消费消息, 后更新offset来保证消息不丢失;(还需要考虑only-once的问题)
   配置如下:
   1.生产者---使用send(msg, callback)带有回调的send方法;
   2.生产者---设置acks = all, 表明所有Broker都要接收到消息，该消息才算是“已提交”;
   3.生产者---设置retries(重试次数)为一个较大的值,当出现网络抖动时,生产者重试发送消息，尽量避免消息丢失;同时配置retry.backoff.ms参数表示消息生产超时失败后重试的间隔时间。
   4.Broker端的参数----unclean.leader.election.enable = false, 如果一个Broker落后原先的Leader太多，那么他将不会被选举为leader
   5.Broker端的参数----设置replication.factor >= 3。副本数,保证每个 分区(partition) 至少有 3 个副本, 最好设置为 min.insync.replicas + 1;
   6.Broker端的参数----设置min.insync.replicas > 1; 控制消息至少要被写入到多少个副本才算是“已提交”
   7.消费者---手动提交offset, 确保消息消费完成再提交。Consumer端有个参数enable.auto.commit，最好设置成false，并自己来处理offset的提交更新。
   8.1 多线程场景怎么处理?
   多线程异步消费时, 会出现a更新失败, b更新成功, 如果是自动commit offset, a这条消息其实是丢失了;
   解决办法: 手动提交offset, 但是也可能会出现消息被多次消费的问题;
9. kafka怎么保证不重复消费?(如何保证消息消费的幂等性?)
   一句话:通过保证消息消费的幂等性来保证不重复消费;
   1.消息使用唯一id标示, 成功消费后在redis中标记已处理过;
   2.数据库建立去重表, 通过唯一键约束保证消息不重复消费;
   10.kafka怎么保证消息的顺序性? 如果实现又要使用多线程, 又要保证消息的顺序性?
   首先: 全局有序:如果要保证消息的全局有序，首先只能由一个生产者往Topic发送消息，并且一个Topic内部只能有一个分区。消费者也必须是单线程消费这个分区。这样的消息就是全局有序的！
   不过一般情况下我们都不需要全局有序，即使是同步MySQL Binlog也只需要保证单表消息有序即可。
   生产者保证消息顺序生产:
   解决办法1: 同步发送模式：发出消息后，必须阻塞等待收到通知后，才发送下一条消息
   解决办法2: 异步发送模式：一直往缓冲区写，然后一把写到队列中去
   各有利弊: 同步发送模式虽然吞吐量小，但是发一条收到确认后再发下一条，既能保证不丢失消息，又能保证顺序
   一个分区不能被一个消费者组中的多个消费者消费;

   已知kafka在消费者多线程消费情况下,无法保证消息顺序性消费问题?
   解决办法: 生产者将需要排序的一串消息(abc)全部都加一个唯一标示key, 写N个queue, 将key相同的消息放入同一个queue;然后对于 N 个线程，每个线程分别消费一个内存 queue
11. kafka消息堆积的时候怎么处理?
    1.增加分区和消费者(消费数 = 分区数);因为:kafka中一个消费组内的一个消费者线程只能对应一个主题内的一个分区进行消费，
    也就是说如果你单独增加消费者线程对消息堆积问题是没有任何效果的，只会浪费多余的消费者线程;
    只有在增加了分区，多余的消费者线程才能进行工作，否则空闲。
    2.单个消费者情况下, 使用线程池;(需要注意消息丢失问题)
    3.增加consumer每批次拉取消息数量, 默认是500(修改这个参数必须要注意, 不要让kafka重平衡了)
    注意: max.poll.records 表示每次默认拉取消息条数，默认值为 500。
    假设每条消息消费所需时间是200ms; 则200*500=100000ms 必须小于max.poll.interval.ms默认值是300000;
    一旦超过了 max.poll.interval.ms 所设置的时间, 就会被消费组所在的 coordinator 剔除掉, 从而导致重平衡;
12. kafka发生重平衡有哪些情况?
    1. 消费组成员发生变更，有新消费者加入或者离开，或者有消费者崩溃；
    2. 消费组订阅的主题数量发生变更；
    3. 消费组订阅的分区数发生变更。
       重平衡跟消费组紧密相关，就是保证分区公平分配且只能被 同一个消费组的一个消费者订阅
        1. 同一个消费组，一个分区只能被一个消费者订阅消费，但一个消费者可订阅多个分区，也即是每条消息只会被同一个消费组的某一个消费者消费，确保不会被重复消费；
        2. 一个分区可被不同消费组订阅，这里有种特殊情况，加入每个消费组只有一个消费者，这样分区就会广播到所有消费者上，实现广播模式消费。
           Kafka 重平衡过程中是不能消费的，会导致消费组处于类似 stop the world 的状态下，重平衡过程中也不能提交位移，这会导致消息重复消费从而使得消费组的消费速度下降，导致消息堆积;




# 算法
1. 常用限流算法有哪些
    1. 滑动窗口(计数器):适用于自己系统的
    2. 漏桶:适用于调用别的系统
    3. 令牌桶:
        - 所有的请求在处理之前都需要拿到一个可用的令牌才会被处理；
        - 根据限流大小，设置按照一定的速率往桶里添加令牌；
        - 桶设置最大的放置令牌限制，当桶满时、新添加的令牌就被丢弃或者拒绝；
        - 请求达到后首先要获取令牌桶中的令牌，拿着令牌才可以进行其他的业务逻辑，处理完业务逻辑之后，将令牌直接删除；
        - 令牌桶有最低限额，当桶中的令牌达到最低限额的时候，请求处理完之后将不会删除令牌，以此保证足够的限流；
2. 找出二叉树任意两个节点的最近的公共父节点
   思路:DFS,深度优先搜索
   if (cur == null || cur == p || cur == q)
   return cur;
   TreeNode left = lowestCommonAncestor(cur.left, p, q);
   TreeNode right = lowestCommonAncestor(cur.right, p, q);
   //如果left为空，说明这两个节点在cur结点的右子树上，我们只需要返回右子树查找的结果即可
   if (left == null)
   return right;
   //同上
   if (right == null)
   return left;
   //如果left和right都不为空，说明这两个节点一个在cur的左子树上一个在cur的右子树上，
   //我们只需要返回cur结点即可。
   return cur;




# 分库分表(垂直分库/表, 水平分库分表)
1. 分库分表/读写分离, 目前有两种方式, 个人推荐使用shading-sphere;
   但是我老大当时考虑到两个方面从而决定用mycat,
   1.mycat是一个中间件, 不需要改代码, 解耦, 初级程序员会写普通sql就行;
   2.mycat可以实现实时新增分片节点;
   a: 基于proxy代理层(mycat,atlas,shading-proxy),
   b: 基于jdbc应用层(apache的shading-sphere,未开源的淘宝的TDDL),是jar,所以只支持java;
   proxy的性能没有jdbc的好, 前者需要4步(多进行了两次网络通讯), 后面只需要2步返回(直接在内存中操作)
   c. proxy和jdbc都支持多种数据库, postgresql/oracle/sql server/还有mangodb
2. mycat优点:
    1. mycat是一个中间件的应用, 不需要修改代码, 解耦, 降低开发难度;
    2. mycat分片算法有10种之多(分别是: 1.枚举 2.固定分片hash算法 3.范围约定: eg:id从1-x放在a库
       4.某个字段求膜法(平均分), 5. 按照日期分
       后面的不常见: 6.通配取模 7. ASCII码求模通配 8. 编程指定 9. 字符串拆分hash解析 10. 一致性hash)
       一致性hash: 主要是用于解决分布式缓存的问题;
       很好的解决了: 容错性(某台redis宕机), 扩展性(新增redis节点)

   mycat缺点:
   1. 跨库查询(目前只支持两个分库的left join)
   2. 分布式事务(目前只支持XA二阶段提交)
   3. 需要服务器, 增加了维护成本;
   4. 性能不如jdbc增强模式, 多了两次网络通讯;

3. mycat分布式全局唯一id(mycat有2种方式:
   1. 本地文件(重启会出问题, 在2.0版本已修复)
   2. 在一个数据库表里维护每个表的最大id
   3. 本地时间戳
   4. 基于zk实现的递增序列号(类似雪花算法)
4. 雪花算法: 同一秒内生成的id数量: 26万, 分为4部分,
   1. 1位, 默认是0,代表正数
   2. 41位, 时间戳
   3. 10位, 记录不同服务器id;
   4. 12位, 记录毫秒内产生的id;
   源码: 1.synchronize加锁生成方法;2.序列号达到最大,就获取下一毫秒的
   疑问:
   一致性hash算法:



# 综合问题
1. 项目中成就感的地方?
   我觉得很多同事都在用我引入的中间件, 就是我最大的成就;
2. 遇到过哪些难以解决的bug, 最后怎么解决的?
    1. logback1.2.3的一个bug: 当天文件超过三位数的时候, 无法限制住总文件大小;
       解决办法: 基于源码修复这个问题; (没有升级到最新版1.2.6, 是担心新版可能会引发新的问题;)
    2. 刚接触mycat时, 分布式全局id用的是数据库配置一个表维护全局id的方式, 在压测的过程中, 发现性能特别差;
       解决办法: down下mycat源码, 将mycat配置中的myid拆分为workId和machineId, 写一个和我们公司业务公用的雪花算法;
    3. redis与数据库数据不一致  
       延迟双删, 内存队列(不推荐)  
       我的解决办法是通过canal中间件解析mysql二进制,然后采用kafka做异步消息;
       模拟的mysql主备集群一样
       其中:我要解决的问题就是mq顺序性问题: 单topic单分区，单topic多分区、多topic单分区、多topic多分区
       canal.mq.dynamicTopic，主要控制是否是单topic还是多topic
       canal.mq.partitionsNum 要控制是否多分区
       canal.mq.partitionHash 控制分区的partition的路由计算
        1. 单topic单分区，可以严格保证和binlog一样的顺序性，缺点就是性能比较慢，单分区的性能写入大概在5k-5w的TPS
        2. 多topic单分区，可以保证表级别的顺序性，一张表或者一个库的所有数据都写入到一个topic的单分区中，可以保证有序性，针对热点表也存在写入分区的性能问题
        3. 单/多topic + 多分区, 可能会有数据消费顺序问题, 可以自定义table模式,或者将id hash后分发到不同分区(可以做到同一个id的delete和insert顺序不会变)
           2.1 在做项目过程中遇到哪些问题?
           1.频繁改需求
           1.规范开发流程,非紧急需求放到下一期迭代中做;
           2.和产品的沟通以需求文档为主，减少消息传递过程中信息的丢失;
           3.需求出来后，和相关开发人员开一个技术实现会，之后按照定下来的技术实现方案开发就行了
           2.遇到紧急需求
           解决办法：首先分析需求合理性，思考实现方案，和产品沟通，如果先用性能较差的方案先实现功能行不行?
           如果产品同意，那和组员一起加个班把需求做完, 后续的迭代中再用更好的方案优化性能
3. 异常处理规范, 捕获异常?
   1.自定义ErrorHandler用来解析各种code和不同ExceptionHandler的异常;
   2.由于feign之间抛异常不是200(应该是500), 默认会进入到ErrorDecoder中,因此要解析异常信息就需要重写ErrorDecoder,根据response.body()反序列化为自定义的ResultData类,最后抛出我们处理过的RuntimeException;
4. 公司qps多少?
   经过压测, 我们服务器是4C8G的配置;每秒能抗住200并发, 一分钟12000;一小时72万, 一天(6-7小时):432万;一个月:8640万;
   真实:每秒50,一分钟3000,一小时:18万,一天100万;一个月两千万;
5. 设计一个高并发系统, 需要考虑哪些方面?
   1.页面静态化
   2.按钮置灰, 活动开始前不允许点击
   3.服务拆分, 抢购服务/库存服务/订单服务等等, 秒杀系统挂了不会影响到其他系统
   4.秒杀链接加盐, 防止url明文暴露
   5.限流+降级+熔断
   限流分为: 对同一个ip限流,对接口限流,或者Sentinel/Hystrix进行限流
   降级:就是秒杀服务有问题了，就降级处理，不要影响别的服务
   怎么降级, 降级的方式, 熔断是降级的一种方式
   6.分布式锁
   7.mq异步队列
6. 怎么判断长连接是否断开?怎么恢复的?
   项目中使用socket长连接, 自己实现一个心跳检测, 一段时间内未收到自定义的心跳包则标记为已断开;
   如果发现断开, 则将监听去掉, 重新设置监听和重连;重连时, 每次的重连时间都会翻倍, 直接1分钟;
7. 设计两个线程做递增操作?同时保证线程安全?
   1.使用atomicInteger(里面方法有:addAndGet(先加后返回新值);crementAndGet(等同于++1));实现原理是volatile+cas, 实现无阻塞的线程安全;
   2.使用synchronize(底层是通过monitor监视器锁来实现的, 反编译之后, 过程如下:
   当monitor的进入数为0, 则执行monitorenter进入monitor,将进入数设置为1;
   当重入时,monitor+1; 当monitor不为0时, 其他线程不可进入; 结束时执行monitorexit, monitor-1)
   3.volatile+cas
   4.使用lock接口的相关实现(三个实现:ReentrantLock/ReentrantReadWriteLock/StampedLock)
8. 如果你们系统遇到内存泄露了(oom), 怎么排查问题?
   内存泄漏指系统资源（各方面的资源，堆、栈、线程等）在错误使用的情况下，导致使用完毕的资源无法回收（或没有回收），从而导致新的资源分配请求无法完成
   常见的oom是:老年代堆空间被占满, 所有堆空间都被无法回收的垃圾对象占满，虚拟机无法再在分配新空间。

