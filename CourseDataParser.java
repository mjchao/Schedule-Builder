import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class CourseDataParser {
	
	//Global variables that are necessary
	private String title = null, description = null, prereqs = null;
	int credits;
	
	private ArrayList< ArrayList< String > > prereqsList;
	//Constructor. Useless
	public CourseDataParser( String filename ){
		getInfo( filename );
		prereqsList = prerequisites( filename );
	}
	
	//Parses title, description, and prerequisites as a long string out of a file
	//with name "fileName"
	public void getInfo(String fileName){
		File file = new File(fileName);
		BufferedReader in = null;
		//String title = null, description = null, prereqs = null;
		
		//I have no idea what this try catch stuff is but it was like this online
		try {
			in = new BufferedReader(new FileReader(file));
			String text = null;

			//Loop through each line
		    while ((text = in.readLine()) != null) {
		        if(text.contains("Title:") != false){
		        	title = text.substring(7);
		        }
		        else if(text.contains("Credits:") != false){
		        	String temp = text.substring(8);
		        	credits = Integer.parseInt(temp);
		        }
		        else if(text.contains("Description:") != false){
		        	description = text.substring(13);
		        }
		        else if(text.contains("Pre-requisites:") != false){
		        	prereqs = text.substring(16);
		        }
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (in != null) {
		            in.close();
		        }
		    } catch (IOException e) {
		    }
		}
		
		
	}
	
	
	/*This method takes a long string of all prerequisites and stores them in a 
	 * 2D array list where one row consists of classes that satisfy the same prereq (eg engr101 and 151)
	 * and each new row consists of another requirement
	 * Example. [EECS151, EECS101]
	 * 			[EECS203]			
	 * 		Means that it needs EECS 151 or 101 and EECS 203	
	 */
	public ArrayList<ArrayList<String>> prerequisites(String pre){
		ArrayList<ArrayList<String>> preList = new ArrayList<ArrayList<String>>();
		//ArrayList<String> tempArray = new ArrayList<String>();
		ArrayList<String> temp2 = new ArrayList<String>();
		
		int comma = pre.indexOf(',');
		int space = pre.indexOf(' ');
		int start = 0;
		do{			
			//If neither a space nor a comma is found, then just add the word to the array
			if(comma == -1 && space ==-1){
				ArrayList<String> tempArray = new ArrayList<String>();
				String tempString = pre.substring(start);
				tempArray.add(tempString);
				preList.add(tempArray);
			}
			
			//If comma comes before a space...
			else if(space == -1 || (comma != -1 && comma < space)){
				ArrayList<String> tempArray = new ArrayList<String>();
				
				//This part basically takes a block of comma separated strings
				//and adds them all to an arraylist and then adds that to the 
				//overall arraylist
				
				//Comma comes before space, add the string up to the comma to the arraylist
				while(comma != -1 && comma < space){
					String tempString = pre.substring(start, comma);
					tempArray.add(tempString);
					//Set next starting point (after the comma/space we just used) and 
					//finds the next comma/space
					start = comma+1;
					comma = pre.indexOf(',', start);
				}
				
				//Comma not found or the next one is after a space
				if(comma == -1 || comma > space){
					String tempString = pre.substring(start,space);
					tempArray.add(tempString);
					start = space + 1;
					space = pre.indexOf(' ', start);
				}
				preList.add(tempArray);
			}
			
			//Comma not found/space comes before comma
			else if(comma==-1 || (space != -1 && space < comma)){
				ArrayList<String> tempArray1 = new ArrayList<String>();
				String tempString = pre.substring(start, space);
				tempArray1.add(tempString);
				preList.add(tempArray1);
				start = space + 1;
				space = pre.indexOf(' ', start);
			}
		}while(comma != -1 || space != -1);
			
		return preList;
	}
	
	//Gets user input if necessary
	public String userInput(){
		Scanner keyboard = new Scanner(System.in);
		String name = null;
		System.out.println("Enter the file name: ");
		name = keyboard.next();
		return name;
		
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public ArrayList< ArrayList< String > > getPrereqs() {
		ArrayList< ArrayList< String > > rtn = new ArrayList< ArrayList< String > >();
		String[] andReqs = this.prereqs.split( " " );
		for ( String and : andReqs ) {
			ArrayList< String > courseGroup = new ArrayList< String >();
			String[] orReqs = and.split( "," );
			for ( String s : orReqs ) {
				courseGroup.add( s );
			}
			rtn.add( courseGroup );
		}
		return rtn;
	}
	
	//Replaces "," with "or" and " " with "and"
	public String orAnd(String s){
		String replaced = s.substring(0, s.length()-1);
		String replaced2=replaced.replaceAll(" "," and ");
		String replacedFinal=replaced2.replaceAll(","," or ");
		
		return replacedFinal;
	}
	
	
	/*public static void main(String args[]){
		
		CourseDataParser test = new CourseDataParser("data/eecs/eecs281");
		
		//CourseDataParser test = new CourseDataParser( "eecs/eecs183" );
		System.out.println("Title is " + test.title);
		System.out.println("Number of credits is " + test.credits);
		System.out.println("Description is " + test.description);
		System.out.println("Prerequisites is/are " + test.orAnd(test.prereqs));
		ArrayList< ArrayList< String > > prereqs = test.getPrereqs();
		for ( ArrayList< String > l1 : prereqs ) {
			for ( String  s : l1 ) {
				System.out.print( s + " or " );
			}
			System.out.println();
		}
		//test.prerequisites(test.prereqs);

	}//*/
	
}
