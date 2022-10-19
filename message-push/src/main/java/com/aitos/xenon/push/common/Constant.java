package com.aitos.xenon.push.common;

public interface Constant {

    /**
     * 状态
     */
    interface Status {
        /**
         * 发送中
         */
        int SENDING = 1;

        /**
         * 发送成功
         */
        int SUCCESS = 2;

        /**
         * 失败
         */
        int FAIL = 3;
    }

    /**
     * 消息类型
     */
    interface MessageType {
        int EMAIL = 1;

        int IOS = 2;

        int ANDROID = 3;
    }
}
