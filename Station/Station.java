package Station;

import java.util.HashMap;
import java.util.Map;

public class Station{
        public String name;
        public String src;
        public int km;
        public String time;
        public HashMap<String,Integer> seats;
        public boolean[] sl;
        public boolean tAC[];
        public boolean sAC[];
        public boolean fAC[];

        public Station(String name,String src,int km,int s,int tA,int sA,int fA){
            this.name=name;
            this.src=src;
            this.km=km;
            this.sl=new boolean[s];
            this.tAC=new boolean[tA];
            this.sAC=new boolean[sA];
            this.fAC=new boolean[fA];
        }
    
    public void seatShow(){
        for(Map.Entry m : seats.entrySet()){
                int r=(int) m.getValue();
                if(r!=0){
                    System.out.println(m.getKey()+" - "+m.getValue());
                }
        }
    }
}
