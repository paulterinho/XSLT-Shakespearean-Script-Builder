/*
 * ScriptBuilder.java
 *
 * Created on April 25, 2008, 4:21 PM By Paul Walter
 */

package org.walterp.scriptbuilder.ui;

//imports
import java.io.File;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.swing.JOptionPane;
import java.net.URL;
import javax.swing.JFileChooser;
import java.net.URI;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

//import the utilities function.
import org.walterp.scriptbuilder.util.*;


/**
 *
 * @author  Paul Walter
 */
public class ScriptBuilder extends javax.swing.JFrame {

    private Hashtable <String, File> shakespeare_plays = new Hashtable<String, File>();
	 static Logger logger = Logger.getLogger(ScriptBuilder.class);
    //private String play_path ="Script_Builder/test/shakespeare_files/";
	 //private String play_path ="E:/paul/cats/pics/Script_Builder/test/shakespeare_files/";

    private String play_path ="";
	private URI play_path_uri =null;

    private boolean debug = true;
    private final JFileChooser fc = new JFileChooser();

    /** Creates new form ScriptBuilder */
    public ScriptBuilder() {
	 	try
		{
			initProperties();
		}
		catch(IOException ioe){
			//logger.info("Properties not loaded: " + ioe.getMessage());
			System.out.println("Properties not loaded: " + ioe.getMessage());
		}
    		    
		  initComponents();


        // --------------------------------------------------------------------------------------------------------
        // NOTE:    we don't need to call the "get speakers" xslt to populate the "combo_selected_play" combobox
        //          because when you say "combo_selected_play.addItem(title);" it will call that combo box's action
        //          listener and we can handle adding elements to the "combo_selected_character"
        // --------------------------------------------------------------------------------------------------------
    }

	 private void initProperties() throws IOException
	 {
	 	// create and load default properties
		Properties defaultProps = new Properties();
		FileInputStream in = new FileInputStream("scriptbuilder.properties");
		defaultProps.load(in);
		in.close();
		
		// create application properties with default
		Properties applicationProps = new Properties(defaultProps);
		String filePath = applicationProps.getProperty("xml.path");
		File directory =new File(filePath);
		File thisDirectory =new File(".");
		if(directory.isDirectory())
		{
			play_path_uri = ( directory ).toURI();
			play_path = play_path_uri.toString();
			System.out.println("is directory!");
	  		//populate_plays(play_path_uri);

		}else{
			System.out.println("Is not directory! " + directory.getCanonicalPath());
		}
		
				//play.path


	 }
    private boolean populate_plays(URI thisPathURI)
    {
         //GET A LIST OF ALL FILES IN THE DIRECTORY
        File thisList [] = XSLT_FO_Utilities.getFolderFiles(thisPathURI);
        if(debug)System.out.println("\n\tFile list is gotten" + thisList.length);


        //ITERATE THROUGH THOSE FILES TO SEE WHAT THE PLAY NAMES ARE (only if it is an xml file)
        for(int i = 0; i<thisList.length; i++)
        {
		  		System.out.println("Tryingto aquire: " + play_path + thisList[i].getName());
            // GET THE TITLE OF THE CURRENT FILE
            File thisFile = new File(play_path + thisList[i].getName());
            if(debug)System.out.println("Paul: " + play_path + thisList[i].getName() + "  " + thisFile.getName());
            String title="";// WE ARE ONLY INTERERESTED IN THE XML FILES and nothing else

            if(thisList[i].getName().matches(".*xml$"))
            {
      			 title = XSLT_FO_Utilities.simpleTransformToStr(play_path + thisList[i].getName(), play_path +"shakespeare_get_title.xsl");
	     				System.out.println("isolating name: " + title);        
					 title = title.replaceAll("^.*>", "");  //THIS IS A HACK, i am having trouble isolating just the text for the title, so i am using regular expressions here
                      
					 
					 //POPULATE the "selected play" combo box, and hashtable to keep track of its associated file object
                shakespeare_plays.put(title, thisList[i]);
                combo_selected_play.addItem(title);

                if(debug)System.out.println("\n\there is the xml file" +play_path + thisList[i].getName());
                if(debug)System.out.println("\n\there is the xslt file" +play_path + "shakespeare_get_title.xsl");

            }
        }
        return true;
    }

  /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btn_transform = new javax.swing.JButton();
        combo_fileformat = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        combo_selected_character = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        combo_selected_play = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        choose_directory_btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Script Builder", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 18)));

