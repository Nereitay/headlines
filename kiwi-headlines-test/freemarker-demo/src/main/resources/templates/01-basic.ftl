<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
    <b>普通文本 String 展示：</b><br><br>
    <#--缺失变量默认值使用 "!"-->
    Hello ${name!'Anonymous'} <br>
    <hr>
    <b>对象Student中的数据展示：</b><br/>
    姓名：${(stu.name)!'Capi'}<br/>
    年龄：${stu.age}
    <hr>
</body>
</html>