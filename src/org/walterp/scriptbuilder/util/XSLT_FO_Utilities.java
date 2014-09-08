// package
package org.walterp.scriptbuilder.util;

//import
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.String;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
import java.net.URI;

/**
 * Utility file for handling XSLT transforms. 
 *
 * @author Paul Walter
 */
public class XSLT_FO_Utilities {

    private static boolean debug = true;
	
	 public static URI appendFileNameToURI(URI thisUri, String filename)
	 {
	 	try
		{
			
			String tempStr = thisUri.toString() + filename;
			URI returnURI = new URI(tempStr);
			return returnURI;		}catch(Exception exp)
		{
			System.out.println("Exception with the URI: " + exp);
		}
		return null;
	 }	
    public static File [] getFolderFiles(String aPath)
    {
        try
        {
            File thisFile = new File(aPath);

            return thisFile.listFiles();

        }
        catch(Exception exp)
        {
            System.out.println("Error listing file names: " +exp);
        }
        return null;
    }
	  public static File [] getFolderFiles(URI aPathURI)
    {
        try
        {
            File thisFile = new File(aPathURI);

            return thisFile.listFiles();

        }
        catch(Exception exp)
        {
            System.out.println("Error listing file names: " +exp);
        }
        return null;
    }

    public static String [] getFolderFileNames(String aPath)
    {
        try
        {
            File thisFile = new File(aPath);

            return thisFile.list();

        }
        catch(Exception exp)
        {
            System.out.println("Error listing file names: " +exp);
        }
        return null;
    }
	 
	 public static String [] getFolderFileNames(URI aPathURI)
    {
        try
        {
            File thisFile = new File(aPathURI);

            return thisFile.list();

        }
        catch(Exception exp)
        {
            System.out.println("Error listing file names: " +exp);
        }
        return null;
    }

