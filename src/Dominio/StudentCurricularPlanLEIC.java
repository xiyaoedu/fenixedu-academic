/*
 * Created on Jul 8, 2004
 *
 */
package Dominio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ServidorAplicacao.Servico.exceptions.BothAreasAreTheSameServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.AreaType;

/**
 * @author Jo�o Mota
 */

public class StudentCurricularPlanLEIC extends StudentCurricularPlan implements IStudentCurricularPlan {

    protected Integer secundaryBranchKey;

    protected IBranch secundaryBranch;

    protected Integer creditsInSpecializationArea;

    protected Integer creditsInSecundaryArea;

    public StudentCurricularPlanLEIC() {
        ojbConcreteClass = getClass().getName();
    }

    public IBranch getSecundaryBranch() {
        return secundaryBranch;
    }

    public Integer getSecundaryBranchKey() {
        return secundaryBranchKey;
    }

    public void setSecundaryBranch(IBranch secundaryBranch) {
        this.secundaryBranch = secundaryBranch;
    }

    public void setSecundaryBranchKey(Integer secundaryBranchKey) {
        this.secundaryBranchKey = secundaryBranchKey;
    }

    public boolean getCanChangeSpecializationArea() {
        return true;
    }

    public boolean areNewAreasCompatible(IBranch specializationArea, IBranch secundaryArea)
            throws ExcepcaoPersistencia, BothAreasAreTheSameServiceException,
            InvalidArgumentsServiceException {
        if (specializationArea == null && secundaryArea == null) {
            return true;
        }
        if (specializationArea == null || secundaryArea == null) {
            throw new InvalidArgumentsServiceException();
        }
        if (specializationArea.equals(secundaryArea)) {
            throw new BothAreasAreTheSameServiceException();
        }
        
        List curricularCoursesFromSpecArea = getCurricularCoursesFromArea(specializationArea, AreaType.SPECIALIZATION_OBJ);
        List curricularCoursesFromSecArea = getCurricularCoursesFromArea(secundaryArea, AreaType.SECONDARY_OBJ);

        List curricularCoursesBelongingToAnySpecializationAndSecundaryArea = getCurricularCoursesBelongingToAnySpecializationAndSecundaryArea();

        List studentsAprovedEnrollments = getStudentEnrollmentsWithApprovedState();
        
        studentsAprovedEnrollments.addAll(getStudentEnrollmentsWithEnrolledState());
        
        int specCredits = 0;
        int secCredits = 0;

        Iterator iterator = studentsAprovedEnrollments.iterator();
        while (iterator.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iterator.next();
            if (curricularCoursesBelongingToAnySpecializationAndSecundaryArea.contains(enrolment
                    .getCurricularCourse())){
                if(curricularCoursesFromSpecArea.contains(enrolment.getCurricularCourse())) {
                    specCredits += enrolment.getCurricularCourse().getCredits().intValue();
                }
                else if(curricularCoursesFromSecArea.contains(enrolment.getCurricularCourse())) {
                    secCredits += enrolment.getCurricularCourse().getCredits().intValue();
                }
                else {
                    return false;
                }
                
            }
        }
        return checkCredits(specializationArea.getSpecializationCredits().intValue(), specCredits, secundaryArea.getSecondaryCredits().intValue(), secCredits);
    }
    
 
    private boolean checkCredits(int specAreaCredits, int specCredits, int secAreaCredits, int secCredits) {
        if((specCredits > specAreaCredits) || (secCredits > secAreaCredits))
            return false;
        else 
            return true;
    }

    protected List getCurricularCoursesFromArea(IBranch specializationArea, AreaType areaType) {
        List curricularCourses = new ArrayList();

        List groups = specializationArea.getAreaCurricularCourseGroups(areaType);

        Iterator iterator = groups.iterator();
        while (iterator.hasNext()) {
            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator.next();
            curricularCourses.addAll(curricularCourseGroup
                    .getCurricularCourses());
        }

        return curricularCourses;
    }
    
    /**
     * @param studentCurricularPlan
     * @return CurricularCoursesBelongingToAnySpecializationAndSecundaryArea
     */
    protected List getCurricularCoursesBelongingToAnySpecializationAndSecundaryArea() {
        List curricularCourses = new ArrayList();
        List specializationAreas = getDegreeCurricularPlan().getSpecializationAreas();

        List secundaryAreas = getDegreeCurricularPlan().getSecundaryAreas();

        addAreasCurricularCoursesWithoutRepetitions(curricularCourses, specializationAreas,
                AreaType.SPECIALIZATION_OBJ);
        addAreasCurricularCoursesWithoutRepetitions(curricularCourses, secundaryAreas,
                AreaType.SECONDARY_OBJ);

        return curricularCourses;
    }

    /**
     * @param curricularCourses
     * @param specializationAreas
     */
    protected void addAreasCurricularCoursesWithoutRepetitions(List curricularCourses, List areas,
            AreaType areaType) {
        Iterator iterator = areas.iterator();
        while (iterator.hasNext()) {
            IBranch area = (IBranch) iterator.next();
            List groups = area.getAreaCurricularCourseGroups(areaType);
            Iterator iterator2 = groups.iterator();
            while (iterator2.hasNext()) {
                ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) iterator2.next();
                Iterator iterator3 = curricularCourseGroup.getCurricularCourses().iterator();
                while (iterator3.hasNext()) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) iterator3.next();
                    if (!curricularCourses.contains(curricularCourse)) {
                        curricularCourses.add(curricularCourse);
                    }
                }
            }
        }
    }
}