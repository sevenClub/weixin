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
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @PostMapping("/findAllOrders")
    public ResponseResult queryOrderItemAll(@RequestBody OrderItem orderItem){
        MyPageInfo<OrderItem> projectItemPageInfo = orderItemService.queryOrderItemAll(orderItem);
        ResponseResult resp = ResponseResult.makeSuccResponse(null, projectItemPageInfo);
        return resp;
    }

    /**
     * 创建订单的信息
     * @param orderItem
     * @return
     */
    @PostMapping("/createOrderItem")
    @Transactional
    public ResponseResult createProjectItem(@RequestBody OrderItem orderItem) {

        OrderDetails orderDetails = new OrderDetails();
        ResponseResult resp = null;
        try {
            int item = orderItemService.insertOrderItem(orderItem);
            //订单创建的时候，需要将发起人的信息插入到order_details订单明细表
            orderDetails.setOrderId(orderItem.getId());
            orderDetails.setWechatOpenid(orderItem.getStartWechatOpenid());
            // 0 在线  1 退出拼单
            orderDetails.setIsActive("0");
            orderDetails.setProjectFee(orderItem.getProjectCost());
            //0是 1否 发起人
            orderDetails.setIsCaptain("0");
//            int i = 1/0;
            int details = orderDetailsService.insertOrderDetails(orderDetails);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resp = ResponseResult.makeFailResponse(null, "失败");
        }
        return resp;
    }

    /**
     * 手动关闭订单，可以开始游戏，即使没有满员的情况也可以开始游戏
     * 如果是aa的项目，将费用平摊出去
     */

    public void closeOrderStartGame(@RequestBody OrderItem orderItem) {

//        game_status 为0 ，可以开始游戏
        orderItem.setGameStatus("0");
//      1  未满员
        orderItem.setIsFull("1");
        int itemResult = orderItemService.updateOrderItemByKey(orderItem);

        //如果是aa项目需要将订单金额平分
        if (1 == itemResult && "AA".equals(orderItem.getFeeTags())) {
            
        }
    }
}
