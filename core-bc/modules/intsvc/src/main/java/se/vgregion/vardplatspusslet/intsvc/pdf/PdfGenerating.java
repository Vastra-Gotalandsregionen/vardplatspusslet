package se.vgregion.vardplatspusslet.intsvc.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
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
        Chunk chunk = new Chunk("            Kostlista " + unit.getName() , font);
        document.add(chunk);
        font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph( "      Detta dokument är utskrivet: " + dateFormat.format(date), font);
        document.add(paragraph);
        paragraph = new Paragraph("   ");
        document.add(paragraph);
        PdfPTable table = null;
        Boolean motherChildDiet = isTrue(unit.getHasMotherChildDietFeature());
        Boolean diet = isTrue(unit.getHasKostFeature());
        Integer antalColumn = motherChildDiet && diet ? 2: diet || motherChildDiet? 1 : 0;
        if (antalColumn == 1 && isTrue(unit.getHasKostFeature())) {
            String header[] = {"Säng", "Kost", "Info"};
            table = new PdfPTable(3);
            addTableHeader(table, header);
            for (Bed bed : unit.getBeds()) {
                String bedName = bed.getLabel();
                /*String dietName = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getDiet() != null? bed.getPatient().getDiet().getName() : "" ) : "";
                String info = (dietName.length() > 0 && bed.getPatient() != null)? bed.getPatient().getInfoDiet() : "";*/

                String dietName = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getSpecialDiet() ? "Special kost" : "" ) : "";
                String info = (dietName.length() > 0 && bed.getPatient() != null)? "Special kost" : "";
                if (dietName.length() > 0)
                {
                    table.addCell(bedName);
                    table.addCell(dietName);
                    table.addCell(info);
                }
            }
        } else if (antalColumn == 1 && isTrue(unit.getHasMotherChildDietFeature())) {
            String header[] = {"Säng", "Kost Mor", "Info", "Kost barn",  "Info"};
            table = new PdfPTable(5);
            addTableHeader(table, header);
            for (Bed bed : unit.getBeds()) {
                String bedName = bed.getLabel();
                /*String dietMother = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getDietMother() != null ?  bed.getPatient().getDietMother().getName() : "") : "";
                String dietChild = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getDietChild() != null ?  bed.getPatient().getDietChild().getName() : "") : "";
                String infoMother = (dietMother.length() > 0 && bed.getPatient() != null)? bed.getPatient().getInfoDietMother(): "";
                String infoChild = (dietChild.length() > 0 && bed.getPatient() != null) ? bed.getPatient().getInfoDietChild(): "";*/

                String dietMother = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getSpecialDietMother()  ?  "Special kost" : "") : "";
                String dietChild = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getSpecialDietChild() ? "Special kost" : "") : "";
                String infoMother = (dietMother.length() > 0 && bed.getPatient() != null)? "Special kost" : "";
                String infoChild = (dietChild.length() > 0 && bed.getPatient() != null) ? "Special kost" : "";
                if (dietMother.length() > 0 || dietChild.length() > 0)
                {
                    table.addCell(bedName);
                    table.addCell(dietMother);
                    table.addCell(infoMother);
                    table.addCell(dietChild);
                    table.addCell(infoChild);
                }
            }
        }
        else if (antalColumn == 2) {
            String header[] = {"Säng", "Kost Mor", "Info", "Kost barn", "Info", "Kost", "Info"};
            table = new PdfPTable(7);
            addTableHeader(table, header);
            for (Bed bed : unit.getBeds()) {
                String bedName = bed.getLabel();
               /* String dietMother = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getDietMother() != null ?  bed.getPatient().getDietMother().getName() : "") : "";
                String dietChild = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getDietChild() != null ?  bed.getPatient().getDietChild().getName() : "") : "";
                String dietPatient = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getDiet() != null ?  bed.getPatient().getDiet().getName() : "") : "";
                String infoMother = (dietMother.length() > 0 && bed.getPatient() != null) ? bed.getPatient().getInfoDietMother() : "";
                String infoChild = (dietChild.length() > 0 && bed.getPatient() != null) ? bed.getPatient().getInfoDietChild() : "";
                String info = (dietPatient.length() > 0 && bed.getPatient() != null) ? bed.getPatient().getInfoDiet(): "";*/

                String dietMother = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getSpecialDietMother() ?  "Special kost" : "") : "";
                String dietChild = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getSpecialDietChild() ? "Special kost" : "") : "";
                String dietPatient = isTrue(bed.getBedStatus() == BedStatus.OCCUPIED && bed.getPatient() != null) ? (bed.getPatient().getSpecialDiet()  ?  "Special kost" : "") : "";
                String infoMother = (dietMother.length() > 0 && bed.getPatient() != null) ?"Special kost" : "";
                String infoChild = (dietChild.length() > 0 && bed.getPatient() != null) ? "Special kost" : "";
                String info = (dietPatient.length() > 0 && bed.getPatient() != null) ?"Special kost" : "";
                if (dietMother.length() > 0 || dietChild.length() > 0 || dietPatient.length() > 0) {
                    table.addCell(bedName);
                    table.addCell(dietMother);
                    table.addCell(infoMother);
                    table.addCell(dietChild);
                    table.addCell(infoChild);
                    table.addCell(dietPatient);
                    table.addCell(info);
                }
            }
        }
        document.add(table);
        document.close();
        return stream.toByteArray();
    }

    private boolean isTrue(Boolean b) {
        return Boolean.TRUE.equals(b);
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        BaseColor color = new BaseColor(150,250,150);
        for (String columnTitle : headers) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(color);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        }
    }
}
