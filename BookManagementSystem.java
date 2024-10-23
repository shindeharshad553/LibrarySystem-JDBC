package project2;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookManagementSystem {

	public static void main(String[] args) {
		

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","root");
		    if(con!=null) {
		    	System.out.println("---------------WELCOME TO THE BOOK MANAGEMENT SYSTEM---------------");
				Scanner sc=new Scanner(System.in);
				int ch;
				do {
					System.out.println();
					System.out.println(" 1. Add book details");
					System.out.println(" 2. Check the availability");
					System.out.println(" 3. Remove book details ");
					System.out.println(" 4. Modify the book name ");
					System.out.println(" 5. Exit ");
					System.out.println("Enter your choice:=");
					ch=sc.nextInt();
					createDatabase(con);
					switch(ch){
						case 1:
							addBookDetails(con,sc);
							break;
						case 2:
							checkAvailability(con,sc);
							break;
						case 3:
							removeBook(con,sc);
							break;
						case 4:
							modifyBook(con,sc);
							break;
						case 5:
							sc.close();
							con.close(); 
							System.out.println("Thank You For Using Book Management System");
							System.exit(0);
						default:
							System.out.println("Please enter choice between(1-5)");
					}		
				} while (ch != 5);		
		    	}
		    }catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
		e.printStackTrace();
	 }
}

	public static void createDatabase(Connection con) throws SQLException{
			Statement stmt=con.createStatement();
			stmt.execute("Create database if not exists BookManagement");
	    	stmt.execute("use BookManagement;");
	    	stmt.execute("Create table if not exists bookDetails(id int,book_name varchar(255),price int,author_name varchar(255));");
	    	try {
				if(stmt!=null)
					stmt.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
	}
		
	public  static void addBookDetails(Connection con,Scanner sc) throws SQLException{    	
		PreparedStatement stmt1=con.prepareStatement("insert into bookDetails values (?,?,?,?);");
		PreparedStatement stmt2=con.prepareStatement("select * from bookDetails;");
		System.out.println("Enter book details ");
		System.out.println("Enter book id =");
		int id=sc.nextInt();
		System.out.println("Enter book name =");
		sc.nextLine();
		String name=sc.nextLine();
		System.out.println("Enter book price =");
		while(!sc.hasNextInt()) {
			sc.next();
		}
		int price=sc.nextInt();
		System.out.println("Enter book author name =");
		sc.nextLine();
		String authorName=sc.nextLine();
		stmt1.setInt(1, id);
		stmt1.setString(2, name);
		stmt1.setInt(3,price);
		stmt1.setString(4,authorName);
		stmt1.execute();
		System.out.println("book added successfully!!");
		System.out.println();
		ResultSet rs1=stmt2.executeQuery();
		System.out.println("Displaying table details");
		while(rs1.next()) {
			System.out.println(rs1.getInt("id")+" "+rs1.getString("book_name")+" "+rs1.getInt("price")+" "+rs1.getString("author_name"));
		}
		try {
			if(stmt1!=null)
				stmt1.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void checkAvailability(Connection con,Scanner sc) throws SQLException{
		PreparedStatement stmt=con.prepareStatement("select book_name from bookDetails where book_name=?;");
		System.out.println("Enter book name to check availability = ");
		sc.nextLine();
		String name=sc.nextLine();
		stmt.setString(1, name);
		ResultSet rs=stmt.executeQuery();
		if(rs.next()) {
			System.out.println("book is available "+rs.getString("book_name"));
		}
		else
			System.out.println("Book is not available");
		try {
			if(stmt!=null)
				stmt.close();
			if(rs!=null)
				rs.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeBook(Connection con,Scanner sc) throws SQLException {
		System.out.println("Enter the name of book to delete ");
		sc.nextLine();
		String name=sc.nextLine();
		PreparedStatement stmt=con.prepareStatement("delete from bookDetails where book_name=?;");
		stmt.setString(1,name);
		int rowsChanged=stmt.executeUpdate();
		if(rowsChanged>0)
			System.out.println("Book removed Successfully");
		else
			System.out.println("No book exists with this name ");
		try {
			if(stmt!=null)
				stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void modifyBook(Connection con,Scanner sc) throws SQLException {
//		Update the book name 
		System.out.println("Enter the book name to update = ");
		sc.nextLine();
		String oldName=sc.nextLine();
		System.out.println("Enter the new book name =");
		String newName=sc.nextLine();
		PreparedStatement stmt=con.prepareStatement("Update bookDetails set book_name =? where book_name=?;");
		stmt.setString(1, newName);
		stmt.setString(2, oldName);
		int rowsChanged=stmt.executeUpdate();
		if(rowsChanged>0)
			System.out.println("Book modify Successfully");
		else
			System.out.println("No book exists with this name ");
		try {
			if(stmt!=null)
				stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
			
	}
	
	
}