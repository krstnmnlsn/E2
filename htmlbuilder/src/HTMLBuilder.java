import java.io.*; 
import java.text.*;


public class HTMLBuilder {
     public static void main(String[] a) throws IOException {
    	 		// Open pre-existing html file for additions
                PrintWriter pw = new PrintWriter(new FileWriter("/Users/kristina/Documents/Year3/CPSC 310/Workspace/htmlbuilder/rounded-corners-gen.html", true));
               
                // Would want to grab data about the user's own schedule here,
                // I'll just make up a couple values for now:
                double startingTime = 8;
                double endingTime = 19;
                String timeString;
                
                
                // For each hour the user is at school print an extra row in the table
                // (sorry about the strange indexing):
                for (double time=startingTime-1; time<=endingTime-1; time=time+0.5){
                	
                	// Construct a string containing the correct hour for this row:
                	timeString = Integer.toString((((int) time) % 12) + 1);
                	if ((int) time == time){
                		timeString = timeString + ":00";
                	} else {
                		timeString = timeString + ":30";
                	}
                	
                	// Print table row 
                	pw.println("<tr><td class=\"leftcol\">" + timeString + "</td><td class=\"timeblk\"></td><td class=\"timeblk\"></td><td class=\"timeblk\"></td><td class=\"timeblk\"></td><td class=\"timeblk\"></td></tr>");

                	
                }
                
                
                
                // Print out closing of elements.
                pw.println("</tbody></table></body></html>");
                pw.close();
    }
}