package com.yangmj.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.yangmj.entity.Consumer;

@Mapper
public interface ConsumerMapper {

    Consumer findConsumerById(Long id);

    Consumer findConsumerByWechatOpenid(String wechatOpenid);

    void insertConsumer(Consumer consumer);

    void updateConsumer(Consumer consumer);

}
