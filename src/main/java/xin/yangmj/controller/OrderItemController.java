package xin.yangmj.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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
import xin.yangmj.util.DateUtil;

import java.util.List;

@RestController
@Slf4j
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
     * 查看我发起运动项目和我参加的运动项目
     * 运动的最新状态
     */
    @PostMapping("/queryLeaderOrFollower")
    public ResponseResult queryLeaderOrFollower( @RequestBody JSONObject jsonObject){
        String isCaptain=jsonObject.get("isCaptain").toString();
        String orderStatus=jsonObject.get("orderStatus").toString();
        String wechatOpenid=jsonObject.get("wechatOpenid").toString();
        List<OrderItem> orderItems = orderItemService.queryLeaderOrFollower(isCaptain, orderStatus,wechatOpenid);
        ResponseResult resp = ResponseResult.makeSuccResponse(null, orderItems);
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
//            发起订单的人初始为1
            orderItem.setCurrNum(1);
//            0开始游戏，1还在招募人员，主要是针对手动关闭订单开始游戏
            orderItem.setGameStatus("1");
//            订单状态，0 组队中 1 待参加 2 已结束
//            TODO 暂时没考虑订单人数未1的人数
            orderItem.setOrderStatus("0");
//            0满员 1 未满员
            orderItem.setIsFull("1");
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
            String details = orderDetailsService.insertOrderDetails(orderDetails);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resp = ResponseResult.makeFailResponse(null, "服务器繁忙");
        }
        return resp;
    }


    /**
     * 订单人数未达标，人为关闭订单，意义是即使人员没有满也可以开始游戏
     * @param orderItem
     * @return
     */
    @PostMapping("/tmpStartGame")
    public ResponseResult tmpStartGame(@RequestBody OrderItem orderItem) {
//        安全性需要自己从表或者然后更新
        MyPageInfo<OrderItem> queryOrderItemAll = orderItemService.queryOrderItemAll(orderItem);
        OrderItem orderItemBean = queryOrderItemAll.getList().get(0);
//        0满员 1 未满员
        orderItemBean.setIsFull("1");
//        0 组队中 1 待参加 2 已结束
        orderItemBean.setOrderStatus("1");
//        按钮显示是否可以玩，不管人数满员否，0开始游戏，1还在招募人员
        orderItemBean.setGameStatus("0");
        orderItem.setUpdateTime(DateUtil.formatDateTime());
        ResponseResult resp = null;
        try {
            int itemResult = orderItemService.updateOrderItemByKey(orderItemBean);
            resp = ResponseResult.makeSuccResponse(null, itemResult);
        } catch (Exception e) {
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse("网络错误", "");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resp;
    }

    /**
     * 发起人取消订单
     * @param orderItem
     * @return
     */
    @PostMapping("/cancleOrder")
    public ResponseResult cancleOrder(@RequestBody OrderItem orderItem) {

        //        0 组队中 1 待参加 2 已结束 3 取消
        orderItem.setOrderStatus("3");
        orderItem.setUpdateTime(DateUtil.formatDateTime());

        ResponseResult resp = null;
        try {
            int item = orderItemService.updateOrderItemByKeySelective(orderItem);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse("网络错误", "");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resp;
    }
}
