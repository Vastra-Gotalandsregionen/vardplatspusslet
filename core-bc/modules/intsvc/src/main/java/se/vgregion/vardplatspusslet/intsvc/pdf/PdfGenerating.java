package se.vgregion.vardplatspusslet.intsvc.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.BedStatus;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfGenerating {
    public PdfGenerating() {
    }

    public byte[] createPdf(Unit unit)
            throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, stream);
        document.open();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        Font font = FontFactory.getFont(FontFactory.COURIER_BOLD, 18, BaseColor.BLACK);
        Chunk chunk = new Chunk("            Kostlista " + unit.getName(), font);
        document.add(chunk);
        font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph("      Detta dokument är utskrivet: " + dateFormat.format(date), font);
        document.add(paragraph);
        paragraph = new Paragraph("   ");
        document.add(paragraph);
        PdfPTable table = null;
        Boolean motherChildDiet = isTrue(unit.getHasMotherChildDietFeature());
        Boolean diet = isTrue(unit.getHasKostFeature());
        Boolean detailedDietFeature = isTrue(unit.getHasDetailedDietFeature());
        Boolean detailedMotherChildDietFeature = isTrue(unit.getHasDetailedMotherChildDietFeature());
        Integer antalColumn = (motherChildDiet && diet || detailedMotherChildDietFeature && diet || detailedDietFeature && motherChildDiet || detailedDietFeature && diet || detailedDietFeature && detailedMotherChildDietFeature) ? 2 : (diet || motherChildDiet || detailedDietFeature || detailedMotherChildDietFeature) ? 1 : 0;
        if (antalColumn == 1 && (diet || detailedDietFeature)) {
            table = toOneColumnDietTable(unit, diet, detailedDietFeature);
        } else if (antalColumn == 1 && (motherChildDiet)) {
            table = toMotherChildDietTable(unit);
        } else if (antalColumn == 1 && detailedMotherChildDietFeature) {
            table = toDietsAndAllergiesPdf(unit);
        } else if (antalColumn == 2) {
            table = toTwoColumnTable(unit, motherChildDiet, detailedMotherChildDietFeature, diet, detailedDietFeature);
        }
        document.add(table);
        document.close();
        return stream.toByteArray();
    }

    private PdfPTable toOneColumnDietTable(Unit unit, Boolean diet, Boolean detailedDiet) {
        String header[] = {"Säng", "Kost"};
        PdfPTable table = new PdfPTable(2);
        addTableHeader(table, header);
        for (Bed bed : unit.getBeds()) {
            if (bed.getBedStatus() == BedStatus.OCCUPIED) {
                String bedName = bed.getLabel();
                String dietName = "";
                if (diet) {
                    dietName = (bed.getPatient().getSpecialDiet() != null && bed.getPatient().getSpecialDiet()) ? "Specialkost" : "";
                } else if (detailedDiet) {
                    dietName = (bed.getPatient().getDetailedDiet() != null && bed.getPatient().getDetailedDiet().getName() != null) ? bed.getPatient().getDetailedDiet().getName() : "";
                }
                table.addCell(bedName);
                table.addCell(dietName);
            }
        }
        return table;
    }

    private PdfPTable toTwoColumnTable(Unit unit, Boolean motherChildDiet, Boolean detailedMotherChildDiet, Boolean diet, Boolean detailedDiet) {
        String header[] = {"Säng", "Kost Mor", "Kost barn", "Kost"};
        PdfPTable table = new PdfPTable(4);
        addTableHeader(table, header);
        for (Bed bed : unit.getBeds()) {
            String bedName = "";
            String dietMother = "";
            String dietChild = "";
            String dietName = "";
            if (bed.getBedStatus() == BedStatus.OCCUPIED && diet && motherChildDiet) {
                bedName = bed.getLabel();
                dietMother = (bed.getPatient().getSpecialDietMother() != null && bed.getPatient().getSpecialDietMother()) ? "Specialkost" : "";
                dietChild = (bed.getPatient().getSpecialDietChild() != null && bed.getPatient().getSpecialDietChild()) ? "Specialkost" : "";
                dietName = (bed.getPatient().getSpecialDiet() != null && bed.getPatient().getSpecialDiet()) ? "Specialkost" : "";
                table.addCell(bedName);
                table.addCell(dietMother);
                table.addCell(dietChild);
                table.addCell(dietName);
            } else if (bed.getBedStatus() == BedStatus.OCCUPIED && detailedDiet && detailedMotherChildDiet) {
                bedName = bed.getLabel();
                dietMother = (bed.getPatient().getMothersDiet() != null && bed.getPatient().getMothersDiet().getName() != null) ? bed.getPatient().getMothersDiet().getName() : "";
                dietChild = (bed.getPatient().getChildrensDiet() != null && bed.getPatient().getChildrensDiet().getName() != null) ? bed.getPatient().getChildrensDiet().getName() : "";
                dietName = (bed.getPatient().getDetailedDiet() != null && bed.getPatient().getDetailedDiet().getName() != null) ? bed.getPatient().getDetailedDiet().getName() : "";
                table.addCell(bedName);
                table.addCell(dietMother);
                table.addCell(dietChild);
                table.addCell(dietName);
            } else if (bed.getBedStatus() == BedStatus.OCCUPIED && detailedDiet && motherChildDiet) {
                bedName = bed.getLabel();
                dietMother = (bed.getPatient().getSpecialDietMother() != null && bed.getPatient().getSpecialDietMother()) ? "Specialkost" : "";
                dietChild = (bed.getPatient().getSpecialDietChild() != null && bed.getPatient().getSpecialDietChild()) ? "Specialkost" : "";
                dietName = (bed.getPatient().getDetailedDiet() != null && bed.getPatient().getDetailedDiet().getName() != null) ? bed.getPatient().getDetailedDiet().getName() : "";
                table.addCell(bedName);
                table.addCell(dietMother);
                table.addCell(dietChild);
                table.addCell(dietName);
            } else if (bed.getBedStatus() == BedStatus.OCCUPIED && diet && detailedMotherChildDiet) {
                bedName = bed.getLabel();
                dietMother = (bed.getPatient().getMothersDiet() != null && bed.getPatient().getMothersDiet().getName() != null) ? bed.getPatient().getMothersDiet().getName() : "";
                dietChild = (bed.getPatient().getChildrensDiet() != null && bed.getPatient().getChildrensDiet().getName() != null) ? bed.getPatient().getChildrensDiet().getName() : "";
                dietName = (bed.getPatient().getSpecialDiet() != null && bed.getPatient().getSpecialDiet()) ? "Specialkost" : "";
                table.addCell(bedName);
                table.addCell(dietMother);
                table.addCell(dietChild);
                table.addCell(dietName);
            }
        }

        return table;
    }

    PdfPTable toMotherChildDietTable(Unit unit) {
        if (!isTrue(unit.getHasMotherChildDietFeature()))
            throw new RuntimeException();
        String header[] = {"Säng", "Kost Mor", "Kost barn"};
        PdfPTable table = new PdfPTable(3);
        addTableHeader(table, header);
        for (Bed bed : unit.getBeds()) {
            if (bed.getBedStatus() == BedStatus.OCCUPIED) {
                String bedName = bed.getLabel();
                String dietMother = (bed.getPatient().getSpecialDietMother() != null && bed.getPatient().getSpecialDietMother()) ? "Specialkost" : "";
                String dietChild = (bed.getPatient().getSpecialDietChild() != null && bed.getPatient().getSpecialDietChild()) ? "Specialkost" : "";
                table.addCell(bedName);
                table.addCell(dietMother);
                table.addCell(dietChild);
            }
        }
        return table;
    }

    private boolean isTrue(Boolean b) {
        return Boolean.TRUE.equals(b);
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        BaseColor color = new BaseColor(150, 250, 150);
        for (String columnTitle : headers) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(color);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        }
    }

    public PdfPTable toDietsAndAllergiesPdf(Unit unit) {
        if (!isTrue(unit.getHasDetailedMotherChildDietFeature()))
            throw new RuntimeException();
        String[] header = {"Säng", "Kost Mor", "Allergi"};
        PdfPTable table = new PdfPTable(3);
        addTableHeader(table, header);
        for (Bed bed : unit.getBeds()) {
            if (bed.getBedStatus() == BedStatus.OCCUPIED) {
                String bedName = bed.getLabel();
                String dietMother = (bed.getPatient().getMothersDiet() != null && bed.getPatient().getMothersDiet().getName() != null) ? bed.getPatient().getMothersDiet().getName() : "";
                String allergy = bed.getPatient() == null ? "" : bed.getPatient().getAllergier();
                table.addCell(bedName);
                table.addCell(dietMother);
                table.addCell(allergy);
            }
        }
        return table;
    }

}
