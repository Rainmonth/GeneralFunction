package com.rainmonth.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Note：
 * 1、调用startService() 和 bindService()后，要销毁service必须同时调用stopService()和unbindService()方法
 * 才可以，点击Stop Service按钮只会让Service停止，点击Unbind Service按钮只会让Service和Activity解除关联，
 * 一个Service必须要在既没有和任何Activity关联又处理停止状态的时候才会被销毁；
 * 2、Service和Thread的关系——没有半毛钱关系
 * Thread，用于开启一个子线程，在里面执行一些耗时操作就不会阻塞主线程的运行；
 * Service，最初的理解是用来处理一些“后台”任务的，就认为一些比较耗时的操作就可以放在这里（后台任务嘛），这就产生了
 * 混淆了；其实Service也是运行在主线程中的
 * 3、后台和和子线程的概念
 * Android 的后台指的是它的运行完全不依赖UI；通常Service中需要另外开启一个子线程来执行那些不需要UI的耗时操作的；
 * 4、既然Service也要创建子线程去执行耗时操作，为何不再最初久在Activity中创建子线程去执行而要多Service这一步呢？
 * 关键在可控制行：Activity可以对Service进行很好的控制，所有的Activity都可以于Service进行关联，并且可以很好的
 * 操作Service中的方法，即使Activity被销毁了，只要重新创建就可以重新建立于Service的连接；而Activity对线程则是
 * 十分无奈的，当Activity被销毁之后，就没有其它方法再去获得之前创建的子线程的示例，而且一个Activity创建的子线程，
 * 另一个Activity是无法对其进行操作的。
 * 5、RemoteService虽然是单独开了一个进程（即和当前应用程序不再同一个进程中）且进行耗时操作也不会出现ANR（但只是不
 * 在戒面上进行展示，即不会出现ANR弹出框）程序似乎不会继续向下运行。还有RemoteService是有弊端的，由于他和开启它的
 * Activity不再同一个进程当中，就不能采用之前NormalService那样的方式进行bind操作了，这样会导致程序崩溃。
 * 6、RemoteService在供给其它应用程序调用的时候，其在AndroidManifest.xml文件中定义时android:exported应该为true，
 * 表示可以供外部访问；如果该RemoteService在声明的时候进行了权限保护，那么调用该Service的外部应用使用该Service的
 * 时候必须先声明使用该Service自定义的权限，即使用use-permission来使用相应的权限，否则提示not allowed to bind
 * to service intent
 * 7、Android 5.0要求声明使用Service的Intent必须是显式调用，如Intent intent = new Intent(context, XxxService.class);
 * 而不能采用隐式调用，如Intent intent = new Intent("ActionString);
 * 8、Android Studio中编写AIDL文件时，编写完成后记得要先将项目build一下，否则在使用时会找不到AIDL中定义的方法；
 * 9、IntentService的使用，具体的逻辑处理主要发生在IntentService的回调方法onHandleIntent中，根据提供的示例可以发现
 * IntentService应该是批量下载的绝佳实现方式了；
 * 10、Service的开机启动
 * a、AndroidManifest.xml文件中获取开机启动的权限；
 * b、定义一个BroadcastReceiver来监听boot_completed这个Action（即添加
 * <intent-filter>
 * <action android:name="android.intent.action.BOOT_COMPLETED" />
 * </intent-filter>）
 * 这样当手机启动后，就会出发该广播了；
 * c、在定义的BroadcastReceiver的onReceiver()方法中调用startService()方法即可；
 * 11、Service在被杀死后重新启动
 * a、在Service的onDestroy()方法中进行重新启动该Service即可（前提是Service被杀死后会走onDestroy()这个生命周期
 * 函数；（简单的调用startService()方法；
 * b、在Service的onDestroy()方法中发送广播重新开启自己，然后自己接受这个广播；
 * c、开启两个服务A和B，A监听B的广播来启动B，B监听A的广播来启动A；
 * d、双进程守护
 *  参考资料：
 *  a、http://my.oschina.net/u/1777508/blog/345846；
 *  b、http://blog.csdn.net/yyh352091626/article/details/50542554；
 * 12、降低服务因系统资源紧张而被杀死的方法：
 * a、提高优先级（修改onStartCommand()方法的返回值）；
 * b、设置为前台Service（本质还是提高优先级）；
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        TextView tvBtnInstruction = (TextView) findViewById(R.id.tv_btn_instruction);
        TextView tvBtnNormalService = (TextView) findViewById(R.id.tv_btn_normal_service);
        TextView tvBtnForegroundService = (TextView) findViewById(R.id.tv_btn_foreground_service);
        TextView tvBtnRemoteService = (TextView) findViewById(R.id.tv_btn_remote_service);
        TextView tvBtnExitStillRunService = (TextView) findViewById(R.id.tv_btn_exit_still_run_service);
        TextView tvBtnIntentService = (TextView) findViewById(R.id.tv_btn_intent_service);

        tvBtnInstruction.setOnClickListener(this);
        tvBtnNormalService.setOnClickListener(this);
        tvBtnForegroundService.setOnClickListener(this);
        tvBtnRemoteService.setOnClickListener(this);
        tvBtnExitStillRunService.setOnClickListener(this);
        tvBtnIntentService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_btn_instruction:
                intent = new Intent(this, ServiceInstructionActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_btn_normal_service:
                intent = new Intent(this, NormalServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_btn_foreground_service:
                // todo 对android 2.0也就是API 5 进行兼容时该如何处理
                //  前台Service
                intent = new Intent(this, ForegroundService.class);
                startService(intent);
                break;
            case R.id.tv_btn_remote_service:
                //  Remote Service
                intent = new Intent(this, RemoteServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_btn_exit_still_run_service:
                // todo 了解双进程守护
                break;
            case R.id.tv_btn_intent_service:
                intent = new Intent(this, IntentServiceSampleActivity.class);
                startActivity(intent);
                break;
            default:

                break;
        }
    }
}
