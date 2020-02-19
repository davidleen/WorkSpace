<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
script>
<head>
    <meta charset="utf-8">
    <title>菜鸟教程(runoob.com)</title>
    <script>
        function displayResult(){
            var x=document.getElementById("mySelect");
            var option=document.createElement("option");
            option.text="Kiwi";
            try{
                // 对于更早的版本IE8
                x.add(option,x.options[null]);
            }catch (e){
                x.add(option,null);
            }
        }
    </script>
</head>
<body>

<form>
    <select id="mySelect">
        <option>Apple</option>
        <option>Pear</option>
        <option>Banana</option>
        <option>Orange</option>
    </select>
</form>
<br>
<button type="button" onclick="displayResult()">插入选项</button>
<p><b>注意:</b>add()方法在IE8或更高版本中正常工作,要在页面中添加一个!DOCTYPE声明。对于IE 8之前的版本还要注意额外的代码。</p>


</body>
</html>