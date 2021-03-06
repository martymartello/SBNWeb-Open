<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
		almaviva2 - Inizio Codifica Agosto 2006
-->

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml/>
<layout:page>

	<sbn:navform
		action="/gestionebibliografica/titolo/gestioneLegameTitoloTitolo.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
		</div>

		<table border="0">
			<tr>
				<td class="etichetta"><bean:message key="ricerca.titoloRiferimento"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td width="20" class="testoNormale"><html:text
					property="areaDatiLegameTitoloVO.bidPartenza" size="10" readonly="true"
					></html:text></td>
				<td width="150" class="etichetta"><html:text
					property="areaDatiLegameTitoloVO.descPartenza" size="50" readonly="true"
					></html:text></td>
			</tr>
		</table>

		<hr color="#dde8f0" />



		<c:choose>
			<c:when
				test="${gestioneLegameTitoloTitoloForm.creaLegameInferiore eq 'SI'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/gestioneDatiLegameTitoloInf.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/gestioneDatiLegameTitoloTit.jsp" />
			</c:otherwise>
		</c:choose>


		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodGestLegTitTit">
					<bean:message key="button.ok" bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodGestLegTitTit">
					<bean:message key="button.annulla"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>

			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>
