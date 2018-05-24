package com.example.sacrew.numericov4.fragments.systemEquations;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.bValuesText;
import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.matrixAText;

/**
 * Created by sacrew on 23/05/18.
 */

public class baseSystemEquations extends Fragment {
    public baseSystemEquations(){

    }

    public void begin(){
        int n = matrixAText.getChildCount();
        double [][] expandedMatrix = new double[n][n+1];
        for(int i=0; i<n; i++){
            for (int j=0; j< n;j++){
                EditText aux = ((EditText) ((TableRow) matrixAText.getChildAt(i)).getChildAt(j));
                try {
                    double x = 0;

                    x = Double.parseDouble((aux.getText())
                            .toString());
                    //System.out.print(x);
                    expandedMatrix[i][j] = x;
                }catch (Exception e){
                    aux.setError("invalid value");
                    return;
                }
            }
            EditText aux = ((EditText)bValuesText.getChildAt(i));
            try{
                double x = Double.parseDouble(aux.getText().toString());
                expandedMatrix[i][n] = x;
            }catch (Exception e){
                aux.setError("invalid value");
                return;
            }
        }
        execute(expandedMatrix);
    }
    public void execute(double[][] expandedMatrix){

    }

    public static String convertirTexto(String val){
        Locale.setDefault(Locale.US);
        DecimalFormat num = new DecimalFormat("#.##");
        return num.format(val);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public TextView defaultEditText(String value){
        TextView text = new EditText(getContext());
        text.setLayoutParams(new TableRow.LayoutParams(100, 110));
        text.setEms(2);
        text.setMaxLines(1);
        text.setBackground(null);
        text.setTypeface(null, Typeface.BOLD);
        text.setBackgroundColor(Color.rgb(76,175,80));
        text.setTextColor(Color.WHITE);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setKeyListener(null);
        text.setText(value);
        return text;
    }
}
