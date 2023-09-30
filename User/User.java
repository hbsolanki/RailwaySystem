package User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import Admin.Admin.Train;
import Database.Database;
import Station.Station;
import Check.Check;

public class User {

    static Scanner sc=new Scanner(System.in);
    
    public class Ticket{
        public String username;
        public int ticketNo;
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
        String src=sc.nextLine();
        System.out.print("Enter Destiny Station : ");
        String dest=sc.nextLine();
        if(src.equals(dest)){
            System.out.println("Both Same!! Invalid Selections...");
            System.out.println();
            ticketBook(username, allTrain, map);
            return;
        }
        ArrayList<Train> availTrain=getTrains(src, dest,allTrain);
        if(availTrain.isEmpty()){
            System.out.println("For This Route No Any Train Availabel");
            System.out.println();
            return;
        }

        System.out.println();
        System.out.println();
        System.out.println("     --Availabel Trains-- ");
        System.out.println();
        for(int i=0;i<availTrain.size();i++){
            System.out.println("Train : "+(i+1));
            System.out.println("Name : "+availTrain.get(i).tname);
            System.out.println(src+" : "+availTrain.get(i).getStationDetails(src).time);
            System.out.println(dest+" : "+availTrain.get(i).getStationDetails(dest).time);
        }
        System.out.println();
        System.out.print("Enter Train Number For Select : ");
        int select=sc.nextInt();
        if(select<1 && select>availTrain.size()){
            System.out.println("Please Select Valid Try Again.....");
            return;
        }
        Train t=availTrain.get(select-1);
        
        
        synchronized(this){
            System.out.println("How Many Ticket You want to Book?");
            int n=sc.nextInt();
            HashMap<String,Integer> person=new HashMap<>();
            for(int i=1;i<=n;i++){
            sc.nextLine();
            System.out.println("Enter Details Of Person : "+i);
            System.out.print("name : ");
            String name=sc.nextLine();
            System.out.print("Age : ");
            int age=sc.nextInt();
            person.put(name, age);
            }

            sc.nextLine();
            
            String number;
            do{
                System.out.print("Enter Mobile Number : ");
                number=sc.nextLine();
            }while(!Check.mobileNumber(number));

            int ticketNo=(int)(Math.random()*1000);

            int price=calculate(t, src, dest)*n;

            Ticket tic=new Ticket(username,ticketNo,price,person, number, t,t.getStationDetails(src),t.getStationDetails(dest));
            Database.ticketDetailsInDB(tic);
            System.out.println();
            System.out.println("You Pay "+price + "inr");
            System.out.println("Your Ticket Book Successfuly \nTicket No."+ticketNo);
        }

        
    }

    private static int calculate(Train t,String src,String dest){
        int km=t.getStationDetails(dest).km-t.getStationDetails(src).km;
        int price=(int) ((int) km*0.533);
        if(price<30){
            price=30;
        }
        return price;
    }

    public void viewTicket(HashMap<Integer,Ticket> map){
        System.out.print("Enter Ticket No : ");
        int ticketNumber=sc.nextInt();
        if(!map.containsKey(ticketNumber)){
            System.out.println("Invalid Ticket Number Try Again...");
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
        System.out.println("Persons Lists : ");
        HashMap<String,Integer> list=tick.person;
        for(Map.Entry m : list.entrySet()){
            System.out.println("   ✤ "+m.getKey()+" - "+m.getValue());
        }
    }

    public void printTicket(HashMap<Integer,Ticket> map)throws IOException{
        System.out.print("Enter Ticket No : ");
        int ticketNumber=sc.nextInt();
        if(!map.containsKey(ticketNumber)){
            System.out.println("Invalid Ticket Number Try Again...");
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
        br.write("Persons Lists : ");
        br.newLine();
        HashMap<String,Integer> list=tick.person;
        for(Map.Entry m : list.entrySet()){
            br.write("   ✤"+m.getKey()+" - "+m.getValue());
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
