package com.github.resouce.utils.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class MDCFilter implements Filter {
	public static String TRACE_NO = "traceNo";
	public static String mdcPrefix;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest httpReq = (HttpServletRequest) request;
			String traceNo = httpReq.getHeader(TRACE_NO);
			traceMDC(traceNo);
			
			chain.doFilter(request, response);
		} finally {
			MDCUtils.clear();
		}
	}

	@Override
	public void destroy() {
	}
	
	public static void traceMDC(String traceNo){
	    StringBuilder builder = new StringBuilder();
	    if (StringUtils.isNotBlank(traceNo)) {
            builder.append(traceNo);
            builder.append("_");
        }
        if (StringUtils.isNotBlank(mdcPrefix)) {
            builder.append(mdcPrefix);
            builder.append("-");
        }
        builder.append(shortJavaUUID());
        
        MDCUtils.initMDC(builder.toString());
	}

	public String getMdcPrefix() {
		return mdcPrefix;
	}

	public void setMdcPrefix(String mdcPrefix) {
		if (StringUtils.isNotBlank(mdcPrefix)) {
			this.mdcPrefix = mdcPrefix;
		}
	}

	// 同一时间点有重复，基于时间
	public String shortUUID() {
		StringBuilder builder = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		Date date = new Date();
		String time = sdf.format(date);
		for (int i = 0; i < time.length() / 2; i++) {
			String singleChar;
			String x = time.substring(i * 2, (i + 1) * 2);
			int b = Integer.parseInt(x);
			if (b < 10) {
				singleChar = Integer.toHexString(Integer.parseInt(x));
			} else if (b >= 10 && b < 36) {
				singleChar = String.valueOf((char) (Integer.parseInt(x) + 55));
			} else {
				singleChar = String.valueOf((char) (Integer.parseInt(x) + 61));
			}
			builder.append(singleChar);
		}
		return builder.toString();
	}

	// 10000次内有五次左右重复，可接受
	public static String shortJavaUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
	}
	
	public static void main(String[] args) {
		MDCFilter mdc = new MDCFilter();
		Map<String, String> m = new HashMap<String, String>();
		for (int i = 0; i < 5000; i++) {
			System.out.println(mdc.shortJavaUUID());
			System.out.println(mdc.shortUUID());
			m.put(mdc.shortJavaUUID(), mdc.shortJavaUUID());
		}
		System.out.println(m.size());
	}
}
