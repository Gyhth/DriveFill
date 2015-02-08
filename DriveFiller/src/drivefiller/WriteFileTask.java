/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivefiller;

import java.io.File;
import java.io.FileWriter;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

class WriteFileTask extends SwingWorker<Void, Void> {
     
     private final String DRIVE;
     private final int MAX_FILESIZE = 10000000;
     
     public WriteFileTask(String drive) {
        this.DRIVE = drive;
     }

     @Override
     public Void doInBackground() {
         File driveWritingTo = new File(DRIVE);
         Integer fileNumber = 1;  
         FileWriter fw;
         File writingFile;
         Long ttlDriveSizeInBytes = driveWritingTo.getTotalSpace();
         Long ttlDriveSpaceFreeInBytes = driveWritingTo.getFreeSpace();
         try {   
            while (driveWritingTo.getFreeSpace() < driveWritingTo.getTotalSpace()) {
                writingFile = new File(DRIVE+fileNumber+".txt");
                fw = new FileWriter(writingFile.getAbsoluteFile());
                while (writingFile.length() < MAX_FILESIZE) {
                    fw.append("1");
                }
                fw.close();
                setProgress(Math.round((float) (ttlDriveSpaceFreeInBytes.doubleValue()/ttlDriveSizeInBytes.doubleValue())));
                fileNumber++;   
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error has occured: "+e.getMessage(),"Unable to write files.", JOptionPane.ERROR_MESSAGE);
        }
        return null;
                 
    }
}

