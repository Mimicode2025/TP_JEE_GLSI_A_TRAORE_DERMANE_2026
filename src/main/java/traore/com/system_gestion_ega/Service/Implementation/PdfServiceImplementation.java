package traore.com.system_gestion_ega.Service.Implementation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import traore.com.system_gestion_ega.Model.Transaction;
import traore.com.system_gestion_ega.Service.PdfService;
import traore.com.system_gestion_ega.dto.ReleveBancaireDTO;

import java.io.ByteArrayOutputStream;

@Service
public class PdfServiceImplementation implements PdfService {

    @Override
    public byte[] genererRelevePdf(ReleveBancaireDTO releve) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Ajout du contenu (Titre, Infos Client, Tableau des transactions)
        document.add(new Paragraph("RELEVE BANCAIRE EGA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph("Client: " + releve.getNomClient() + " " + releve.getPrenomClient()));
        document.add(new Paragraph("Numero de Compte: " + releve.getNumCompte()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.addCell("Date");
        table.addCell("Description");
        table.addCell("Type de tansaction");
        table.addCell("Montant");

        for (Transaction t : releve.getTransactions()) {
            table.addCell(t.getTransactionDate().toString());
            table.addCell(t.getDescription());
            table.addCell(t.getTypeTransaction().toString());
            table.addCell(t.getMontant().toString());
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }
}