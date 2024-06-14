package org.example.datavid_cake_tracker.Service;

import org.example.datavid_cake_tracker.Exceptions.DuplicateMemberException;
import org.example.datavid_cake_tracker.Exceptions.UnderageMemberException;
import org.example.datavid_cake_tracker.Model.Member;
import org.example.datavid_cake_tracker.Repo.Repo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Service {
    private Repo repo;

    public Service(Repo repo) {
        this.repo = repo;
    }

    private void validateMemberUniqueness(Member member) throws DuplicateMemberException {
        ArrayList<Member> members = repo.getMembers();
        for (var member_iterate : members) {
            if (member_iterate.getFirstName().equals(member.getFirstName()) &&
                    member_iterate.getLastName().equals(member.getLastName()) &&
                    member_iterate.getCountry().equals(member.getCountry()) &&
                    member_iterate.getCity().equals(member.getCity())) {

                throw new DuplicateMemberException("Duplicate Member");
            }

        }
    }

    private void validateMemberAge(Member member) throws UnderageMemberException {
        if (member.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new UnderageMemberException("Members must be 18+ in order to be added");
        }
    }

    public void addMember(Member member) throws UnderageMemberException, DuplicateMemberException, SQLException {

        try {
            validateMemberUniqueness(member);
            validateMemberAge(member);

        } catch (DuplicateMemberException e) {
            throw new DuplicateMemberException("Duplicate Member");
        } catch (UnderageMemberException e) {
            throw new UnderageMemberException("Underage Member");
        }
        // If the new member is unique and is 18+, then add
        repo.addMember(member);
    }

    public ArrayList<Member> sortMembersByBirthDate() throws SQLException {

        ArrayList<Member> members = repo.getMembers();
        LocalDate today = LocalDate.now();

        return(ArrayList<Member>) members.stream()
                .sorted((m1, m2) -> {
                    LocalDate nextBirthday1 = getNextBirthday(m1.getBirthDate(), today);
                    LocalDate nextBirthday2 = getNextBirthday(m2.getBirthDate(), today);

                    long daysUntilBirthday1 = java.time.temporal.ChronoUnit.DAYS.between(today, nextBirthday1);
                    long daysUntilBirthday2 = java.time.temporal.ChronoUnit.DAYS.between(today, nextBirthday2);

                    return Long.compare(daysUntilBirthday1, daysUntilBirthday2);
                })
                .collect(Collectors.toList());
    }

    private LocalDate getNextBirthday(LocalDate birthDate, LocalDate today) {
        LocalDate nextBirthday = birthDate.withYear(today.getYear());
        if (nextBirthday.isBefore(today) || nextBirthday.equals(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }
        return nextBirthday;
    }


    public ArrayList<Member> getAll() {
        return repo.getMembers();
    }

    public void deleteMember(int id) throws SQLException {
        repo.deleteMember(id);
    }


}
