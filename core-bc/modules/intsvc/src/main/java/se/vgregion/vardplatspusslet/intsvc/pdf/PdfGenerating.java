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
        Boolean detailedDiet = isTrue(unit.getHasDetailedDietFeature());
        Boolean detailedMotherChildDiet = isTrue(unit.getHasDetailedMotherChildDietFeature());
        Integer antalColumn1 = motherChildDiet && diet ? 2: diet || motherChildDiet? 1 : 0;
        Integer antalColumn2 = detailedMotherChildDiet && detailedDiet ? 2: detailedDiet || detailedMotherChildDiet? 1 : 0;
        Integer antalColumn = 0;
        String bedName;
        String dietName = "";
        String dietMother = "";
        String dietChild = "";
        if (motherChildDiet || diet){
            antalColumn = antalColumn1;
        }
        else if(detailedDiet || detailedMotherChildDiet){
            antalColumn = antalColumn2;
        }
        if (antalColumn == 1 && (diet || detailedDiet)) {
            String header[] = {"Säng", "Kost"};
            table = new PdfPTable(2);
            addTableHeader(table, header);
            for (Bed bed : unit.getBeds()) {
                if (bed.getBedStatus() == BedStatus.OCCUPIED){
                    bedName = bed.getLabel();
                    if (diet){
                        dietName= (bed.getPatient().getSpecialDiet() != null && bed.getPatient().getSpecialDiet()) ? "Specialkost" : "";
                    }
                    else if(detailedDiet){
                        dietName= (bed.getPatient().getDetailedDiet() != null && bed.getPatient().getDetailedDiet().getName() != null) ? bed.getPatient().getDetailedDiet().getName() : "";
                    }

                    table.addCell(bedName);
                    table.addCell(dietName);
                }
            }
        } else if (antalColumn == 1 && (motherChildDiet || detailedMotherChildDiet)) {
            String header[] = {"Säng", "Kost Mor", "Kost barn"};
            table = new PdfPTable(3);
            addTableHeader(table, header);
            for (Bed bed : unit.getBeds()) {
                if (bed.getBedStatus() == BedStatus.OCCUPIED){
                    bedName = bed.getLabel();
                    if(motherChildDiet){
                        dietMother =  (bed.getPatient().getSpecialDietMother() != null && bed.getPatient().getSpecialDietMother()) ? "Specialkost" : "";
                        dietChild =   (bed.getPatient().getSpecialDietChild() != null && bed.getPatient().getSpecialDietChild()) ? "Specialkost" : "";
                    }
                    else if(detailedMotherChildDiet)
                    {
                        dietMother =  (bed.getPatient().getMothersDiet() != null && bed.getPatient().getMothersDiet().getName() != null) ? bed.getPatient().getMothersDiet().getName() : "";
                        dietChild =   (bed.getPatient().getChildrensDiet() != null && bed.getPatient().getChildrensDiet().getName()!= null) ? bed.getPatient().getChildrensDiet().getName() : "";
                    }

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
                if(bed.getBedStatus() == BedStatus.OCCUPIED && diet && motherChildDiet) {
                    bedName = bed.getLabel();
                    dietMother =  (bed.getPatient().getSpecialDietMother() != null && bed.getPatient().getSpecialDietMother()) ? "Specialkost" : "";
                    dietChild =   (bed.getPatient().getSpecialDietChild() != null && bed.getPatient().getSpecialDietChild()) ? "Specialkost" : "";
                    dietName = (bed.getPatient().getSpecialDiet() != null && bed.getPatient().getSpecialDiet()) ? "Specialkost" : "";
                    table.addCell(bedName);
                    table.addCell(dietMother);
                    table.addCell(dietChild);
                    table.addCell(dietName);
                }
                else if(bed.getBedStatus() == BedStatus.OCCUPIED && detailedDiet && detailedMotherChildDiet){
                    bedName = bed.getLabel();
                    dietMother =  (bed.getPatient().getMothersDiet() != null && bed.getPatient().getMothersDiet().getName() != null) ? bed.getPatient().getMothersDiet().getName() : "";
                    dietChild =   (bed.getPatient().getChildrensDiet() != null && bed.getPatient().getChildrensDiet().getName()!= null) ? bed.getPatient().getChildrensDiet().getName() : "";
                    dietName = (bed.getPatient().getDetailedDiet() != null && bed.getPatient().getDetailedDiet().getName() != null) ? bed.getPatient().getDetailedDiet().getName() : "";
                    table.addCell(bedName);
                    table.addCell(dietMother);
                    table.addCell(dietChild);
                    table.addCell(dietName);
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
