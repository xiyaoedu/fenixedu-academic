package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.util.EntryPhase;

import org.joda.time.DateTime;

public abstract class StudentCandidacy extends StudentCandidacy_Base {

    public StudentCandidacy() {
	super();
    }

    protected void init(Person person, ExecutionDegree executionDegree) {
	if (executionDegree == null) {
	    throw new DomainException("execution degree cannot be null");
	}
	if (person == null) {
	    throw new DomainException("person cannot be null");
	}
	if (person.hasStudentCandidacyForExecutionDegree(executionDegree)) {
	    StudentCandidacy existentCandidacy = person.getStudentCandidacyForExecutionDegree(executionDegree);
	    if (!existentCandidacy.hasRegistration() || existentCandidacy.getRegistration().getActiveStateType().isActive())
		throw new DomainException("error.candidacy.already.created");
	}
	setExecutionDegree(executionDegree);
	setPerson(person);
	setPrecedentDegreeInformation(new PrecedentDegreeInformation());
    }

    private void checkParameters(final Person person, final ExecutionDegree executionDegree, final Person creator,
	    Double entryGrade, String contigent, String ingression, EntryPhase entryPhase) {
	if (executionDegree == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.executionDegree.cannot.be.null");
	}

	if (person == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.person.cannot.be.null");
	}

	if (person.hasDegreeCandidacyForExecutionDegree(executionDegree)) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.candidacy.already.created");
	}

	if (creator == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.creator.cannot.be.null");
	}

	if (entryPhase == null) {
	    throw new DomainException("error.candidacy.DegreeCandidacy.entryPhase.cannot.be.null");
	}

    }

    protected void init(final Person person, final ExecutionDegree executionDegree, final Person creator, Double entryGrade,
	    String contigent, String ingression, EntryPhase entryPhase) {
	checkParameters(person, executionDegree, creator, entryGrade, contigent, ingression, entryPhase);
	super.setExecutionDegree(executionDegree);
	super.setPerson(person);
	super.setPrecedentDegreeInformation(new PrecedentDegreeInformation());
	super.setEntryGrade(entryGrade);
	super.setContigent(contigent);
	super.setIngression(ingression);
	super.setEntryPhase(entryPhase);
    }

    public DateTime getCandidacyDate() {
	return Collections.min(getCandidacySituations(), CandidacySituation.DATE_COMPARATOR).getSituationDate();
    }

    public static StudentCandidacy createStudentCandidacy(ExecutionDegree executionDegree, Person studentPerson) {

	switch (executionDegree.getDegree().getTipoCurso()) {

	case BOLONHA_DEGREE:
	case DEGREE:
	    return new DegreeCandidacy(studentPerson, executionDegree);

	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return new DFACandidacy(studentPerson, executionDegree);

	case BOLONHA_PHD_PROGRAM:
	    return new PHDProgramCandidacy(studentPerson, executionDegree);

	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	    return new IMDCandidacy(studentPerson, executionDegree);

	case BOLONHA_MASTER_DEGREE:
	    return new MDCandidacy(studentPerson, executionDegree);

	case BOLONHA_SPECIALIZATION_DEGREE:
	    return new SDCandidacy(studentPerson, executionDegree);

	default:
	    return null;
	}

    }

    public static Set<StudentCandidacy> readByIds(final List<Integer> studentCandidacyIds) {
	final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();

	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy instanceof StudentCandidacy) {
		if (studentCandidacyIds.contains(candidacy.getIdInternal())) {
		    result.add((StudentCandidacy) candidacy);
		}
	    }
	}

	return result;
    }

    public static Set<StudentCandidacy> readByExecutionYear(final ExecutionYear executionYear) {
	final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy instanceof StudentCandidacy) {
		final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
		if (studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear) {
		    result.add(studentCandidacy);
		}
	    }
	}

	return result;
    }

    public static Set<StudentCandidacy> readNotConcludedBy(final ExecutionDegree executionDegree,
	    final ExecutionYear executionYear, final EntryPhase entryPhase) {
	final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy instanceof StudentCandidacy) {
		final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
		if (studentCandidacy.hasAnyCandidacySituations() && !studentCandidacy.isConcluded() && studentCandidacy.getExecutionDegree() == executionDegree
			&& studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear
			&& studentCandidacy.getEntryPhase() != null && studentCandidacy.getEntryPhase().equals(entryPhase)) {
		    result.add(studentCandidacy);
		}
	    }
	}

	return result;
    }

    public void delete() {
	removeRegistration();
	removeExecutionDegree();

	if (hasPrecedentDegreeInformation() && !getPrecedentDegreeInformation().hasStudent()) {
	    getPrecedentDegreeInformation().delete();
	}

	super.delete();
    }

    @Override
    public boolean isConcluded() {
	return (getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.REGISTERED || getActiveCandidacySituation()
		.getCandidacySituationType() == CandidacySituationType.CANCELLED);
    }

    public Ingression getIngressionEnum() {
	return getIngression() != null ? Ingression.valueOf(getIngression()) : null;
    }

    public boolean cancelCandidacy() {
	if (!isConcluded()) {
	    new CancelledCandidacySituation(this, this.getPerson());
	    return true;
	}
	return false;
    }

}