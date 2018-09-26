package se.vgregion.vardplatspusslet.domain.jpa;

/**
 * For (at least) awaiting patient.
 */
public enum PatientCategory {

    BARN("Barn"), GYN("Gyn"), KIR("Kir"), KÄK("Käk"), ONK("Onk"), ORT("Ort"), V_BARN("V-barn"), ÖNH("ÖNH");

    private final String label;

    PatientCategory(String label) {
        this.label = label;
    }
}
