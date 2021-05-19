package com.rf.springsecurity.justToCheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        TransactionTemplate template = new TransactionTemplate();


        String QUERY = "select * from dishes where id = 109";
        String QUERY2 = "select * from dishes where id = 110";
        String update_query = "update dishes set calories = 4499 where id = 109";
        String update_query2 = "update dishes set calories = 10599 where id = 110";

        Class.forName("org.postgresql.Driver");
        try(Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/weightloss_system?currentSchema=public",
                "postgres", "123");
            Connection conn1 = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/weightloss_system?currentSchema=public",
                "postgres", "123")){
            Statement statemnt1 = conn.createStatement();
            ResultSet rs1 = statemnt1.executeQuery("select * from dishes");
            printResSet(rs1);

            conn.setAutoCommit(false);
            System.out.println("Conn setAutoCommit = " + conn.getAutoCommit());

            statemnt1 = conn.createStatement();
            int return_rows = statemnt1.executeUpdate(update_query);

            Statement statement2 = conn1.createStatement();
            ResultSet rs;
            rs = statement2.executeQuery(QUERY);
            printResSet(rs);
            conn.commit();

            Savepoint s1 = conn.setSavepoint();

            rs = statement2.executeQuery(QUERY);
            printResSet(rs);

            rs = statemnt1.executeQuery(QUERY);
            rs1 = statemnt1.executeQuery(QUERY2);
            printResSet(rs1);

            statemnt1.executeUpdate(update_query2);

            rs1 = statemnt1.executeQuery(QUERY2);
            printResSet(rs1);

            conn.rollback(s1);
            rs1 = statemnt1.executeQuery(QUERY2);
            printResSet(rs1);
        }catch (SQLException e){

        }
    }

    public static void printResSet(ResultSet rs) throws SQLException{
        while(rs.next()){
            System.out.println(
                    rs.getInt("id") + ", " +
                    rs.getInt("user_id") + "," +
                    rs.getString("name") +", "+
                    rs.getInt("calories"));
        }
    }
}
