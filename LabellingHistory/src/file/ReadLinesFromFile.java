package file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

public class ReadLinesFromFile {

	public static void main(String a[]){
        BufferedReader br = null;
        String strLine = "";
        SortedSet<String> entityNames = new TreeSet<String>();
        entityNames.add("Channel");
        entityNames.add("ComponentMapping");
        entityNames.add("ControlClass");
        entityNames.add("Dedupe");
        entityNames.add("Exception");
        entityNames.add("Rules");
        entityNames.add("Component");
        entityNames.add("DocumentClass");
        entityNames.add("Entity");
        entityNames.add("JAR");
        entityNames.add("JS");
        entityNames.add("Operation");
        entityNames.add("OperationLinkage");
        entityNames.add("ProcessTree");
        entityNames.add("Product");
        entityNames.add("Property");
        entityNames.add("Sla");
        entityNames.add("View");
        entityNames.add("Workspace");
        entityNames.add("DataField");
        entityNames.add("ListViewMaintenance");
        entityNames.add("Screen");
        entityNames.add("Source");
        entityNames.add("Branch");
        entityNames.add("Constraint");
        entityNames.add("Location");
        entityNames.add("MessageFormat");
        entityNames.add("MessageLifeCycle");
        entityNames.add("Queue");
        entityNames.add("SERVLET");
        entityNames.add("Scheduler");
        entityNames.add("Calendar");
        entityNames.add("Enquiry");
        entityNames.add("ImageOCRZoneMapping");
        entityNames.add("JSP");
        entityNames.add("Process");
        entityNames.add("RuleSet");
        entityNames.add("Store Procedure");
        entityNames.add("Application");
        entityNames.add("Communication");
        entityNames.add("ComponentInvocation");
        entityNames.add("ExceptionMapping");
        entityNames.add("Letter");
        entityNames.add("LinkedDataField");
        entityNames.add("Override");
        entityNames.add("Priority");
        entityNames.add("ReferenceDataEntity");
        entityNames.add("Stage");
        entityNames.add("Uic");
        entityNames.add("CSS");
        entityNames.add("ExceptionCategory");
        entityNames.add("Filter");
        entityNames.add("IMAGE");
        entityNames.add("ImageIDMZoneMapping");
        entityNames.add("LinkedControlClass");
        entityNames.add("LinkedDataClass");
        entityNames.add("OverrideCategory");
        entityNames.add("Template");
        entityNames.add("DataClass");
        entityNames.add("GlobalAttribute");
        entityNames.add("LinkedTemplate");
        entityNames.add("Sequence");
        entityNames.add("Table");
        StringBuilder content = new StringBuilder();
        try {
            br = new BufferedReader( new FileReader("C:\\Users\\mithul.bhansali\\Desktop\\Testting Template.txt"));
            while( (strLine = br.readLine()) != null){
            	
                content.append(strLine + "\n");
            }
            
            for(String entityName: entityNames){
            	System.out.println("--Entity Start: " + entityName);
            	System.out.println(new String(content).replace("???", entityName));
            	System.out.println("--Entity End: " + entityName);
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find the file: fileName");
        } catch (IOException e) {
            System.err.println("Unable to read the file: fileName");
        }
    }
	
	
}

