package traore.com.system_gestion_ega.Service.Implementation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import traore.com.system_gestion_ega.Model.Transaction;
import traore.com.system_gestion_ega.Service.PdfService;
import traore.com.system_gestion_ega.dto.ReleveBancaireDTO;

import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

@Service
public class PdfServiceImplementation implements PdfService {

    private static final BaseColor VIOLET_SOMBRE = new BaseColor(48, 25, 52);
    private static final BaseColor GRIS_FOND = new BaseColor(245, 245, 245);
    private static final BaseColor VIOLET_CLAIR = new BaseColor(102, 51, 153);

    @Override
    public byte[] genererRelevePdf(ReleveBancaireDTO releve) throws DocumentException {
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Polices
        Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, VIOLET_SOMBRE);
        Font fontValue = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);

        // --- 1. EN-TÊTE : NOM DE LA BANQUE VS BLOC PROPRIÉTAIRE ---
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(30);

        // GAUCHE : Nom de la banque (Uniquement le titre)
        PdfPCell bankCell = new PdfPCell();
        bankCell.addElement(new Phrase("EGA BANK", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26, VIOLET_SOMBRE)));
        bankCell.addElement(new Phrase("L'excellence à votre service", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY)));
        bankCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(bankCell);

        // DROITE : Informations Propriétaire alignées à droite
        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);

        // Mini-tableau pour gérer l'alignement des labels
        PdfPTable clientDetails = new PdfPTable(2);
        clientDetails.setWidthPercentage(85); // Réduit pour coller à droite
        clientDetails.setHorizontalAlignment(Element.ALIGN_RIGHT);
        clientDetails.setWidths(new float[]{1.2f, 2f}); // Ratio pour les labels/valeurs

        ajouterLigneInfos(clientDetails, "Propriétaire du compte : ", releve.getNomClient() + " " + releve.getPrenomClient(), fontLabel, fontValue);
        ajouterLigneInfos(clientDetails, "Adresse : ", releve.getAdresse(), fontLabel, fontValue);
        ajouterLigneInfos(clientDetails, "E-mail : ", releve.getEmail(), fontLabel, fontValue);
        ajouterLigneInfos(clientDetails, "Téléphone : ", releve.getTelephoneClient(), fontLabel, fontValue);

        clientCell.addElement(clientDetails);
        headerTable.addCell(clientCell);
        document.add(headerTable);

        // --- 2. RÉSUMÉ DU COMPTE ---
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(100);
        summaryTable.setSpacingAfter(25);

        String typeLabel = (releve.getTypeCompte() != null) ? releve.getTypeCompte().toString() : "COMPTE COURANT";
        PdfPCell infoCompte = new PdfPCell(new Phrase("Compte " + typeLabel + " n° " + releve.getNumCompte(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE)));
        infoCompte.setBackgroundColor(VIOLET_CLAIR);
        infoCompte.setPadding(10);
        infoCompte.setBorder(Rectangle.NO_BORDER);
        summaryTable.addCell(infoCompte);

        PdfPCell soldeCell = new PdfPCell(new Phrase("Solde actuel : " + String.format("%,.2f FCFA", releve.getSolde()),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE)));
        soldeCell.setBackgroundColor(VIOLET_SOMBRE);
        soldeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        soldeCell.setPadding(10);
        soldeCell.setBorder(Rectangle.NO_BORDER);
        summaryTable.addCell(soldeCell);
        document.add(summaryTable);

        // --- 3. TABLEAU DES OPÉRATIONS ---
        Paragraph titreTableau = new Paragraph("DÉTAIL DES OPÉRATIONS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, VIOLET_SOMBRE));
        titreTableau.setSpacingAfter(10);
        document.add(titreTableau);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 7, 4, 3});

        Stream.of("Date", "Description", "Type", "Montant").forEach(title -> {
            PdfPCell h = new PdfPCell(new Phrase(title, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE)));
            h.setBackgroundColor(VIOLET_SOMBRE);
            h.setPadding(8);
            h.setHorizontalAlignment(Element.ALIGN_CENTER);
            h.setBorder(Rectangle.NO_BORDER);
            table.addCell(h);
        });

        int i = 0;
        for (Transaction t : releve.getTransactions()) {
            BaseColor rowColor = (i % 2 == 0) ? BaseColor.WHITE : GRIS_FOND;
            ajouterCelluleStylee(table, t.getTransactionDate().toString(), rowColor);
            ajouterCelluleStylee(table, t.getDescription(), rowColor);
            ajouterCelluleStylee(table, t.getTypeTransaction().toString(), rowColor);
            ajouterCelluleStylee(table, String.format("%,.2f", t.getMontant()), rowColor);
            i++;
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    private void ajouterLigneInfos(PdfPTable table, String label, String valeur, Font fLabel, Font fValue) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, fLabel));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setHorizontalAlignment(Element.ALIGN_RIGHT); // Label aligné à droite contre la valeur
        cellLabel.setPaddingBottom(5);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(valeur != null ? valeur : "Non renseigné", fValue));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT); // Valeur collée à la marge droite
        cellValue.setPaddingBottom(5);
        table.addCell(cellValue);
    }

    private void ajouterCelluleStylee(PdfPTable table, String texte, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(texte, FontFactory.getFont(FontFactory.HELVETICA, 9)));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(7);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}