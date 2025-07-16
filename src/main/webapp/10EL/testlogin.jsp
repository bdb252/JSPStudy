<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
session.setAttribute("UserId", "");
session.setAttribute("UserPwd", "");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>login</h2>
	<ul>
		<li>이름 : ${ personObj.name }</li>
		<li>나이 : ${ personObj.age }</li>
	</ul>
</body>
</html>