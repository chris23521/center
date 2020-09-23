package com.ncarzone.data.simple.center.facade;

import java.util.Map;

public interface YidaServiceFacade {

    AecpGatewayResult baseRequest(Map<String, String> param, String url);
}
