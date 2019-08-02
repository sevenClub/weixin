package xin.yangmj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xin.yangmj.common.MyPageInfo;
import xin.yangmj.common.ResponseResult;
import xin.yangmj.entity.OrderDetails;
import xin.yangmj.entity.OrderItem;
import xin.yangmj.service.OrderDetailsService;
import xin.yangmj.service.OrderItemService;

@RestController
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private OrderItemService orderItemService;

    /**
     *
     * @param orderItem
     * @return
     */
    @PostMapping("/findOrderDetail")
    public ResponseResult queryOrderItemAll(@RequestBody OrderDetails orderItem){
        MyPageInfo<OrderDetails> orderDetailsPageInfo = orderDetailsService.queryOrderDetailsAll(orderItem);
        ResponseResult resp = ResponseResult.makeSuccResponse(null, orderDetailsPageInfo);
        return resp;
    }

    /**
     * 拼单进入该活动项目，更新订单的信息
     * 参与拼单活动
     * @param orderItem
     * @return
     */

    @PostMapping("/insertDetails")
    @Transactional
    public ResponseResult insertDetails(@RequestBody OrderItem orderItem) {

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(orderItem.getId());
        orderDetails.setWechatOpenid(orderItem.getStartWechatOpenid());
        // 0 在线  1 退出拼单
        orderDetails.setIsActive("0");
        orderDetails.setProjectFee(orderItem.getProjectCost());
        //0是 1否 发起人
        orderDetails.setIsCaptain("1");
        ResponseResult resp = null;
        try {
            int details = orderDetailsService.insertOrderDetails(orderDetails);
            resp = ResponseResult.makeSuccResponse(null, details);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resp = ResponseResult.makeFailResponse(null, "失败");
        }

        return resp;
    }
}
