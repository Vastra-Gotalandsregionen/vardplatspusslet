package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Message;
import se.vgregion.vardplatspusslet.repository.MessageRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Date;
import java.util.TreeSet;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UnitRepository unitRepository;

    public TreeSet<Message> findByUnit(String unitId) {
        return messageRepository.findAllByUnitEquals(unitRepository.getOne(unitId));
    }

    public TreeSet<Message> findByUnitToday(String unitId) {
        DayOfWeek dayOfWeek = new Date().toInstant().atZone(ZoneId.systemDefault()).getDayOfWeek();

        Date today = new Date();

        return messageRepository.findAllByUnitToday(
                unitRepository.getOne(unitId),
                dayOfWeek,
                today);
    }
}
