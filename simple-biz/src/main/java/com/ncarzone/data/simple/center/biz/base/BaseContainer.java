package com.ncarzone.data.simple.center.biz.base;


import com.ncarzone.data.simple.center.biz.aviator.ComplexTagsExpression;

public class BaseContainer implements Container {


    @Override
    public void start() {
        ComplexTagsExpression.init();
    }



    @Override
    public void stop() {
        ComplexTagsExpression.close();
    }
}