    /**
    * <p>Creates a pdf file and returns a true if it was successful</p>
    *
    * @param  xmlFile           the name and path of the xml file
    * @param  xslFile           the name and path of the xslt file
    * @param  outputFileName    the name and path of the xml file, you do not have to type in the file extension
    * @return                   the image at the specified URL
    */
    public static boolean create_pdf(URI xml_file_uri, URI xsl_file_uri,  URI output_file_uri, Hashtable parametersToAdd, URI thisURI)
    {
        if(create_document(xml_file_uri, xsl_file_uri,output_file_uri, MimeConstants.MIME_PDF, parametersToAdd, thisURI))
        {
            return true;
        }
        return false;

    }
    /**
    * <p>Creates a rtf file and returns a true if it was successful</p>
    *
    * @param  xmlFile           the name and path of the xml file
    * @param  xslFile           the name and path of the xslt file
    * @param  outputFileName    the name and path of the xml file, you do not have to type in the file extension
    * @return                   the image at the specified URL
    */
    public static boolean create_rtf(URI xml_file_uri, URI xsl_file_uri,URI output_file_uri, Hashtable parametersToAdd, URI thisURI)
    {
        if(create_document(xml_file_uri, xsl_file_uri,output_file_uri, MimeConstants.MIME_RTF, parametersToAdd, thisURI ))
        {
            return true;
        }
        return false;
    }
    /**
    * <p>Creates a plain text document (.txt) and returns a true if it was successful</p>
    *
    * @param  xmlFile           the name and path of the xml file
    * @param  xslFile           the name and path of the xslt file
    * @param  outputFileName    the name and path of the xml file, you do not have to type in the file extension
    * @return                   the image at the specified URL
    */
    public static boolean create_plain_text(URI xml_file_uri, URI xsl_file_uri,URI output_file_uri, Hashtable parametersToAdd, URI thisURI)
    {
        if(create_document(xml_file_uri, xsl_file_uri,output_file_uri, MimeConstants.MIME_PLAIN_TEXT, parametersToAdd, thisURI))
        {
            return true;
        }
        return false;
    }
    /**
    * <p>Creates a EPS file (an adobe file format) and returns a true if it was successful</p>
    *
    * @param  xmlFile           the name and path of the xml file
    * @param  xslFile           the name and path of the xslt file
    * @param  outputFileName    the name and path of the xml file, you do not have to type in the file extension
    * @return                   the image at the specified URL
    */
    public static boolean create_eps(URI xml_file_uri, URI xsl_file_uri,URI output_file_uri, Hashtable parametersToAdd, URI thisURI)
    {
        if(create_document(xml_file_uri, xsl_file_uri, output_file_uri, MimeConstants.MIME_EPS, parametersToAdd, thisURI))
        {
            return true;
        }
        return false;
    }
    /**
    * <p>Creates a Postscript file and returns a true if it was successful</p>
    *
    * @param  xmlFile           the name and path of the xml file
    * @param  xslFile           the name and path of the xslt file
    * @param  outputFileName    the name and path of the xml file, you do not have to type in the file extension
    * @return                   the image at the specified URL
    */
    public static boolean create_postscript(URI xml_file_uri, URI xsl_file_uri,URI output_file_uri, Hashtable parametersToAdd, URI thisURI)
    {
        if(create_document(xml_file_uri, xsl_file_uri, output_file_uri, MimeConstants.MIME_POSTSCRIPT, parametersToAdd, thisURI))
        {
            return true;
        }
        return false;
    }
    /**
    * <p>Doesn't directly write a file, it opens up a Java previewer so you can see what it looks like</p>
    *
    * @param  xmlFile           the name and path of the xml file
    * @param  xslFile           the name and path of the xslt file
    * @param  outputFileName    the name and path of the xml file, you do not have to type in the file extension
    * @return                   the image at the specified URL
    */
    public static boolean create_awt(URI xml_file_uri, URI xsl_file_uri,URI output_file_uri, Hashtable parametersToAdd, URI thisURI)
    {
        // AWT preview doesn't actually write the file, it just lets you see what it is going to look like
        if(create_document(xml_file_uri, xsl_file_uri,output_file_uri, MimeConstants.MIME_FOP_AWT_PREVIEW, parametersToAdd, thisURI))
        {
            return true;
        }
        return false;
    }
    /**
    * <p>Creates a FOP Print file (.mdi) file and returns a true if it was successful</p>
    *
    * @param  xmlFile           the name and path of the xml file
    * @param  xslFile           the name and path of the xslt file
    * @param  outputFileName    the name and path of the xml file, you do not have to type in the file extension
    * @return                   the image at the specified URL
    */
    public static boolean create_fop_print(URI xml_file_uri, URI xsl_file_uri,URI output_file_uri, Hashtable parametersToAdd, URI thisURI)
    {
        // AWT preview doesn't actually write the file, it just lets you see what it is going to look like
        if(create_document(xml_file_uri, xsl_file_uri,output_file_uri, MimeConstants.MIME_FOP_PRINT, parametersToAdd, thisURI))
        {
            return true;
        }
        return false;
    }
    /**
    * <p>Creates a document</p>
    *
    * @param  xmlFile           the name and path of the xml file
    * @param  xslFile           the name and path of the xslt file
    * @param  outputFileName    the name and path of the xml file, you do not have to type in the file extension
    * @param  mimeConst         the MIME_CONSTANT type that is being passed in.
    * @return                   the image at the specified URL
    * @see    MimeConstants
    */
	 private static boolean create_document(URI xml_file_uri, URI xsl_file_uri,  URI output_file_uri, String mimeConst, Hashtable parametersToAdd , URI thisURI)
    {

        try{
            // -----------------------------------------------------
            // Modified from: http://xmlgraphics.apache.org/fop/0.94/embedding.html
            //
            //      Paul, make sure all the jar files are included
            //
            // -----------------------------------------------------

            // Step 1: Construct a FopFactory
            // (reuse if you plan to render multiple documents!)
            FopFactory fopFactory = FopFactory.newInstance();

            // Step 2: Set up output stream.
            // Note: Using BufferedOutputStream for performance reasons (helpful with FileOutputStreams).
            //OutputStream out = new BufferedOutputStream(new FileOutputStream(new File("C:/temp/Krustz.pdf")));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(output_file_uri)));


            try {
              // Step 3: Construct fop with desired output format
              Fop fop = fopFactory.newFop(mimeConst, out);



              // Step 4: Setup JAXP using a specified XSLT file
              //    Paul, the transformer object needs this like a load in filter, then it can apply
              //    styles to the document
              TransformerFactory factory = TransformerFactory.newInstance();
              Transformer transformer = factory.newTransformer(new javax.xml.transform.stream.StreamSource(new File(xsl_file_uri))); // identity transformer

              //transformer.setParameter("who", "SALARINO");

              // STEP 4.1 ADD THE PARAMETERS
              Enumeration e = parametersToAdd.keys();
              while(e.hasMoreElements())
              {
                  String thisKey = (String) e.nextElement();
                  //System.out.println(thisKey);
                  transformer.setParameter(thisKey, parametersToAdd.get(thisKey).toString());
                  //System.out.println("Parameters added: -" + thisKey+ "-, value: -" +parametersToAdd.get(thisKey)+ "-" );
              }
              // Step 5: Setup input and output for XSLT transformation
              // Setup input stream
              //Source src = new StreamSource(new File("C:/temp/krusty.fo"));
              Source src = new StreamSource(new File(xml_file_uri));

              // Resulting SAX events (the generated FO) must be piped through to FOP
              Result res = new SAXResult(fop.getDefaultHandler());

              // Step 6: Start XSLT-FO transformation and FOP processing
              transformer.transform(src, res);

            } finally {
              //Clean-up
              out.close();
              return true;
            }

        }
        catch(Exception exp)
        {
             // Catch it
            System.out.println(exp);
        }
        return false;
    }
    /**
    * <p>Use this to grab things like the Plays title, or the characters in the play.</p>
    * <p>You will have to specify the XSLT file to do this</p>
    *
    * @param  xmlFile           the name and path of the xml file
    * @param  xslFile           the name and path of the xslt file
     *@param  delim             the delimiter you wish to split with.
    * @return                   A string delimited of the transformed document
    * @see    MimeConstants
    */
    public static String simpleTransformToStr(String xmlFile, String xslFile)
    {
        // --------------------------------------------------------------------
        // http://www.oreillynet.com/pub/a/oreilly/java/news/javaxslt_0801.html
        // --------------------------------------------------------------------

        String returnValue = "";
         try {
            // JAXP reads data using the Source interface
            Source xmlSource = new StreamSource(xmlFile);
            Source xsltSource = new StreamSource(xslFile);

            // the factory pattern supports different XSLT processors
            TransformerFactory transFact = TransformerFactory.newInstance();

            Transformer trans;
            
            trans = transFact.newTransformer(xsltSource);

            // ----------------------------------------------------------
            // http://forum.java.sun.com/thread.jspa?threadID=636335&messageID=3709470
            //      Paul, you had alot of trouble figuring out how to pass the output of the transformation, the url above is hwere you got the following chunk
            // ----------------------------------------------------------
				
            StringWriter output = new StringWriter();
            trans.transform(xmlSource, new StreamResult(output));

            return output.toString();

         } catch (TransformerException ex) {
                ex.printStackTrace();
         }
         return null;
    }

}
