package User;

import java.util.*;
import Admin.Admin.Train;

import Station.Station;

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

    public void ticketBook(String username,ArrayList<Train> allTrain,HashMap<Integer,Ticket> map){
        System.out.println("Enter Srouce Station : ");
        String src=sc.nextLine();
        System.out.println("Enter Destiny Station : ");
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
        System.out.println("Availabel Trains");
        for(int i=0;i<availTrain.size();i++){
            System.out.println("Train : "+(i+1));
            System.out.println("Name : "+availTrain.get(i).tname);
        }
        System.out.println();
        System.out.print("Enter Train Number For Select :");
        int select=sc.nextInt();
        if(select<0 && select>availTrain.size()){
            System.out.println("Please Select Valid Try Again.....");
            return;
        }
        Train t=availTrain.get(select+1);
        
        
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
            boolean flag=false;
            String number;
            do{
                System.out.print("Enter Mobile Number : ");
                number=sc.nextLine();
                if(number.length()==10){
                    for(int i=0;i<10;i++){
                        if(number.charAt(i)<'0' && number.charAt(i)>'9'){
                            flag=true;
                            continue;
                        }
                    }
                } 
            }while(flag);

            int ticketNo=(int)(Math.random()*6);

            int price=calculate(t, src, dest)*n;

            Station s=null;
            Station d=null;
            ArrayList<Station> stops=t.stop;
            for(int i=0;i<stops.size();i++){
                if(stops.get(i).name.equals(src)){
                    s=stops.get(i);
                }
                if(s!=null){
                    stops.get(i).totalSeats-=n;
                }
                if(stops.get(i).equals(dest)){
                    d=stops.get(i);
                    break;
                }
            }

            Ticket tic=new Ticket(username,ticketNo,price,person, number, t, s, d);
            map.put(ticketNo, tic);
            System.out.println("You Pay "+price + " inr");
            System.out.println("Your Ticket Book Successfuly \nTicket No."+ticketNo);
        }

        
    }

    private static int calculate(Train t,String src,String dest){
        int sidx=t.stop.indexOf(src);
        int eidx=t.stop.indexOf(dest);
        int km=t.stop.get(sidx).km-t.stop.get(eidx).km;
        int price=(int) ((int) km*0.533);
        if(price<30){
            price=30;
        }
        return price;
    }

    public synchronized void ticketView(int ticketNo,HashMap<Integer,Ticket> map){
        Ticket tick=map.get(ticketNo);
        System.out.println("            *-*-* Railway Ticket *-*-*");
        System.out.println();
        System.out.println("Ticket No : "+ticketNo);
        System.out.println("username : "+tick.username);
        System.out.println("Phone Number : "+tick.phone);
        System.out.println("Source Station : "+tick.src);
        System.out.println("Destiniy Station : "+tick.dest);
        System.out.println("Time : "+tick.src.time+"-"+tick.dest.time);
        System.out.println("Total KM : "+(tick.dest.km-tick.src.km));
        System.out.println("Price : "+tick.price);
        System.out.println("Persons Lists : ");
        HashMap<String,Integer> list=new HashMap<>();
        for(Map.Entry m : list.entrySet()){
            System.out.println(m.getKey()+" - "+m.getValue());
        }
    }

    private ArrayList<Train> getTrains(String src,String dest,ArrayList<Train> allTrain){
        ArrayList<Train> list=new ArrayList<>();
        for(int i=0;i<allTrain.size();i++){
            Train t=allTrain.get(i);
            ArrayList<Station> stops=t.stop;
            boolean flag=false;
            for(int k=0;k<stops.size();i++){
                if(stops.get(k).equals(src)){
                    flag=true;
                }
                if(flag && stops.get(k).equals(dest)){
                    list.add(t);
                    break;
                }
            }
        }
        return list;
    }
}
