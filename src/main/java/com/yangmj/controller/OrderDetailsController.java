package com.yangmj.controller;

import com.yangmj.common.MyPageInfo;
import com.yangmj.common.ResponseResult;
import com.yangmj.common.SystemDefault;
import com.yangmj.entity.OrderDetails;
import com.yangmj.entity.OrderItem;
import com.yangmj.service.OrderDetailsService;
import com.yangmj.service.OrderItemService;
import com.yangmj.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

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
        ResponseResult resp = null;
        String wechatOpenid = orderItem.getStartWechatOpenid();
        Integer orderItemId = orderItem.getId();
        String phone = orderItem.getContactDir();
        //参数必输
        if (StringUtils.isEmpty(wechatOpenid) || StringUtils.isEmpty(orderItemId)|| StringUtils.isEmpty(phone)) {
            return ResponseResult.makeFailResponse(SystemDefault.PARAMETER_MISSING,null );
        }
        //手机号验证
        String phoneResult = CommonUtils.checkPhone(phone);
        if ("01".equals(phoneResult)) {
            return ResponseResult.makeFailResponse(SystemDefault.MOBILE_MISSING, "");
        }
        if ("02".equals(phoneResult)){
            return ResponseResult.makeFailResponse(SystemDefault.MOBILE_ERROR, "");
        }
        //判断是否重复参加
        int isPartIn = orderDetailsService.verifyRepeatedPartIn(orderItemId, wechatOpenid);
        if (isPartIn >= 1) {
            return ResponseResult.makeFailResponse(SystemDefault.NOTICE_FRIENDLY,null );
        }

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(orderItemId);
        orderDetails.setWechatOpenid(wechatOpenid);
        // 0 在线  1 退出拼单
        orderDetails.setIsActive(SystemDefault.IS_ACTIVE_ONLINE_0);
        orderDetails.setProjectFee(orderItem.getProjectCost());
        //0是 1否 发起人 第一个发起人为是，后面通过本端口都是1
        orderDetails.setIsCaptain(SystemDefault.IS_CAPTAIN_NO);

        try {
            String details = orderDetailsService.insertOrderDetails(orderDetails);
//            00 为插入数据库成功
            if("00".equals(details)){
                resp = ResponseResult.makeSuccResponse(null, details);
            }else if("01".equals(details)){
                resp = ResponseResult.makeFailResponse(SystemDefault.PART_IN_NUM_FULL,null );
            }else {
//                参数不完整，如参数传递错误
                resp = ResponseResult.makeFailResponse(details,null );
            }
            //TODO 人数达标返回消息，如消息模板id，和数据的信息相关
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resp = ResponseResult.makeFailResponse(SystemDefault.NETWORK_ERROR, "");
        }

        return resp;
    }

    @RequestMapping("/viewDetailsOneOrder")
    public ResponseResult viewDetailsOneOrder(@RequestBody OrderItem orderItem) {
        ResponseResult resp = null;
        List<Map> orderItems = orderDetailsService.viewDetailsOneOrder(orderItem.getId());
        System.out.println(orderItem.toString());

        resp = ResponseResult.makeSuccResponse(null, orderItems);
        return resp;
    }
}
