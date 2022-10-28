package bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.ContentExtractException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractContentExtractThread;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;
import java.util.function.Supplier;

public class ContentExtractor extends AbstractContentExtractThread<Question2> {

    public ContentExtractor() {
    }

    public ContentExtractor(XWPFDocument docxFile, String urlDocxFile) {
        super(docxFile, urlDocxFile);
    }

    @Override
    public List<Question2> call() throws ContentExtractException {
        if (true) throw new ContentExtractException("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        return super.call();
    }

    @Override
    protected Supplier<Question2> factoryQuestion() {
        return Question2::new;
    }
}
