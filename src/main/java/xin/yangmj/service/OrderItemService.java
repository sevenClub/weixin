package xin.yangmj.service;

import xin.yangmj.common.MyPageInfo;
import xin.yangmj.entity.OrderItem;

public interface OrderItemService {


    MyPageInfo<OrderItem> queryOrderItemAll(OrderItem orderItem);

    int insertOrderItem(OrderItem record);
}
