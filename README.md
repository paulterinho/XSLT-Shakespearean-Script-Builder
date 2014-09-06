XSLT-Shakespearean-Script-Builder
=================================

A Java Desktop tool for converting XML files of Shakesperean Plays into various formats (rtf, doc, txt, fop) with a specific actors parts highlighted.

Very Quick and Dirty little app. 

To use it:
1. Unzip the contents of the /xml folder
2. Open a command line and navigate to the project root.
3. Invoke the "ant" command, it will run "clean", "compile", and then "run"
4. When the Java Desktop app comes up, click the "Choose Directory" button.
5. Navigate the file chooser to the "./xml/xml_shakespeare_files" directory.
6. When the play menu populates, choose the one you wish to create a highlighted script for. 
7. Select the format you wish to transform it into (pdf, postscript, txt, rtf, etc...). 
8. Click on the "Transform" button.
9. Wait.
10. Check in that same source directory (I know, that's silly, need to change this) for the resulting file.
