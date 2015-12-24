package competition;

import java.util.ArrayList;
import java.util.Scanner;
/*
 * TODO;
 * 	high priority
 * 		message()
 * 		team()
 * 		participant()
 * 	med priority
 * 		clean code
 * 	low priority
 * 		read from file
 */
public class Competition {
	private ArrayList<Event> events = new ArrayList<Event>();
	private ArrayList<Result> results = new ArrayList<Result>();
	private ArrayList<Participant> participants = new ArrayList<Participant>();
	private ArrayList<Team> teams = new ArrayList<Team>();
	private int nrOfRemoved = 0;
	
	public static void main(String[] args){
		Competition thisCompetition = new Competition();
		thisCompetition.run();	
	}
	
	private void run(){
		menu();
		while(true){
			handleCommands(readCommand());
		}
						
	}
	
	private String readCommand(){
		return normalize(inputString("Lyssnar:"),false,true);
	}
	
	private void handleCommands(String userInput){
					
			
		if(userInput.equals("test")){
			//test();
		}
		else if(userInput.equals("add event")){
			addEvent();
		}			
		else if(userInput.equals("add participant")){
			addParticipant();
		}
		else if(userInput.equals("remove participant")){
			removeParticipant();
		}
		else if(userInput.equals("add result")){
			addResult();								
		}
		else if(userInput.equals("participant")){
			//printParticipantResultsByEvent();
		}			
		else if(userInput.equals("teams")){				
		}
		else if(userInput.contains("message")){
			message(userInput);
		}
		else if(userInput.equals("reinitialize")){
			reinitialize();				
		}
		else if(userInput.equals("exit")){
			System.exit(0);
		}
		else{				
			boolean wrongInput = true;
			System.out.println(userInput);
			for(Event thisEvent : events){
				if(thisEvent.getName().equalsIgnoreCase(userInput)){
					wrongInput = false;
					resultByEvent(userInput);
				}										
			}
			if(wrongInput){
				System.out.println("Error 00; wrong input given");
				menu();
			}
		}	
	}
	
	private void menu(){
		System.out.println("Availible options, non case-sensitive;");
		System.out.println("\"test\" - enters test mode with several predefined participants, events and results");
		System.out.println("\"add event\" - adds an event with given options");
		System.out.println("\"add participant\" - adds a participant and gives them an autogenerated ID");
		System.out.println("\"add remove participant\" - removes a participant by ID");
		System.out.println("\"add add result\" - adds a result for a participant, by ID, for a specific event, by name");
		System.out.println("$eventName - shows the result for given event");
	}
	private void addEvent(){
		String eventName;
		boolean incorrectName = false;
		
		do{
			eventName = normalize(inputString("Event name:"),true,false);
			if(eventName==null){
				incorrectName = true;
				System.out.println("Error 01:Names cannot be empty!");
			}
		}while(incorrectName);
		
		int attempts;
		boolean tooLowAttempts = false;
		do{
			attempts = inputNumber("Attempts allowed:").intValue();
			if(attempts<1){
				tooLowAttempts = true;
				System.out.println("Error 02: Attempts value too low, allowed: 1 or higher");
			}
		}while(tooLowAttempts);
		
		String biggerBetter;
		boolean incorrectInput = false;
		do{
			biggerBetter = normalize(inputString("Bigger better? (Y/N):"),false,true);
			if(biggerBetter==null && biggerBetter!="y" && biggerBetter!="n" && biggerBetter!="yes" && biggerBetter!="no"){
				incorrectInput = true;
				System.out.println("Error 03: Incorrect input, allowed sepparated by \",\": y,n,yes,no");
			}
		}while(incorrectInput);
		
		boolean isBiggerBetter = false;
		if(biggerBetter=="Y" || biggerBetter=="Yes"){
			isBiggerBetter = true;
		}
		
		Event thisEvent = new Event(eventName,attempts,isBiggerBetter);
		boolean alreadyExists = false;
		String eName = thisEvent.getName();
		
		for(Event e: events){					
			if(e.getName().equals(eName)){
				alreadyExists = true;
				System.out.println("Error 04:"+eName+" has already been added");
			}						
		}
		if(!alreadyExists){
			events.add(thisEvent);
			System.out.println(thisEvent.getName()+" added");
		}
	}
	
	private void addParticipant(){
		String gName = normalize(inputString("Participants given name:"),true,false);
		String fName = normalize(inputString("Participants family name:"),true,false);
		String tName = normalize(inputString("Participants team name:"),true,false);
		
		if(gName != null && fName != null && tName != null){
			int ID = 100;
			
			if(!participants.isEmpty()){
				ID = 1+participants.get(participants.size()-1).getID();
			}				
			participants.add(new Participant(gName,fName,tName,ID));
			System.out.println(participants.get(ID-100-nrOfRemoved)+" added");
		}
		else{
			System.out.println("Error 05; null value in add participant");
			}
		if(!doesTeamExist(tName)){
			makeTeam(tName);
		}
	}
	
