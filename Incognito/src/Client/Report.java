package Client;

/**
 * Created by Matthew on 11/12/2016.
 */
public class Report {

    public Report() {
//        try {
//            // create a JAXBContext capable of handling classes generated into package
//            javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance("../lib/jaxb-ri");
//            // create an object to marshal
//            TypeToMarshal objectToMarshal = new TypeToMarshal();
//            // create a Marshaller and do marshal
//            javax.xml.bind.Marshaller marshaller = jaxbContext.createMarshaller();
//            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//            marshaller.marshal(objectToMarshal, new java.io.FileOutputStream("filename.xml"));
//        } catch (javax.xml.bind.JAXBException je) {
//            je.printStackTrace();
//        } catch (java.io.FileNotFoundException io) {
//            io.printStackTrace();
//        }
    }

    private void createReport() {
        //TODO: Work out a way of asking the user if they are sure they want to produce a report
        //TODO: and then if "Yes", produce the report..
        //TODO: I could have a boolean and then if input is yes or no while boolean true, then export report....
    }
}
