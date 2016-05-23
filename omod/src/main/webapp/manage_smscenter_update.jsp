<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<!--Variaveis para setar texto no botão-->
<spring:message code="smsreminder.manage_smscenter.update.save" var="variable1"/>

<h4>
    <spring:message code="smsreminder.manage_smscenter.update"/>
</h4>

<a href="${pageContext.request.contextPath}/module/smsreminder/manage_smscenter.form"><spring:message
        code="smsreminder.manage_smscenter.title"/></a><br/><br/>


<div class="box">
    <spring:message code="smsreminder.manage_smscenter.update.title"/>
    <form method="post"  onSubmit="return confirm('${variable1}?');">
        <table>
            <tr>
                <th valign="top"><spring:message code="smsreminder.manage_smscenter.update.text"/></th>
                <td><input type="text" name="smscenter" maxlength="15" required="required">
                </td>
            </tr>
        </table>
        <input type="submit" value="${variable1}">
    </form>

    <!--Função javascript para automaticamente focar o ponteiro no 1º campo da página-->
    <script type="text/javascript">
        document.forms[0].elements[0].focus();
    </script>
</div>


<%@ include file="/WEB-INF/template/footer.jsp" %>