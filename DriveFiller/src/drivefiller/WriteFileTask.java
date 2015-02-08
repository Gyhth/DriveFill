/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drivefiller;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

class WriteFileTask extends SwingWorker<Void, Void> {
     
     private final String DRIVE;
     private final int MAX_FILESIZE = 10000000;
     private boolean cancelled;
     
     public WriteFileTask(String drive) {
        this.DRIVE = drive;
        this.cancelled = false;
     }

     @Override
     public Void doInBackground() {
         File driveWritingTo = new File(DRIVE);
         int writeFileSize;
         Integer fileNumber = 1;  
         FileWriter fw;
         File writingFile;
         Long ttlDriveSizeInBytes = driveWritingTo.getTotalSpace();
         Long ttlDriveSpaceFreeInBytes = driveWritingTo.getFreeSpace();
         try {
            while (!isCancelled()) {
                while (driveWritingTo.getFreeSpace() < driveWritingTo.getTotalSpace()) {
                    if (driveWritingTo.getFreeSpace() < MAX_FILESIZE) {
                        writeFileSize = (int) driveWritingTo.getFreeSpace();
                    }
                    else {
                        writeFileSize = MAX_FILESIZE;
                    }
                    writingFile = new File(DRIVE+fileNumber+".txt");
                    fw = new FileWriter(writingFile.getAbsoluteFile());
                    while (writingFile.length() < writeFileSize) {
                        fw.append("1");
                    }
                    fw.close();
                    setProgress(Math.round((float) (ttlDriveSpaceFreeInBytes.doubleValue()/ttlDriveSizeInBytes.doubleValue())/2));
                    fileNumber++;   
                }
                Files.walk(Paths.get(driveWritingTo.getAbsolutePath())).forEach((Path filePath) -> {
                    if (Files.isRegularFile(filePath)) {
                        try {                        
                            Files.delete(filePath);
                            setProgress(50 + (50-Math.round((float) (ttlDriveSpaceFreeInBytes.doubleValue()/ttlDriveSizeInBytes.doubleValue())/2)));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "An error has occured: "+e.toString(),"Unable to delete files.", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
          }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error has occured: "+e.getMessage(),"Unable to write files.", JOptionPane.ERROR_MESSAGE);
        }
        return null;
                 
    }
     
     public boolean getCancelled() {
         return this.cancelled;
     }
     
     public void setCancelled() {
         this.cancelled = true;
     }
     
     public void clear() {
         this.cancelled = false;
     }
}

