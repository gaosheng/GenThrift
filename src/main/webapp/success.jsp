<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="com.gs.thrift.param.Settings" %>

<html>
<head>
    <title>GenThrift</title>
</head>
<body>
    <%@page language="java" contentType="application/x-msdownload" pageEncoding="gb2312"%>
    <%
        response.setContentType("application/x-download");

        String file = (String) session.getAttribute("file");
        Settings settings = (Settings) session.getAttribute("settings");

        String download = settings.getCommonDir() + file + ".zip";
        String filename = URLEncoder.encode(file+".zip", "UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=" + filename);

        OutputStream outputStream = null;
        FileInputStream inputStream = null;
        try
        {
            outputStream = response.getOutputStream();
            inputStream = new FileInputStream(download);

            byte[] b = new byte[1024];
            int i;

            while((i = inputStream.read(b)) > 0)
                outputStream.write(b, 0, i);

            outputStream.flush();

            out.clear();
            out = pageContext.pushBody();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(inputStream != null)
                inputStream.close();
            if (outputStream != null)
                outputStream.close();
        }
    %>
</body>
</html>