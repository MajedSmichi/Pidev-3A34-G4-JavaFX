//package Controller;
//
//import Entity.User;
//import Services.UserService;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.SQLException;
//
//@WebServlet("/response")
//public class ResponseServlet extends HttpServlet {
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String email = request.getParameter("email");
//        String action = request.getParameter("action");
//
//        if (email != null && action != null) {
//            if (action.equals("no")) {
//                // Update the isVerified field to false
//                UserService userService = new UserService();
//                User user = userService.findUserByEmail(email);
//                if (user != null) {
//                    user.setVerified(false);
//                    try {
//                        userService.updateUser(user, false);
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//            // Handle 'yes' action if needed
//        }
//    }
//}