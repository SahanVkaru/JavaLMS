/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

/**
 *
 * @author DUVINDU
 */
import admingui.*;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        LOGIN_Frame gIN_Frame = new LOGIN_Frame();
        gIN_Frame.RunLogIn();
        Login Uid = new Login();
        System.out.println(Uid.getUid());
    }
    
}
