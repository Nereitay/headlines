<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>

<body>

<#--list数据的显示-->
<b>展示list中的stu数据：</b>
<br>
<br>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>

    <#--判断某变量是否存在使用 "??"-->
    <#if stus??>
        <#list stus as stu>
        <#--在list集合中判断学生为小红的数据字体显示为红色-->
            <#if stu.name='Mango'>
                <tr style="color:indianred">
                    <#--${k_index}：得到循环的下标，使用方法是在stu后边加"_index"，它的值是从0开始-->
                    <td>${stu_index + 1}</td>
                    <td>${stu.name}</td>
                    <td>${stu.age}</td>
                    <td>${stu.money}</td>
                </tr>
            <#else >
                <tr style="color:steelblue">
                    <#--${k_index}：得到循环的下标，使用方法是在stu后边加"_index"，它的值是从0开始-->
                    <td>${stu_index + 1}</td>
                    <td>${stu.name}</td>
                    <td>${stu.age}</td>
                    <td>${stu.money}</td>
                </tr>
            </#if>
        </#list>
    </#if>
</table>
<#--内建函数语法格式： 变量+?+函数名称-->
stu集合的大小：${stus?size}
<hr>

<#-- Map 数据的展示 -->
<b>map数据的展示：</b>
<br/><br/>
<a href="###">方式一：通过map['keyname'].property</a><br/>
输出stu1的学生信息：<br/>
姓名：${stuMap['stu1'].name}<br/>
年龄：${stuMap['stu1'].age}<br/>
<br/>
<a href="###">方式二：通过map.keyname.property</a><br/>
输出stu2的学生信息：<br/>
姓名：${stuMap.stu2.name}<br/>
年龄：${stuMap.stu2.age}<br/>

<br/>
<a href="###">遍历map中两个学生信息：</a><br/>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#list stuMap?keys as key>
        <tr>
            <td>${key_index + 1}</td>
            <td>${stuMap[key].name}</td>
            <td>${stuMap[key].age}</td>
            <td>${stuMap[key].money}</td>
        </tr>
    </#list>
</table>
<hr>
<#--显示年月日-->
当前的日期为：${today?date}
<br>
<#--自定义格式化-->
当前的日期为：${today?string("yyyy年MM月")}

<hr>
<#--不想显示为每三位分隔的数字，可以使用c函数将数字型转成字符串输出-->
${point} -----> ${point?c}
<hr>

<#--将json字符串转成对象-->
<#--assign的作用是定义一个变量-->
<#assign text="{'bank':'DB', 'account':'123456789'}"/>
<#assign data=text?eval/>
Bank:${data.bank} Account:${data.account}

</body>
</html>