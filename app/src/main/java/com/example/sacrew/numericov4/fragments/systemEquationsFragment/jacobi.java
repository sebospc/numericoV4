package com.example.sacrew.numericov4.fragments.systemEquationsFragment;


import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.tableview.TableViewModel;

import java.util.LinkedList;
import java.util.List;

import static com.example.sacrew.numericov4.fragments.systemEquations.animations;
import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;
import static com.example.sacrew.numericov4.fragments.systemEquations.count;
import static com.example.sacrew.numericov4.fragments.systemEquations.matrixAText;
import static com.example.sacrew.numericov4.fragments.systemEquations.xValuesText;

/**
 * A simple {@link Fragment} subclass.
 */
public class jacobi extends baseIterativeMethods{
    private EditText error,iters,relaxation;
    private ToggleButton errorToggle;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout initialValues;


    public jacobi() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jacobi, container, false);
        error = view.findViewById(R.id.error);
        iters = view.findViewById(R.id.iterations);
        relaxation = view.findViewById(R.id.relaxation);
        Button run = view.findViewById(R.id.run);
        errorToggle = view.findViewById(R.id.errorToggle);
        initialValues = view.findViewById(R.id.initialValues);
        for(int i = 0; i < count; i++) {
            initialValues.addView(defaultEditText("0",false));
        }
        Button pivoter = view.findViewById(R.id.pivoting);
        Button runChart = view.findViewById(R.id.runChart);
        runChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calc)
                    executeChart(getContext());
            }
        });
        pivoter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                securePivot();
            }
        });
        run.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                begin();
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void bootStrap(double[][] expandedMatrix){
        xValuesText.removeAllViews();
        double [] initial = new double[expandedMatrix.length];
        for(int i = 0;i<initialValues.getChildCount();i++){
            EditText aux = ((EditText)initialValues.getChildAt(i));
            try{
                initial[i] = Double.parseDouble(aux.getText().toString());
            }catch (Exception e){
                aux.setError("invalid value");
                return;
            }
        }

        boolean works = true;
        int iterations = 0;
        try {
            iterations=Integer.parseInt(iters.getText().toString());
        }catch (Exception e){
            iters.setError("Invalid iterations");
            works = false;
        }
        double tolerance = 0;
        try{
            tolerance = Double.parseDouble(error.getText().toString());
        }catch (Exception e){
            error.setError("Invalid tolerance");
            works = false;
        }
        double relax = 0;
        try {
            relax = Double.parseDouble(relaxation.getText().toString());
        }catch (Exception e){
            relaxation.setError("Invalid relaxation");
        }
        if(works)
            jacobiMethod(iterations,tolerance,relax,initial,expandedMatrix);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void jacobiMethod(int iters, double tolerance,double relax ,double[] initials,double[][] expandedMAtrix){
        int contador = 0;
        double dispersion = tolerance + 1;
        double [] x0 = initials;
        List<List<String>> totalInformation = new LinkedList<>();
        List<String> lisTitles = new LinkedList<>();
        List<String> aux = new LinkedList<>();
        for(int i = 0; i < initials.length; i++){
            lisTitles.add("X"+(i+1));
            aux.add(String.valueOf(x0[i]));

        }
        lisTitles.add("Norma");
        aux.add(String.valueOf(dispersion));

        TableViewModel.getTitles(lisTitles);

        totalInformation.add(aux);
        while(dispersion > tolerance && contador < iters){
            aux = new LinkedList<>();
            double [] x1 ;
            x1 = calcNewJacobi(x0,expandedMAtrix,relax);

            if(errorToggle.isChecked())
                dispersion = norma(minus(x1,x0));
            else
                dispersion = norma(minus(x1,x0))/norma(x1);
            for(double v:x1)aux.add(String.valueOf(v));
            aux.add(String.valueOf(dispersion));
            totalInformation.add(aux);
            x0 = x1;
            contador = contador + 1;
        }
        TableViewModel.getCeldas(totalInformation);
        calc = true;
        if(dispersion < tolerance){
            for(double val: x0)
                xValuesText.addView(defaultEditText((val+"      ").substring(0,6)));
        }else{
            for(double val: x0)
                xValuesText.addView(defaultEditText((val+"      ").substring(0,6)));
            Toast.makeText(getContext(),  "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public double[] calcNewJacobi(double[] x0,double[][] expandedMatrix, double relax){
        double[] x = new double[x0.length];
        int n = expandedMatrix.length;
        for(int i = 0; i < n ; i++){
            double suma = 0;
            for(int j = 0; j < n ; j++){
                if( j!= i)
                    suma = suma + expandedMatrix[i][j]*x0[j];
            }
            double value = (relax*((expandedMatrix[i][n] - suma)/expandedMatrix[i][i]))+(1-relax)*x0[i];
            x[i] = value;
        }
        return x;
    }



}
