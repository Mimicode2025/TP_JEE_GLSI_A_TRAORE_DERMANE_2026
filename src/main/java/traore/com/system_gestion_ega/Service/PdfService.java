package traore.com.system_gestion_ega.Service;

import traore.com.system_gestion_ega.dto.ReleveBancaireDTO;
import com.itextpdf.text.DocumentException;

public interface PdfService {
    byte[] genererRelevePdf(ReleveBancaireDTO releve) throws DocumentException;
}
