学习笔记

1.字节码 
Java Bytecode由单字节byte的指令组成，理论上最多支持256个操作码
2.类加载

加载：找Class文件，获取二进制classfile格式的字节流
验证：验证格式、依赖
准备：静态字段、方法表
解析：解析常量池中的符号引用转为直接引用，包括类或接口的解析、字段解析、类方法解析、接 口方法解析
初始化：执行构造函数、执行静态代码块、静态变量赋初值
使用
卸载
3.类加载器

启动类加载器BootstrapClassLoader
扩展类加载器ExtClassLoader
应用类加载器AppClassLoader
4.类加载机制
双亲委派
负责依赖
缓存加载
5.内存模型

Stack 栈

a.线程栈
b.虚拟机栈
c.本地方法栈

Heap 堆
a.年轻代
b.老年代

Non-Heap 非堆

a.元数据区 Metaspace
b.Code Cache
