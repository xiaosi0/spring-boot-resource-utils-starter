package com.github.resouce.utils.dubbo;

import java.util.Arrays;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
 */
@Activate(group = { Constants.PROVIDER })
public class DubboTimeoutFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(DubboTimeoutFilter.class);

    public DubboTimeoutFilter() {
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        long elapsed = System.currentTimeMillis() - start;
        if (invoker.getUrl() != null && elapsed > (long) invoker.getUrl().getMethodParameter(invocation.getMethodName(),
                "timeout", 2147483647) && logger.isWarnEnabled()) {
            logger.error("invoke time out. method: " + invocation.getMethodName() + "arguments: "
                    + Arrays.toString(invocation.getArguments()) + " , url is " + invoker.getUrl() + ", invoke elapsed "
                    + elapsed + " ms.");
        }

        return result;
    }
}
