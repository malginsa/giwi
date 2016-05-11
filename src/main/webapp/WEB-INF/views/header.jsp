<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-default">
		<div class="container-fluid">
		
			<div class="navbar-header">
      			<a class="navbar-brand" href="<spring:url value="/"/>">giwi</a>
    		</div>
    		
    		<ul class="nav navbar-nav">
    		
    			<li><a href="<spring:url value="/"/>">Choose one of your Card</a></li>
    		
    			<li class="dropdown">
          			
          			<a href="#" class="dropdown-toggle" 
          				data-toggle="dropdown" role="button" 
          				aria-expanded="false">Do operations<span class="caret"></span></a>
          	
          			<ul class="dropdown-menu" role="menu">
<!-- TODO operations permitted and can be performed -->
            			<li><a href="<spring:url value="/operations/deposit/${card.id}"/>">Deposit</a></li>
            			<li><a href="<spring:url value="/operations/withdrawal/${card.id}"/>">Withdrawal</a></li>
            			<li><a href="<spring:url value="/operations/block/${card.id}"/>">Block</a></li>
          			</ul>
          			
        		</li>
        		
    			<li><a href="<spring:url value="/transactions/${card.id}"/>">Show Transactions</a></li>
    		
    		</ul>
    		
		</div>
</nav>
