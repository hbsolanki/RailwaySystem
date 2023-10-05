package Database;

import java.util.*;
import java.sql.*;

import Station.Station;
import Admin.Admin.Train;
import Admin.Admin;
import Admin.Admin.Route;
import User.User;
import User.User.Ticket;


public class DB {
        static Admin a=new Admin();
        static User u=new User();

        public static ArrayList<Integer> routeNo()throws Exception{
            return getallRouteIDFromDB();
        }
        public static ArrayList<Route> getAllRoute() throws Exception{
            return getAllRouteFromDB();
        }

        public static ArrayList<Train> getAllTrain() throws Exception{
            return getAllTrainFromDB();
        }

        public static HashMap<Integer,Ticket> getAllTicket() throws Exception{
            return getAllTicketFromDB();
        }
        
        public static HashMap<String,String> allAdmins()throws Exception{
            return getAdminsFromDb();
        }

        public static HashMap<String,String> allUsers()throws Exception{
            return getUsersFromDB();
        }

        private static Connection getCon()throws Exception{
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/railway", "root","8160062665");
        }

        //allroute(rid int)
        public static void routeIdStoreInDB(int rid)throws Exception{

            String sql="insert into allroute(rid) values (?)";
            Connection con=getCon();
            
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setInt(1,rid);
            pst.executeUpdate();
                
        }

