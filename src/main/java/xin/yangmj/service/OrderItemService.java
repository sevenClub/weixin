package xin.yangmj.service;

import xin.yangmj.common.MyPageInfo;
import xin.yangmj.entity.OrderItem;

import java.util.List;

public interface OrderItemService {


    MyPageInfo<OrderItem> queryOrderItemAll(OrderItem orderItem);

    int insertOrderItem(OrderItem record);

    /**
     * 查询订单的发起者或参与者，目前的订单状态
     * @param isCaptain
     * @param orderStatus
     * @return
     */
    List<OrderItem> queryLeaderOrFollower(String isCaptain,String orderStatus,String wechatOpenid);
}
