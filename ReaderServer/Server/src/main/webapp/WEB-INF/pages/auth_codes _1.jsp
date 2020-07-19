<%--
  Created by IntelliJ IDEA.
  User: davidleen29
  Date: 2015/5/14
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <script type="text/javascript" charset="utf8" src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"></script>
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.js"></script>
    <style>
        html {           -ms-text-size-adjust: 100%;            -webkit-text-size-adjust: 100%;            -webkit-tap-highlight-color: transparent;
            height: 100%;
        }
        body {
            margin: 0;            font-size: 14px;            font-family: "Helvetica Neue", Helvetica, STHeiTi, Arial, sans-serif;
            line-height: 1.5;            color: #333;            background-color: #fff;            min-height: 100%;
        }
        .form{

            margin: 0;
            outline: 0;
            float: left;
            background: #fff;
            vertical-align: middle;
            border-color: #dfdfdf;
            box-shadow: none;
            height: 36px;
            line-height: 36px;
            padding: 0 38px 0 12px;
            font-size: 14px;
            color: #000;
            border: 1px solid #ccc;
            font-weight: 400;
            box-sizing: border-box;
            width: 100%;
        }
        .submit{margin: 0;
            outline: 0;
            font-weight: 700;
            text-align: center;
            font-family: Microsoft YaHei,Hiragino Sans GB,\5b8b\4f53;
            box-shadow: 0 1px 1px #ebe7e6;
            zoom: 100%;
            height: 40px;
            padding: 0;
            font-size: 14px;
            color: #fff;
            border: none;
            background: #008bea;
            cursor: pointer;
            -webkit-appearance: none;
            -webkit-border-radius: 0;
            width: 100%;
            margin-top: 10px;}
    </style>
</head>
<body>
<form method="POST" enctype="multipart/form-data" class="ajax_form">
    <h6>平台</h6>
    <div><select  id="id_platform"  name="platform" style="width:100px">   </select></div>
    <h6>口令</h6>
    <div><input id="id_code" value=""    type="text" name="code" class="form"/></div>
    <button type="button" id="btn_submit" class="submit">提交</button>
</form>

</body>
</html>