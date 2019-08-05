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

    @PostMapping("/takePartInSportDetails")
    @Transactional
    public ResponseResult insertDetails(@RequestBody OrderItem orderItem) {

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(orderItem.getId());
        orderDetails.setWechatOpenid(orderItem.getStartWechatOpenid());
        // 0 在线  1 退出拼单
        orderDetails.setIsActive("0");
        orderDetails.setProjectFee(orderItem.getProjectCost());
        //0是 1否 发起人 第一个发起人为是，后面通过本端口都是1
        orderDetails.setIsCaptain("1");
        ResponseResult resp = null;
        try {
            String details = orderDetailsService.insertOrderDetails(orderDetails);
//            00 为插入数据库成功
            if("00".equals(details)){
                resp = ResponseResult.makeSuccResponse(null, details);
            }else {
                resp = ResponseResult.makeFailResponse("名额已满，看看其他活动吧~",null );
            }

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resp = ResponseResult.makeFailResponse(null, "失败");
        }

        return resp;
    }
}
