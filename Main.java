import User.User;
import User.User.*;
import Admin.Admin;
import Admin.Admin.*;
import Database.Database;

import java.util.*;

public class Main {

    static Scanner sc=new Scanner(System.in);

    private static ArrayList<Route> getAllRoute() throws Exception{
        return Database.getAllRoute();
    }

    private static ArrayList<Train> getAllTrain() throws Exception{
        return Database.getAllTrain();
    }

    private static HashMap<Integer,Ticket> getAllTicket() throws Exception{
        return Database.getAllTicket();
    }

    private static HashMap<String,String> allAdmins()throws Exception{
        return Database.allAdmins();
    }

    private static HashMap<String,String> allUsers()throws Exception{
        return Database.allUsers();
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
                                        user(username,u);
                                        break;
                                    }else{
                                        System.out.println("Enter Valid Password");
                                    }
                                }while(true);
                                
                            }else{
                                System.out.println("this username not found");
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
                            Database.UserDatailsInDB(username, password);
                            user(username,u);
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
                                        System.out.println("Enter Valid Password");
                                    }
                                }while(true);
                                
                            }else{
                                System.out.println("this ID not found");
                            }
                         }else if(c2==2){
                            sc.nextLine();
                            System.out.println("*-*New Admin \nEnter Id");
                            String id=sc.nextLine();
                            if(allAdmin.containsKey(id)){
                                System.out.println(id+" id already Taken try again..");
                                break;
                            }
                            System.out.print("Enter Password : ");
                            String password=sc.nextLine();
                            Database.adminDatailsInDB(id, password);
                            admin(id,a);
                         }
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
                         a.addNewRoute();
                         break;
                case 2 : System.out.println();
                         a.addTrain(getAllRoute());
                         break;
                case 3 : return;
                default : System.out.println("InValid Option");
            }
        }while(ch!=3);
    }

    public static void user(String username,User u) throws Exception{
        System.out.println();
        int ch;
        do{
            System.out.println("(1)Ticket Book (2)view Ticket (3)Print Ticket (4)Exit");
            ch=sc.nextInt();
            switch(ch){
                case 1 :System.out.println(); 
                        u.ticketBook(username,getAllTrain(),getAllTicket());
                        System.out.println();
                         break;
                case 2 : u.viewTicket(getAllTicket());
                         break;
                case 3 : u.printTicket(getAllTicket());
                         break;
                case 4 : return;
                default : System.out.println("InValid Option");            
            }
        }while(ch!=3);
    }
}
