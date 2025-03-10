package com.rainmonth.utils;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.rainmonth.utils.log.LogUtils;

/**
 * 广播管理类
 */
public class BroadcastMgr {
    /**
     * 发送程序内 broadcast
     *
     * @param intent 广播内容数据
     * @return true 表示广播发送成功，false 表示广播发送失败
     */
    public static boolean sendLocalBroadcast(Intent intent) {
        try {
            return LocalBroadcastManager.getInstance(Utils.getApp()).sendBroadcast(intent);
        } catch (Throwable e) {
            LogUtils.printStackTrace(e);
        }

        return false;
    }

    /**
     * 同步发送程序内 broadcast
     *
     * @param intent 广播内容
     */
    public static void sendLocalBroadcastSync(Intent intent) {
        try {
            LocalBroadcastManager.getInstance(Utils.getApp()).sendBroadcastSync(intent);
        } catch (Throwable e) {
            LogUtils.printStackTrace(e);
        }
    }

    /**
     * 注册程序内 receiver
     *
     * @param receiver 广播接收者
     * @param filter   广播过滤器
     */
    public static void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {

        try {
            LocalBroadcastManager.getInstance(Utils.getApp()).registerReceiver(receiver, filter);
        } catch (Throwable e) {
            LogUtils.printStackTrace(e);
        }
    }

    /**
     * 反注册程序内 receiver
     *
     * @param receiver 广播接收者
     */
    public static void unregisterLocalReceiver(BroadcastReceiver receiver) {
        try {
            LocalBroadcastManager.getInstance(Utils.getApp()).unregisterReceiver(receiver);
        } catch (Throwable e) {
            LogUtils.printStackTrace(e);
        }
    }

    /**
     * 发送系统全局 broadcast
     *
     * @param intent 广播内容数据
     */
    public static void sendBroadcast(Intent intent) {
        try {
            Utils.getApp().sendBroadcast(intent);
        } catch (Throwable e) {
            LogUtils.printStackTrace(e);
        }
    }

    /**
     * 发送系统全局 broadcast, 并指定接收者需要指定权限
     *
     * @param intent             广播内容数据
     * @param receiverPermission 广播接收者需要的权限
     */
    public static void sendBroadcast(Intent intent, String receiverPermission) {
        try {
            Utils.getApp().sendBroadcast(intent, receiverPermission);
        } catch (Throwable e) {
            LogUtils.printStackTrace(e);
        }
    }

    /**
     * 注册系统全局 receiver
     *
     * @param receiver 广播接收者
     * @param filter   广播接收者过滤器
     */
    public static void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {

        try {
            Utils.getApp().registerReceiver(receiver, filter);
        } catch (Throwable e) {
            LogUtils.printStackTrace(e);
        }
    }

    /**
     * 反注册系统全局 receiver
     *
     * @param receiver 广播接收者
     */
    public static void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            Utils.getApp().unregisterReceiver(receiver);
        } catch (Throwable e) {
            LogUtils.printStackTrace(e);
        }
    }
}
