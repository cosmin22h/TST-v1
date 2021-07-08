package com.PSproject.TvShowsTracker.utils.exporter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class XMLFileExporter implements FileExporter {

    @Override
    public String exportData(Object object) {
        String xmlContent = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(object, sw);
            xmlContent = sw.toString();
        } catch (JAXBException e) {

        }

        return xmlContent;
    }
}
