<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<li
		<c:if test='<%=request.getRequestURI().contains("/manage_port")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/smsreminder/manage_port.form"><spring:message
				code="smsreminder.manage_port" /></a>
	</li>
	<li
		<c:if test='<%=request.getRequestURI().contains("/manage_smscenter")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/smsreminder/manage_smscenter.form"><spring:message
				code="smsreminder.manage_smscenter" /></a>
	</li>
	<li
		<c:if test='<%=request.getRequestURI().contains("/manage_message")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/smsreminder/manage_message.form"><spring:message
				code="smsreminder.manage_message" /></a>
	</li>
	<li
		<c:if test='<%=request.getRequestURI().contains("/manual_submission")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/smsreminder/manual_submission.form"><spring:message
				code="smsreminder.manual_submission" /></a>
	</li>

	<li
		<c:if test='<%=request.getRequestURI().contains("/smslib_manual_submission")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/smsreminder/smslib_manual_submission.form"><spring:message
				code="smsreminder.smslib_manual_submission" /></a>
	</li>
	<li
		<c:if test='<%=request.getRequestURI().contains("/manual_follow_up_submission")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/smsreminder/manual_follow_up_submission.form"><spring:message
				code="smsreminder.manual_follow_up_submission" /></a>
	</li>

	<li
		<c:if test='<%=request.getRequestURI().contains("/smslib_manual_read")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/smsreminder/smslib_manual_read.form"><spring:message
				code="smsreminder.smslib_manual_read" /></a>
	</li>

	<!-- Add further links here -->
</ul>
<h3>
	<spring:message code="smsreminder.title" />
</h3>
