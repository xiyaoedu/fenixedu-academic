<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" scope="session" />
<div id="vert-spacer">
<table width="100%">
	<tr> 
	     <td><h2 style="display: inline;">Aluno N�: </h2><bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/>
	     	<br />
			<br />
	     </td>
  	</tr>
	<tr>
        <td align="center"><h2>DECLARA��O</h2></td>
  	</tr>
	<tr>
		<td>
			<br />	
			<br />
			<%-- The Original Declaration --%>
				<jsp:include page="./declarationTemplate1.jsp" flush="true" />
   					<logic:equal name="infoStudentCurricularPlan" property="specialization" value="Mestrado">
    		<%-- Candidate Information if necessary --%>
   				<jsp:include page="./declarationTemplate2.jsp" flush="true" />
					</logic:equal >	
				<jsp:include page="./templateDocumentReason.jsp" flush="true" />
				<jsp:include page="./templateFinal.jsp" flush="true" />
		</td>
	</tr>  	
</table>
</div>