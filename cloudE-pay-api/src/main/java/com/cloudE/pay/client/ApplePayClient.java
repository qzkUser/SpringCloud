package com.cloudE.pay.client;

import com.cloudE.dto.BaseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "cloudE-pay-provider")
public interface ApplePayClient {

    @RequestMapping(value = "apple/recharge", method = RequestMethod.GET)
    BaseResult<Boolean> recharge(@RequestParam("userId") Long userId, @RequestParam("amount") Double amount);

    @RequestMapping(value = "user/createUser",method = RequestMethod.POST)
    BaseResult<Boolean> distributedTxTest(@RequestParam("name")String name,@RequestParam("password")String password);
}