        btn_transform.setText("Transform!");
        btn_transform.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_transformActionPerformed(evt);
            }
        });

        combo_fileformat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "preview", "pdf", "postscript", "eps", "mdi", "rtf", "txt", "fo" }));

        jLabel2.setText("File Format:");

        combo_selected_character.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "don't highlight anybody" }));

        jLabel1.setText("Character:");

        combo_selected_play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_selected_playActionPerformed(evt);
            }
        });

        jLabel3.setText("Play:");

        choose_directory_btn.setText("Choose Directory");
        choose_directory_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choose_directory_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel3)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(choose_directory_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                    .addComponent(combo_selected_play, javax.swing.GroupLayout.Alignment.LEADING, 0, 321, Short.MAX_VALUE)
                    .addComponent(btn_transform, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                    .addComponent(combo_fileformat, javax.swing.GroupLayout.Alignment.LEADING, 0, 321, Short.MAX_VALUE)
                    .addComponent(combo_selected_character, javax.swing.GroupLayout.Alignment.LEADING, 0, 321, Short.MAX_VALUE))
                .addGap(79, 79, 79))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(choose_directory_btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_selected_play, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(combo_selected_character, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(combo_fileformat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_transform)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void combo_selected_playActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_selected_playActionPerformed
        //JOptionPane.showMessageDialog(null,"action event! " + combo_selected_character.getSelectedItem()+": " + combo_fileformat.getSelectedItem());
        String incomingStr = "";
        incomingStr = XSLT_FO_Utilities.simpleTransformToStr(play_path + shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName(), play_path +"shakespeare_get_speakers.xsl");
        incomingStr = incomingStr.replaceAll("^.*>", "");   //THIS IS A HACK, i am having trouble isolating just the text for the title, so i am using regular expressions here
        //incomingStr = incomingStr.toLowerCase();            //make sure all the characters names are of the same case for visual consistency
        String speakers [] = incomingStr.split("~");        //turn the incoming string into an array so it can be added to the combo box

        //combo_selected_character.removeAll();
        //combo_selected_character.addItem("here");

        // REMOVE ANY EXTRA CHARACTERS LEFT ON THE COMBOBOX
        for(int y=combo_selected_character.getItemCount()-1; y>0; y--)
        {
           combo_selected_character.removeItemAt(y);
        }

        // POPULATE THE "selected speaker" DROP DOWN WITH ALL THE SPEAKERS IN THIS PLAY
        for(int i=0; i<speakers.length; i++)
        {
            if(!speakers[i].matches("^\\W*")) // MAKE SURE THERE ISN"T ANY WHITE SPACE
            {
                combo_selected_character.addItem(speakers[i]);
                //System.out.println("current speakers: "+ combo_selected_character.getItemAt(i));
            }
        }

    }//GEN-LAST:event_combo_selected_playActionPerformed

    private void btn_transformActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_transformActionPerformed
       Hashtable<String, String> thisHash  = new Hashtable<String, String>();

        // CLEAN ALL LEADING AND TRAILING WHITESPACE THAT MAY BE THERE
		  String selectedChar = ((String)combo_selected_character.getSelectedItem()).replaceAll("^[ \\s]+|[ \\s]+$", "");
		  thisHash.put("who", selectedChar);
        if(debug)System.out.println("UI: Here is who we are searching for: -"  + selectedChar + "-");

        //preview, pdf, postscript, eps, mdi, rtf, txt, fo
        URI xmlFile = XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName());
        URI xslFile = XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,"shakespeare_FO_1.xsl");
		  URI outputFile = null;

				
		//GET URI
			
	     if(combo_fileformat.getSelectedItem().toString().equals("pdf")) {
		  	  	outputFile =  XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".pdf");
           	XSLT_FO_Utilities.create_pdf(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else  if(combo_fileformat.getSelectedItem().toString().equals("preview")) {
      		outputFile =  XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".awt");
        	  	XSLT_FO_Utilities.create_awt(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else  if(combo_fileformat.getSelectedItem().toString().equals("postscript")) {
    			outputFile =  XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".ps");
       		XSLT_FO_Utilities.create_postscript(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else  if(combo_fileformat.getSelectedItem().toString().equals("eps")) {
    			outputFile =  XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".eps");
         	XSLT_FO_Utilities.create_eps(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else  if(combo_fileformat.getSelectedItem().toString().equals("mdi")) {
      		outputFile =  XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".mdi");
          	XSLT_FO_Utilities.create_fop_print(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else  if(combo_fileformat.getSelectedItem().toString().equals("txt")) {
     			outputFile =  XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".txt");     
			 	XSLT_FO_Utilities.create_plain_text(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else  if(combo_fileformat.getSelectedItem().toString().equals("rtf")) {
     		  	outputFile =  XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".rt");	      
			  	XSLT_FO_Utilities.create_rtf(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        }
		        //create_awt("test/shakespeare_files/merchant_test2.xml", "test/shakespeare_files/shakespeare_FO_1.xsl", "test/shakespeare_files/output", thisHash);

        // DO THE TRANSFORMATIONS TO A WRITTEN FILE
    }//GEN-LAST:event_btn_transformActionPerformed

    private void choose_directory_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choose_directory_btnActionPerformed
        // TODO add your handling code here:
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
      			try{          
						 File file = fc.getSelectedFile();
	                play_path_uri =fc.getSelectedFile().toURI(); // PAUL, you must use a URI in order to easily access the files you want
						 play_path = play_path_uri.toString();
						 System.out.println("\n\t\t Path: " + play_path_uri);
						 populate_plays(play_path_uri);
						 }
				   catch(Exception exp)
					{
						System.err.println("Problems applying regex" + exp + "\n\nFor this url:");	 
					}
		  } else {
            //log.append("Open command cancelled by user." + newline);
            System.out.println("Open command cancelled by user." );
        }
}//GEN-LAST:event_choose_directory_btnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScriptBuilder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_transform;
    private javax.swing.JButton choose_directory_btn;
    private javax.swing.JComboBox combo_fileformat;
    private javax.swing.JComboBox combo_selected_character;
    private javax.swing.JComboBox combo_selected_play;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
