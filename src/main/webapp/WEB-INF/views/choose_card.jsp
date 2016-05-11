<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Project Manager</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<link rel="stylesheet" href="<spring:url value="/resources/css/home.css"/>" type="text/css"/>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>
<body>
	
<%-- 	<jsp:include page="../views/header.jsp"></jsp:include> --%>			

	<div class="container">
		
		<h2>Choose one of your card</h2>
		<table class="table table-hover">
			<tbody>
				<tr>
					<th>Number</th><th>Balance</th><th>Blocked</th>
				</tr>
				<c:forEach items="${cards}" var="card">
					<tr>
						<td><a href="<spring:url value="/card/${card.id}"/>">${card.number}</a></td>
						<td>${card.balance}</td>
						<td>${card.isBlocked}</td>
					</tr>	
				</c:forEach>
			</tbody>
		</table>
		
	</div>
</body>
</html>
