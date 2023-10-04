package User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import Admin.Admin.Train;
import Station.Station;
import Check.Check;
import Database.DB;

public class User {

    static Scanner sc=new Scanner(System.in);
    
    public class Ticket{
        public String username;
        public int ticketNo;
        public String ticketType;
        public HashMap<String,Integer> person;
        public int price;
        public String phone;
        public Train t;
        public Station src;
        public Station dest;
        public Ticket(String username,int ticketNo,int price,HashMap<String,Integer> person,String phone,Train t,Station src,Station dest){
            this.username=username;
            this.ticketNo=ticketNo;
            this.price=price;
            this.person=person;
            this.phone=phone;
            this.t=t;
            this.src=src;
            this.dest=dest;
        }
    }

    public void ticketBook(String username,ArrayList<Train> allTrain,HashMap<Integer,Ticket> map) throws Exception{
        System.out.print("Enter Srouce Station : ");
        String sourceStationName=sc.nextLine();
        System.out.print("Enter Destiny Station : ");
        String destinyStationName=sc.nextLine();

        //check Both Not Same 
        if(sourceStationName.equals(destinyStationName)){
            System.out.println("Both Same!! Invalid Selections...");
            System.out.println();
            ticketBook(username, allTrain, map);
            return;
        }

        //get all Available Train List
        ArrayList<Train> availableTrain=getTrains(sourceStationName, destinyStationName,allTrain);
        if(availableTrain.isEmpty()){
            System.out.println("For This Route No Any Train Availabel");
            System.out.println();
            return;
        }

        System.out.println();
        System.out.println();
        System.out.println("     --Available Trains-- ");
        System.out.println();
        for(int i=0;i<availableTrain.size();i++){
            System.out.println("Train : "+(i+1));
            System.out.println("Name : "+availableTrain.get(i).tname);
            System.out.println(sourceStationName+" : "+availableTrain.get(i).getStationDetails(sourceStationName).time);
            System.out.println(destinyStationName+" : "+availableTrain.get(i).getStationDetails(destinyStationName).time);
            System.out.println();
        }
        System.out.println();
        System.out.print("Enter Train Number For Select : ");
        int select=sc.nextInt();
        if(select<1 && select>availableTrain.size()){
            System.out.println("Please Select Valid Try Again.....");
            return;
        }
        Train selectTrain=availableTrain.get(select-1);
        
        Station sourcStationObj=selectTrain.getStationDetails(sourceStationName);
        Station destiniyStationObj=selectTrain.getStationDetails(destinyStationName);
        System.out.println("Available Seats : ");
        sourcStationObj.seatShow();

        System.out.println();
        sc.nextLine();
        String couch;
        do{
            System.out.println("Enter Couch Name : ");
            couch=sc.nextLine();
            if(sourcStationObj.seats.containsKey(couch)){
                break;
            }
        }while(true);
        
        synchronized(this){
            System.out.println("How Many Ticket You want to Book?");
            int n=sc.nextInt();
            if(sourcStationObj.seats.get(couch)<n){
                System.out.println("Ticket not Availabel");
                return;
            }
            decressSeats(couch,selectTrain,sourceStationName,destinyStationName,n);


            HashMap<String,Integer> person=new HashMap<>();
            for(int i=1;i<=n;i++){
                sc.nextLine();
                System.out.println("Enter Details Of Person : "+i);
                System.out.print("name : ");
                String name=sc.nextLine();
                System.out.print("Age : ");
                int age=sc.nextInt();
                int totalper=0;
            
                //sl
                if(couch.equalsIgnoreCase("sl")){
                    for(int q=0;q<sourcStationObj.sl.length;q++){
                        if(sourcStationObj.sl[q]==false){
                            totalper++;
                            person.put(name+":"+age+" seatNo-",q);
                            sourcStationObj.sl[q]=true;
                            if(totalper==n){
                                break;
                            }
                        }
                    }
                }else if(couch.equalsIgnoreCase("3rdAC")){
                    for(int q=0;q<sourcStationObj.tAC.length;q++){
                        if(sourcStationObj.tAC[q]==false){
                            totalper++;
                            person.put(name+":"+age+" seatNo-",q);
                            sourcStationObj.tAC[q]=true;
                        }
                        if(totalper==n){
                                break;
                        }
                    }
                }else if(couch.equalsIgnoreCase("2rdAC")){
                    for(int q=0;q<sourcStationObj.sAC.length;q++){
                        if(sourcStationObj.sAC[q]==false){
                            totalper++;
                            person.put(name+":"+age+" seatNo-",q);
                            sourcStationObj.sAC[q]=true;
                        }
                        if(totalper==n){
                                break;
                        }
                    }
                }else{
                    for(int q=0;q<sourcStationObj.fAC.length;q++){
                        if(!(sourcStationObj.fAC[q])){
                            totalper++;
                            person.put(name+":"+age+" seatNo-",(q+1));
                            sourcStationObj.fAC[q]=true;
                        }
                        if(totalper==n){
                                break;
                        }
                    }
                }
            }

            String number;
            sc.nextLine();
            do{
                System.out.print("Enter Mobile Number : ");
                number=sc.nextLine();
            }while(!Check.mobileNumber(number));

            int ticketNo=(int)(Math.random()*1000);

            int price=calculate(couch,selectTrain, sourceStationName, destinyStationName)*n;

            Ticket tic=new Ticket(username,ticketNo,price,person, number, selectTrain,sourcStationObj,destiniyStationObj);
            tic.ticketType=couch;
            DB.ticketDetailsStoreInDB(tic);
            System.out.println();
            System.out.println("You Pay "+price + " inr");
            System.out.println("Your Ticket Book Successfuly \nTicket No."+ticketNo);

        }

        
        
    }
    
