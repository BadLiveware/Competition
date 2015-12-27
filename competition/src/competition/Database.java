package competition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Database {
	private File folder = new File(new File(".").getAbsolutePath()+"/db");
	private File file;
	private Competition comp;
	
	public Database(Competition c){
		comp = c;
		checkDbFolder();
		file = selectDatabase();
	}
	public void writeToFile(ArrayList<Event> events, ArrayList<Participant> part, ArrayList<Team> teams) throws IOException{
			try {
				PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
				for(Event e : events){
					writer.println(e.toDb());
				}
				for(Participant p : part){
					writer.println(p.toDb());
				}
				for(Team t : teams){
					writer.println(t.toDb());
				}
				writer.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public File selectDatabase(){
		listDB();
		String db = folder.getAbsolutePath() + "/" + comp.inputString("Choose which database to use or make a new one>") + ".db";
		return new File(db);
	}
	public void listDB(){
		File[] listOfFiles = folder.listFiles();
		for(File f : listOfFiles){
			if(f.isFile()){
				System.out.println(f.getName());
			}
		}
	}
	public void checkDbFolder(){
		if(!folder.exists() && !folder.isDirectory()){
			folder.mkdir();
		}
	}
	public ArrayList<Participant> getParticipantsFromDb(){
		ArrayList<Participant> parts = new ArrayList<>();
		String[] tags = {"|p|","|/p|" , "|f|","|/f|" , "|t|","|/t|" , "|i|","|/i|"};
		try {
			Scanner sc = new Scanner(new FileReader(file));
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				if(line.contains(tags[0])){
					Participant tempParticipant = new Participant(null,null,null,0);
					for(int i = 0; i<tags.length;i+=2){
						switch(tags[i]){
						case "|p|":
							tempParticipant.setName(line.substring(line.indexOf(tags[i])+3, line.indexOf(tags[i+1])));
							break;
						case "|f|":
							tempParticipant.setFamilyName(line.substring(line.indexOf(tags[i])+3, line.indexOf(tags[i+1])));
							break;
						case "|t|":
							tempParticipant.setTeam(new Team(line.substring(line.indexOf(tags[i])+3, line.indexOf(tags[i+1]))));
							break;
						case "|i|":
							tempParticipant.setID(Integer.parseInt(line.substring(line.indexOf(tags[i])+3,line.indexOf(tags[i+1]))));
							break;
						}
					}
					parts.add(tempParticipant);
				}
			}
			sc.close();
			return parts;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean databaseExists(){
		if(file.exists()){
			return true;
		}
		else{
			return false;
		}
	}
}
