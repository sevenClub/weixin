package com.yangmj.service;

import com.yangmj.common.MyPageInfo;
import com.yangmj.entity.OrderDetails;

import java.util.Map;

public interface OrderDetailsService {
    MyPageInfo<OrderDetails> queryOrderDetailsAll(OrderDetails orderDetails);

    String insertOrderDetails(OrderDetails record);

    /**
     * 查询是否重复参加该订单
     * @param orderId
     * @param wechatOpenid
     * @return
     */
    int verifyRepeatedPartIn(Integer orderId,String wechatOpenid);

    Map<String, Object> viewDetailsOneOrder(Integer id);



}
