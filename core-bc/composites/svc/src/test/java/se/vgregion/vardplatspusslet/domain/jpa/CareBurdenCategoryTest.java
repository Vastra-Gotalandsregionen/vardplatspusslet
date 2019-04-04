package se.vgregion.vardplatspusslet.domain.jpa;

import org.junit.Test;

import static org.junit.Assert.*;

public class CareBurdenCategoryTest {

    @Test
    public void compareTo() {
        CareBurdenCategory c1 = new CareBurdenCategory();
        CareBurdenCategory c2 = new CareBurdenCategory();

        c1.compareTo(c2);

        // Passed if no exception. Should handle null names.
    }
}