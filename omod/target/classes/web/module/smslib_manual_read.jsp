<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>
<h4>
    <spring:message code="smsreminder.smslib_manual_read"/>
</h4>
<br />
<c:if test="${not empty openmrs_msg}">
<span class="retiredMessage">
	<spring:message code="${openmrs_msg}" text="${openmrs_msg}" />
</span>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>