package se.vgregion.vardplatspusslet.intsvc.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
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
        Boolean motherChildDiet = unit.getHasMotherChildDietFeature();
        Boolean diet = unit.getHasKostFeature();
        Integer antalColumn = motherChildDiet && diet ? 2: diet || motherChildDiet? 1 : 0;
        if (antalColumn == 1 && unit.getHasKostFeature()) {
            String header[] = {"Säng", "Kost"};
            table = new PdfPTable(2);
            addTableHeader(table, header);
            for (Bed bed : unit.getBeds()) {
                String bedName = bed.getLabel();
                String dietName = bed.getOccupied() ? (bed.getPatient().getDiet() != null? bed.getPatient().getDiet().getName(): "" ): "";
                if (dietName.length() > 0)
                {
                    table.addCell(bedName);
                    table.addCell(dietName);
                }
            }
        } else if (antalColumn == 1 && unit.getHasMotherChildDietFeature()) {
            String header[] = {"Säng", "Kost Mor", "Kost barn"};
            table = new PdfPTable(3);
            addTableHeader(table, header);
            for (Bed bed : unit.getBeds()) {
                String bedName = bed.getLabel();
                String dietMother = bed.getOccupied() ? (bed.getPatient().getDietMother() != null ?  bed.getPatient().getDietMother().getName(): "") : "";
                String dietChild = bed.getOccupied() ? (bed.getPatient().getDietChild() != null ?  bed.getPatient().getDietChild().getName(): "") : "";
                if (dietMother.length() > 0 || dietChild.length() > 0)
                {
                    table.addCell(bedName);
                    table.addCell(dietMother);
                    table.addCell(dietChild);
                }
            }
        }
        else if (antalColumn == 2) {
            String header[] = {"Säng", "Kost Mor", "Kost barn", "Kost"};
            table = new PdfPTable(4);
            addTableHeader(table, header);
            for (Bed bed : unit.getBeds()) {
                String bedName = bed.getLabel();
                String dietMother = bed.getOccupied() ? (bed.getPatient().getDietMother() != null ?  bed.getPatient().getDietMother().getName(): "") : "";
                String dietChild = bed.getOccupied() ? (bed.getPatient().getDietChild() != null ?  bed.getPatient().getDietChild().getName(): "") : "";
                String dietPatient = bed.getOccupied() ? (bed.getPatient().getDiet() != null ?  bed.getPatient().getDiet().getName(): "") : "";
                if (dietMother.length() > 0 || dietChild.length() > 0 || dietPatient.length() > 0) {
                    table.addCell(bedName);
                    table.addCell(dietMother);
                    table.addCell(dietChild);
                    table.addCell(dietPatient);
                }
            }
        }
        document.add(table);
        document.close();
        return stream.toByteArray();
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
