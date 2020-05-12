package com.example.projetoaziz.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projetoaziz.R;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.ConfiguracaoDatabase;
import com.example.projetoaziz.helpers.MoneySort;
import com.example.projetoaziz.helpers.VariationSort;
import com.example.projetoaziz.models.ListaCommodities;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartsFragment extends Fragment {
    private static int[] FABINHO_COLORS = {rgb("#f44336"),
            rgb("#9c27b0"),
            rgb("#3f51b5"),
            rgb("#03a9f4"),
            rgb("#009688"),
            rgb("#8bc34a"),
            rgb("#ffeb3b"),};
    private FirebaseUser user;
    private List<ListaCommodities> listaCommodities = new ArrayList<>();
    private View v;
    private DatabaseReference db;
    private String caminho, acumulado, variado;
    private int posicao, size;
    private TextView colocacao;

    public ChartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_charts, container, false);
        db = null;
        user = ConfiguracaoDatabase.getFirebaseAutenticacao().getCurrentUser();
        Bundle bundle = getArguments();
        caminho = bundle.getString("idTurma");
        colocacao = v.findViewById(R.id.colocacaoText);

        recuperarListas();


        final BarChart mbar = v.findViewById(R.id.barChart);
        final BarChart mbar2 = v.findViewById(R.id.barChart2);

        Button variacao = v.findViewById(R.id.exibirVariacao);
        Button patrimonio = v.findViewById(R.id.exibirPatrimonio);

        variacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbar.setVisibility(View.GONE);
                mbar2.setVisibility(View.VISIBLE);
                colocacao.setText(Html.fromHtml(variado));

            }
        });

        patrimonio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbar.setVisibility(View.VISIBLE);
                mbar2.setVisibility(View.GONE);
                colocacao.setText(Html.fromHtml(acumulado));
            }
        });


        return v;
    }

    @SuppressLint("ResourceAsColor")
    private void plotarGraficos(List<ListaCommodities> seteAcumulado, List<ListaCommodities> seteVariacao) {
        TextView ranking = v.findViewById(R.id.textPosicao);
        String POSICAO = "Colocação: " + posicao + "/" + size;
        ranking.setText(POSICAO);
        BarChart patrimonioBarChart = v.findViewById(R.id.barChart);
        BarChart variacaoBarChart = v.findViewById(R.id.barChart2);
        List<BarEntry> patrimonioEntries = new ArrayList<>();
        List<BarEntry> variacaoEntries = new ArrayList<>();

        acumulado = "";
        for (int i = 0; i < seteAcumulado.size(); i++) {
            ListaCommodities usuario = seteAcumulado.get(i);
            patrimonioEntries.add(new BarEntry(i, usuario.getPatrimonio()));
            if (i == 0) {
                acumulado += String.format("<strong> <font color=#f44336> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());
            }
            if (i == 1) {
                acumulado += String.format("<strong> <font color=#9c27b0> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 2) {
                acumulado += String.format("<strong> <font color=#3f51b5> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 3) {
                acumulado += String.format("<strong> <font color=#03a9f4> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 4) {
                acumulado += String.format("<strong> <font color=#009688> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 5) {
                acumulado += String.format("<strong> <font color=#8bc34a> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 6) {
                acumulado += String.format("<strong> <font color=#ffeb3b> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
        }

        variado = "";
        for (int i = 0; i < seteVariacao.size(); i++) {
            ListaCommodities usuario = seteVariacao.get(i);
            variacaoEntries.add(new BarEntry(i, usuario.getPatrimonio() / usuario.getPatrimonioAnterior()));
            if (i == 0) {
                variado += String.format("<strong> <font color=#f44336> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());
            }
            if (i == 1) {
                variado += String.format("<strong> <font color=#9c27b0> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 2) {
                variado += String.format("<strong> <font color=#3f51b5> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 3) {
                variado += String.format("<strong> <font color=#03a9f4> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 4) {
                variado += String.format("<strong> <font color=#009688> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 5) {
                variado += String.format("<strong> <font color=#8bc34a> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }
            if (i == 6) {
                variado += String.format("<strong> <font color=#ffeb3b> %2d ) %s %s </font> </strong> <br/>", i, usuario.getNome(), usuario.getSobrenome());

            }

        }

        BarDataSet patrimonioDataSet = new BarDataSet(patrimonioEntries, "Patrimônio");
        BarDataSet variacaoDataSet = new BarDataSet(variacaoEntries, "Variação");
        patrimonioDataSet.setColors(FABINHO_COLORS);
        variacaoDataSet.setColors(FABINHO_COLORS);
        BarData patrimonioBarData = new BarData(patrimonioDataSet);
        YAxis yAxisRight = patrimonioBarChart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisLeft = patrimonioBarChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        XAxis xAxis = patrimonioBarChart.getXAxis();
        xAxis.setEnabled(false);
        patrimonioBarData.setBarWidth(0.9f);
        patrimonioBarChart.setDrawValueAboveBar(true);
        patrimonioBarChart.fitScreen();
        Description description = patrimonioBarChart.getDescription();
        description.setEnabled(false);
        Legend legend = patrimonioBarChart.getLegend();
        legend.setEnabled(false);

        patrimonioBarChart.setDrawGridBackground(false);
        patrimonioBarChart.setDrawBorders(false);
        patrimonioBarChart.setFitBars(true);
        patrimonioBarChart.setData(patrimonioBarData);
        patrimonioBarChart.invalidate();
        BarData variacaoBarData = new BarData(variacaoDataSet);
        YAxis yAxisRight2 = variacaoBarChart.getAxisRight();
        yAxisRight2.setEnabled(false);
        YAxis yAxisLeft2 = variacaoBarChart.getAxisLeft();
        yAxisLeft2.setEnabled(false);
        XAxis xAxis2 = variacaoBarChart.getXAxis();
        xAxis2.setEnabled(false);
        variacaoBarData.setBarWidth(0.9f);
        variacaoBarChart.setDrawValueAboveBar(true);
        variacaoBarChart.fitScreen();
        Description description2 = variacaoBarChart.getDescription();
        description2.setEnabled(false);
        Legend legend2 = variacaoBarChart.getLegend();
        legend2.setEnabled(false);
        variacaoBarChart.setDrawGridBackground(false);
        variacaoBarChart.setDrawBorders(false);
        variacaoBarChart.setFitBars(true);
        variacaoBarChart.setData(variacaoBarData);
        variacaoBarChart.invalidate();
        colocacao.setText(Html.fromHtml(acumulado));
        listaCommodities.clear();
        db = null;
    }

    private void recuperarListas() {

        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(caminho);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        listaCommodities.add(dsp.getValue(ListaCommodities.class));
                    }
                    size = listaCommodities.size() - 1;

                    List<ListaCommodities> variacao = new ArrayList<>(listaCommodities);
                    Collections.sort(listaCommodities, new MoneySort());
                    Collections.sort(variacao, new VariationSort());
                    for (int i = 0; i < listaCommodities.size(); i++) {
                        if (listaCommodities.get(i).getIdDono().equals(Base64Handler.codificarBase64(user.getEmail()))) {
                            posicao = i;
                            break;
                        }
                    }
                    while (listaCommodities.size() > 6) {
                        listaCommodities.remove(6);
                    }
                    while (variacao.size() > 6) {
                        variacao.remove(6);
                    }
                    recuperarListaUsuario(listaCommodities, variacao);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarListaUsuario(final List<ListaCommodities> patrimonio, final List<ListaCommodities> variacao) {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(caminho).child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    patrimonio.add(dataSnapshot.getValue(ListaCommodities.class));
                    variacao.add(dataSnapshot.getValue(ListaCommodities.class));
                }
                plotarGraficos(patrimonio, variacao);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
