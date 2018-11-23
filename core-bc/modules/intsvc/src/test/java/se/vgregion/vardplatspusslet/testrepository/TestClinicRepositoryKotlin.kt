package se.vgregion.vardplatspusslet.testrepository

import org.springframework.data.domain.*
import se.vgregion.vardplatspusslet.domain.jpa.Clinic
import se.vgregion.vardplatspusslet.repository.ClinicRepository
import java.util.*

class TestClinicRepositoryKotlin : ClinicRepository {

    val clinics = HashSet<Clinic>()

    override fun <S : Clinic?> save(entities: MutableIterable<S>?): MutableList<S> {
        return Collections.emptyList()
    }

    override fun <S : Clinic> save(entity: S): S {
        clinics.add(entity)
        return entity
    }

    override fun deleteInBatch(entities: MutableIterable<Clinic>?) {
        
    }

    override fun findAll(): MutableList<Clinic> {
        return Collections.emptyList()
    }

    override fun findAll(sort: Sort?): MutableList<Clinic> {
        return Collections.emptyList()
    }

    override fun findAll(ids: MutableIterable<String>?): MutableList<Clinic> {
        return Collections.emptyList()
    }

    override fun <S : Clinic?> findAll(example: Example<S>?): MutableList<S> {
        return Collections.emptyList()
    }

    override fun <S : Clinic?> findAll(example: Example<S>?, sort: Sort?): MutableList<S> {
        return Collections.emptyList()
    }

    override fun findAll(pageable: Pageable?): Page<Clinic>? {
        return null
    }

    override fun <S : Clinic?> findAll(example: Example<S>?, pageable: Pageable?): Page<S>? {
        return null
    }

    override fun deleteAllInBatch() {
        
    }

    override fun <S : Clinic?> saveAndFlush(entity: S): S? {
        return null
    }

    override fun flush() {
        
    }

    override fun findAllByOrderById(): MutableList<Clinic> {
        return Collections.emptyList()
    }

    override fun deleteAll() {
        
    }

    override fun findOne(id: String?): Clinic? {
        return null
    }

    override fun <S : Clinic?> findOne(example: Example<S>?): S? {
        return null
    }

    override fun count(): Long {
        return 0
    }

    override fun <S : Clinic?> count(example: Example<S>?): Long {
        return 0
    }

    override fun getOne(id: String?): Clinic? {
        val example = Clinic()
        example.id = id
        return clinics.stream().filter { t -> t.id == id }.findAny().get()
    }

    override fun exists(id: String?): Boolean {
        return false
    }

    override fun <S : Clinic?> exists(example: Example<S>?): Boolean {
        return false
    }

    override fun delete(id: String?) {
        
    }

    override fun delete(entity: Clinic?) {
        
    }

    override fun delete(entities: MutableIterable<Clinic>?) {
        
    }

}