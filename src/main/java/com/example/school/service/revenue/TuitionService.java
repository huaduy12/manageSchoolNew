package com.example.school.service.revenue;

import com.example.school.dto.RevenueDto;
import com.example.school.entity.RevenueClass;
import com.example.school.security.EntityUserDetail;

import java.util.List;

public interface TuitionService {

    List<RevenueClass> getTuitionNotPaid(EntityUserDetail userDetail);  // các khoản chưa thanh toán
    List<RevenueClass> getTuitionPaid();     // các khoản đã thanh toán

    void addMoneyStudent(EntityUserDetail userDetail, Long money);
    boolean paymentTuitionPersonal(EntityUserDetail userDetail,int idRevenue);
    boolean paymentTuitionOnline(EntityUserDetail userDetail,int idRevenue);
}
