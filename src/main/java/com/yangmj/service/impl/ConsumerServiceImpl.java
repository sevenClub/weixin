package com.yangmj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yangmj.entity.Consumer;
import com.yangmj.mapper.ConsumerMapper;
import com.yangmj.service.ConsumerService;
@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private ConsumerMapper consumerMapper;
    @Override
    public Consumer findConsumerByWechatOpenid(String wechatOpenid) {
        return consumerMapper.findConsumerByWechatOpenid(wechatOpenid);
    }
}
