package com.atguigu.srb.core.controller.api;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.result.R;
import com.atguigu.srb.base.utils.JwtUtils;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.pojo.vo.UserBindVo;
import com.atguigu.srb.core.service.UserBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author wangjun
 * @Date 2021/9/18 17:54
 * @Description
 */
@Api(tags = "会员账号绑定")
@RestController
@RequestMapping("/api/core/userBind")
@Slf4j
public class UserBindController {

    @Resource
    private UserBindService userBindService;

    @ApiOperation("账户绑定提交数据")
    @PostMapping("/auth/bind")
    public R bind(@RequestBody UserBindVo userBindVo, HttpServletRequest request) {
        //从header中获取token，并对token进行校验，确保用户已登录，并从token中获取userId
        String token = request.getHeader("token");
        //根据用户id做账户绑定,生成一个动态表单的字符串
        Long userId = JwtUtils.getUserId(token);
        String formStr = userBindService.commitBindUser(userBindVo, userId);


        return R.ok().data("formStr", formStr);

    }

    @ApiOperation("账户绑定异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        //汇付宝向尚融宝发起回调请求时携带的参数
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("账户绑定异步回调接受的参数如下：" + JSON.toJSONString(paramMap));

        if (!RequestHelper.isSignEquals(paramMap)) {
            log.error("用户账户绑定异步回调签名验证错误:" + JSON.toJSONString(paramMap));
            return "fail";
        }

        log.info("验签成功！开始用户绑定");
        userBindService.notify(paramMap);

        return "success";
    }

}
