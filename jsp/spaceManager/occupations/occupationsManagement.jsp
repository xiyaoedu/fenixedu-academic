<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.occupations.management" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="selectedSpaceInformation">	
	
	<br/>
	
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<jsp:include page="../spaceCrumbs.jsp"/>
	
	<br/><br/>	
	
	<h3><bean:message key="label.active.occupations" bundle="SPACE_RESOURCES"/></h3>
	<fr:view schema="PersonOccupations" name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
		<fr:layout name="tabular">      			
   			<fr:property name="rowClasses" value="listClasses"/>	
   			<fr:property name="columnClasses" value="listClasses"/>
   			   			
   			<fr:property name="link(edit)" value="/manageSpaceOccupations.do?method=editSpacePersonOccupation"/>
            <fr:property name="param(edit)" value="idInternal/oid"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="/manageSpaceOccupations.do?method=deleteSpacePersonOccupation"/>
            <fr:property name="param(delete)" value="idInternal/oid"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>            
    	</fr:layout>
	</fr:view>		

	<br/><br/>
	<h3><bean:message key="label.add.person" bundle="SPACE_RESOURCES"/>:</h3>	
	<fr:create id="create" type="net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation" schema="AddPersonOccupation">	   	
		<fr:hidden slot="space" name="selectedSpaceInformation" property="space" />
	</fr:create>
	
	<br/><br/>
	
	<h3><bean:message key="label.inactive.occupations" bundle="SPACE_RESOURCES"/></h3>
	<fr:view schema="PersonOccupations" name="selectedSpaceInformation" property="space.inactivePersonSpaceOccupations">
		<fr:layout name="tabular">      			
   			<fr:property name="rowClasses" value="listClasses"/>	
   			<fr:property name="columnClasses" value="listClasses"/>
   			   			
   			<fr:property name="link(edit)" value="/manageSpaceOccupations.do?method=editSpacePersonOccupation"/>
            <fr:property name="param(edit)" value="idInternal/oid"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="/manageSpaceOccupations.do?method=deleteSpacePersonOccupation"/>
            <fr:property name="param(delete)" value="idInternal/oid"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>               
    	</fr:layout>
	</fr:view>
		
</logic:present>