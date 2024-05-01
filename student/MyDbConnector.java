/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Genshin
 */
class MyDbConnector {
    
    
    private String url="Jdbc:mysql://localhost:3306/teclms_new";

    private String user="root";
    private String pass="1234";

    private Connection mycon=null;

    private void registerMyConnection() throws ClassNotFoundException{

        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Successfully registered...");
    }

    public Connection getMyConnection() throws ClassNotFoundException{

        registerMyConnection();

        try{
            mycon=DriverManager.getConnection(url,user,pass);
        }catch(SQLException e){
            System.out.println(e);
        }

    return mycon;
    }

   
    
}