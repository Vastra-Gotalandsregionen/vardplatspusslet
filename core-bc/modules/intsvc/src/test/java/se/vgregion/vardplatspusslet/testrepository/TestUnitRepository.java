package se.vgregion.vardplatspusslet.testrepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestUnitRepository implements UnitRepository {

    private Set<Unit> units = new HashSet<>();

    @Override
    public List<Unit> findDistinctByClinicIsLike(Clinic clinic) {
        return units.stream().filter(unit -> unit.getClinic().equals(clinic)).collect(Collectors.toList());
    }

    @Override
    public Unit findUnitByIdIsLikeAndClinicIsLike(String id, Clinic clinic) {
        return null;
    }

    @Override
    public Unit findUnitWithBeds(String id) {
        return units.stream().filter(unit -> unit.getId().equals(id)).findFirst().get();
    }

    @Override
    public List<Unit> findDistinctByIdIn(Collection<String> ids, Sort sort) {
        return units.stream().filter(unit -> ids.contains(unit.getId())).collect(Collectors.toList());
    }

    @Override
    public List<Unit> findAll() {
        return new ArrayList<>(units);
    }

    @Override
    public List<Unit> findAll(Sort sort) {
        return new ArrayList<>(units);
    }

    @Override
    public Page<Unit> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Unit> findAll(Iterable<String> strings) {
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
    public void delete(Unit entity) {

    }

    @Override
    public void delete(Iterable<? extends Unit> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Unit> S save(S entity) {
        units.add(entity);
        return entity;
    }

    @Override
    public <S extends Unit> List<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public Unit findOne(String s) {
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
    public <S extends Unit> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Unit> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Unit getOne(String s) {
        return null;
    }

    @Override
    public <S extends Unit> S findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Unit> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Unit> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Unit> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Unit> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Unit> boolean exists(Example<S> example) {
        return false;
    }
}
