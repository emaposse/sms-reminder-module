<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<openmrs:require privilege="Edit Message" otherwise="/login.htm" redirect="/module/smsreminder/manage_message.form"/>
<!--Variaveis para setar texto no botão e texto de ajuda no campo mensagem-->
<spring:message code="smsreminder.manage_message.update.save" var="variable1"/>
<spring:message code="smsreminder.manage_message.update.ph_help" var="variable2"/>

 <script type="text/javascript">
        document.forms[0].elements[0].focus();

          function soma(message) {
                        totalChar =message.value.length;
                        maximo = "150";
                        if (maximo>=totalChar) {
                            document.forms[0].total.value = maximo - totalChar;
                        } else {
                            alert("Atingiste o numero maximo de caracteres permitidos!");
                        }
                    }
 </script>


<h4>
    <spring:message code="smsreminder.manage_message.update"/>
</h4>
<a href="${pageContext.request.contextPath}/module/smsreminder/manage_message.form"><spring:message
        code="smsreminder.manage_message.title"/></a><br/><br/>

<div class="box">
    <spring:message code="smsreminder.manage_message.update.title"/>

    <form method="post"  onSubmit="return confirm('${variable1}?');">
        <table>
            <tr>
                <th valign="top"><spring:message code="smsreminder.manage_message.update.text"/></th>
                <td align="left">
                  <textarea name="message" rows="3" cols="70" placeholder="${variable2}"
                  required="required" maxlength="150" onkeydown="soma(this)" onkeyup="soma(this)" style="color: blue">${message}</textarea><br>
                  <input type="text" size=3 value="${total}" name="total" style="color: red" readonly="true" />Characters Left
                </td>
            </tr>
        </table>
        <input type="submit" value="${variable1}">
    </form>

</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>