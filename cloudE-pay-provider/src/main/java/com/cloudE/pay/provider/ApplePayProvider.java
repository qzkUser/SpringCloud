package com.cloudE.pay.provider;

import com.cloudE.dto.BaseResult;
import com.cloudE.entity.User;
import com.cloudE.pay.redis.RedisService;
import com.cloudE.pay.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api("apple支付")
@RestController
public class ApplePayProvider {

    private static final Logger log = LoggerFactory.getLogger(ApplePayProvider.class);


    @Resource
    private RedisService redisService;

    @Resource
    private UserService userService;

    @GetMapping(value = "/")
    public String hello(){
        return "helloWorld";
    }


    @ApiOperation(value = "apple充值", notes = "测试接口")
    @HystrixCommand(fallbackMethod = "rechargeFallBack", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500000")})
    //@PostMapping(value = "apple/recharge")
    @GetMapping(value = "apple/recharge")
    public BaseResult<Boolean> applePayRecharge(@RequestParam("userId") Long userId, @RequestParam("amount") Double amount) throws Exception {
        log.info("apple recharge {}", amount);
        redisService.set("apple_recharge_" + userId, amount.toString(), 3600);
        return new BaseResult<>(true);
    }

    @ApiOperation(value = "分布式事物测试",notes = "事物测试接口")
    @HystrixCommand(fallbackMethod = "distributedTxTestFallBack",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500000")})
    @PostMapping(value = "user/createUser")
    public BaseResult<Boolean> distributedTxTest(@RequestParam("name")String name,@RequestParam("password")String password){
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.setStatus(new Byte("1"));
        boolean b = userService.addUser(user);
        return new BaseResult<>(b);
    }

    private BaseResult<Boolean> rechargeFallBack(Long userId, Double amount, Throwable e) {
        log.error("user:{},apple recharge amount:{}, fail :{}", userId, amount, e.getMessage(), e);
        return new BaseResult<>(false, e.getMessage());
    }

    private BaseResult<Boolean> distributedTxTestFallBack(String name,String password,Throwable e){
        log.error("create user: name:{},password:{},fail:{}",name,password,e);
        return new BaseResult<>(false,false,"1010","create user fall"+e.getMessage());
    }

}
