import User.User;
import User.User.*;
import Admin.Admin;
import Admin.Admin.*;
import Database.DB;

import java.util.*;

public class Main {

    static Scanner sc=new Scanner(System.in);

    private static ArrayList<Route> getAllRoute() throws Exception{
        return DB.getAllRoute();
    }

    private static ArrayList<Train> getAllTrain() throws Exception{
        return DB.getAllTrain();
    }

    private static HashMap<Integer,Ticket> getAllTicket() throws Exception{
        return DB.getAllTicket();
    }

    private static HashMap<String,String> allAdmins()throws Exception{
        return DB.allAdmins();
    }

    private static HashMap<String,String> allUsers()throws Exception{
        return DB.allUsers();
    }
    public static void main(String[] args) throws Exception {
        Admin a=new Admin();
        User u=new User();

        HashMap<String,String> allUsers=new HashMap<>();

        
        int m;
        do{
            System.out.println("(1)User (2)Admin (3)Quit");
            m=sc.nextInt();
            switch(m){
                case 1 : System.out.println("(1)Already User (2)New User (3)Quit");
                         HashMap<String,String> allUser=allUsers();
                         int c1=sc.nextInt();
                         if(c1==1){
                            sc.nextLine();
                            System.out.print("Enter UserName : ");
                            String username=sc.nextLine();
                            
                            if(allUser.containsKey(username)){
                                do{
                                    System.out.print("Enter Password : ");
                                    String pass=sc.nextLine();
                                    if(allUser.get(username).equals(pass)){
                                        user(username,u,getAllTrain());
                                        break;
                                    }else{
                                        throw new Exception("Wrong Password Try Again..");
                                    }
                                }while(true);
                                
                            }else{
                                throw new Exception("This Username Not Found");
                            }
                         }else if(c1==2){
                            sc.nextLine();
                            System.out.print("*-*New USER \nEnter UserName : ");
                            String username=sc.nextLine();
                            if(allUser.containsKey(username)){
                                System.out.println(username+" username already taken try again...");
                            }
                            System.out.print("Enter Password : ");
                            String password=sc.nextLine();
                            DB.UserDatailsStoreInDB(username, password);
                            user(username,u,getAllTrain());
                         }
                        break;
                case 2 : System.out.println("(1)Already Admin (2)New Admin (3)Quit");
                         HashMap<String,String> allAdmin=allAdmins();
                         int c2=sc.nextInt();
                         if(c2==1){
                            sc.nextLine();
                            System.out.print("Enter ID : ");
                            String id=sc.nextLine();
                            
                            if(allAdmin.containsKey(id)){
                                do{
                                    System.out.print("Enter Password : ");
                                    String pass=sc.nextLine();
                                    if(allAdmin.get(id).equals(pass)){
                                        admin(id, a);
                                        break;  
                                    }else{
                                        throw new Exception("Wrong Password Try Again..");
                                    }
                                }while(true);
                                
                            }else{
                                throw new Exception("This id Not Found");
                            }
                         }else if(c2==2){
                            sc.nextLine();
                            System.out.println("*-*New Admin ");
                            System.out.print("Enter Id");
                            String id=sc.nextLine();
                            if(allAdmin.containsKey(id)){
                                System.out.println(id+" id already Taken try again..");
                                break;
                            }
                            System.out.print("Enter Password : ");
                            String password=sc.nextLine();
                            DB.adminDatailsStoreInDB(id, password);
                            admin(id,a);
                         }
                        break;
                case 3 : System.exit(0);
                         break;
                default : System.out.println("Enter valid Option");
            }
        }while(m!=3);  
    }
    

    public static void admin(String id,Admin a) throws Exception{
        System.out.println();
        int ch;
        do{
            System.out.println("(1)Add Route (2)Add Train (3)Quit");
            ch=sc.nextInt();
            switch(ch){
                case 1 : System.out.println();
                         ArrayList<Integer> rNO=DB.routeNo();
                         if(rNO.size()==0){
                            sc.nextLine();
                            a.addNewRoute(1);
                         }else{
                            sc.nextLine();
                            a.addNewRoute(rNO.get(rNO.size()-1)+1);
                         }
                         
                         break;
                case 2 : System.out.println();
                         a.addTrain(getAllRoute());
                         break;
                case 3 : return;
                default : System.out.println("InValid Option");
            }
        }while(ch!=3);
    }

    public static void user(String username,User u,ArrayList<Train> allTrains) throws Exception{
        System.out.println();
        int ch;
        do{
            System.out.println("(1)Ticket Book (2)view Ticket (3)Print Ticket (4)Exit");
            ch=sc.nextInt();
            switch(ch){
                case 1 :System.out.println(); 
                        u.ticketBook(username,allTrains,getAllTicket());
                        System.out.println();
                        sc.nextLine();
                         break;
                case 2 : System.out.println();
                u.viewTicket(getAllTicket());
                         break;
                case 3 : u.printTicket(getAllTicket());
                         break;
                case 4 : return;
                default : System.out.println("InVlid Option");            
            }
        }while(true);
    }
}
