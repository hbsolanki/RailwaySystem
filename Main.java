import User.User;
import User.User.*;
import Admin.Admin;
import Admin.Admin.*;
import Database.Database.*;

import java.util.*;

public class Main {
    static ArrayList<Route> allRoute=new ArrayList<>();
    static ArrayList<Train> allTrain=new ArrayList<>();
    static HashMap<Integer,Ticket> map=new HashMap<>();
    static Scanner sc=new Scanner(System.in);

    public static void main(String[] args) {
        Admin a=new Admin();
        User u=new User();

        HashMap<String,String> allAdmins=new HashMap<>();
        HashMap<String,String> allUsers=new HashMap<>();

        
        int m;
        do{
            System.out.println("(1)User (2)Admin (3)Quit");
            m=sc.nextInt();
            switch(m){
                case 1 : System.out.println("(1)Already User (2)New User (3)Quit");
                         int c1=sc.nextInt();
                         if(c1==1){
                            sc.nextLine();
                            System.out.print("Enter UserName : ");
                            String username=sc.nextLine();
                            
                            if(allUsers.containsKey(username)){
                                do{
                                    System.out.print("Enter Password : ");
                                    String pass=sc.nextLine();
                                    if(allUsers.get(username).equals(pass)){
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
                            System.out.print("New USER \nEnter UserName : ");
                            String username=sc.nextLine();
                            System.out.print("Enter Password : ");
                            String password=sc.nextLine();
                            allUsers.put(username, password);
                            user(username,u);
                         }
                        break;
                case 2 : System.out.println("(1)Already Admin (2)New Admin (3)Quit");
                         int c2=sc.nextInt();
                         if(c2==1){
                            sc.nextLine();
                            System.out.print("Enter ID : ");
                            String id=sc.nextLine();
                            
                            if(allAdmins.containsKey(id)){
                                do{
                                    System.out.print("Enter Password : ");
                                    String pass=sc.nextLine();
                                    if(allAdmins.get(id).equals(pass)){
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
                            System.out.println("New Admin \nEnter Id");
                            String id=sc.nextLine();
                            System.out.print("Enter Password : ");
                            String password=sc.nextLine();
                            allAdmins.put(id,password);
                            admin(id,a);
                         }
                        break;
                default : System.out.println("Enter valid Option");
            }
        }while(m!=3);  
    }
    

    public static void admin(String id,Admin a){
        int ch;
        do{
            System.out.println("(1)Add Route (2)Add Train (3)Quit");
            ch=sc.nextInt();
            switch(ch){
                case 1 : System.out.println();
                        Route r=a.addNewRoute();
                        System.out.println();
                         if(r!=null){
                            allRoute.add(r);
                         }
                         break;
                case 2 : System.out.println();
                         Train t=a.addTrain(allRoute);
                         System.out.println();
                         if(t!=null){
                           allTrain.add(t); 
                         }
                         break;
                case 3 : return;
                default : System.out.println("InValid Option");
            }
        }while(ch!=3);
    }

    public static void user(String username,User u){
        int ch;
        do{
            System.out.println("(1)Ticket Book (2)Ticket view (3)Exit");
            ch=sc.nextInt();
            switch(ch){
                case 1 :System.out.println(); 
                        u.ticketBook(username,allTrain,map);
                        System.out.println();
                         break;
                case 2 : System.out.println("Enter Ticket No");
                         int ticketNo=sc.nextInt();            
                         if(map.containsKey(ticketNo)){
                            System.out.println();
                            u.ticketView(ticketNo,map);
                            System.out.println();
                         }else{
                            System.out.println("Invalid TicketNumber");
                         }
                         
                         break;
                case 3 : return;
                default : System.out.println("InValid Option");            
            }
        }while(ch!=3);
    }
}
