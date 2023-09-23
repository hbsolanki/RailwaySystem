package Database;

import java.util.*;
import java.sql.*;

import Station.Station;
import Admin.Admin.Train;
import Admin.Admin;
import Admin.Admin.Route;
import User.User;
import User.User.Ticket;


public class Database {
        static Admin a=new Admin();
        static User u=new User();

        public static ArrayList<Route> getAllRoute() throws Exception{
            return getRouteDB();
        }

        public static ArrayList<Train> getAllTrain() throws Exception{
            return getTrainDB();
        }

        public static HashMap<Integer,Ticket> getAllTicket() throws Exception{
            return getTicketDB();
        }

        private static Connection getCon()throws Exception{
            return DriverManager.getConnection("com.mysql.cj.jdbc.Driver", "root","");
        }

        //route Table(rid,name,src,km)
        public static void routeInDB(ArrayList<Station> routeStations)throws Exception{
            String sql="insert into Route(rid,name,src,km) values (?,?)";
            Connection con=getCon();
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setInt(1,1);
            String station="";
            for(int i=0;i<routeStations.size();i++){
                Station s=routeStations.get(i);
                station=s.name+","+s.src+","+s.km+";";
            }
        }

        //train(tname,tno,totalseats,stops)
        public static void trainInDB(Train t)throws Exception{
            String sql="insert into Train(tname,tno,totalseats,stops) values(?,?,?,?)";
            Connection con=getCon();
            PreparedStatement pst=con.prepareStatement(sql);
            ArrayList<Station> stop=t.stop;
            String stops="";
            for(int i=0;i<stop.size();i++){
                Station s=stop.get(i);
                stops=s.name+","+s.src+","+s.km+","+s.time+";";
            }
            pst.setString(1, t.tname);
                pst.setInt(2, t.tno);
                pst.setInt(3, stop.get(0).totalSeats);
                pst.setString(4, stops);
            pst.executeUpdate();
        }

        //ticket(username,ticketno,price,personlist(oneString),phone,Train t(trainnumber,trainname),srcname,destname,srctime,desttime)
        public static void ticketDetailsInDB(Ticket tick)throws Exception{
            String sql="insert into ticket(username,ticketno,price,personlist,phone,trainnumber,trainname,source,destiniy,stime,dtime) values(?,?,?,?,?,?,?,?,?,?,?)";
            Connection con=getCon();
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setString(1,tick.username);
            pst.setInt(2, tick.ticketNo);
            pst.setInt(3, tick.price);

            HashMap<String,Integer> list=tick.person;
            String perlist="";
            for(Map.Entry m : list.entrySet()){
                perlist=m.getKey()+" - "+m.getValue()+";";
            }

            pst.setString(4,perlist);
            pst.setString(5, tick.phone);
            pst.setInt(6, tick.t.tno);
            pst.setString(7, tick.t.tname);
            pst.setString(8, tick.src.name);
            pst.setString(9, tick.dest.name);
            pst.setString(10, tick.src.time);
            pst.setString(11, tick.dest.time);
        }


    //route Table(name,src,km)
    private static ArrayList<Route> getRouteDB()throws Exception{
        String sql="select * from Route";
        Connection con=getCon();
        PreparedStatement pst=con.prepareStatement(sql);
        ResultSet rs=pst.executeQuery();
        Route r=null;
        ArrayList<Station> rut=new ArrayList<>();
        while(rs.next()){
            
            String allStation=rs.getString(2);
            String stations[]=allStation.split(";");
            for(int i=0;i<stations.length;i++){
                String station=stations[i];
                String instation[]=station.split(",");
                Station s=null;
                s.name=instation[0];
                s.src=instation[1];
                String km=instation[3];
                s.km=Integer.parseInt(km);
                rut.add(s);
            }
            
        }
        ArrayList<Route> allRoute=new ArrayList<>();
        allRoute.add(a.new Route(rut));
        return allRoute;
    }

    //train(tname,tno,totalseats,stops)
    //s.name+","+s.src+","+s.km+","+s.time+";";
    private static ArrayList<Train> getTrainDB()throws Exception{
        ArrayList<Train> allTrain=new ArrayList<>();
        String sql="select * from train";
        Connection con=getCon();
        PreparedStatement pst=con.prepareStatement(sql);
        ResultSet rs=pst.executeQuery();
        while(rs.next()){
            String tname=rs.getString(1);
            int tno=rs.getInt(2);
            int totalSeats=rs.getInt(3);
            String main=rs.getString(4);

            String []stop=main.split(";");
            ArrayList<Station> stops=new ArrayList<>();
            for(int i=0;i<stop.length;i++){
                String sAll=stop[i];
                String s[]=sAll.split(",");

                String stationName=s[0];
                String src=s[1];
                int km=Integer.parseInt(s[2]);
                String time=s[3];
                Station stationADD=new Station(stationName, src, km);
                stationADD.time=time;

                stops.add(stationADD);
            }
            allTrain.add(a.new Train(tno, tname, stops));
        }
        return allTrain;
    }

    //ticket(username,ticketno,price,personlist(oneString),phone,Train t(trainnumber,trainname),srcname,destname,srctime,desttime)
    private static HashMap<Integer,Ticket> getTicketDB()throws Exception{
        HashMap<Integer,Ticket> map=new HashMap<>();
        String sql="select * from ticket";
        Connection con=getCon();
        PreparedStatement pst=con.prepareStatement(sql);
        ResultSet rs=pst.executeQuery();

        while(rs.next()){
            String username=rs.getString(1);
            int ticketNo=rs.getInt(2);
            int price=rs.getInt(3);
            String personlistInString=rs.getString(4);

            String presonlists[]=personlistInString.split(";");
            HashMap<String,Integer> mapp=new HashMap<>();
            for(int i=0;i<presonlists.length;i++){
                String []person=presonlists[i].split("-");
                String name=person[0];
                int age=Integer.parseInt(person[1]);
                mapp.put(name, age);

            }

            String phone=rs.getString(5);
            int trainNumber=rs.getInt(6);
            String trainname=rs.getString(7);

            String srcName=rs.getString(8);
            String destName=rs.getString(9);
            String srcTime=rs.getString(10);
            String destTime=rs.getString(11);

            // pst.setString(4,perlist);
            // pst.setString(5, tick.phone);
            // pst.setInt(6, tick.t.tno);
            // pst.setString(7, tick.t.tname);
            // pst.setString(8, tick.src.name);
            // pst.setString(9, tick.dest.name);
            // pst.setString(10, tick.src.time);
            // pst.setString(11, tick.dest.time);
            Train t=getTrainT(trainNumber);
            Station src=getStationS(t, srcName);
            Station dest=getStationS(t, destName);
            Ticket tick=u.new Ticket(username, ticketNo, price, mapp, phone, t, src, dest);
            map.put(ticketNo, tick);
        }
        return map;
    }

    private static Train getTrainT(int trainNumber) throws Exception{
        ArrayList<Train> allTrain=getAllTrain();
        for(int i=0;i<allTrain.size();i++){
            if(allTrain.get(i).tno==trainNumber){
                return allTrain.get(i);
            }
        } 
        return null;
    }

    private static Station getStationS(Train t,String name){
        ArrayList<Station> all=t.stop;
        for(int i=0;i<all.size();i++){
            if(all.get(i).name.equals(name)){
                return all.get(i);
            }
        }
        return null;
    }
}

