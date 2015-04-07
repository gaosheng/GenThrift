<%@ page import="com.gs.thrift.result.GenThriftResult" %>
<%@ page import="com.gs.thrift.result.GenThriftFlag" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    GenThriftResult result = (GenThriftResult) session.getAttribute("result");
    if(result != null)
        if(GenThriftFlag.RUNSUCCESS == result.getFlag())
            out.println("Success!目标文件已经下载到本地！");
%>