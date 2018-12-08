

# Activity 打卡
1. `Activity`是什么? 如何使用? mete-data有什么用? intent是什么？怎么用?
2. `Activity`的生命周期
3. `Activity`的启动模式和Flag有哪些，各有什么样的用途
4. `Activty`源码截图
5. 组件间通信 (Activity和Service之间、Activity与Activity 通过什么方式来通信?)
6. `Fragment`的生命周期以及与`Activity`的交互方式?


## Activity 第一部分
本质:  Activity(活动)Android四大组件之一，主攻界面。它提供了一个用于UI展示以及与用户进行交互的窗口或者说是界面。
形式： 它既可以是全屏界面的也可以是悬浮形式(通过带有R.attr的主题)，或者嵌入到另一个活动中(使用ActivityGroup)
使用:  1、每一个Activity的在使用前都需要在AndroidManifest.xml文件中先声明 `<acvitity>`
                                                                        `<activity>`。
2、启动Activity有两种方式 显式启动和隐式启动 具体看`Intent`部分。    

`meta-data`: 

### Intent 
中文为意图，表达你想通过intent完成什么操作。Intent是一个消息传递对象，可以使用它从其他组件请求操作。 其基本用途主要包括以下三个
       
1. `启动activity`
通过intent传递给startAcitity，用以启动新的Activity实例。
```java
Intent intent = new Intent(A.this,B.class); //A,B都是Activity类
context.startActivity(intent)
Intent描述了要启动的Activity，并携带了任何的必要数据
```        
      
2. `启动服务`
Service 是一个不使用用户界面而在后台执行操作的组件。通过将 Intent 传递给 startService()，您可以启动服务执行一次性操作（例如，下载文件）。
Intent 描述了要启动的服务，并携带了任何必要数据 
```java
Intent intent = new Intent(A.this, B.class);
content.startService(intent)
```

       
3. `传递广播`
广播是任何应用均可接收的消息。系统将针对系统事件（例如：系统启动或设备开始充电时）传递各种广播
通过将 Intent 传递给 sendBroadcast()、sendOrderedBroadcast() 或 sendStickyBroadcast()，您可以将广播传递给其他应用

Intent的两种类型
1. 显示Intent
按名称（完全限定类名）指定要启动的组件，创建显式 Intent 启动 Activity 或服务时，系统将立即启动 Intent 对象中指定的应用组件. 
2. 隐式Intent
不会指定特定的组件，而是声明要执行的常规操作，从而允许其他应用中的组件来处理它，创建隐式 Intent 时，Android 系统通过将 Intent 的内容与在设备上其他应用的清单文件中声明的 Intent 过滤器进行比较，从而找到要启动的相应组件
Inten过滤器 <intent-filter>
Intent 过滤器是应用清单文件中的一个表达式，它指定该组件要接收的 Intent 类型 比如action、category、meta-data等等

##Intent包含以下信息

component
组件名称，可选项。但也是构建显式 Intent 的一项重要信息，这意味着 Intent 应当仅传递给由组件名称定义的应用组件。 如果没有组件名称，则 Intent 是隐式的
Intent 的这一字段是一个 ComponentName 对象，您可以使用目标组件的完全限定类名指定此对象，其中包括应用的软件包名称。 例如， com.example.ExampleActivity。您可以使用 setComponent()、setClass()、setClassName() 或 Intent 构造函数设置组件名称。
```java
Component component = new Component(MainActivity.this,SecondActivity.class);
Intent intent = new Intent(component);
Context.startActivity(intent);
```

action
指定要执行的操作。比如选取、或者查看等 操作可以自定义，以供在自己的应用内使用，比如操作是启动一个自己应用内的Activity等，Android也提供了很多的操作，用以完成各种各样的功能。
可以通过setAction()或者Intent的构造函数来指定操作

data
引用待操作数据和/或该数据 MIME 类型的 URI（Uri 对象）。提供的数据类型通常由 Intent 的操作决定。
要仅设置数据 `URI`，请调用 `setData()`。 要仅设置 `MIME` 类型，请调用 `setType()`。如有必要，您可以使用 setDataAndType() 同时显式设置二者。

