package com.example.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String Databasename = "kk";
    SQLiteDatabase mdataBase;

    EditText edittextName, editTextSalary;
    Spinner spinnerDept;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edittextName = findViewById(R.id.editTextname);
        editTextSalary = findViewById(R.id.editTextSalary);
        spinnerDept = findViewById(R.id.SpinnerDepartment);

        findViewById(R.id.btnAddEmployee).setOnClickListener(this);
        findViewById(R.id.tvViewEmp).setOnClickListener(this);
        // in order to open or create database
        mdataBase= openOrCreateDatabase(Databasename , MODE_PRIVATE, null);
        createTable(mdataBase);


    }

    private void createTable(SQLiteDatabase mdataBase)
    {
        String sql = "CREATE TABLE IF NOT EXISTS EMPLOYEES ("+
                "id INTEGER NOT NULL CONSTRAINT employee_pk PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(200) NOT NULL," +
                "department VARCHAR(200) NOT NULL," +
                "joiningDAte DATETIME NOT NULL,"+
                "salary DOUBLE NOT NULL);";
        mdataBase.execSQL(sql);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAddEmployee:
                addEmploee();
                break;

            case R.id.tvViewEmp:

        }

    }



    private void addEmploee()
    {
        String name = edittextName.getText().toString().trim();
        String salary = editTextSalary.getText().toString().trim();
        String dept  = spinnerDept.getSelectedItem().toString();

        // Calander to get the current time

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningData = sdf.format(calendar.getTime());

        if (name.isEmpty())
        {
            edittextName.setError("name is Mandatory");
            edittextName.requestFocus();
            return;
        }
        if (salary.isEmpty())
        {
            editTextSalary.setError("salary is Mandatory");
            editTextSalary.requestFocus();
            return;
        }

        String sql = "INSERT INTO employees (name, department, joiningdate, salary)" + "VALUES (?,?,?,?);";
        mdataBase.execSQL(sql, new String[] {name, dept, joiningData, salary});
        Toast.makeText(this, "Employee ADdded", Toast.LENGTH_LONG).show();
    }
}
