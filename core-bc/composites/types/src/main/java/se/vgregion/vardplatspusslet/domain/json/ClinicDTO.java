package se.vgregion.vardplatspusslet.domain.json;

import java.util.ArrayList;
import java.util.List;

public class ClinicDTO {

    private String id;

    private String name;

    private List<UnitDTO> units = new ArrayList<>();

    public ClinicDTO() {
    }

    public ClinicDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UnitDTO> getUnits() {
        return units;
    }

    public void setUnits(List<UnitDTO> units) {
        this.units = units;
    }
}
