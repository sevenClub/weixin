package xin.yangmj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.yangmj.entity.Consumer;
import xin.yangmj.mapper.ConsumerMapper;
import xin.yangmj.service.ConsumerService;
@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private ConsumerMapper consumerMapper;
    @Override
    public Consumer findConsumerByWechatOpenid(String wechatOpenid) {
        return consumerMapper.findConsumerByWechatOpenid(wechatOpenid);
    }
}
