package xin.yangmj.service.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import org.springframework.stereotype.Service;

@Service
public class AlipayService {


    private AlipayClient alipayClient = new DefaultAlipayClient ("https://openapi.alipaydev.com/gateway.do",
            "2016081600254320",
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCoU8TgorKPS3FE6wLff1YZUSQT9Xr0098DQWFFiJ3XkpW6OJs5tj/b/GyTsxa8A8JAKUAYJ4EYjNbErm9gFucThJ9lFbvvYOHWlogUIBIA+9V7Ed2Qz8vV2it8b0CsCMSCERn5cDcVwhyV63BXZu+6j8H2TR/P51BYSr8Y5qgZvwHqHkh8otxzwotPWiYCW3MDUq9igxeJMdJcDWJzSlF7iUh1DINqkzdLy4V3elaAWCWaya+SSWBKcQG7YTOFI9m/xu8hMYj8fhPhBboVtLc16b9ZG6ZkJRJri1lGlXhQ+JvslB41nudRIDiCA+6e+wSdCeKPxxSSxGMaYWDf4H8/AgMBAAECggEBAJmoBnOM8XTvSFHCOlgtFDp+IHH7x6cQdWayWwovYO71h6p7m+891+OH9NGLTPiKcdtCDJlADo+LmU3nchIENiJSEXMkOO+Wv/G0bjn44JZ3TPRQZbKYjZkE1Gd940pnooKc+9oAvrgk/xU329BdTf8+1a9xzdVqcaR3byRbAh6lC0An38/VZfwGaBPBy0Dkp1YzcRgZDKN4DUDbE6fK8JaApLy3ArSEV9JRF1rUrGLsAkH0WmP6pbU+z2ZkYynpRNy7z3ftzwTWU93UEGZZF/cMtPr2DZw2My66LP8D5cHBTq5ylUBAHR8M0Rj48LOJR1V3uuXymU4JrDORQzQjWmECgYEA3o3xdBA9XuCUIHhtQ8kOT0X3tNz1JmKHUBL2GaXsuFscDlHYo3oF98FNhNJ/VXdNjZjiLo99zPf325+HcHc29kmsst+JlLVJc1lM4qlH9070+lfuy9xAlkzZBXtEkQ2LQ0vpl2e7aogiGdfQUe+xeiAlNu7hjilEdORobBFdHI8CgYEAwZ+cEy2pynB/RXpE/cUhpTbmt5kJbBWeh0v6XAQhwt4r8Gox3JzM0VmhBSr4Emf2ygOCuoImqsJvVpp/SAB/83GmN+AwUWpRm1Jfy7WxzQaUl1+I2CRVrfAkaAMp5DArsqukGKfKMRp5+HP1f1G06Nx8Hufk4dD2B5kViI0VKlECgYAX/sHbu6yCQEB4R8OwHUtXrLmKXJBp9RGVxpWWKledgOVDffuXlJ50Gflbqr5j3psT+znXv0zW/p1QpqCZtaDNaSAfoQ5aZDlpIGWzwoCp3292Vmi+vh6QlfSILMQ1x3oOZW7oDh0c3VSf6K7jRq6h9jlM9PCIvVZOLBo+HJknbQKBgEtbtqAUPqgmvrIo0189cUnfI07DxlIF3AxmesuDZCx20f0hJ8ToViMI0k2zmpd3VSrs0N2MOEZvpGu9d9prL2FtkzbeD5AwZBCir9wWHFxiukRsD1OoWJx5AYJl8ND6GpfQi+54p44S8vPLFfp2r4WOAtT4hjGJZagxYZsi5eDxAoGAFGqnnTnp3Fd7h7JhBWTQ8T260yh+Qc0eV/nBq2WaKaqsQqROiYBSg9IHEUiueyud8cIOBU5uVYIMK09VNYfFsimLTgodxi9fg88TFmWwsHEjj6eTGDsc7bYke1+cSw8jnwsuDMtZ81vNMKfT0I/wso1HCNiI4AWf9w7rb05bf0Q=",
            "json",
            "utf-8",
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv56LLR73J82cMrdWSqQv+ODFugZdiaVaKWaPYlsmfxoNtnS1l5eJl9Lg8SBU6EC7CXmCPiylp8Wg06QckdyYc3toa8MeG7DznxywcfD7ieLr5IgDXGznl01a7yWf+GFTO4pi+NgNPKyg736CjnUtsAniPW//WrF0xQ9gLuQwLRXphAviN4b8XaUn3LstvU+ZwChBVcH+9KMCxs8seamYj9SV3ZNbfi9qkFm6FNw7KULr9XGupNxXxCJbDqIEz1ZYIxQvx5NI8Q7V3cwaXFL0WFErcIJZAZhcWAR7TIw/rptGaBi4ecwzq5FWIstvcwmPXJc3jsh2RGCX3xj+7gIPaQIDAQAB",
            "RSA2" );


    public void barCodePay() {
        AlipayTradePayRequest request = new AlipayTradePayRequest();

        AlipayTradePayModel model = new AlipayTradePayModel();
        request.setBizModel(model);

        model.setOutTradeNo(System.currentTimeMillis()+"");
        model.setSubject("Iphone6 16G");
        model.setTotalAmount("0.01");
        model.setAuthCode("286618443736661893");//沙箱钱包中的付款码
        model.setScene("bar_code");


        AlipayTradePayResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println(response.getBody());
        System.out.println(response.getTradeNo());
    }


}
