package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.vgregion.vardplatspusslet.domain.jpa.Message;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.TreeSet;

public interface MessageRepository extends JpaRepository<Message, Long> {

    TreeSet<Message> findAllByUnitEquals(Unit unit);

    @Query("select m from Message m where m.unit = ?1 and (m.dayOfWeek = ?2 or m.date = ?3)")
    TreeSet<Message> findAllByUnitEqualsToday(Unit unit, DayOfWeek dayOfWeek, Date date);

}
