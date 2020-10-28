学习笔记
GC日志分析
-Xloggc:gc.demo.log（导出日志）-XX:+PrintGCDateStamps（打印时间戳）
默认情况
[GC (Allocation Failure)
[PSYoungGen: 65536K->10742K(76288K)] 65536K->21628K(251392K), 0.0040638 secs]
[Times: user=0.03 sys=0.06, real=0.00 secs]
GC 表示年轻代GC (minor GC)

Allocation Failure 表示GC原因，分配空间失败（空间不足）

PSYoungGen: 65536K->10742K(76288K) 年轻代GC前后大小，括号内为年轻代大小

65536K->21628K(251392K) 整个堆GC前后大小，括号内为整个堆大小

user 用户线程时间

sys 系统线程时间

real 真正GC的时间

[Full GC (Ergonomics)
[PSYoungGen: 10751K->0K(272896K)] 
[ParOldGen: 120961K->121417K(262144K)] 131712K->121417K(535040K), 
[Metaspace: 2637K->2637K(1056768K)], 0.0150500 secs] 
[Times: user=0.00 sys=0.00, real=0.02 secs]
Full GC

默认使用的是ParallelGC

串行GC
-XX:+UseSerialGC

[GC (Allocation Failure) 
[DefNew: 139776K->17472K(157248K), 0.0210434 secs] 
139776K->47171K(506816K), 0.0213852 secs] 
[Times: user=0.00 sys=0.02, real=0.02 secs]
DefNew 表示年轻代

设置-Xmx2048k以下都会提示 Error occurred during initialization of VM GC triggered before VM initialization completed. Try increasing NewSize, current value 640K. 计算 640 * 3 = 1920 < 2048 堆空间需要大于2048K，2M

并行GC
-XX:+UseParallelGC

CMS GC 
Initial Mark初始标记
伴随STW暂停，第一个阶段暂停时间很短。初始标记的目标是标记所有的根对象，包括根对象直接引用的对象，以及被年轻代中所有存活对象所引用的对象（rset）（老年代单独回收）。

Concurrent Mark并发标记
在此阶段，CMS GC遍历老年代，标记所有的存活对象，从前一阶段“Initial Mark”找到的根对象开始算起。“并发标记”阶段，就是与应用程序同时进行，不用暂停的阶段。

Concurrent Preclean并发预清理
此阶段同样是与应用程序并发执行的，不需要停止应用线程。因为前一阶段（并发标记）与程序并发执行，可能有一些引用关系已经发生了改变。如果在并发标记过程中引用关系发生了变化，JVM会通过“Card”卡片的方式将发生了改变的区域标记为“脏”区，这就是所谓的卡片标记(Card Marking)。

Final Remark最终标记
最终标记阶段是此次GC事件中的第二次（也是最后一次）STW暂停。本阶段的目标是完成老年代中所有存活对象的标记。因为之前的预清理阶段是并发执行的，有可能GC线程跟不上应用程序的修改速度。所以需要一次STW暂停来处理各种复杂的情况。通常CMS会尝试在年轻代尽可能空的情况下执行Final Remark阶段，以免连续触发多次STW事件。

Concurrent Sweep并发清除
此阶段与应用程序并发执行，不需要STW停顿。JVM在此阶段删除不再使用的对象，并回收它们占用的内存空间。

Concurrent Reset并发重置
此阶段与应用程序并发执行，重置CMS算法相关的内部数据，为下一次GC循环做准备。

CMS垃圾收集器在减少停顿时间上做了很多复杂而有用的工作，用于垃圾回收的并发线程执行的同时，并不需要暂停应用线程。CMS也有一些缺点，最大的问题就是老年代内存碎片问题（因为没有压缩整理），在某些情况下GC会造成不可预测的暂停时间，特别是堆内存较大的情况下。