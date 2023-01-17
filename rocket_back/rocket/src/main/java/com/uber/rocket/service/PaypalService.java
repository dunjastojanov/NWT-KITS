package com.uber.rocket.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.uber.rocket.dto.PaymentDTO;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalService {
    @Autowired
    private APIContext apiContext;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserService userService;
    public static final String SUCCESS_URL = "http://localhost:4200/test";
    public static final String CANCEL_URL = "http://localhost:4200/test";
    public static final String CURRENCY = "USD";
    public static final String INTENT = "sale";
    public static final String DESCRIPTION = "Purchase of rocket tokens";
    public static final String PAYMENT_METHOD = "paypal";


    public String createPayment(HttpServletRequest request) {
        double total = Double.parseDouble(request.getParameter("amount"));
        Amount amount = new Amount();
        amount.setCurrency(CURRENCY);
        total = BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.valueOf(total));
        Transaction transaction = new Transaction();
        transaction.setDescription(DESCRIPTION);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(PAYMENT_METHOD);

        Payment payment = new Payment();
        payment.setIntent(INTENT);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(CANCEL_URL);
        redirectUrls.setReturnUrl(SUCCESS_URL);
        payment.setRedirectUrls(redirectUrls);
        try {
            Payment createdPayment = payment.create(apiContext);
            for (Links link : createdPayment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    storePaymentInfo(total, request, createdPayment.getId());
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Unsuccessful creation of payment");
        }
        return null;
    }

    private void storePaymentInfo(double total, HttpServletRequest request, String paypalPaymentId) {
        com.uber.rocket.entity.user.Payment storeData = new com.uber.rocket.entity.user.Payment();
        storeData.setAmount(total);
        storeData.setSuccessful(false);
        storeData.setUserId(userService.getUserFromRequest(request).getId());
        storeData.setPaypalPaymentId(paypalPaymentId);
        storeData.setTransactionDate(LocalDateTime.now());
        paymentRepository.save(storeData);
    }

    public String executePayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setId(paymentDTO.getPaymentId());
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(paymentDTO.getPayerId());
        try {
            Payment executedPayment = payment.execute(apiContext, paymentExecution);
            if (executedPayment.getState().equals("approved")) {
                addTokensToUser(paymentDTO);
                return "Successful payment";
            } else {
                throw new RuntimeException("Unsuccessful payment");
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Unsuccessful payment");
        }
    }

    private void addTokensToUser(PaymentDTO paymentDTO) {
        com.uber.rocket.entity.user.Payment data = paymentRepository.findByPaypalPaymentId(paymentDTO.getPaymentId());
        data.setSuccessful(true);
        paymentRepository.save(data);
        userService.addTokens(data);
    }
}
