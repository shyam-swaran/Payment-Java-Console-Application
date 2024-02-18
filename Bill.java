import java.time.LocalDate;

public class Bill {
    static int  idgen=1;
    int id;
    String vendorName;
    double amount;
    LocalDate date;
    boolean status;
    
    Bill(String vendorName,double amount,LocalDate date){
        this.vendorName=vendorName;
        this.amount=amount;
        this.date=date;
        id=idgen++;
        this.status=false;
    }
   
    public double getAmount() {
        return 200;
    }
    public void markPaid(double paymentForBill) {
        this.status=true;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public double getRemainingAmount() {
       return (double)0;
    }
}
