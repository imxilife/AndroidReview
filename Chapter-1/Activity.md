

#### Activity 打卡
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

##问题:
1. intent中的action和category ？
action： ACTION_MAIN
category: CATEGORY_HOME
谁配了 这两个 谁就是主入口？

### Activity 第二部分