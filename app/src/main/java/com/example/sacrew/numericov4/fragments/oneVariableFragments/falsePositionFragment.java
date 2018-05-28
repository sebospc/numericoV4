package com.example.sacrew.numericov4.fragments.oneVariableFragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpFalsePosition;
import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.FalsePosition;
import com.example.sacrew.numericov4.fragments.listViewCustomAdapter.FalsePositionListAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class falsePositionFragment extends baseOneVariableFragments {


    public falsePositionFragment() {
        // Required empty public constructor
    }


    private GraphView graph;
    private View view;
    private ListView listView;
    private EditText xi, xs;
    private ToggleButton errorToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_false_position, container, false);
        } catch (InflateException e) {
            //ojo
        }
        Button runFake = view.findViewById(R.id.runFalse);
        runFake.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                bootStrap();
            }
        });
        Button runHelp = view.findViewById(R.id.runHelp);
        listView = view.findViewById(R.id.listView);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        graph = view.findViewById(R.id.falseGraph);
        textFunction = view.findViewById(R.id.function);
        iter = view.findViewById(R.id.iterations);
        textError = view.findViewById(R.id.error);
        xi = view.findViewById(R.id.xi);
        xs = view.findViewById(R.id.xs);
        errorToggle = view.findViewById(R.id.errorToggle);

        textFunction.setAdapter(new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, graphFragment.allFunctions));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpFalsePosition.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute(boolean error, double errorValue, int ite) {
        this.xi.setError(null);
        this.xs.setError(null);
        double xiValue = 0.0;
        double xsValue = 0.0;

        try {
            xiValue = Double.parseDouble(xi.getText().toString());

        } catch (Exception e) {
            xi.setError("Invalid Xi");
            error = false;
        }
        try {
            xsValue = Double.parseDouble(xs.getText().toString());
        } catch (Exception e) {
            xs.setError("Invalid xs");
            error = false;
        }

        try {
            errorValue = new Expression(textError.getText().toString()).eval().doubleValue();
        } catch (Exception e) {
            textError.setError("Invalid error value");
        }
        if (error) {
            if (errorToggle.isChecked()) {
                falsePosition(xiValue, xsValue, errorValue, ite, true);
            } else {
                falsePosition(xiValue, xsValue, errorValue, ite, false);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void falsePosition(double xi, double xs, double tol, int ite, boolean errorRel) {
        graph.removeAllSeries();
        function.setPrecision(100);
        ArrayList<FalsePosition> listValues = new ArrayList<>();
        FalsePosition titles = new FalsePosition("n", "Xi", "Xs", "Xm", "f(Xm)", "Error");
        listValues.add(titles);
        if (tol >= 0) {
            if (ite > 0) {
                double yi = (this.function.with("x", BigDecimal.valueOf(xi)).eval()).doubleValue();
                if (yi != 0) {
                    double ys = (this.function.with("x", BigDecimal.valueOf(xs)).eval()).doubleValue();
                    if (ys != 0) {
                        if (yi * ys < 0) {

                            double xm = xi - (yi * (xi - xs)) / (yi - ys);
                            double ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();
                            double error = tol + 1;
                            FalsePosition iteZero = new FalsePosition(String.valueOf(0), String.valueOf(convertirNormal(xi)), String.valueOf(convertirNormal(xs)), String.valueOf(convertirNormal(xm)), String.valueOf(convertirCientifica(ym)), String.valueOf(convertirCientifica(error)));
                            listValues.add(iteZero);
                            int cont = 1;
                            double xaux = xm;
                            while ((ym != 0) && (error > tol) && (cont < ite)) {
                                if (yi * ym < 0) {
                                    xs = xm;
                                    ys = ym;
                                } else {
                                    xi = xm;
                                    yi = ym;
                                }
                                xaux = xm;
                                //graphStraight(xi,yi,xs,ys,graph);
                                xm = xi - ((yi * (xi - xs)) / (yi - ys));
                                //graphPoint(xm,0,PointsGraphSeries.Shape.POINT,graph,getActivity(),"#FA4659",false);
                                ym = (this.function.with("x", BigDecimal.valueOf(xm)).eval()).doubleValue();

                                if (errorRel)
                                    error = Math.abs(xm - xaux) / xm;
                                else
                                    error = Math.abs(xm - xaux);
                                FalsePosition iteNext = new FalsePosition(String.valueOf(cont), String.valueOf(convertirNormal(xi)), String.valueOf(convertirNormal(xs)), String.valueOf(convertirNormal(xm)), String.valueOf(convertirCientifica(ym)), String.valueOf(convertirCientifica(error)));
                                listValues.add(iteNext);
                                cont++;
                            }

                            if (ym == 0) {
                                graphSerie(xm - 0.2, xm + 0.2, this.function.getExpression(), graph, Color.BLUE);
                                graphPoint(xm, ym, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                                Toast.makeText(getContext(), convertirNormal(xm) + " is an aproximate root", Toast.LENGTH_SHORT).show();

                            } else if (error < tol) {
                                graphSerie(xm - 0.2, xm + 0.2, this.function.getExpression(), graph, Color.BLUE);
                                graphPoint(xaux, ym, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                                Toast.makeText(getContext(), convertirNormal(xaux) + " is an aproximate root", Toast.LENGTH_SHORT).show();


                            } else {

                            }
                        } else {
                            Toast.makeText(getContext(), "The interval dont have root", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), convertirNormal(xs) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                        graphPoint(xs, ys, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                    }
                } else {
                    Toast.makeText(getContext(), convertirNormal(xi) + " is an aproximate root", Toast.LENGTH_SHORT).show();
                    graphPoint(xi, yi, PointsGraphSeries.Shape.POINT, graph, getActivity(), "#0E9577", true);
                }
            } else {
                iter.setError("Wrong iterates");


            }
        } else {
            textError.setError("Tolerance must be > 0");
        }
        FalsePositionListAdapter adapter = new FalsePositionListAdapter(getContext(), R.layout.list_adapter_false_position, listValues);
        listView.setAdapter(adapter);
    }//


}


