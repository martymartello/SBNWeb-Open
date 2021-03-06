<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<table align="center">
	<tr>
		<td align="center"><html:submit property="methodGestDes">
			<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
		</html:submit></td>
		<c:choose>
			<c:when test="${GestioneDescrittoreForm.enableElimina}">
				<sbn:checkAttivita idControllo="CANCELLA">
					<td align="center"><html:submit property="methodGestDes">
						<bean:message key="button.elimina"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</sbn:checkAttivita>
			</c:when>
		</c:choose>
		<sbn:checkAttivita idControllo="SOGGETTI">
			<td align="center"><html:submit property="methodGestDes">
				<bean:message key="button.soggetti" bundle="gestioneSemanticaLabels" />
			</html:submit></td>
		</sbn:checkAttivita>
		<td align="center"><html:submit property="methodGestDes">
			<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
		</html:submit></td>
		<td align="center"><html:submit property="methodGestDes">
			<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
		</html:submit></td>
	</tr>
</table>





