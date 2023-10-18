package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.service.IamPort;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@RestController
@RequestMapping(value="/api")
public class ImportApiController {
    private IamportClient api;
    private IamPort iamPort;


    public ImportApiController(IamPort iamPort) {
        // REST API 키와 REST API secret 를 아래처럼 순서대로 입력한다.
//        this.api = new IamportClient("[복사했던 REST API키]","[복사했던 REST API secret]");
        this.iamPort = iamPort;
        String secretKey = iamPort.getSecretKey();
        String apiKey = iamPort.getApiKey();
        this.api = new IamportClient(apiKey,secretKey);
    }

    @ResponseBody
    @RequestMapping(value="/verifyIamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(
            Model model
            , Locale locale
            , HttpSession session
            , @PathVariable(value= "imp_uid") String imp_uid) throws IamportResponseException, IOException
    {
        return api.paymentByImpUid(imp_uid); //  imp_uid를 검사하며, 데이터를 보내주게되는데, 이 데이터와 처음 금액이 일치하는지를 확인하면 된다
    }
}