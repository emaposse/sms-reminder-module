<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>



<openmrs:require privilege="Edit Message" otherwise="/login.htm" redirect="/module/smsreminder/manage_message.form"/>

<h4>
    <spring:message code="smsreminder.manage_message"/>
</h4>
<br />
<c:if test="${not empty openmrs_msg}">
<span class="retiredMessage">
	<spring:message code="${openmrs_msg}" text="${openmrs_msg}" />
</span>
</c:if>

<a href="${pageContext.request.contextPath}/module/smsreminder/manage_message_update.form"><spring:message
        code="smsreminder.manage_message.update"/></a><br/><br/>

<b class="boxHeader"><spring:message code="smsreminder.manage_message.title"/></b>

<div class="box">
<spring:message code="smsreminder.manage_message.text"/>${message}
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>