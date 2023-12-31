package com.example.school.vnpay.Service;

import jakarta.servlet.http.HttpServletRequest;

public interface VnpayService {
    public String createOrder(long total,String infor, String urlReturn);
    public int orderReturn(HttpServletRequest request);
}
