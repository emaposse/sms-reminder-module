<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>
<h4>
    <spring:message code="smsreminder.manage_port"/>
</h4>

<a href="${pageContext.request.contextPath}/module/smsreminder/manage_port_update.form"><spring:message
        code="smsreminder.manage_port.update"/></a><br/><br/>

<b class="boxHeader"><spring:message code="smsreminder.manage_port.title"/></b>

<div class="box">
    <spring:message code="smsreminder.manage_port.text"/>${port}
</div>


<%@ include file="/WEB-INF/template/footer.jsp" %>