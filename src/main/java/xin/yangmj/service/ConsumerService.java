package xin.yangmj.service;

import xin.yangmj.entity.Consumer;


public interface ConsumerService {

    /**
     * 根据openid获取用户的信息
     * @param wechatOpenid
     * @return
     */
    Consumer findConsumerByWechatOpenid(String wechatOpenid);
}
