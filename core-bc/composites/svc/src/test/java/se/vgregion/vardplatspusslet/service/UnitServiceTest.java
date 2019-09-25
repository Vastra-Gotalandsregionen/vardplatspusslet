package se.vgregion.vardplatspusslet.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.vardplatspusslet.domain.jpa.CareBurdenCategory;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.repository.CareBurdenCategoryRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
public class UnitServiceTest {

    @Autowired
    private UnitService unitService;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private CareBurdenCategoryRepository careBurdenCategoryRepository;

    @Test
    @DirtiesContext
    public void save() {
        Unit unit = new Unit();
        unit.setId("unit1");

        unitService.save(unit, true);

        assertEquals(1, unitRepository.findAll().size());
    }

    @Test
    @DirtiesContext
    public void repeatSave() {
        Unit unit = new Unit();
        unit.setId("unit1");

        ArrayList<CareBurdenCategory> categories = new ArrayList<>();

        CareBurdenCategory category1 = new CareBurdenCategory();
        CareBurdenCategory category2 = new CareBurdenCategory();

        category1 = careBurdenCategoryRepository.save(category1);
        category2 = careBurdenCategoryRepository.save(category2);

        categories.add(category1);
        categories.add(category2);

        unit.setCareBurdenCategories(categories);

        unit = unitService.save(unit, true);

        assertEquals(2, unit.getCareBurdenCategories().size());

        unit = new Unit();
        unit.setId("unit1");

        unit.setCareBurdenCategories(Arrays.asList(category2));

        unit = unitService.save(unit, true);

        assertEquals(1, unitRepository.findAll().size());
        assertEquals(1, unit.getCareBurdenCategories().size());
    }
}
