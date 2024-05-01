/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms_lecture;

/**
 *
 * @author DUVINDU
 */
public class Marks {

    private String subject;

    public Marks() {
    }

    ;
    
    public Marks(String subject) {
        this.subject = subject;

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double calculateFinalMarksMethod0(double quizzesMarks, double midTermMarks, double finalTheoryMarks, double finalPracticalMarks) {
        double quizzesWeightedMarks = quizzesMarks * 10 / 100.0;
        double midTermWeightedMarks = midTermMarks * 20 / 100.0;
        double finalTheoryWeightedMarks = finalTheoryMarks * 40 / 100.0;
        double finalPracticalWeightedMarks = finalPracticalMarks * 30 / 100.0;

        return quizzesWeightedMarks + midTermWeightedMarks + finalTheoryWeightedMarks + finalPracticalWeightedMarks;
    }

    public double calculateFinalMarksMethod1(double quizzesMarks, double assessmentsMarks, double midTermMarks, double finalTheoryMarks) {
        double quizzesWeightedMarks = quizzesMarks * 10 / 100.0;
        double assessmentsWeightedMarks = assessmentsMarks * 10 / 100.0;
        double midTermWeightedMarks = midTermMarks * 20 / 100.0;
        double finalTheoryWeightedMarks = finalTheoryMarks * 60 / 100.0;

        return quizzesWeightedMarks + assessmentsWeightedMarks + midTermWeightedMarks + finalTheoryWeightedMarks;
    }

    public double calculateFinalMarksMethod2(double quizzesMarks, double assessmentsMarks, double finalTheoryMarks, double finalPracticalMarks) {
        double quizzesWeightedMarks = quizzesMarks * 10 / 100.0;
        double assessmentsWeightedMarks = assessmentsMarks * 20 / 100.0;
        double finalTheoryWeightedMarks = finalTheoryMarks * 40 / 100.0;
        double finalPracticalWeightedMarks = finalPracticalMarks * 30 / 100.0;

        return quizzesWeightedMarks + assessmentsWeightedMarks + finalTheoryWeightedMarks + finalPracticalWeightedMarks;
    }

    public double calculateFinalMarksMethod3(double quizzesMarks, double assessmentsMarks, double finalTheoryMarks, double finalPracticalMarks) {

        double quizzesWeightedMarks = quizzesMarks * 10 / 100.0;
        double assessmentsWeightedMarks = assessmentsMarks * 20 / 100.0;
        double finalTheoryWeightedMarks = finalTheoryMarks * 30 / 100.0;
        double finalPracticalWeightedMarks = finalPracticalMarks * 40 / 100.0;

        return quizzesWeightedMarks + assessmentsWeightedMarks + finalTheoryWeightedMarks + finalPracticalWeightedMarks;
    }
    

}
