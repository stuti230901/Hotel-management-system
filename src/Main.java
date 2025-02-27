import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
    private static final String username = "root";
    private static final String password = "Shubhi@098";
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";


    private static void reserveRoom(Scanner scanner, Statement statement) throws SQLException {
        try{
            System.out.println("Enter Guest name");
            String guestName = scanner.next();
//            guestName.trim();
            System.out.println("Enter the room number guest wishes to stay in.");
            int roomNo = scanner.nextInt();
            System.out.println("Enter guest's contact number");
            String contactNo = scanner.next();
//            contactNo.trim();

            String sql = "INSERT INTO reservations (guest_name,room_number,contact_number) " + "VALUES ('" + guestName + "', '" + roomNo + "' , '" + contactNo + "');";

            int affectedRows = statement.executeUpdate(sql);

            if(affectedRows > 0){
                System.out.println("Reservation successful !!");
            }else{
                System.out.println("Reservation failed!!");
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(InputMismatchException e){
            System.out.println("Please enter the values in correct datatype");
        }
    }

    private static void viewReservations(Statement statement){
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations;";

        try{
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            //Here in the resultSet methods, we have mentioned the column names in the database.
            while(resultSet.next()){
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void exit(){
        System.out.println("Exiting System!");
        int i=5;
        try{
            while(i != 0){
                System.out.print(".");
                Thread.sleep(1000);
                i--;
            }
            System.out.println();
            System.out.println("ThankYou For Using Hotel Reservation System!!!");
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }

    }
    private static void getRoomNumber(Scanner scanner, Statement statement){
        System.out.println("Please enter the name of guest");
        String name = scanner.next();
        System.out.println("Please enter the reservation id for the booking");
        int id = scanner.nextInt();
        String sql = "SELECT room_number FROM reservations WHERE reservation_id=" + id + " AND guest_name='" + name + "'";
        try{
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()) {
                int room_num = resultSet.getInt("room_number");
                System.out.println("The room number " + room_num + " is booked for reservation ID " + id + " and guest name " + name);
            }else{
                System.out.println("No booking is made associated with the mentioned Reservation Id and guest name. Please check again.");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void updateReservation(Scanner scanner, Statement statement){
            System.out.println("Enter the reservation ID the guest wants to update.");
            int reservationId = scanner.nextInt();

            System.out.println("Please enter the details as follows to be updated");
            System.out.println("Please enter the guest name");
            String name = scanner.next();
            System.out.println("Enter room number");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter contact number");
            String contactNumber = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '" + name + "' , room_number = " + roomNumber + ", contact_number = '" + contactNumber + "' WHERE reservation_id = " + reservationId;
            try{
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows > 0) {
                    System.out.println("Data is updated successfully");
                }else{
                    System.out.println("Data update is unsuccessful");
                }
            }catch(SQLException e) {
                System.out.println(e.getMessage());
            }
    }

    private static void deleteReservation(Scanner scanner, Statement statement){
        System.out.println("Please enter the reservation ID which needs to be deleted");
        int reservationId = scanner.nextInt();

        String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;
        try{
            int affectedRows = statement.executeUpdate(sql);
            if(affectedRows > 0) {
                System.out.println("Delete successful");
            }else{
                System.out.println("Reservation with the given reservation ID does not exist");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) throws ClassNotFoundException {
        //forName used to load all the functions regarding Driver class, if that class uis not found, thus the Exception.
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            //getting connection details in the connection object.
            //url - jdbc mysql url
            //username - MySQL username
            //password - MySQL password
            Connection connection = DriverManager.getConnection(url, username, password);

            //While loop
            while(true){
                System.out.println();
                Scanner scanner = new Scanner(System.in);
                Statement statement = connection.createStatement();
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number in case of online booking");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                switch(choice){
                    case 1:
                        reserveRoom(scanner,statement);
                        break;
                    case 2:
                        viewReservations(statement);
                        break;
                    case 3:
                        getRoomNumber(scanner, statement);
                        break;
                    case 4:
                        updateReservation(scanner, statement);
                        break;
                    case 5:
                        deleteReservation(scanner, statement);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice, try again!");


                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}