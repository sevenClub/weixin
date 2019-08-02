package xin.yangmj.service;

import xin.yangmj.common.MyPageInfo;
import xin.yangmj.entity.OrderDetails;

public interface OrderDetailsService {
    MyPageInfo<OrderDetails> queryOrderDetailsAll(OrderDetails orderDetails);

    String insertOrderDetails(OrderDetails record);
}
