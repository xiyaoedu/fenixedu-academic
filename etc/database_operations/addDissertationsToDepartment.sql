--  SQL file representing changes to the functionalities model
--  Generated at Wed Sep 26 09:47:43 WEST 2007
--  DO NOT EDIT THIS FILE, run the generating script instead

--  Preamble
SET AUTOCOMMIT = 0;

START TRANSACTION;

-- 
--  Inserting new functionalities
-- 

--  ID: 308120 UUID: 'a1032483-0c50-4d6d-aa01-ff4c39dcfcea'
INSERT INTO `ACCESSIBLE_ITEM` (`UUID`, `OJB_CONCRETE_CLASS`, `KEY_ROOT_DOMAIN_OBJECT`, `KEY_PARENT`, `KEY_MODULE`, `KEY_AVAILABILITY_POLICY`, `NAME`, `TITLE`, `DESCRIPTION`, `PATH`, `PREFIX`, `RELATIVE`, `ENABLED`, `PARAMETERS`, `ORDER_IN_MODULE`, `VISIBLE`, `MAXIMIZED`, `PRINCIPAL`, `KEY_SUPERIOR_SECTION`, `SECTION_ORDER`, `KEY_SITE`, `LAST_MODIFIED_DATE_YEAR_MONTH_DAY`, `KEY_SECTION`, `ITEM_ORDER`, `INFORMATION`, `KEY_FUNCTIONALITY`, `SHOW_NAME`, `KEY_INTRODUCTION_UNIT_SITE`) SELECT 'a1032483-0c50-4d6d-aa01-ff4c39dcfcea', 'net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality', 1, NULL, `ID_INTERNAL`, NULL, 'pt12:Dissertaçõesen13:Dissertations', NULL, NULL, '/theses.do?method=showTheses', NULL, 1, 1, 'selectedDepartmentUnitID', 8, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL FROM `ACCESSIBLE_ITEM` WHERE `UUID` = '3adea1cf-e612-456f-b8be-c24882b77c3e';

COMMIT;
