/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.phd.alert;

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyPeriod;
import org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyReferee;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class PhdCandidacyRefereeAlert extends PhdCandidacyRefereeAlert_Base {

    static private final int INTERVAL = 7; // number of days

    private PhdCandidacyRefereeAlert() {
        super();
    }

    public PhdCandidacyRefereeAlert(final PhdCandidacyReferee referee) {
        this();
        String[] args = {};
        if (referee == null) {
            throw new DomainException("error.PhdCandidacyRefereeAlert.invalid.referee", args);
        }
        init(referee.getIndividualProgramProcess(), generateSubject(referee), generateBody(referee));
        setReferee(referee);
    }

    private MultiLanguageString generateSubject(final PhdCandidacyReferee referee) {
        return new MultiLanguageString(String.format(BundleUtil.getString(Bundle.PHD, "message.phd.email.subject.referee"),
                referee.getCandidatePerson().getName(), referee.getCandidatePerson().getName()));
    }

    private MultiLanguageString generateBody(final PhdCandidacyReferee referee) {
        return new MultiLanguageString(referee.getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod()
                .getEmailMessageBodyForRefereeForm(referee));
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.PHD, "message.phd.referee.alert", Integer.toString(INTERVAL));
    }

    @Override
    protected boolean isToDiscard() {
        return getReferee().getLetter() != null || candidacyPeriodIsOver();
    }

    private boolean candidacyPeriodIsOver() {
        final LocalDate candidacyDate = getReferee().getPhdProgramCandidacyProcess().getCandidacyDate();
        final PhdCandidacyPeriod period = getReferee().getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod();
        return new DateTime().isAfter(period.getEnd());
    }

    @Override
    protected boolean isToFire() {
        int days = Days.daysBetween(calculateStartDate().toDateMidnight(), new LocalDate().toDateMidnight()).getDays();
        return days >= INTERVAL;
    }

    private LocalDate calculateStartDate() {
        return getFireDate() != null ? getFireDate().toLocalDate() : getReferee().getPhdProgramCandidacyProcess()
                .getCandidacyDate();
    }

    @Override
    protected void generateMessage() {
        new Message(getSender(), null, Collections.<Recipient> emptyList(), buildMailSubject(), buildMailBody(), getEmail());
    }

    private Set<String> getEmail() {
        return Collections.singleton(getReferee().getEmail());
    }

    @Override
    protected void disconnect() {
        setReferee(null);
        super.disconnect();
    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

    @Override
    public boolean isSystemAlert() {
        return true;
    }

}