    private static void decressSeats(String couch,Train t,String source,String dest,int n){
        ArrayList<Station> stops=t.stop;
        boolean flag=false;
        for(int i=0;i<stops.size();i++){
            Station s=stops.get(i);
            if(s.name.equalsIgnoreCase(source)){
                
                int r=s.seats.get(couch);
                s.seats.put(couch, r-n);
                flag=true;
            }
            if(s.name.equalsIgnoreCase(dest)){
                break;
            }
            if(flag){
                int r=s.seats.get(couch);
                s.seats.put(couch, r-n);
            }
        }
    }

    private static int calculate(String couch,Train t,String src,String dest){
        int km=t.getStationDetails(dest).km-t.getStationDetails(src).km;
        double price=0;
        //"sl","3rdAC","2ndAC","1stAC"
        if(couch.equalsIgnoreCase("sl")){
            price=km*(1.94);
        }else if(couch.equalsIgnoreCase("3rdAC")){
            price=km*(3);
        }else if(couch.equalsIgnoreCase("2ndAC")){
            price=km*(5);
        }else if(couch.equalsIgnoreCase("1stAC")){
            price=km*(7);
        }
        if(price<30){
            price=30;
        }
        return (int)price;
    }

    public void viewTicket(HashMap<Integer,Ticket> map){
        System.out.println("Ticket view");
        System.out.print("Enter Ticket No : ");
        int ticketNumber=sc.nextInt();
        if(!map.containsKey(ticketNumber)){
            System.out.println("Invalid Ticket Number Try Again...");
            return;
        }
        Ticket tick=map.get(ticketNumber);
        System.out.println("            *-*-* Railway Ticket *-*-*");
        System.out.println();
        System.out.println("Ticket No : "+ticketNumber);
        System.out.println("username : "+tick.username);
        System.out.println("Phone Number : "+tick.phone);
        System.out.println("Source Station : "+tick.src.name);
        System.out.println("Destiniy Station : "+tick.dest.name);
        System.out.println("Time : "+tick.src.time+"-"+tick.dest.time);
        System.out.println("Total KM : "+(tick.dest.km-tick.src.km));
        System.out.println("Price : "+tick.price);
        System.out.println("TicketType : "+tick.ticketType);
        System.out.println("Persons Lists : ");
        HashMap<String,Integer> list=tick.person;
        for(Map.Entry m : list.entrySet()){
            System.out.println("   ✤ "+m.getKey()+" - "+m.getValue());
        }
    }

    public void printTicket(HashMap<Integer,Ticket> map)throws IOException{
        // System.out.println("Tickt Printing");
        System.out.print("Enter Ticket No : ");
        int ticketNumber=sc.nextInt();
        if(!map.containsKey(ticketNumber)){
            System.out.println("Invalid Ticket Number Try Again...");
            return;
        }
        Ticket tick=map.get(ticketNumber);
        String fileName=ticketNumber+".txt";
        BufferedWriter br=new BufferedWriter(new FileWriter(fileName));
        br.write("            *-*-* Railway Ticket *-*-*");
        br.newLine();
        br.newLine();
        br.write("Ticket No : "+ticketNumber);
        br.newLine();
        br.write("username : "+tick.username);
        br.newLine();
        br.write("Phone Number : "+tick.phone);
        br.newLine();
        br.write("Source Station : "+tick.src.name);
        br.newLine();
        br.write("Destiniy Station : "+tick.dest.name);
        br.newLine();
        br.write("Time : "+tick.src.time+"-"+tick.dest.time);
        br.newLine();
        br.write("Total KM : "+(tick.dest.km-tick.src.km));
        br.newLine();
        br.write("Price : "+tick.price);
        br.newLine();
        br.write("Ticket Type :"+tick.ticketType);
        br.newLine();
        br.write("Persons Lists : ");
        br.newLine();
        HashMap<String,Integer> list=tick.person;
        for(Map.Entry m : list.entrySet()){
            br.write("   ✤"+m.getKey()+" - "+m.getValue());
            br.newLine();
        }

        br.flush();
        br.flush();
        System.out.println("File Save.. File Name : "+fileName);
    }

    private ArrayList<Train> getTrains(String src,String dest,ArrayList<Train> allTrain){
        ArrayList<Train> list=new ArrayList<>();
        for(int i=0;i<allTrain.size();i++){
            Train t=allTrain.get(i);
            ArrayList<Station> stops=t.stop;
            boolean flag=false;
            for(int k=0;k<stops.size();k++){
                if(stops.get(k).name.equalsIgnoreCase(src)){
                    flag=true;
                }
                if(flag && stops.get(k).name.equalsIgnoreCase(dest)){
                    list.add(t);
                    break;
                }
            }
        }
        return list;
    }
}
