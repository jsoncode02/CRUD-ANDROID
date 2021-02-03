package com.example.telefonos;
import android.annotation.SuppressLint;

import android.os.StrictMode;

import android.util.Log;



import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.SQLException;


public class Conexion {

    String ip = "192.168.0.13";

    String classs = "net.sourceforge.jtds.jdbc.Driver";

    String db = "master";

    String un = "jason";

    String password = "1234";

    String port = "1433";



    @SuppressLint("NewApi")

    public Connection CONN() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()

                .permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Connection conn = null;

        String ConnURL = null;

        try {



            Class.forName(classs);

            ConnURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";"

                    + "databaseName=" + db + ";user=" + un + ";password="

                    + password + ";instance=MSSQLSERVER;loginTimeout=5;socketTimeout=2";

            conn = DriverManager.getConnection(ConnURL);

        } catch (SQLException se) {

            Log.e("ERRO1", se.getMessage());

        } catch (ClassNotFoundException e) {

            Log.e("ERRO2", e.getMessage());

        } catch (Exception e) {

            Log.e("ERRO3", e.getMessage());

        }

        return conn;

    }
}
