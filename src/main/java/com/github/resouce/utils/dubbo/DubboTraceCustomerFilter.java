package com.github.resouce.utils.dubbo;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.github.resouce.utils.filter.MDCFilter;
import com.github.resouce.utils.filter.MDCUtils;

@Activate(group = { Constants.CONSUMER })
public class DubboTraceCustomerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> context = new HashMap<>();
        context.put(MDCFilter.TRACE_NO, MDCUtils.getMDC());
        RpcContext.getContext().setAttachments(context);
        return invoker.invoke(invocation);
    }

}
