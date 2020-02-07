package com.example.databasedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.AlteredCharSequence;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter
{
    Context mContext;
    int layoutRes;
    List<Employee> employees;
    SQLiteDatabase mDatabase;

    public EmployeeAdapter(@NonNull Context mContext, int layoutRes, List<Employee> employees, SQLiteDatabase mDatabase)
    {
        super(mContext, layoutRes, employees);
        this.mContext = mContext;
        this.layoutRes = layoutRes;
        this.employees = employees;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(layoutRes, null);
        TextView tvName = v.findViewById(R.id.tv_name);
        TextView tvSalary = v.findViewById(R.id.tv_salary);
        TextView tvDepartment = v.findViewById(R.id.tv_dept);
        TextView tvJoiningDate = v.findViewById(R.id.tv_joiningdate);

        final Employee employee = employees.get(position);
        tvName.setText(employee.getName());
        tvSalary.setText(String.valueOf(employee.getSalary()));
        tvDepartment.setText(employee.getDept());
        tvJoiningDate.setText(employee.getJoiningDate());

        v.findViewById(R.id.btn_edit_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmployee(employee);
            }
        });

        v.findViewById(R.id.btn_delete_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEmployee(employee);
            }
        });

        return v;

    }

    private void deleteEmployee(final Employee employee)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Are you Sure ?");
        builder.setPositiveButton("Yes 👍", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String sql = "DELETE from employee WHERE id=?";
                mDatabase.execSQL(sql, new Integer[]{employee.getId()});
                loadEmployees();
            }
        });

        builder.setNegativeButton("NO 😈", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateEmployee(final Employee employee)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.update_employee,null);
        alert.setView(v);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

        final EditText etName = v.findViewById(R.id.editTextName);
        final EditText etSalary = v.findViewById(R.id.editTextSalary);
        final Spinner spinner = v.findViewById(R.id.spinnerDepartment);

        etName.setText(employee.getName());
        etSalary.setText(String.valueOf(employee.getSalary()));

        v.findViewById(R.id.btnUpdateEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String salary = etSalary.getText().toString().trim();
                String dept = spinner.getSelectedItem().toString();

                if(name.isEmpty())
                {
                    etName.setError("Name Field is Mandatory");
                    etName.requestFocus();
                    return;
                }

                if(salary.isEmpty())
                {
                    etSalary.setError("Salary Field is Mandatory");
                    etSalary.requestFocus();
                    return;
                }

                String updatesql = "UPDATE employee SET name = ?, department = ?, salary = ? WHERE id = ?";
                mDatabase.execSQL(updatesql, new String[]{name, dept, salary, String.valueOf(employee.getId())});
                Toast.makeText(mContext, "Employee Updated", Toast.LENGTH_SHORT).show();
                loadEmployees();
                alertDialog.dismiss();
            }
        });




    }

    private void loadEmployees()
    {
        String sql = "SELECT *  FROM employee";
        Cursor cursor = mDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            employees.clear();

            do {
                employees.add(new Employee(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        notifyDataSetChanged();
    }
}