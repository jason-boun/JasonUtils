package com.jason.jasonsubutils.complexservice;

import com.jason.jasonsubutils.complexservice.Product;

interface IMyService {  
    Map getMap(in String country, in Product product);
    Product getProduct();
    String callMethodInService(String str);
}          