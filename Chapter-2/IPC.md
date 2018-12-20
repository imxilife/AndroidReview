
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

### 进程间通讯方式 (AIDL、Bundle、文件共享、Messenger、ContentProvider、Socket)

### Bundle方式  
四大组件之三 Activity、Service、BroadCasetReceiver 都是支持在Intent之间携带Bundle数据的，由于Bundle实现了Parcelable接口 所以它可以很方便的在不同进程间传输    
基于这一点 当我们在一个进程启动另一个进程的Activty、Service、BroadCaseReceiver时，我们就可以在Bundler中附加我们需要传输的数据给远程进程的信息通过Intent发送出去。    
我们传输的数据必须能够被序列化，比如基本类型、实现了Parcelable接口的对象实现了Serializable接口的对象以及一些Android支持的特殊类型。但对于Bitmap这种不支持Parcelable接口的    
对象就可以转成byte字节组的方式再发送出去。   

### 使用文件共享  
两个进程通过读写同一个文件来交换数据。比如A进程把数据写入共享文件，B进程从共享文件中读取。但不建议并发的写文件。这样会造成数据不一致。所以SharePreference本身是支持多进程的，但在    
多进程的方式下数据变得不可靠.  

### Messenger    
Messager的底层实现就是Binder，只不过系统封装的更好调用而已。Messenger通信主要涉及三个类    

Message: 实现了Parcelable接口，用在进程间来携带要传递的信息。     
Messenger: 实现了Parcelable接口，提供send()方法给外部调用。 构造方法中需要传入Handler对象。通过Handler的getIMessenger()方法获取的IMessenger对象    
Handler: 内部类MessengerImpl实现了IMessenger.Stub,其实就是实现了Bindler接口，并且在实现的send()方法中调用了Handler的sendMessage()方法来处理消息    

3.1 具体实现客户端和服务端通信的步骤    
服务端：  
1. 新建MyService类继承自Service类，同时new Messenger、Handler对象。  
2. 在服务的onBind()方法中通过messenger的getBinder()方法返回binder对象  
```java
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"MessengerService onCreate...");
        messengerHandler = new MessengerHandler(this);
        messenger = new Messenger(messengerHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"MessengerServie onBind ...");
        return messenger.getBinder();
    }
```

客户端:  
1. 实现ServerConnection类，并覆写onServiceConnected()方法，在回调方法中获取binder对象创建Messenger对象  
2. 调用bindService()方法传入serverConnection对象绑定到MyService服务。  
3. 通过messenger对象调用send()方法 关键代码实现如下  
```java
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,MessengerService.class);
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {  //绑定成功后的回调
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.i(TAG,"onServiceConnected");
            messenger = new Messenger(binder);
            Message message = Message.obtain(null,MessengerService.MSG_CLIENT_SEND);
            Bundle bundle = new Bundle();
            bundle.putString("key","hello this is client");
            message.setData(bundle);
            message.replyTo = mClientMessenger;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG,"onServiceDisconnected");
        }
    };
```


```java
Messenger.java
    private final IMessenger mTarget;

    /**
     * Create a new Messenger pointing to the given Handler.  Any Message
     * objects sent through this Messenger will appear in the Handler as if
     * {@link Handler#sendMessage(Message) Handler.sendMessage(Message)} had
     * been called directly.
     * 
     * @param target The Handler that will receive sent messages.
     */
    public Messenger(Handler target) {
        mTarget = target.getIMessenger();
    }

    public void send(Message message) throws RemoteException {
        mTarget.send(message);
    }
----------------------------------------
Handler.java
private final class MessengerImpl extends IMessenger.Stub {
        public void send(Message msg) {
            msg.sendingUid = Binder.getCallingUid();
            Handler.this.sendMessage(msg);
        }
    }

final IMessenger getIMessenger() {
        synchronized (mQueue) {
            if (mMessenger != null) {
                return mMessenger;
            }
            mMessenger = new MessengerImpl();
            return mMessenger;
        }
    }
```

