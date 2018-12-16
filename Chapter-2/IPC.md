
# IPC  

#### Android中多进程的概念以及多进程开发模式中常见的注意事项  

1. IPC：进程间通信或者说跨进程通信，指的是两个或多个进程间进行数据交换的过程．

进程: 一般指一个执行单元，在PC或移动设备上指一个程序或一个应用。一个进程可以包含多个线程    
线程: CPU调度的最小单元，同时线程也是一种有限的系统资源    
进程和线程是包含和被包含的关系.  
最简单的情况 ，一个进程只有一个线程 即主线程。在Android中主线程也叫UI线程,在UI线程才可以操作界面元素。  

2. 各个系统都有`IPC`机制  
Windows：通过剪贴板、管道、邮槽来进行通信。  
Linux: 通过共享内存、管道、信号量来进行通信  
Android: 本身基于Linux内核，但Android有自己的IPC通信机制，这就是Binder。通过Binder可以很容易实现进程间通信。  
  
3. 只有多进程IPC才有意义，一般一个应用多进程的场景主要有以下两种情况  
 1、一个应用由于某些原因自身需要采用多进程模式来实现。比如 有些模块由于特殊原因需要运行在单独的空间，又或者为了加大一个可使用的内存所以需要通过多进程来获取多份内存空间  
 在Android早期，应用能够分配的内存空间比较小，一般是16MB左右，(不同设备获取大小不一样)。  
 2、对于两个不同的应用，有时需要共享数据，比如用的应用需要获取联系人列表等，这种情况下就需要进程间数据共享。  

4. 怎么开启多进程    
 Android中有且仅有一种方式能开启多进程，那就是在AndroidManifest.xml中对`四大组件`设置`android:process属性`。(注:我们无法给一个线程或者实体类指定其运行时所在的进程).  
 关于进程的命名:  
 `android:process="com.kelly.chapter.remote"`  
 `android:process=":remote"`  
 这两种方式的命名最终导致的进程名是有区别的，一般情况下 默认的进程名就是包名 比如说 `com.kelly.chapter`.  
 对于`:remote`这种情况 `:`的含义是要在当前进程名的前面附加当前的包名, 而且这表示当前应用的私有进程，其他应用无法访问到。  
 对于`com.kelly.chapter.remote` 这是一种完整的命名方式，不会附加包名信息，属于全局进程，其他应用可以通过ShareUID的方式和它跑在同一个进程中.  

关于ShareUID  
Android为每个应用都分配了一个唯一的ShareUID，据用相同UID的应用才能共享数据。两个应用通过ShareUID跑在同一个进程的要求是需要两个应用有相同的UID且签名一致才可以。
跑在同一进程的两个应用，可以共享data目录、组件信息、共享内存数据等。  

5. 多进程带来的影响  
每个进程都分配了独立的虚拟机，同时虚拟机在内存分配上有不同的地址空间，导致在不同的虚拟机中访问同一个类的对象会产生多份副本 .凡是通过通过共享内存的方式提供数据交互的都会出现多进程的问题

1、静态成员和单例模式模式完全失效  (静态成员和单例模式都是基于类的，在单进程时候，类只有一个在静态区，所以全局唯一。但在多进程情况下，每一个进程都会有自己的虚拟机同时也会分配不同的内存空间，在A、B进程间 都会存在同一个类C且互不干扰，在当前进程修改类的属性值只会影响当前进程，对其他进程没有任何影响)。  

2、线程同步机制完全失效  (原因和上面类型，这个时候不管是锁对象还是锁全局类都没有用了，因为不同进程锁的不是同一个对象)

3、SharePerferences可靠性下降  

4、Applicaiton会多次创建    

#### Android中的序列化和Binder  
#### 1、什么是序列化和反序列化，为什么需要 序列化和反序列化
 序列化和反序列化: 序列化是把对象转成二进制数据，而反序列化则反之 将二进制数据还原成对象
 为什么需要序列化和反序列化: 因为计算机本身只能处理二进制数据，当要在进程间交换对象数据(进程是相互隔离的，一个进程本身不能访问另一个进程或被访问)，以及将对象数据持久化到内存中，网络传输信息时，计算机根本搞不定，而只有把对象转成计算机能"认识"的二进制数据的时候，才可以happy的完成上面的工作。


#### 2、Android常用的序列化和反序列化有哪些方式    
 Serializable和Parcelable接口可以完成对象的序列化过程。    
 (1)通过Intent或者Binder来传输数据时就需要使用Parcelable或者Serializable.    
 (2)将对象持久化到存储设备上或者通过网络传输给其他客户端就需要实现Serializable接口。    

#### 3、如果通过 Serializable怎么实现序列化和反序列化 ，serialVersionUID有什么用？    
 STEP1: 要实现序列化的类实现Serializable接口，同时声明一个serialVersionUID属性即可。  
 STEP2: 序列化和反序列
```java

//序列化过程
       ObjectOutputStream out;
        ObjectInputStream ois;

        {
            try {
                User user = new User("kelly","sport");
                out = new ObjectOutputStream(new FileOutputStream("a.txt"));
                out.writeObject(user);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//反序列化过程
            try {
                 ois = new ObjectInputStream(new FileInputStream("a.txt"));
                User user = (User) ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally {..
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
        }
```
`serialVersionUID`有什么用？
serialVersionUID本意是用来辅助序列化和反序列化的。序列化的时候会把serialVersionUID写入文件中(或者其他中介)，反序列化的时候再从文件中读出来和类中的serialVersionUID比较
如果一直 序列化成功，不一致说明当前类和序列化的类发生了变化，无法成功还原。抛出invalidClassException错误.

#### 4、Parcelable怎么实现序列化和反序列化。既然已经有了Serilizable为什么还需要Parceable。  
STEP1: 要实现序列化和反序列化的类实现Parcelable接口，覆写 describeContents()、writeToParcel()、实现 CREATOR接口 并实现createFromParcel()和newArray()方法   
```java
    public static final Creator<xxx> CREATOR = new Creator<xxx>() {
        @Override
        public xxx createFromParcel(Parcel source) {
            return new xxx(source);
        }

        @Override
        public xxx[] newArray(int size) {
            return new xxx[size];
        }
    };
```
以上的过程可以通过编辑器的插件来自动完成对象的序列化和反序列。  
#### 以上各个方法的作用解释：  
writeToParcel(Parcel out): 通过Parcel的一系列write方法完成序列化工作  
CREATE： 其内部标明了如何建立数组以及创建序列化对象，通过Parcel的一系列read方法完成反序列化工作  
describeContents(): 内容描述，但几乎在所有情况下这个方法都应该返回 0  

#### 既然已经有了Serilizable为什么还需要Parceable  
Serializable是Java中的序列化接口，其使用起来简单但开销很大，序列化和反序列化都需要大量的IO操作
Parcelabel是Android中的序列化方式，效率高 ，主要用在内存序列化上。
如果是进程间通信用Parcelable,如果是持久化或者网络传输用Serializable

#### 扩展: Serilizable和Parceable 实现序列化的方式有什么不同? 

#### 进程间通讯方式 (AIDL、Bundle、文件共享、Messenger、ContentProvider、Socket)     


#### 各种进程间通信方式的优缺点和适用场景    
