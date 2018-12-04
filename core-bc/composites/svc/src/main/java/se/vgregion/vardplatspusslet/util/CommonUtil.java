package se.vgregion.vardplatspusslet.util;

import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;

import java.util.stream.Collectors;

public class CommonUtil {

    public static String[] getUnitIds(User user) {
        return user.getUnits().stream()
                .map(Unit::getId)
                .collect(Collectors.toList())
                .toArray(new String[]{});
    }
}