3.2 总结  
1. 基于Messenger方式的进程间通信使用于对并发要求不高的情况，因为消息是通过Handler来处理的，一次只能处理一个请求。
2. 如果需要客户端也返回消息给服务端的话，客户端也需要实现Messenger和Handler接口，同时在serverConnection回调方法中调用message的replyTo方法将客户端Messenger对象传回服务端  
```java

    private ServiceConnection serviceConnection = new ServiceConnection() {  //绑定成功后的回调  
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.i(TAG,"onServiceConnected");
            messenger = new Messenger(binder);
            Message message = Message.obtain(null,MessengerService.MSG_CLIENT_SEND);
            Bundle bundle = new Bundle();
            bundle.putString("key","hello this is client");
            message.setData(bundle);
            message.replyTo = mClientMessenger;  //这里带回客户端的Messenger  
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

//客户端Handler实现  
    private static final class ClientMessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessengerService.MSG_SERVICE_SEND:
                    Bundle bundle = msg.getData();
                    Log.i(TAG,"服务端返回的:" + bundle.getString("key"));
                    break;
            }
        }
    }        
```

### socket   
通过socket的方式一般用于网络，当然也可以在两个进程间通信  
socket通信有TCP和UDP方式两种，TCP是面对连接的，整个通信过程是基于状态的，而UDP是无状态的，整个通信过程UDP不关心通信质量，不会对数据校验。  

socket通信的一般形式是创建服务端和客户端，完成信息共享  
服务端:  
1. 创建ServerSocket，ServerSocket serverSocket = new ServerSocket(9090);  
2. 调用accept()方法阻塞，在9090端口监听客户端连接  
3. 接收到客户端的连接后，创建线程，建立和客户端的通信。  

客户端:  
1. new Socket("192.168.1.102",9090); 创建Socket并连接到指定IP的9090端口上  
2. 成功返回socket后 分别获取输入流和输出流 保存到全局变量  

#### 注意:  
     * 1、如果接收方是通过readLine()方法来读数据的话 发送方在发送的末尾一定要加'\n'换行符 否则会导致接收方一直读不到换行符而获取不到发送的数据  
     * 2、发送方在数据发送完毕时要调用flush()方法将缓存区的数据写入流  
     * 3、不要在Handler中收、发数据 会导致Handler阻塞  
     * 4、一次通信完就关闭Socket输入流会导致Socket本身被关闭。因此如果想一直用这个Socket的话，需要在建立连接的时候把Socket输入、输出流对象保存为全局，只有不需要的时候才释放掉  
     * 5、readLine()是阻塞方法，只要没有读到‘\n’，就一直阻塞线程等待数据,处于阻塞情况下是不返回的 也就是说while循环是不会继续往下执行。

1. 在网络通信中，一般需要关注 是否连通、心跳、对方是否已经关闭等 对应的手段同时也是连接超时，读取数据超时，它们在socket中都提供了参数，在socket原始的连接方法中，有一个方法  
connet(SocketAddress endpoint, int timeout)  
当设置了timeout参数后 就可以达到连接超时的效果，而通过调用方法 setSoTimeout(int timeout) 可以设置每次读取超时。         

2. Socket常用的API  
setReuseAddress(true|false) 允许将多个Socket绑定到同一个端口上，通过getReuseAddree()方法来获取当前值即可，但在发生socket。bind()之前必须先设置才会生效  
setTcpDelay(true|false) 该参数默认值是false，会启用Ngle算法  
setSoLinger(true|fase,int linger) 该参数决定Socket关闭时是否尝试继续发送Kernel缓冲区中还未发送出去的数据，若设置为true 则由第二个int型参数决定发送Kernel缓冲还未发送的内容最长的等待时间   
单位是秒，通过getSoLinger()可以获取到设置的值  
setSendBufferSize() 设置发送缓冲区的大小  默认值是8192字节 一般保持默认就好 通过getSendBufferSize()来获取值  
setReceiveBufferSize(int) 设置接收数据的缓冲区大小，默认值是8192字节，通过getReceiverBuffSize()来获取设置值  
setKeepAlive(true|false) 它和前端的keepAlive是有区别的，它的原理是每个一段时间(例如2小时)会将数据包发送给对方，如果对方响应，则认为链接依然存活。如果未响应，则在十多分钟后再发送一个数据包；
如果对方还未响应，则再过十多分钟再发送一个数据包，则会将客户端的socket关闭。该参数默认值是false 通过getKeepAlive()来获取当前值  
setOOBInline(true|false) 这个参数默认为false 若开启 则允许通过socket的方法 sendUrgentData(int)发送，这个API是直接发送，不会经过缓冲区。  


### AIDL  



#### 各种进程间通信方式的优缺点和适用场景    
