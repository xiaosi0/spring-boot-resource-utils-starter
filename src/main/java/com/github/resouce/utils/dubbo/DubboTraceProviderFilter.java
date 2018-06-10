package com.github.resouce.utils.dubbo;

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

@Activate(group = { Constants.PROVIDER })
public class DubboTraceProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            String traceNo = RpcContext.getContext().getAttachment(MDCFilter.TRACE_NO);
            MDCFilter.traceMDC(traceNo);
            return invoker.invoke(invocation);
        } finally {
            MDCUtils.clear();
        }
    }

}
