//package com.ncarzone.data.simple.center.web.controller;
//
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/yida")
//public class Test {
//    public static final String GET_FORM_DATAS = "/yida_vpc/form/searchFormDatas.json";
//
//    @Autowired
//    private YidaServiceFacade yidaServiceFacade;
//
//
//    @RequestMapping("/data")
//    public void yiDaTest() {
//        AecpGatewayResult result = null;
//        try {
//            Map<String, String> param = new HashMap<String, String>();
////            param.put("pageSize", "10");
////            param.put("currentPage", "1");
//            param.put("appType", "APP_F1F8OK4QRYQLSEC60ZIO");
//
//            param.put("systemToken", "ZC966OC1B48JE856ZTDVN9T2CSOY11AWVV3FKDA");
//
//            param.put("userId", "15917686674534905");
//
//            param.put("formUuid", "FORM-FG666XC14C8J4Z7S5XVF8AR0IQQC27670W3FK5C");
//
//            param.put("dynamicOrder", "{\"textField_kf3w0agt\":\"+\"}");
//
////            param.put("searchFieldJson", "{\"textField_v1npl9a\":\"test\"}");
//
//            result = yidaServiceFacade.baseRequest(param, GET_FORM_DATAS);
//            String resultData = result.getResult();
//            System.out.println(resultData);
//            org.json.JSONObject jsonObject = new org.json.JSONObject(resultData);
//            JSONArray jsonArray = jsonObject.getJSONArray("data");
//            System.out.println(jsonArray.length());
//            for(int i = 0; i < jsonArray.length(); i ++) {
//                org.json.JSONObject jsonData = jsonArray.getJSONObject(i);
//                String name = jsonData.getJSONObject("modifyUser").getJSONObject("name").getString("pureEn_US");
//                String userId = jsonData.getJSONObject("modifyUser").getString("userId");
//
//                JSONObject formData = jsonData.getJSONObject("formData");
//                String selectBigArea = formData.getString("selectField_kf3w0agn");//选择大区
//                String selectSmallerArea = formData.getString("selectField_kf3w0ago");//选择小区
//                String warehouse = formData.getString("selectField_kf3w0agp");//选择仓库
//                String shopArea = formData.getString("textField_kf3w0agq");//门店面积
//                String sumPerson = formData.getString("textField_kf3w0agr");//门店人数
//                String sumCustomer = formData.getString("textField_kf3w0ags");//总客户数
//                String permonthNum = formData.getString("textField_kf3w0agt");//月交易sku数
//                String perOrderSum = formData.getString("textField_kfdavldk");//月度订单数
//                System.out.println("姓名:"+name +" 用户id:"+userId  + " 选择大区:" );
//            }
//
//        } catch (Exception e) {
//
//        }
//    }
//}
