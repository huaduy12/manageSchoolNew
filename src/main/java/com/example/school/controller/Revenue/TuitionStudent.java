package com.example.school.controller.Revenue;


import com.example.school.dto.StudentDto;
import com.example.school.entity.Revenue;
import com.example.school.entity.RevenueClass;
import com.example.school.entity.Revenue_Detail;
import com.example.school.security.EntityUserDetail;
import com.example.school.service.StudentService;
import com.example.school.service.revenue.RevenueDetailService;
import com.example.school.service.revenue.RevenueService;
import com.example.school.service.revenue.TuitionService;
import com.example.school.vnpay.Service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student/tuition")
public class TuitionStudent {

    private TuitionService tuitionService;
    private StudentService studentService;
    private RevenueService revenueService;
    private RevenueDetailService revenueDetailService;
    private VnpayService vnpayService;

    @Autowired
    public TuitionStudent(TuitionService tuitionService,StudentService studentService,
                          VnpayService vnpayService,RevenueService revenueService,RevenueDetailService revenueDetailService) {
        this.tuitionService = tuitionService;
        this.studentService = studentService;
        this.vnpayService = vnpayService;
        this.revenueService = revenueService;
        this.revenueDetailService =revenueDetailService;
    }

    @GetMapping("/not-paid")
    public String getTuitionNoPaid(Model model,@AuthenticationPrincipal EntityUserDetail userDetail){
        List<RevenueClass> revenueClasses = tuitionService.getTuitionNotPaid(userDetail);
        model.addAttribute("revenueClasses",revenueClasses);

        StudentDto studentDto = studentService.getStudentLogin(userDetail);
        model.addAttribute("totalMoney",studentDto.getAccount_balance());

        return "student/tuitionNotPaid";
    }

    //tạo hóa đon để nạp tiền vào tài khoản cá nhân
    @PostMapping("/addTuition")
    public String createOrder(@AuthenticationPrincipal EntityUserDetail userDetail, @RequestParam("moneyTuition") String moneyTuition,
                             RedirectAttributes redirectAttributes, HttpServletRequest request){

        Long money = null;
        moneyTuition = moneyTuition.replaceAll(",","");
        try{
            money =Long.parseLong(moneyTuition);
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("addFail","Nạp tiền thất bại");
            return "redirect:/student/tuition/not-paid";
        }
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnpayService.createOrder(money,"Nop tien tai khoan", baseUrl);
        return "redirect:" + vnpayUrl;
    }

    // tạo hóa đơn để thanh toán online
    @PostMapping("/payment-tuition-online")
    public String createOrderOnline(@AuthenticationPrincipal EntityUserDetail userDetail, @RequestParam("idRevenue") int idRevenue,
                              RedirectAttributes redirectAttributes, HttpServletRequest request){
        Revenue revenue = revenueService.getRevenueById(idRevenue);

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnpayService.createOrder(revenue.getPrice(),""+revenue.getId(), baseUrl);
        return "redirect:" + vnpayUrl;
    }

    // cộng tiền vào tài khoản cá nhân
    @GetMapping("/vnpay-payment")
    public String GetMapping(@AuthenticationPrincipal EntityUserDetail userDetail,HttpServletRequest request, Model model,
                             RedirectAttributes redirectAttributes){
        int paymentStatus =vnpayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        Long money = Long.parseLong(totalPrice);


        if(money >0 && paymentStatus ==1){
            if(orderInfo.equals("Nop tien tai khoan")){
                tuitionService.addMoneyStudent(userDetail,money/100);
                redirectAttributes.addFlashAttribute("addSuccess","Nạp tiền thành công");
            }
            else{
                try{
                    Integer idRevenue = Integer.parseInt(orderInfo);
                    boolean isPayment = tuitionService.paymentTuitionOnline(userDetail,idRevenue);
                    if(isPayment){
                        redirectAttributes.addFlashAttribute("paymentSuccess","Thanh toán thành công");
                    }else{
                        redirectAttributes.addFlashAttribute("paymentFail","Thanh toán thất bại");
                    }
                }catch (Exception e){
                     e.printStackTrace();
                     redirectAttributes.addFlashAttribute("paymentFail","Thanh toán thất bại");
                }
            }

        }else {
            redirectAttributes.addFlashAttribute("addFail","Nạp tiền thất bại");
        }
        return "redirect:/student/tuition/not-paid";
       //return paymentStatus == 1 ? "student/orderSuccess" : "student/orderFail";
    }

    // thanh toán học phí qua số dư cá nhân
    @PostMapping("/payment-tuition")
    public String paymentTuition(@AuthenticationPrincipal EntityUserDetail userDetail,@RequestParam("idRevenue") int idRevenue,
                                 RedirectAttributes redirectAttributes){

        boolean isPayment = tuitionService.paymentTuitionPersonal(userDetail,idRevenue);
        if(isPayment){
            redirectAttributes.addFlashAttribute("paymentSuccess","Thanh toán thành công");
        }else{
            redirectAttributes.addFlashAttribute("paymentFail","Thanh toán thất bại");
        }
        return "redirect:/student/tuition/not-paid";
    }


    @GetMapping("/paid")
    public String getTuitionPaid(Model model,@AuthenticationPrincipal EntityUserDetail userDetail){

        StudentDto studentDto = studentService.getStudentLogin(userDetail);
        List<Revenue_Detail> revenue_details = revenueDetailService.getRevenueDetailBtStudent(studentDto.getId());
        System.out.println(revenue_details);
        model.addAttribute("revenue_details",revenue_details);
        return "student/tuitionPaid";
    }

}
