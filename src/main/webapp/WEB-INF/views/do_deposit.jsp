<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>giwi</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<link rel="stylesheet" href="<spring:url value="/resources/css/home.css"/>" type="text/css"/>
	<link rel="stylesheet" href="<spring:url value="/resources/css/bootstrap-select.min.css"/>" type="text/css"/>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<script src="<spring:url value="/resources/js/bootstrap-select.min.js"/>"></script>

</head>
<body>

	<jsp:include page="../views/header.jsp"></jsp:include>
	
	<h3>Card</h3>
	<table class="table table-hover"><tbody><tr>
		<th>Id = ${card.id}</th> 
		<th>Number = ${card.number}</th> 
		<th>Balance = ${card.balance}</th> 
		<th>isBlocked = ${card.isBlocked}</th>
	</tr></tbody></table>
	
	<div class="container">
		<div class="row">
		
			<spring:url value="/operations/deposit/${card.id}"  var="depositUrl"/>

<!-- Here using Spring-form binding with model SubmitForm --> 		
			<form:form action="${depositUrl}" method="POST" modelAttribute="submitForm">
			
				<div class="form-group">
					<label for="deposit-amount">Amount</label>
					<form:input path="amount" cssClass="form-control" id="deposit-amount"/>
					<form:errors path="amount"/>
				</div>
			
				<button type="submit" class="btn btn-default">Submit</button>
	
			</form:form>
			
		</div>
	</div>
</body>
</html>
