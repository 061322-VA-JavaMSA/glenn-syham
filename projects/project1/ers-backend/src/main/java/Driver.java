import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.revature.dto.UserDTO;
import com.revature.exceptions.ReimbursementNotFoundException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;

 
public class Driver {
 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReimbursementService rs = new ReimbursementService();
		List<Reimbursement> reim = rs.getReimburse();
		UserService us = new UserService();
//		List<User> users = us.getUsers(); 
//		AuthService as = new AuthService();
//		try {
//			User principal = as.login("mike", "12345");
//			System.out.println(principal);
//
//		} catch (UserNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (LoginException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		List<Reimbursement> reimburse = rs.getReimburse(); 
//		System.out.println(reimburse);

		try {
			User u = us.getUserById(12);
			List<Reimbursement> reimburse = rs.getByAuthor(u);
			
//			users.forEach(u -> usersDTO.add(new UserDTO(u)));

			System.out.println(reimburse);
		} catch (ReimbursementNotFoundException | UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        String regex = "[0-9]/[\breim]";
//
//		String patString = "23/task";
//         Pattern pattern = Pattern.compile(regex);
//         Matcher matcher = pattern.matcher(patString);
//         if(matcher.find()) {
//            System.out.println("Match occurred");
//         } else {
//            System.out.println("Match not occurred");
//         }         
 	}

}