	private void removeParticipant(){
		int removedID = inputNumber("Participant ID to be removed:").intValue();
		
		int i = 0;				
		for(Participant p: participants){
			if(p.getID()==(removedID)){
				break;
			}
			i++;
		}
		if(i>=0 && i<participants.size()){
			System.out.println("Removing: "+ participants.get(i));
			participants.remove(i);
			nrOfRemoved++;
		}
		else{
			System.out.println("Error 06: No participant with ID: "+removedID);										
		}
	}
	
	private void addResult(){
		int pID = inputNumber("Participants ID:").intValue();
		String eventName = normalize(inputString("Event name:"),true,false);
		boolean incorrectP = true;
		boolean incorrectE = true;
		
		for(Participant p : participants){
			if(p.getID() == pID){
				incorrectP = false;
			}
		}
		Event thisEvent = null;
		for(Event e : events){
			if(e.getName() == eventName){
				incorrectP = false;
				thisEvent = e;
			}
		}
		int i = 0;
		for(Result r : results){
			if(r.getID() == pID){
				i++;
			}
		}
		if(incorrectP){
			System.out.println("Error 07: Incorrect participant ID given: "+pID);
			if(incorrectE){
				System.out.println("Error 08: Incorrect event name given: "+eventName);
			}
		}
		else if(incorrectE){
			System.out.println("Error 08: Incorrect event name given");
		}
		else{
			double thisResult;
			int attempts = 0;
			do{
				if(attempts>0){
					System.out.println("Error 09: Incorrect input, only results >0 accepted");						
				}					
				thisResult = inputNumber("Result as decimal number");
			}while(thisResult<0);
			
			if(thisEvent!=null){
				if(!incorrectP && !incorrectE && thisEvent.getTries()>=i){
					Result newResult = new Result(pID,eventName,thisResult);
					results.add(newResult);
					System.out.println("Result: "+ newResult+ " has been added");
				}
			}
		}
	}
	private void makeTeam(String name){
		teams.add(new Team(name));
	}

	private boolean doesTeamExist(String name){
		for(Team team : teams){
			if(team.getTeamName().equals(name)){
				return true;
			}
		}
		return false;
	}
	private void resultByEvent(String eventName){
		//DO shit
	}
	private void message(String s){
		//make new Message(), start after "message "
		Message message = new Message(s.substring(8));
		message.printMessage();
	}
	private void reinitialize(){
		//Reset nrOfRemoved, go through all of the ArrayLists and erase everything
		nrOfRemoved=0;
		
		for(int i = 0; i<events.size(); i++){
			events.remove(i);
		}
		for(int i = 0; i<results.size(); i++){
			results.remove(i);
		}
		for(int i = 0; i<participants.size(); i++){
			participants.remove(i);
		}
	}

	public String inputString(String inputString){
		@SuppressWarnings("resource")
		Scanner tangentbord = new Scanner(System.in);
		System.out.print(inputString);
		return tangentbord.nextLine();		
	}

	public Double inputNumber(String inputNumber){		
		@SuppressWarnings("resource")
		Scanner tangentbord = new Scanner(System.in);
		System.out.print(inputNumber);
		Double dOutput = tangentbord.nextDouble();
		tangentbord.nextLine();
		return dOutput;
	}
	/*
	 *remove forbidden character, could use .trim() for whitespace only but
	 *	1. this is more powerful for future additions
	 *	2. because I didn't check the docs before doing it 
	 *	3. then got obsessed with making it work and justifiable
	 *
	 *it will remove all forbidden characters
	 *leave whitespace if it has characters next to it, e.g. "Boo FF"
	*/
	public String normalize(String x,boolean capitalize,boolean forceLowerCase){
		String output = null;
		String[] forbidden = {" "};
		//iterate through the given string		
		for(int i=0; i<x.length(); i++){
			String l = x.substring(i,i+1);
			//iterate through the forbidden list
			for(int y = 0; y<forbidden.length;y++){
				if(!l.equalsIgnoreCase(forbidden[y])){
					//check if output is null
					
					if(output!=null){
						output=output+l;
					}
					else{
						output=l;						
						}					
				}
				//if it is a whitespace check if it has characters next to it
				if(l.equals(" ")){
					//check if it's the first or last character, if it is then it is incorrect anyways, if it's not we can check
					if(i>0 && i<x.length()-1){						
						if(!x.substring(i-1,i).equals(" ") && !x.substring(i+1,i+2).equals(" ")){
							output+=l;							
						}
					}															
				}
			}
		}
		//check if we trimmed the entire string
		if(output.isEmpty()){
			return null;
		}
		else{
			//first letter to uppercase	
			if(capitalize){
				return output.substring(0, 1).toUpperCase()+output.substring(1).toLowerCase();
			}
			else if(forceLowerCase){
				return output.toLowerCase();
			}
			return output;			
		}		
	}

}
