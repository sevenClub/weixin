package com.yangmj.service;

import com.yangmj.common.MyPageInfo;
import com.yangmj.entity.OrderDetails;

public interface OrderDetailsService {
    MyPageInfo<OrderDetails> queryOrderDetailsAll(OrderDetails orderDetails);

    String insertOrderDetails(OrderDetails record);
}
