package Check;

public class Check {
    public static boolean mobileNumber(String number){
        if(number.length()==10){
            for(int i=0;i<10;i++){
                if((number.charAt(i)<'0' || number.charAt(i)>'9')){
                    System.out.println("Invalid Number");
                    return false;
                }
            }
            return true;
        }
        System.out.println("Invalid Number");
        return false;
    }

    public static boolean time(String time){
        if(time.length()==4){
            for(int i=0;i<time.length();i++){
                if(time.charAt(2)!=':'){
                    System.out.println("Invalid Time");
                    return false;
                }
                if(i==2){
                    continue;
                }
                if(time.charAt(i)<'0' || time.charAt(i)>'9'){
                    System.out.println("Invalid Time");
                    return false;
                }
            }
        }
        return true;
    }
}
