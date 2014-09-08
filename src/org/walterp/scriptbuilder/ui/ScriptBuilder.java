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
import java.awt.Component;
import java.awt.Container;
import javax.swing.JTextField;
import java.net.URISyntaxException;


//import the utilities function.
import org.walterp.scriptbuilder.util.*;


/**
*
* @author  Paul Walter
*/
public class ScriptBuilder extends javax.swing.JFrame {

    private Hashtable <String, File> shakespeare_plays = new Hashtable<String, File>();
    static Logger logger = Logger.getLogger(ScriptBuilder.class);

    private String play_path ="";
    private URI play_path_uri =null;

    private boolean debug = true;
   

    /** Creates new form ScriptBuilder */
    public ScriptBuilder() {

        try
        {
            // init the GUI components
            initComponents();

            // initialize the properties now that the gui components have been created. 
            initProperties();
        }
        catch(IOException ioe){
            logger.info("Properties not loaded: " + ioe.getMessage());
            System.out.println("Properties not loaded: " + ioe.getMessage());
        }

        


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
            populate_plays(play_path_uri);

        }else{
            System.out.println("Is not directory! " + directory.getCanonicalPath());
        }

    }

   
/**
Used to populate the dropdown that lists all the plays.

@param thisPathURI 
*/
private boolean populate_plays(URI thisPathURI)
{
    //GET A LIST OF ALL FILES IN THE DIRECTORY
    File thisList [] = XSLT_FO_Utilities.getFolderFiles(thisPathURI);

    //ITERATE THROUGH THOSE FILES TO SEE WHAT THE PLAY NAMES ARE (only if it is an xml file)
    for(int i = 0; i<thisList.length; i++)
    {
        // GET THE TITLE OF THE CURRENT FILE
        File thisFile = new File(play_path + thisList[i].getName());
        String title="";// WE ARE ONLY INTERERESTED IN THE XML FILES and nothing else

        if(thisList[i].getName().matches(".*xml$"))
        {
            title = XSLT_FO_Utilities.simpleTransformToStr(play_path + thisList[i].getName(), play_path +"shakespeare_get_title.xsl");
            title = title.replaceAll("^.*>", "");  //THIS IS A HACK, i am having trouble isolating just the text for the title, so i am using regular expressions here

            //POPULATE the "selected play" combo box, and hashtable to keep track of its associated file object
            shakespeare_plays.put(title, thisList[i]);
            combo_selected_play.addItem(title);
        }
    }
    return true;
}

/**
    This method gets fired when the user chooses one of the plays from the drop down.
*/
private void combo_selected_playActionPerformed(java.awt.event.ActionEvent evt) {
    
    String incomingStr = "";
    incomingStr = XSLT_FO_Utilities.simpleTransformToStr(play_path + shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName(), play_path +"shakespeare_get_speakers.xsl");
    incomingStr = incomingStr.replaceAll("^.*>", "");   //THIS IS A HACK, i am having trouble isolating just the text for the title, so i am using regular expressions here
    //incomingStr = incomingStr.toLowerCase();            //make sure all the characters names are of the same case for visual consistency
    String speakers [] = incomingStr.split("~");        //turn the incoming string into an array so it can be added to the combo box

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
        }
    }

}

/**
    This method will disable the first textfield, the user input, so that the user cannot name the file 
    that will be generated, so that they can only specify the location to be saved to.

    @see https://stackoverflow.com/questions/4167542/how-to-disable-file-input-field-in-jfilechooser
*/
public boolean disableTF(Container c) {
    Component[] cmps = c.getComponents();
    for (Component cmp : cmps) {
        if (cmp instanceof JTextField) {
            ((JTextField)cmp).setEnabled(false);
            return true;
        }
        if (cmp instanceof Container) {
            if(disableTF((Container) cmp)) return true;
        }
    }
    return false;
}

private void btn_transformActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_transformActionPerformed
    Hashtable<String, String> thisHash = new Hashtable<String, String>();
    
    // FIGURE OUT WHERE TO SAVE THE RESULTING FILE
    JFileChooser fc;
    fc = new JFileChooser(".");
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.setDialogTitle("Choose where to save the file");
    fc.setAcceptAllFileFilterUsed(false);
    disableTF(fc); // disallow the file name format to be inputted by the user.

        
    int ret = fc.showSaveDialog(null);
  
    if (ret == JFileChooser.APPROVE_OPTION) {
        
        // GET THE DIRECTORY 
        File file = fc.getSelectedFile();

        // CLEAN ALL LEADING AND TRAILING WHITESPACE THAT MAY BE THERE
        String comboCharStr = (String)combo_selected_character.getSelectedItem();

        String selectedChar = comboCharStr.replaceAll("^[ \\s]+|[ \\s]+$", "");
        thisHash.put("who", selectedChar);

        //LOCATE THE XSLT STYLE SHEET AND XML FILE
        URI xmlFile = XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName());
        URI xslFile = XSLT_FO_Utilities.appendFileNameToURI(play_path_uri,"shakespeare_FO_1.xsl");
        
        URI outputFile = null;

        // GENERATE FILE: APPLY THE STYLESHEET AND CREATE THE RESULTING FILE
        if(combo_fileformat.getSelectedItem().toString().equals("pdf")) {
            outputFile = XSLT_FO_Utilities.appendFileNameToURI(file.toURI(),shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".pdf");
            XSLT_FO_Utilities.create_pdf(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else if(combo_fileformat.getSelectedItem().toString().equals("preview")) {
            outputFile = XSLT_FO_Utilities.appendFileNameToURI(file.toURI(),shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".awt");
            XSLT_FO_Utilities.create_awt(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else if(combo_fileformat.getSelectedItem().toString().equals("postscript")) {
            outputFile = XSLT_FO_Utilities.appendFileNameToURI(file.toURI(),shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".ps");
            XSLT_FO_Utilities.create_postscript(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else if(combo_fileformat.getSelectedItem().toString().equals("eps")) {
            outputFile = XSLT_FO_Utilities.appendFileNameToURI(file.toURI(),shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".eps");
            XSLT_FO_Utilities.create_eps(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else if(combo_fileformat.getSelectedItem().toString().equals("mdi")) {
            outputFile = XSLT_FO_Utilities.appendFileNameToURI(file.toURI(),shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".mdi");
            XSLT_FO_Utilities.create_fop_print(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else if(combo_fileformat.getSelectedItem().toString().equals("txt")) {
            outputFile = XSLT_FO_Utilities.appendFileNameToURI(file.toURI(),shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".txt");
            XSLT_FO_Utilities.create_plain_text(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        } else if(combo_fileformat.getSelectedItem().toString().equals("rtf")) {
            outputFile = XSLT_FO_Utilities.appendFileNameToURI(file.toURI(),shakespeare_plays.get(combo_selected_play.getSelectedItem()).getName() + ".rt");   
            XSLT_FO_Utilities.create_rtf(xmlFile, xslFile, outputFile, thisHash, play_path_uri);
        }

    }
}

// =============================================
//  The following is Netbeans Generated form bulder
// =============================================


/** This method is called from within the constructor to
*   initialize the form.
*   
*   This was created with the Netbeans form creator. 
*/
private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    btn_transform = new javax.swing.JButton();
    combo_fileformat = new javax.swing.JComboBox();
    jLabel2 = new javax.swing.JLabel();
    combo_selected_character = new javax.swing.JComboBox();
    jLabel1 = new javax.swing.JLabel();
    combo_selected_play = new javax.swing.JComboBox();
    jLabel3 = new javax.swing.JLabel();
    
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
