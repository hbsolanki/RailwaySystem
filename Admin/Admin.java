package Admin;

import Station.*;
import java.util.*;
import Database.Database;
import Check.Check;

public class Admin {

    static Scanner sc=new Scanner(System.in);

    public class Route{
        public ArrayList<Station> route;
        public Route(ArrayList<Station> route){
            this.route=route;
        }

        protected void showRoute(){
            for(int i=0;i<route.size();i++){
                Station s=route.get(i);
                System.out.println("Station : "+(i+1));
                System.out.println("Name : "+s.name);
                System.out.println("Station code : "+s.src);
            }
        }
     }
     
     public void addNewRoute() throws Exception{
        boolean flag=true;
        ArrayList<Station> routeStationList=new ArrayList<>();
        int cnt=1;
        while(flag){
            System.out.println("Enter Details Of Station "+cnt);
            System.out.print("Name : ");
            String sName=sc.nextLine();
            System.out.print("Station Code : ");
            String src=sc.nextLine();
            System.out.print("km From Source : ");
            int km=sc.nextInt();
            Station s=new Station(sName, src, km);
            routeStationList.add(s);
            System.out.println();
            System.out.println("For Quit 0 or ADD MORE 1");
            int ch=sc.nextInt();
            if(ch!=1){
                flag=false;
                break;
            }
            cnt++;
            System.out.println();
            sc.nextLine();
        }
          
        Database.routeStoreInDB(routeStationList);
     }

     public class Train{
        public int tno;
        public String tname;
        public ArrayList<Station> stop;
        public Train(int tno,String tname,ArrayList<Station> stop){
            this.tno=tno;
            this.tname=tname;
            this.stop=stop;
        }

        protected void trainShow(){
            System.out.println();
            System.out.println("Train Number : "+tno);
            System.out.println("Train Name : "+tname);
            System.out.println("Train Staops : ");
            for(int i=0;i<stop.size();i++){
                Station s=stop.get(i);
                System.out.println((i+1)+" "+s.name+"-"+s.time);
            }
            System.out.println();
        }

        public Station getStationDetails(String name){
            Station s=null;
            for(int i=0;i<stop.size();i++){
                if(stop.get(i).name.equalsIgnoreCase(name)){
                    return stop.get(i);
                }
            }
            return s;
        }
    }

    public void addTrain(ArrayList<Route> route)throws Exception{
        if(route.size()<=0){
            System.out.println("NO ANY Route Availabel");
            return;
        }
        for(int i=0;i<route.size();i++){
            System.out.println("        *-*-*Route : "+(i+1));
            route.get(i).showRoute();
        }
        System.out.print("Enter Route Number For Select : ");
        int r=sc.nextInt();

        if(r<1 || r>route.size()){
            System.out.println("Invalid Option");
            return;
        }
        Route selectRoute=route.get(r-1);
        ArrayList<Station> routeStation=selectRoute.route;
        System.out.println();
        System.out.print("Enter Train NO : ");
        int no=sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Train Name : ");
        String name=sc.nextLine();
        
        System.out.print("Enter Total Seats int Train : ");
        int totalSeats=sc.nextInt();
        ArrayList<Station> trainStop=new ArrayList<>();
        System.out.println();
        System.out.println();
        System.out.println("->You Select Route ");
        selectRoute.showRoute();
        System.out.println();
        
        int ch;
        int cnt=0;
        do{
            System.out.println("Enter No. For Add Stop For Train for Stop Press 0");
            ch=sc.nextInt();
            if(ch==0){
                break;
            }
            if(ch>routeStation.size()){
                System.out.println("Choice Valid Option");
                continue;
            }
            sc.nextLine();
            String time;
            do{
                System.out.print("Enter Time Reach Train At Station : ");
                time=sc.nextLine();
            }while(!Check.time(time));
            Station routeStaion=routeStation.get(ch-1);
            Station s=new Station(routeStaion.name,routeStaion.src,routeStaion.km);
            s.time=time;
            s.totalSeats=totalSeats;
            trainStop.add(s);
            cnt++;
        }while(ch!=0||ch>route.size()||(cnt==route.size()));

        Train t=new Train(no, name, trainStop);
        System.out.println("*-*-* Train *-*-*");
        t.trainShow();
        
        Database.trainInDB(t);
    }
}

