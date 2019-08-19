package com.yangmj.controller;

import com.yangmj.common.MyPageInfo;
import com.yangmj.common.ResponseResult;
import com.yangmj.common.SystemDefault;
import com.yangmj.entity.OrderDetails;
import com.yangmj.entity.OrderItem;
import com.yangmj.service.MessagePushService;
import com.yangmj.service.OrderDetailsService;
import com.yangmj.service.OrderItemService;
import com.yangmj.service.WechatService;
import com.yangmj.util.CommonUtils;
import com.yangmj.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private MessagePushService messagePushService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private RestTemplate wxAuthRestTemplate = new RestTemplate();

    @Value("${wechat.templateId.partin}")
    private String templateIdPartIn;

//    @Value("${auth.wechat.appId}")
////    private String appId;
////
////    @Value("${auth.wechat.secret}")
////    private String secret;

    @Autowired
    private WechatService wechatService;

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
//        String wxSessionObj = stringRedisTemplate.opsForValue().get(orderItem.getAccessToken());
//        String[] strings = wxSessionObj.split("#");
        String openid = orderItem.getStartWechatOpenid();
        String formId = orderItem.getFormId();

       /* String response = wxAuthRestTemplate
                .getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxcc04c98ba3b06d62&secret=dd1281f54b2dcb84008b4a586739a8d3",
                        String.class);
        JSONObject object = (JSONObject) JSONObject.parse(response);
        String access_token = (String)object.get("access_token");*/

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
        //手机号
        orderDetails.setContactDir(phone);
        //获取其中的formid
        orderDetails.setFormId(formId);

        try {
            String details = orderDetailsService.insertOrderDetails(orderDetails);
            String access_token = wechatService.loginWechatNotice();
//            00 为插入数据库成功
            if("00".equals(details)){
                resp = ResponseResult.makeSuccResponse(null, details);
                //将项目开始时间和结束时间拼接
                String time = orderItem.getActureStartTm()+"~"+ orderItem.getEndTime();
                //当前系统时间格式化
                String dateTime = DateUtil.formatDateTime();
                //添加模板data内容
                String[] value = {orderItem.getSportTitle(),dateTime,time,orderItem.getGameLocation(),"",""};
                System.out.println("accessToken"+access_token);
                System.out.println("openid"+openid);
                System.out.println("formId"+formId);
                System.out.println("templateId"+templateIdPartIn);
                messagePushService.pushOneUser(access_token,openid,formId,templateIdPartIn,value);
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

    /**
     * 查看订单详情
     * @param orderItem
     * @return
     */
    @RequestMapping("/viewDetailsOneOrder")
    public ResponseResult viewDetailsOneOrder(@RequestBody OrderItem orderItem) {
        ResponseResult resp = null;
        Map<String, Object> stringObjectMap = orderDetailsService.viewDetailsOneOrder(orderItem.getId());
        System.out.println(orderItem.toString());

        resp = ResponseResult.makeSuccResponse(null, stringObjectMap);
        return resp;
    }

   /* public String loginWechatNotice() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ this.appId +"&secret="+ this.secret +"";
        System.out.println("url=====+微信消息通知"+url);
        String response = wxAuthRestTemplate
                .getForObject(url,String.class);
        JSONObject object = (JSONObject) JSONObject.parse(response);
        String access_token = (String)object.get("access_token");
        return access_token;
    }*/
}
