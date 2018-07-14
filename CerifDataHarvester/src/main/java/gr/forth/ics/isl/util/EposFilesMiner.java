package gr.forth.ics.isl.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class EposFilesMiner {
    private static final Logger logger=Logger.getLogger(EposFilesMiner.class);
    private static final String OUTPUT_FOLDER_NAME="eposData";
    private static final String EXPECTED_FILENAME_WITHOUT_EXTENSION="metadata";
    private static final String EXPECTED_FILENAME_EXTENSION=".xml";
    
    private static void traverseFolderContents(String initialFolder, boolean recursiveSearch) throws IOException{
        int copiedFilesCounter=1;
        File startDir=new File(initialFolder);
        List<File> foldersToCheck=new ArrayList<>();
        for(File f : startDir.listFiles()){
            if(f.isFile() && f.getName().equals(EXPECTED_FILENAME_WITHOUT_EXTENSION+EXPECTED_FILENAME_EXTENSION)){
                File newFile=new File(OUTPUT_FOLDER_NAME+"/"+EXPECTED_FILENAME_WITHOUT_EXTENSION+copiedFilesCounter+EXPECTED_FILENAME_EXTENSION);
                logger.info("Copying file "+f.getAbsolutePath()+" to file "+newFile.getAbsolutePath());
                Files.copy(f.toPath(), newFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                copiedFilesCounter+=1;
            }else if(f.isDirectory()){
                foldersToCheck.add(f);
            }
        }
        if(recursiveSearch){
            while(!foldersToCheck.isEmpty()){
                File fold=foldersToCheck.remove(0);
                for(File f : fold.listFiles()){
                    if(f.isFile() && f.getName().equals(EXPECTED_FILENAME_WITHOUT_EXTENSION+EXPECTED_FILENAME_EXTENSION)){                        
                        File newFile=new File(OUTPUT_FOLDER_NAME+"/"+EXPECTED_FILENAME_WITHOUT_EXTENSION+copiedFilesCounter+EXPECTED_FILENAME_EXTENSION);
                        logger.info("Copying file "+f.getAbsolutePath()+" to file "+newFile.getAbsolutePath());
                        Files.copy(f.toPath(), newFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                        copiedFilesCounter+=1;
                    }else if(f.isDirectory()){
                        foldersToCheck.add(f);
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) throws IOException{
        traverseFolderContents("C:/temp/EPOS/SEISMIC STATIONS/", true);
    }
}