<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
	</tbody></tr></table>
	
	<div class="container">
		<div class="row">

<!-- Here using HTML-form binding with model SubmitForm --> 		
			<form action="<spring:url value="/operations/withdrawal/${card.id}"/>" method="post" class="col-md-8 col-md-offset-2">
			
				<div class="form-group">
					<label for="withrdawal-amount">Amount</label>
					<input type="text" id="withrdawal-amount"  class="form-control" name="amount"/>
					<form:errors path="amount"/>
					
				</div>
			
				<button type="submit" class="btn btn-default">Submit</button>
	
			</form>
			
		</div>
	</div>
</body>
</html>
