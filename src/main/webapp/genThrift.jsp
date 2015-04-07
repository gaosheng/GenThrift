<%@ page import="com.gs.thrift.result.GenThriftResult" %>
<%@ page import="com.gs.thrift.result.GenThriftFlag" %>
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

    <script>
        $(document).ready(function () {


            $("#submit").click(function () {
                setTimeout(submitHandle, 200)
            });
        })


        //ajax请求执行结果
        function submitHandle() {
            $.get("/result", function (data, status) {
                document.getElementById("result").innerHTML = data;
            });
        }

        function addUploadButton() {
            var uploadForm = document.getElementById("upload");
            var pNode = document.createElement("p");
            pNode.innerHTML = "<input type='file' name='idlFiles'/>";
            uploadForm.appendChild(pNode);
        }

        function CheckForm() {
            if (document.form.filename.value == "") {
                alert("请输入文件名!");
                document.form.filename.focus();
                return  false;
            }

            return true;
        }

    </script>

    <!-- 设置错误显示框的css -->
    <style>
        .divcss {
            width: 550px;
            height: 200px;
            border: 2px solid #5cb85c
        }
    </style>

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

<div class="page-header">
    <h3 style="text-align:center">欢迎使用GenThrift</h3>
</div>

<form action="gen" method="post" name="form" enctype="multipart/form-data"
           onsubmit="return CheckForm();">
    <div class="container">

        <div class="row">

            <div class="col-md-1">
            </div>
            <div class="col-md-4">



                <br>
                <h4><span class="label label-success" style="border-radius: 50%">1</span>&nbsp;&nbsp;输入文件名</h4>

                <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                <input type="text" name="filename" placeholder="filename" required autofocus style="width: 120px">
                <span style="font-size: 20px">.thrift</span>

                <br><br>


                <div id="upload">
                    <h4><span class="label label-success" style="border-radius: 50%">2</span>&nbsp;&nbsp;
                        <button type="button" class="btn btn-default" onclick="addUploadButton()">添加文件 &raquo;</button>
                    </h4>

                </div>
                <br>
                <h4><span class="label label-success" style="border-radius: 50%">3</span>&nbsp;&nbsp;选择语言
                    <select name="language" class="form-control" style="display: inline-block;width: 80px;">
                        <option>java</option>
                        <option>c++</option>
                        <option>php</option>
                        <option>python</option>
                        <option>go</option>
                    </select>
                </h4>
                <br>

                <h4><span class="label label-success" style="border-radius: 50%">4</span>&nbsp;&nbsp;选择Thrift版本
                    <select name="thriftVersion" class="form-control" style="display: inline-block;width: 80px;">
                        <option>0.8.0</option>
                            <%--<option>0.9.2</option>--%>
                    </select>
                </h4>
                <br>

            </div>

            <div class="col-md-7">

                <br>

                <h4><span class="label label-success" style="border-radius: 50%">5</span>&nbsp;&nbsp;生成Maven项目框架
                    <select name="genFrame" class="form-control" style="display: inline-block;width: 80px;">
                        <option>no</option>
                        <option>yes</option>
                    </select>
                </h4>
                <br>

                <h4><span class="label label-success" style="border-radius: 50%">6</span>&nbsp;&nbsp;
                    <input id="submit" type="submit" class="btn btn-default" style="width: 80px"/></h4>
                <br>
                <h4>执行结果</h4>

                <textarea id="result"
                          style="border: 2px solid #5cb85c;margin: 0px; width: 550px; height: 200px; resize: none;">
                    <%
                        GenThriftResult result = (GenThriftResult) session.getAttribute("result");

                        if (result != null) {
                            if (result.getError() != null && result.getFlag() == GenThriftFlag.RUNERROR)
                                out.println(result.getError().trim());
                            result.setFlag(GenThriftFlag.NOTRUN);
                            result.setError("");
                        }

                    %>
                </textarea>
            </div>

        </div>
    </div>
    <div class="page-header">
    </div>
</form>
</body>
</html>