        public static ArrayList<Integer> getallRouteIDFromDB()throws Exception{
            ArrayList<Integer> allRouteN=new ArrayList<>();
            String sql="select * from allroute";
            Connection con=getCon();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                allRouteN.add(rs.getInt(1));
            }
            return allRouteN;
        }

        //
        public static void routeStoreInDB(int rid,ArrayList<Station> routeStationList)throws Exception{
            String sql="create table r"+rid+"(sname varchar(50),src varchar(20),km int)";
            Connection con=getCon();
            Statement st=con.createStatement();
            st.executeUpdate(sql);

            for(int i=0;i<routeStationList.size();i++){
                String sql2="insert into r"+rid+"(sname,src,km) values(?,?,?)";
                PreparedStatement pst=con.prepareStatement(sql2);
                Station s=routeStationList.get(i);
                pst.setString(1, s.name);
                pst.setString(2, s.src);
                pst.setInt(3, s.km);
                pst.executeUpdate();
            }
        }

        public static ArrayList<Route> getAllRouteFromDB()throws Exception{
            ArrayList<Integer> allRouteID=getallRouteIDFromDB();
            ArrayList<Route> allroute=new ArrayList<>();
            Connection con=getCon();
            for(int i=0;i<allRouteID.size();i++){
                ArrayList<Station> rut=new ArrayList<>();
                int rid=allRouteID.get(i);
                String sql="select * from r"+rid+";";
                PreparedStatement pst=con.prepareStatement(sql);
                ResultSet rs=pst.executeQuery();
                while(rs.next()){
                    String sname=rs.getString(1);
                    String src=rs.getString(2);
                    int km=rs.getInt(3);
                    Station s=new Station(sname, src, km, 0, 0, 0, 0);
                    rut.add(s);
                }
                allroute.add(a.new Route(rut));
            }
            return allroute;
        }


        //alltrain(tno int)
        public static void trainNoStoreInDB(int tno)throws Exception{

            String sql="insert into alltrain(tno) values (?)";
            Connection con=getCon();
            
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setInt(1,tno);
            pst.executeUpdate();
        }

        public static ArrayList<Integer> getallTrainNoFromDB()throws Exception{
            ArrayList<Integer> allTrainNo=new ArrayList<>();
            String sql="select * from alltrain";
            Connection con=getCon();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                allTrainNo.add(rs.getInt(1));
            }
            return allTrainNo;
        }

        public static void trainStoreInDB(int tno,Train t)throws Exception{
            String sql="create table t"+tno+"(tname varchar(50),sname varchar(50),src varchar(20),km int,time varchar(10),sl int,tAC int,sAC int,fAC int)";
            Connection con=getCon();
            Statement st=con.createStatement();
            st.executeUpdate(sql);
            for(int i=0;i<t.stop.size();i++){
                Station s=t.stop.get(i);
                HashMap<String,Integer> map=s.seats;

                String sql2="insert into t"+tno+"(tname,sname,src,km,time,sl,tAC,sAC,fAC) values(?,?,?,?,?,?,?,?,?)";
                PreparedStatement pst=con.prepareStatement(sql2);
                pst.setString(1, t.tname);
                pst.setString(2, s.name);
                pst.setString(3, s.src);
                pst.setInt(4, s.km);
                pst.setString(5, s.time);
                pst.setInt(6, map.get("sl"));
                pst.setInt(7, map.get("3rdAC"));
                pst.setInt(8, map.get("2ndAC"));
                pst.setInt(9, map.get("1stAC"));
                pst.executeUpdate();
            }
        }


        public static ArrayList<Train> getAllTrainFromDB()throws Exception{
            ArrayList<Train> allTrain=new ArrayList<>();
            ArrayList<Integer> allTrainNo=getallTrainNoFromDB();
            Connection con=getCon();
            for(int i=0;i<allTrainNo.size();i++){
                String sql="select * from t"+allTrainNo.get(i)+";";
                PreparedStatement pst=con.prepareStatement(sql);
                ResultSet rs=pst.executeQuery();
                String tname="";
                ArrayList<Station> stop=new ArrayList<>();
                while(rs.next()){
                    HashMap<String,Integer> map=new HashMap<>();
                    tname=rs.getString(1);
                    String sname=rs.getString(2);
                    String src=rs.getString(3);
                    int km=rs.getInt(4);
                    String time=rs.getString(5);
                    int sl=rs.getInt(6);
                    int tAC=rs.getInt(7);
                    int sAC=rs.getInt(8);
                    int fAC=rs.getInt(9);
                    Station s=new Station(sname, src, km, sl, tAC, sAC, fAC);
                    s.time=time;
                    //"sl","3rdAC","2ndAC","1stAC"
                    map.put("sl", sl);
                    map.put("3rdAC", tAC);
                    map.put("2ndAC",sAC);
                    map.put("1stAC", fAC);
                    s.seats=map;
                    stop.add(s);
                }
                allTrain.add(a.new Train(allTrainNo.get(i),tname,stop));
            }
            return allTrain;
        }


        //ticket(username,ticketno,price,personlist(oneString),phone,Train t(trainnumber,trainname),srcname,destname,srctime,desttime)
        public static void ticketDetailsStoreInDB(Ticket tick)throws Exception{
            String sql="insert into ticket(username,ticketno,price,personlist,phone,trainnumber,trainname,source,destiniy,stime,dtime,ttype) values(?,?,?,?,?,?,?,?,?,?,?,?)";
            Connection con=getCon();
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setString(1,tick.username);
            pst.setInt(2, tick.ticketNo);
            pst.setInt(3, tick.price);

            HashMap<String,Integer> list=tick.person;
            String perlist="";
            for(Map.Entry m : list.entrySet()){
                perlist+=(m.getKey())+(m.getValue()+";");
            }

            pst.setString(4,perlist);
            pst.setString(5, tick.phone);
            pst.setInt(6, tick.t.tno);
            pst.setString(7, tick.t.tname);
            pst.setString(8, tick.src.name);
            pst.setString(9, tick.dest.name);
            pst.setString(10, tick.src.time);
            pst.setString(11, tick.dest.time);
            pst.setString(12, tick.ticketType);
            pst.executeUpdate();
        }

        // user(username,password)
        public static void UserDatailsStoreInDB(String username,String pass)throws Exception{
            String sql="insert into user(username,password) values(?,?)";
            Connection con=getCon();
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, pass);
            pst.executeUpdate();
        }

    // admin(id,password)
        public static void adminDatailsStoreInDB(String id,String pass)throws Exception{
            String sql="insert into admin(id,password) values(?,?)";
            System.out.println(id);
            Connection con=getCon();
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, pass);
            pst.executeUpdate();
    }


    // ***** Get Data From Database

  
    //ticket(username,ticketno,price,personlist(oneString),phone,Train t(trainnumber,trainname),srcname,destname,srctime,desttime)
    private static HashMap<Integer,Ticket> getAllTicketFromDB()throws Exception{
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
                String name=person[0].trim();
                System.out.println(person[1]);
                int age=Integer.parseInt(person[1].trim());
                mapp.put(name, age);

            }

            String phone=rs.getString(5);
            int trainNumber=rs.getInt(6);
            String trainname=rs.getString(7);

            String srcName=rs.getString(8);
            String destName=rs.getString(9);
            String srcTime=rs.getString(10);
            String destTime=rs.getString(11);
            String ttype=rs.getString(12);
            Train t=getTrainT(trainNumber);
            Station src=t.getStationDetails( srcName);
            Station dest=t.getStationDetails(destName);
            Ticket tick=u.new Ticket(username, ticketNo, price, mapp, phone, t, src, dest);
            src.time=srcTime;
            dest.time=destTime;
            tick.ticketType=ttype;
            tick.person=mapp;
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


    // user(username,password)
    private static HashMap<String, String> getUsersFromDB()throws Exception{
        HashMap<String,String> allUsers=new HashMap<>();
        String sql="select * from user";
        Connection con=getCon();
        PreparedStatement pst=con.prepareStatement(sql);
        ResultSet rs=pst.executeQuery();
        while(rs.next()){
            allUsers.put(rs.getString(1), rs.getString(2));
        }
        return allUsers;
    }

    // admin(id,password)
    private static HashMap<String, String> getAdminsFromDb()throws Exception{
        HashMap<String,String> allAdmins=new HashMap<>();
        String sql="select * from admin";
        Connection con=getCon();
        PreparedStatement pst=con.prepareStatement(sql);
        ResultSet rs=pst.executeQuery();
        while(rs.next()){
            allAdmins.put(rs.getString(1), rs.getString(2));
        }
        return allAdmins;
    }
}

