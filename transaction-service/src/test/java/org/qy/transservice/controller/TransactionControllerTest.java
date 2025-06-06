package org.qy.transservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.jupiter.api.*;
import org.qy.transsdk.common.Result;
import org.qy.transsdk.util.Logger;
import org.qy.transservice.model.TransType;
import org.qy.transservice.model.request.TransactionParam;
import org.qy.transservice.model.request.TransactionUpdateParam;
import org.qy.transservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;


import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionControllerTest {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    TransactionService transactionService;

    private RestTemplate restTemplate;

    private String contextPath = "/v1/rest/bank/transacation";
    private String url = "http://localhost:8976";

    Logger logger = Logger.INSTANCE;

    @BeforeEach
    public void setUp() {
        restTemplate = restTemplateBuilder.rootUri(url).build();
    }

    @Test
    @Order(1)
    void createTransactionDeposit() {
        TransactionParam param = new TransactionParam();
        param.setTransId("trans111");
        param.setAccountNo("1234");
        param.setTransType(TransType.Deposit);
        param.setAmount(new BigDecimal("53.67"));
        param.setClientId("client111");

        ResponseEntity<Result> responseEntity = restTemplate.postForEntity(contextPath, param, Result.class);
        Result result = responseEntity.getBody();
        assertEquals(true,result.getSuccess());
        assertEquals(param.getTransId(),result.getData().toString());

    }

    @Test
    @Order(2)
    void createTransactionDuplicated() {
        TransactionParam param = new TransactionParam();
        param.setTransId("trans111");
        param.setAccountNo("1234");
        param.setTransType(TransType.Deposit);
        param.setAmount(new BigDecimal("1.10"));
        param.setClientId("client111");

        ResponseEntity<Result> responseEntity = restTemplate.postForEntity(contextPath, param, Result.class);
        Result result = responseEntity.getBody();
        assertEquals(false,result.getSuccess());
        logger.debug("createTransactionDuplicated  result : {}", JSON.toJSONString(result));

    }

    @Test
    @Order(3)
    void createTransactionWithdrawLackBal() {
        TransactionParam param = new TransactionParam();
        param.setTransId("trans112");
        param.setAccountNo("1234");
        param.setTransType(TransType.Withdraw);
        param.setAmount(new BigDecimal("60.00"));
        param.setClientId("client111");

        ResponseEntity<Result> responseEntity = restTemplate.postForEntity(contextPath, param, Result.class);
        Result result = responseEntity.getBody();
        assertEquals(false,result.getSuccess());
        assertEquals("balance is lack",result.getMessage());

    }


    @Test
    @Order(4)
    void createTransactionTransfer() {
        TransactionParam param = new TransactionParam();
        param.setTransId("trans113");
        param.setAccountNo("1234");
        param.setTransType(TransType.Transfer);
        param.setToAccountNo("6789");
        param.setAmount(new BigDecimal("20.39"));
        param.setClientId("client111");

        ResponseEntity<Result> responseEntity = restTemplate.postForEntity(contextPath, param, Result.class);
        Result result = responseEntity.getBody();
        assertEquals(true,result.getSuccess());
        assertEquals(param.getTransId(),result.getData().toString());

    }

    @Test
    @Order(5)
    void updateTransactionTransfer() {
        TransactionUpdateParam param = new TransactionUpdateParam();
        param.setTransId("trans113");
        param.setUpdAmount(new BigDecimal("25.39"));

        ResponseEntity<Result> responseEntity = restTemplate.exchange(contextPath, HttpMethod.PUT,new HttpEntity<>(param), Result.class);
        Result result = responseEntity.getBody();
        assertEquals(true,result.getSuccess());
    }

    @Test
    @Order(6)
    void deleteTransactionTransfer() {
        restTemplate.delete(url+contextPath+"/{transId}","trans113"+transactionService.updateTransIdSuffix);
    }

    @Test
    @Order(7)
    void listAllTransactions() {
        String getUrl = url+contextPath+"?pageNum={pageNum}&pageSize={pageSize}";

        Result result = restTemplate.getForObject(getUrl, Result.class, 1, 30);
        logger.debug("all Transactions are below ======> ");
        logger.debug(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
    }

}
