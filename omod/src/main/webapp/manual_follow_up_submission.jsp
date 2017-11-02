<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/css/dataTables_jui.css" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />

<script type="text/javascript">
	$j(document).ready(function() {
		$j('#listaPacientes').dataTable();
	});
</script>

<!--Variaveis para setar texto no botão-->
<spring:message code="smsreminder.manual_submission.save"
	var="variable1" />

<h4>
	<spring:message code="manual_follow_up_submission" />
</h4>

<div class="box">
	<spring:message code="smsreminder.manual_submission.title" />
	<form method="post" onSubmit="return confirm('${variable1}?');">



		<table id="listaPacientes" class="display" width="600px">
			<thead>
				<tr>
					<th><spring:message
							code="smsreminder.manual_submission.patientId" /></th>
					<th><spring:message
							code="smsreminder.manual_submission.nextFila" /></th>
					<th><spring:message
							code="smsreminder.manual_submission.phoneNumber" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="notification" items="${notificationPatients}">
					<tr>
						<td align="left">${notification.patientId}</td>
						<td align="left">${notification.nextFila}</td>
						<td align="left">${notification.phoneNumber}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input type="submit" value="${variable1}">
	</form>

	<!--Função javascript para automaticamente focar o ponteiro no 1º campo da página-->
	<script type="text/javascript">
		document.forms[0].elements[0].focus();
	</script>
</div>


<%@ include file="/WEB-INF/template/footer.jsp"%>