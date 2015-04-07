<!DOCTYPE html>

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="/bootstrap/js/bootstrap.min.js"></script>

    <title>GenThrift</title>

</head>


<body>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class='navbar navbar-inverse'>
    <div class='navbar-inner nav-collapse' style="height: auto;">

        <ul class="nav navbar-nav">
            <li class="active">
                <a href="/">首页</a>
            </li>
            <li class="active">
                <a href="/help" style="color: #5cb85c">使用说明</a>
            </li>
        </ul>
    </div>
</div>

<div class="container">

    <div class="page-header">
        <h3 style="text-align:center">GenThrift使用说明</h3>
    </div>
    <br>


    <div class="alert alert-success" role="alert">
        <strong style="font-size: 15px">1.请保证输入的.thrift文件名与上传的xxx.thrift文件同名</strong>
    </div>

    <div class="alert alert-success" role="alert">
        <strong style="font-size: 15px">2.如果.thrift文件中使用include语法包含了其他.thrift文件，请多次点击添加文件按钮，一并上传</strong>
    </div>

    <div class="alert alert-success" role="alert">
        <strong style="font-size: 15px">3.点击提交按钮，目标代码会自动下载到本地，默认压缩格式</strong>
    </div>

    <div class="alert alert-success" role="alert">
        <strong style="font-size: 15px">4.若选择生成Maven项目框架，下载文件中的ThriftTestDemo包含了自动生成的测试代码框架，只需实现相应的方法，便可Junit单元测试</strong>
    </div>

</div>

</div>
</body>
</html>

