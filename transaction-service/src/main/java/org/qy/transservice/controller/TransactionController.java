package org.qy.transservice.controller;

import org.qy.transsdk.common.Result;
import org.qy.transservice.model.request.TransactionParam;
import org.qy.transservice.model.request.TransactionUpdateParam;
import org.qy.transservice.model.response.HistDto;
import org.qy.transservice.service.HistService;
import org.qy.transservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * RESTFUL接口控制层
 *
 * @author qinyang
 * @date 2025/6/5 15:27
 */
@RestController
@RequestMapping(value = "/v1/rest/bank/transacation")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    HistService histService;

    @RequestMapping(method = RequestMethod.POST)
    public Result<String> createTransaction(@RequestBody TransactionParam param) {
        return Result.buildSuccess(transactionService.createTransaction(param));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Result<Boolean> updateTransaction(@RequestBody TransactionUpdateParam param) {
        return Result.buildSuccess(transactionService.updateTransaction(param));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result<List<HistDto>> listTransactions(@RequestParam(value = "transId", required = false) String transId,
                                                  @RequestParam(value = "accountId", required = false) String accountId,
                                                  @RequestParam(value = "clientId", required = false) String clientId,
                                                  @RequestParam(value = "amount", required = false) BigDecimal amount,
                                                  @RequestParam(value = "pageNum", required = true) Integer pageNum,
                                                  @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        return Result.buildSuccess(histService.listHistDto(transId, accountId, clientId, amount,pageNum,pageSize));
    }

    @RequestMapping(value = "/{transId}", method = RequestMethod.DELETE)
    public Result<Boolean> deleteTransaction(@PathVariable("transId") String transId) {
        return Result.buildSuccess(transactionService.deleteTransaction(transId));
    }

}
