package com.yangmj.service;

import com.yangmj.common.MyPageInfo;
import com.yangmj.entity.OrderDetails;
import com.yangmj.entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
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

    List<Map> viewDetailsOneOrder(Integer id);



}
