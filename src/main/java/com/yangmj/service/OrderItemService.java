package com.yangmj.service;

import com.yangmj.common.MyPageInfo;
import com.yangmj.entity.OrderItem;

import java.util.HashMap;
import java.util.List;

public interface OrderItemService {


    MyPageInfo<OrderItem> queryOrderItemAll(OrderItem orderItem);

//    MyPageInfo<OrderItem> queryOrderItemAll1(OrderItem orderItem);

    int insertOrderItem(OrderItem record);

    /**
     * 查询订单的发起者或参与者，目前的订单状态
     * @param hashMap
     * @param
     * @return
     */
    List<OrderItem> queryLeaderOrFollower(HashMap hashMap);

    int updateOrderItemByKey(OrderItem record);

    int cancleOrderByKey(Integer isCaptain);

    int updateOrderItemByKeySelective(OrderItem record);

    /**
     * 定时任务启动，关闭时间到期还没有满员的订单
     * @return
     */
    List<OrderItem> timerCloseOrder();

    int updateOrderItemBatch(HashMap hashMap);

    /**
     * 订单时间到了最后的时间，该订单进行关闭,正常游戏结束的订单
     */
    List<OrderItem> timerCloseOrderNormalEnd();

    //根据id更新记录
    OrderItem queryOrderItemByKey(OrderItem record);
}
