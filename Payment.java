import java.time.LocalDate;
import java.util.List;

public class Payment {
    LocalDate date;
    double paymentAmount;
    List<PaymentDetail> paymentDetails;
    
    public Payment(LocalDate now, double paymentAmount, List<PaymentDetail> paymentDetails) {
        this.date=now;
        this.paymentAmount=paymentAmount;
        this.paymentDetails=paymentDetails;
    }

}