catgory
类别这个是作为一个辅助信息加入的，它的用途是一个action可以完成多个操作，但具体完成哪个操作可以通过category来说明
使用 addCategory() 指定类别。

#以上列出的这些属性（组件名称、操作、数据和类别）表示 Intent 的既定特征。 通过读取这些属性，Android 系统能够解析应当启动哪个应用组件。

extra
携带完成请求操作所需的附加信息的键值对 ，extra中的信息作为一个额外的数据加入。

flag


##如何构建一个显式的Intent
```java
Intent downloadIntent = new Intent(this,DownloadService.class)
downloadIntent.setData(Uri.parse(fileUrl))
startService(downloadService)
```

##如何构建一个隐式的Intnet
```java
Intent intent = new Intent();
intent.setAction(Intent.ACTION_SEND);
intent.putExtra(Intent.EXTRA_TEXT,textMessage)
intent.setType('text/plain')
if(intent.resolveActivity(getPackageManager)!=null){
  startActivity(intent)
}
```



Note:
为了确保应用的安全性，启动 Service 时，请始终使用显式 Intent，且不要为服务声明 Intent 过滤器

####问题:
1. intent中的action和category ？
action： ACTION_MAIN
category: CATEGORY_HOME
谁配了 这两个 谁就是主入口？

## Activity 第二部分

### 生命周期

