import java.util.ArrayList;
import java.util.List;

public class Vendor {
    String vendorname;
    List<Bill> bills;
    Vendor(String vendorName){
        this.vendorname=vendorName;
        bills=new ArrayList<>();
    }
    public void addBill(Bill bill) {
        bills.add(bill);
    }
    public List<Bill> getBills(){
        return bills;
    }
}
