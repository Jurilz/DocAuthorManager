package org.example.docauthormanager.document.converter;

import org.example.docauthormanager.document.entities.Document;
import org.example.docauthormanager.document.entities.DocumentDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DocumentDTOConverter implements Converter<DocumentDTO, Document> {
    @Override
    @NonNull
    public Document convert(DocumentDTO source) {
        return new Document(
          source.title(),
          source.body(),
          source.authors(),
          source.references()
        );
    }
}