1. Activity生命周期方法流程图
![生命周期流程图](https://user-gold-cdn.xitu.io/2017/3/26/994580561a216cf7a057f07c7c747aa9?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
![生命周期方法描述](https://note.youdao.com/share/?id=72ed3256aafc30f97b77d75a3adce001&type=note#/)

2. 几种常见情况下的Activity的生命周期回调（正常情况）

2.1 `启动一个Activity情况`  
① 针对一个特定的Activity，第一次启动 生命周期方法回调如右： onCreate()->onStart()->onResume()  
② 按home键切换到桌面然后又切回到该Activity时 onPause()->onSaveInstanceState()->onStop()->  
onRestart()->onStart()->onResume()  
③ 按back键退出 onPause()->onStop()->onDestory()  

2.2 `启动二个或多个Activity的情况`  
① 先启动一个Activity后 `A`，然后启动另外一个Activity `B`的情况 各自的生命周期方法回调 如下：  
A 生命周期方法回调 onCreate()->onStart()->onResume()->onPause()->onStop()  
B 生命周期方法回调 onCreate()->onStart()->onResume()  
`注:` 这里A 启动 B的情况 时候 ，先是A activity的onPause()方法先执行，然后会启动B 的生命周期回调，等到B切到前台后也就是调用了onResume()方法，这时才会再执行A的onStop()方法。所以不能在A的  onPause()方法中做耗时操作以免影响B的启动  
② 先启动一个Activity `A`，再启动一个另一个Activity `B` ，(B是窗口样式)  
A 生命周期方法回调 onCreate()->onStart()->onResume()->onPause()  
B 生命周期方法回到 onCreate()->onStart()->onResume()  

`注` 对于启动的Activity样式是Dialog样式的，启动的Activity `A` 的生命周期不会走到onStop()方法中,也就是说它的生命周期回调会在onPause()<->onResume()之间切换  

2.3  `调用finish()方法`  
 ① 调用finish方法后 生命周群方法是(从onCreate()方法开始)   
 onCreate()->onStart()->onResume()->onPause()->onStop->onDestory()  

-----------------------------------------------分界线-----------------------------------------------

 3. 特殊情况下的生命周期  
     在一些特殊情况下，Activity的生命周期的经历有些异常，下面就是两种特殊情况  

  3.1 `横竖屏切换`  

   (1) 横竖屏切换会使得Activity重建，并且调用onSaveInstanceState()和onRestoreInstanceState()这两个方法来保存状态和恢复状态(这里的状态指的是数据)。  
    横竖屏切换的生命周期： onCreate()->onStart()->onResume()->onPause()->onSaveInstance()->onStop()->onDestory->onCreate()->onStart()->onRestoreInstanceState()->onResume()  
    onSaveInstanceState()这个方法的调用是在onStop()之前，它和onPause()没有既定的时序关系  
    当异常终止的Activity被重建以后，系统会调用onRestoreInstanceState()，并且把Activity销毁时onSaveInstanceState()方法所保存的Bundle对象参数同时传递给onSaveInstanceState和onCreate方  法，该方法的调用时机是在onStart之后。其中onCreate()和onRestoreInstanceState()方法来恢复Activity的状态的区别：onRestoreInstanceState回调则表明其中Bundle对象非空，不用加非空判断。  

    (2) 禁止横竖屏切换  
    可以通过在AndroidManifest文件的Activity中指定如下属性：  
```java
     android:configChanges = "orientation| screenSize"
```
     来避免横竖屏切换时，Activity的销毁和重建，而是回调了下面的方法：  
```java
@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
```
    
  3.2 `资源内存不足导致优先级低的Activity被杀死`  
      系统在内存不足的情况下会杀掉某些进程来回收内存，Android会按照以下优先级高低来杀掉进程  

      (1) 前台Activity——正在和用户交互的Activity 优先级最高  

      (2) 可见但非前台Activity ——比如Activity中弹出了对话框或者启动一个窗口样式的Activity，导致Activity可见但是位于后台无法和用户交互  

      (3) 后台Activity——已经被停止的Activity，比如执行了onStop() ,优先级最低  


  onSaveInstanceState和onRestoreInstanceState调用流程图  
  ![调用](https://note.youdao.com/yws/public/resource/72ed3256aafc30f97b77d75a3adce001/xmlnote/B054B3A045374819AEABA254D5AE3F81/33332)


 ## Activity启动模式 LauncherMode  

  1. 出现启动模式的意义: 新建Activity实例or重复利用已有的实例 (场景)

  2. Activity的管理是采用任务栈的形式，任务栈采用“后进先出”的栈结构  
    ![任务栈](https://user-gold-cdn.xitu.io/2017/3/26/5af5cdedb29e8a3dcd743e486d3b86af?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)  

  3. Activity LauncherMode  
    启动模式在AndroidManifest.xml文件 Activity标签下配置 `android:launchMode="xxx"`属性.  

### 3.1 标准模式  standard  
  每启动一次Activity就会创建一个新的Activity实例并置于栈顶，遵循谁启动了这个Activity，那么这个Acitivity就出现在启动它的那个Acitivity所在的栈.  

  `注意`  
  ① 在Android5.0以前，如果是跨应用启动Activity，那么对方应用的Activity会置于启动应用的任务栈中，这显然是难以理解的，所以在5.0版本后，启动模式是Standard, 跨应用启动的
    activity会被放入一个新的栈中(这相当于以SingleTask方式启动).  

  ② 特殊情况下，在Service或者Application中启动一个Activity，会报出以下错误.  
    <font color=#ff0000>Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag</font>  
  错误原因就是因为启动Activity的Service或Applicaiton本身不在任何的栈中，所以无法将被启动的Activity放入任何栈中，解决方法就是构建Intent的时候,  
  指定 `Intent.FLAG_ACTIVITY_NEW_TASK` FLAG ，这样会创建新的任务栈，并将其放入其中.  
  ```java
  intent.setFlag(Intent.FLAG_ACTIVITY_NEW_TASK)
  ```  
  测试结果(在MainActivity和StandardActivity之间来回启动):    
```java
   Running activities (most recent first):
        Run #6: ActivityRecord{33816517 u0 com.kelly.activity_chapter/.MainActivity t10}
        Run #5: ActivityRecord{201d2427 u0 com.kelly.activity_chapter/.StandardActivity t10}
        Run #4: ActivityRecord{3c0f57d3 u0 com.kelly.activity_chapter/.MainActivity t10}
        Run #3: ActivityRecord{e9aae74 u0 com.kelly.activity_chapter/.StandardActivity t10}
        Run #2: ActivityRecord{3db52be6 u0 com.kelly.activity_chapter/.MainActivity t10}
        Run #1: ActivityRecord{34db6c10 u0 com.kelly.activity_chapter/.StandardActivity t10}
        Run #0: ActivityRecord{97d42b9 u0 com.kelly.activity_chapter/.MainActivity t10}
```        
#### 应用场景:
绝大多数Activity
   

### 3.2 栈顶复用模式 singleTop  
  如果要启动的Acitivity已经位于栈顶情况下再次启动不会重新创建新的Activity实例，而是会重用已经在栈顶的这个，并回调 `onNewIntent()` 生命周期方法。 如下
  以上说的只有处于栈顶的时候才会复用，否则其他情况下依然会创建一个新的Activity实例。
```java
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"onNewIntent");
    }
```
#### 应用场景:
在通知栏点击收到的通知，然后需要启动一个Activity，这个Activity就可以用singleTop，否则每次点击都会新建一个Activity。

### 3.3 栈内复用模式 singleTask  
  该模式是一种单例模式，即一个栈内只有一个该Activity实例。该模式，可以通过在AndroidManifest文件的Activity中指定该Activity需要加载到那个栈中，即singleTask的Activity可以指定想要加载的目标栈。singleTask和taskAffinity配合使用，指定开启的Activity加入到哪个栈中。

#### 关于taskAffinity的值：  
  每个Activity都有taskAffinity属性，这个属性指出了它希望进入的Task。如果一个Activity没有显示的指明该Activity的taksAffinity，那么它的属性就等于Application指明的  
  taskAffinity,如果Application也没有指明，那么该taskAffinity的值就等于包名  

  `注意`  
  taskAffinity只有启动模式为SingleTask的Activity设置了才有效果，对于标准模式(Standard)和栈顶复用模式(SingleTop)设置了这个属性是没有任何效果的。被启动的Activity依然会在启动它的  
  Activity所在的任务栈中。

#### 执行逻辑:  
① 如果要启动的Activity指定的栈不存在就创建一个栈，并创建新的Activity实例压入栈中。  
② 如果Activity指定的栈存在，但其中没有Activity，那么就新建Activity实例并压入栈顶。  
③ 如果要启动的Activity已经在栈中，那么就将该Activity在栈中位置以上的所有Activity都出栈，然后回调`onNewIntent()`生命周期方法  
对应如下三种情况  
![一个任务栈S1](https://user-gold-cdn.xitu.io/2017/3/26/b3079aba6ecd625420087f05d1c4bca0?imageslim)  
![一个任务栈S1](https://user-gold-cdn.xitu.io/2017/3/26/c1ee3ce0a20da95206b55f19e84db4a0?imageslim)  
![两个任务栈S1,S2](https://user-gold-cdn.xitu.io/2017/3/26/291ae21d0d9a562a536b8cf77cbd32c8?imageslim)  

#### 应用场景:  
大多数App。 对于一个应用来说，如果应用不需要退出销毁，而是运行在前后台的话这样的场景，可以SingleTask方式来启动。  
另外一种情况是对于大部分应用，当我们在主界面点击回退按钮的时候都是退出应用，那么当我们第一次进入主界面之后，主界面位于栈底，以后不管我们打开了多少个Activity，只要我们再次回到主界面，都应该使用将主界面Activity上所有的Activity移除的方式来让主界面Activity处于栈顶，而不是往栈顶新加一个主界面Activity的实例，通过这种方式能够保证退出应用时所有的Activity都能报销毁。

### 3.4 单例模式 singleInstance  
作为栈内复用模式（singleTask）的加强版,打开该Activity时，直接创建一个新的任务栈，并创建该Activity实例放入新栈中。一旦该模式的Activity实例已经存在于某个栈中，任何应用再激活该Activity时都会重用该栈中的实例。  
![单列模式](https://user-gold-cdn.xitu.io/2017/3/26/51fcd7612ccdfe436102c607f4555ad5?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)  
#### 应用场景:
呼叫来电界面。这种模式的使用情况比较罕见，在Launcher中可能使用。或者你确定你需要使Activity只有一个实例。建议谨慎使用
