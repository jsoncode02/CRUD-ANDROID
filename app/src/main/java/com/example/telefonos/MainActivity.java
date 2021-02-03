package com.example.telefonos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Conexion connectionClass;
    EditText edtCodigo, edtMarca, edtModelo;
    Button btnGuardar, btnActualizar, btnEliminar;
    ProgressBar pbbar;
    ListView lstTelefono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionClass = new Conexion();
        edtCodigo = (EditText) findViewById(R.id.edtCodigo);
        edtMarca = (EditText) findViewById(R.id.edtMarca);
        edtModelo = (EditText) findViewById(R.id.edtModelo);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        lstTelefono = (ListView) findViewById(R.id.lstTelefonos);

        FillList fillList = new FillList();
        fillList.execute();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarTel add = new GuardarTel();
                add.execute("");
                edtCodigo.setText("");
                edtMarca.setText("");
                edtModelo.setText("");
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";
        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override  protected void onPreExecute(){
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override  protected void onPostExecute(String r){
            pbbar.setVisibility((View.GONE));
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();

            String[] from =  {"A", "B", "C"};
            int[] views = {R.id.lblCodigo, R.id.lblMarca, R.id.lblModelo };
            final SimpleAdapter ADA = new SimpleAdapter(MainActivity.this, prolist, R.layout.lsttemple, from, views);
            lstTelefono.setAdapter(ADA);

            lstTelefono.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>)
                            ADA.getItem(arg2);
                    String Codigo = (String) obj.get("A");
                    String Marca = (String) obj.get("B");
                    String Modelo = (String) obj.get("C");
                    edtCodigo.setText(Codigo);
                    edtMarca.setText(Marca);
                    edtModelo.setText(Modelo);
                }
            });
        }
        @Override protected String doInBackground (String... params){
            try {
                Connection con = connectionClass.CONN();
                if(con == null){
                    z = "Error en la conexion con SQL";
                }else{

                    String query = "Select CODIGO, MARCA, MODELO from TELEFONOS";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()){
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("CODIGO"));
                        datanum.put("B", rs.getString("MARCA"));
                        datanum.put("C", rs.getString("MMODELO"));
                        prolist.add(datanum);
                    }

                }

            } catch (Exception ex){
                z = "Error al traer datos de la tabla";
            }
            return  z;
        }
    }

    public class GuardarTel extends AsyncTask<String, String, String>{
        String z = "";
        Boolean isSuccess = false;

        String Codigo = edtCodigo.getText().toString();
        String Marca = edtMarca.getText().toString();
        String Modelo = edtModelo.getText().toString();

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error en la conexion con SQL";
                } else {
                    String query = "inset into TELEFONOS (CODIGO, MARCA, MODELO)" +
                            "VALUES ('" + Codigo + "','" + Marca + "','" + Modelo + "')";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "Guardado Correctamente";
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Error";
            }
            return z;
        }
    }

    public class ActualizarTelefono extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        String codigo = edtCodigo.getText().toString();
        String marca = edtMarca.getText().toString();
        String modelo = edtModelo.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }

        }

        @Override
        protected String doInBackground(String... params) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String query = "Update TELEFONOS set CODIGO='" +codigo+ "', MARCA='"+ marca +"' , MODELO='"+modelo+"' where CODIGO='" +codigo;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Updated Successfully";

                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }

            return z;
        }
    }

    public class EliminarTelefono extends AsyncTask<String, String, String>{
        String z = "";
        Boolean isSuccess = false;

        String codigo = edtCodigo.getText().toString();
        String marca = edtMarca.getText().toString();
        String modelo = edtModelo.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }

        }

        @Override
        protected String doInBackground(String... params) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String query = "delete from TELEFONOS where CODIGO=" + codigo;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Deleted Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            return z;
        }
    }
}