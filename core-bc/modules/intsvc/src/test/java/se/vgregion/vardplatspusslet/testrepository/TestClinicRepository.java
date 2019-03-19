package se.vgregion.vardplatspusslet.testrepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Management;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestClinicRepository implements ClinicRepository {

    private Set<Clinic> clinics = new HashSet<>();

    @Override
    public List<Clinic> findDistinctByManagementIsLike(Management management) {
        return clinics.stream().filter(clinic -> clinic.getManagement().equals(management)).collect(Collectors.toList());
    }

    @Override
    public List<Clinic> findDistinctByIdIn(Collection<String> ids, Sort sort) {
        return clinics.stream().filter(clinic -> ids.contains(clinic.getId())).collect(Collectors.toList());
    }

    @Override
    public List<Clinic> findAllByOrderById() {
        return null;
    }

    @Override
    public List<Clinic> findAll() {
        return null;
    }

    @Override
    public List<Clinic> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Clinic> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Clinic> findAll(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void delete(Clinic entity) {

    }

    @Override
    public void delete(Iterable<? extends Clinic> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Clinic> S save(S entity) {
        clinics.add(entity);
        return entity;
    }

    @Override
    public <S extends Clinic> List<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public Clinic findOne(String s) {
        return null;
    }

    @Override
    public boolean exists(String s) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Clinic> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Clinic> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Clinic getOne(String s) {
        Clinic example = new Clinic();
        example.setId(s);
        return clinics.stream().filter(t -> t.getId().equals(s)).findAny().get();
    }

    @Override
    public <S extends Clinic> S findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Clinic> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Clinic> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Clinic> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Clinic> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Clinic> boolean exists(Example<S> example) {
        return false;
    }
}
