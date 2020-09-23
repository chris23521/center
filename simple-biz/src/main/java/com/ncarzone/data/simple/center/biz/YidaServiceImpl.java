package com.ncarzone.data.simple.center.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.xxpt.gateway.shared.client.http.ExecutableClient;
import com.alibaba.xxpt.gateway.shared.client.http.PostClient;
import com.ncarzone.data.simple.center.facade.AecpGatewayResult;
import com.ncarzone.data.simple.center.facade.YidaServiceFacade;
import org.springframework.util.CollectionUtils;
import java.util.Map;

public class YidaServiceImpl implements YidaServiceFacade {

    @Override
    public AecpGatewayResult baseRequest(Map<String, String> param, String url) {

        try {
            PostClient postClient = ExecutableClient.getInstance().newPostClient(url);
            if (!CollectionUtils.isEmpty(param)) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    postClient.addParameter(entry.getKey(), entry.getValue());
                }
            }
            String result = postClient.post();
            return JSON.parseObject(result, AecpGatewayResult.class);

        }catch(Throwable e){
            AecpGatewayResult result= new AecpGatewayResult();
            result.setSuccess(false);
            result.setResult("false");
            return result;

        }

    }
}
