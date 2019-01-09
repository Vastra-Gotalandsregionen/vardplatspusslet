package se.vgregion.vardplatspusslet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class InitService {

    @PostConstruct
    public void init() {

    }
}
