package com.example.demolauncher.util;
/*************
 * 事件总线
 * EventBus相当于生命周期与进程同步的一个Application全局变量，
 * 调用时会以双重检查模式创建一个EventBus实例，此即为全局变量
 * EventBus实例在创建时初始换用于订阅的相关集合。
 * 注册订阅者时，EventBus实例内通过反射获取订阅者的订阅方法
 * 保存在集合中。在发送事件时，EventBus再根据事件类型查找相
 * 关订阅者，再通过反射的形式调用订阅者的订阅方法。此外，还有
 * 粘性事件机制。
 * ************/
public class MessageEvent {
    private String message;
    public MessageEvent(String msg){
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
