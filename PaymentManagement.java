import java.time.LocalDate;
import java.util.*;

public class PaymentManagement {

    private static Map<String, Vendor> vendors = new HashMap<>();
    private static List<Payment> payments = new ArrayList<>();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int choice;

            do {
                System.out.println("\nPayment Management System");
                System.out.println("1. Add Vendor");
                System.out.println("2. Add Bill");
                System.out.println("3. Pay Bills");
                System.out.println("4. Get Total Payable Amount");
                System.out.println("5. Generate Report");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addVendor();
                        break;
                    case 2:
                        addBill();
                        break;
                    case 3:
                        payBills();
                        break;
                    case 4:
                        getTotalPayableAmount();
                        break;
                    case 5:
                        generateReport();
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } while (choice != 6);
        }
    }

    private static void addVendor() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter vendor name: ");
            String vendorName = scanner.nextLine();

            if (validateVendorName(vendorName)) {
                vendors.put(vendorName, new Vendor(vendorName));
                System.out.println("Vendor added successfully!");
            } else {
                System.out.println("Vendor name already exists");
            }
        }
    }

    private static boolean validateVendorName(String name) {
        return !vendors.containsKey(name);
    }

    private static void addBill() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter vendor name: ");
            String vendorName = scanner.nextLine();

            
            if (!vendors.containsKey(vendorName)) {
                System.out.println("Vendor not found! Please add the vendor first.");
                return;
            }

            Vendor vendor = vendors.get(vendorName);
            System.out.print("Enter bill amount: ");
            double amount = scanner.nextDouble();

            if (validateBillAmount(amount)) {
                System.out.print("Enter bill date (YYYY-MM-DD): ");
                LocalDate date = LocalDate.parse(scanner.next());
                Bill bill=new Bill(vendorName, amount, date);
                vendor.addBill(bill);
                System.out.println("Bill added successfully! \n Bill id: "+ bill.id);
            } else {
                System.out.println("Invalid bill amount. Please enter a positive number.");
            }
        }
    }

    private static boolean validateBillAmount(double amount) {
        return amount > 0; 
    }
    private static void payBills() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter payment amount: ");
            double paymentAmount = scanner.nextDouble();

            if (validatePaymentAmount(paymentAmount)) {
                System.out.println("Choose bills to pay (enter bill ID, separate by commas): ");
                String billIds = scanner.next();

                Set<String> billIdSet = new HashSet<>(Arrays.asList(billIds.split(",")));
                Map<String, Bill> billsToPay = new HashMap<>();

                for (String billId : billIdSet) {
                    try {
                        Bill bill = getBillById(billId);
                        if (bill != null) {
                            billsToPay.put(billId, bill);
                        } else {
                            System.out.println("Invalid bill ID: " + billId);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid bill ID format: " + billId);
                    }
                }

                if (!billsToPay.isEmpty()) {
                    processPayment(billsToPay, paymentAmount);
                } else {
                    System.out.println("No valid bills selected.");
                }
            } else {
                System.out.println("Invalid payment amount. Please enter a positive number.");
            }
        }
    }

    private static boolean validatePaymentAmount(double paymentAmount) {
        return paymentAmount >0;
    }

    private static Bill getBillById(String billId) {
        for (Vendor vendor : vendors.values()) {
            for (Bill bill : vendor.getBills()) {
                if (bill.id==Integer.parseInt(billId)) {
                    return bill;
                }
            }
        }
        return null;
    }

    private static void processPayment(Map<String, Bill> billsToPay, double paymentAmount) {
        double remainingAmount = paymentAmount;
        List<PaymentDetail> paymentDetails = new ArrayList<>();

        for (Bill bill : billsToPay.values()) {
            double paymentForBill = Math.min(remainingAmount, bill.getAmount());
            remainingAmount -= paymentForBill;
            paymentDetails.add(new PaymentDetail(bill.id, paymentForBill));
            bill.markPaid(paymentForBill); 
        }

        Payment payment = new Payment(LocalDate.now(), paymentAmount, paymentDetails);
        payments.add(payment);

        System.out.println("Payment successful! Details:");
        System.out.println(payment);
    }

    private static void getTotalPayableAmount() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Choose period:");
            System.out.println("1. Current month");
            System.out.println("2. Custom date range");
            int choice = scanner.nextInt();

            LocalDate startDate, endDate;
            double totalAmount = 0;

            switch (choice) {
                case 1:
                    startDate = LocalDate.now().withDayOfMonth(1);
                    endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
                    break;
                case 2:
                    System.out.print("Enter start date (YYYY-MM-DD): ");
                    startDate = LocalDate.parse(scanner.next());
                    System.out.print("Enter end date (YYYY-MM-DD): ");
                    endDate = LocalDate.parse(scanner.next());
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            for (Vendor vendor : vendors.values()) {
                for (Bill bill : vendor.getBills()) {
                    if (bill.getDate().isAfter(startDate.minusDays(1)) && bill.getDate().isBefore(endDate.plusDays(1))) {
                        totalAmount += bill.getRemainingAmount();
                    }
                }
            }

            System.out.printf("Total payable amount for %s to %s: %.2f\n", startDate, endDate, totalAmount);
        }
    }

    private static void generateReport() {
        System.out.println("Report generation not yet implemented.");
    }


}