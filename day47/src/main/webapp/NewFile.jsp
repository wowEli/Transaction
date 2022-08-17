<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="b1" class="vo.Bank1" />
<jsp:useBean id="b2" class="vo.Bank2" />
<jsp:useBean id="dao1" class="dao.Bank1DAO" />
<jsp:useBean id="dao2" class="dao.Bank2DAO" />
<%
	if(request.getMethod().equals("POST")){
		if(dao1.transfer(Integer.parseInt(request.getParameter("balance")))){
			out.print("<script>alert('성공!');</script>");
		}
		else{
			out.print("<script>alert('실패...');</script>");
		}
	}

	b1=dao1.selectOne(b1);
	b2=dao2.selectOne(b2);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

신한: <%=b1.getBname()%> | <%=b1.getBalance()%>원 <br>
국민: <%=b2.getBname()%> | <%=b2.getBalance()%>원

<hr>

<form method="post">
	이체할 금액: <input type="number" value="0" min="0" name="balance">원
	<input type="submit" value="계좌이체">
</form>

</body>
</html>