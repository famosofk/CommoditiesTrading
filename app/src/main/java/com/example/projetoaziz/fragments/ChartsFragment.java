package com.example.projetoaziz.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.example.projetoaziz.models.ListaCommodities;
import com.example.projetoaziz.models.Usuario;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
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
    private Usuario aluno;
    private FirebaseUser user;
    private List<ListaCommodities> listaCommodities = new ArrayList<>();

    private static int[] FABINHO_COLORS = {rgb("#f44336"),
            rgb("#9c27b0"),
            rgb("#3f51b5"),
            rgb("#03a9f4"),
            rgb("#009688"),
            rgb("#8bc34a"),
            rgb("#ffeb3b"),};

    private View v;
    private DatabaseReference db;
    private String caminho;
    private int posicao, size;

    public ChartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_charts, container, false);
        user = ConfiguracaoDatabase.getFirebaseAutenticacao().getCurrentUser();
        Bundle bundle = getArguments();
        caminho = bundle.getString("idTurma");

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
            }
        });

        patrimonio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbar.setVisibility(View.VISIBLE);
                mbar2.setVisibility(View.GONE);
            }
        });


        return v;
    }

    @SuppressLint("ResourceAsColor")
    private void plotarGraficos(List<ListaCommodities> list) {
        TextView ranking = v.findViewById(R.id.textPosicao);
        String POSICAO = "Colocação: " + posicao + "/" + size;
        ranking.setText(POSICAO);
        BarChart mbar = v.findViewById(R.id.barChart);
        BarChart mbar2 = v.findViewById(R.id.barChart2);
        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ListaCommodities usuario = list.get(i);
            entries.add(new BarEntry(i, usuario.getPatrimonio()));
            entries2.add(new BarEntry(i, usuario.getPatrimonio() / usuario.getPatrimonioAnterior()));
            LegendEntry legendEntry = new LegendEntry();
            legendEntry.label = usuario.getNome();
            legendEntry.formColor = FABINHO_COLORS[i % 7];
            legendEntries.add(legendEntry);
        }
        BarDataSet set = new BarDataSet(entries, "Patrimônio");
        BarDataSet set2 = new BarDataSet(entries2, "Variação");
        set.setColors(FABINHO_COLORS);
        set2.setColors(FABINHO_COLORS);
        BarData data = new BarData(set);
        YAxis yAxisRight = mbar.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisLeft = mbar.getAxisLeft();
        yAxisLeft.setEnabled(false);
        XAxis xAxis = mbar.getXAxis();
        xAxis.setEnabled(false);
        data.setBarWidth(0.9f);
        mbar.setDrawValueAboveBar(true);
        mbar.fitScreen();
        Description description = mbar.getDescription();
        description.setEnabled(false);
        Legend legend = mbar.getLegend();
        legend.setCustom(legendEntries);
        legend.setTextSize(10);
        mbar.setDrawGridBackground(false);
        mbar.setDrawBorders(false);
        mbar.setFitBars(true);
        mbar.setData(data);
        mbar.invalidate();
        BarData data2 = new BarData(set2);
        YAxis yAxisRight2 = mbar2.getAxisRight();
        yAxisRight2.setEnabled(false);
        YAxis yAxisLeft2 = mbar2.getAxisLeft();
        yAxisLeft2.setEnabled(false);
        XAxis xAxis2 = mbar2.getXAxis();
        xAxis2.setEnabled(false);
        data2.setBarWidth(0.9f);
        mbar2.setDrawValueAboveBar(true);
        mbar2.fitScreen();
        Description description2 = mbar2.getDescription();
        description2.setEnabled(false);
        Legend legend2 = mbar2.getLegend();
        legend2.setCustom(legendEntries);
        legend2.setTextSize(10);
        mbar2.setDrawGridBackground(false);
        mbar2.setDrawBorders(false);
        mbar2.setFitBars(true);
        mbar2.setData(data2);
        mbar2.invalidate();
        db = null;
    }

    private void recuperarListas() {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(caminho);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        listaCommodities.add(dsp.getValue(ListaCommodities.class));
                    }
                    size = listaCommodities.size();
                    for (int i = 0; i < listaCommodities.size(); i++) {
                        if (listaCommodities.get(i).getIdDono().equals(Base64Handler.codificarBase64(user.getEmail()))) {
                            posicao = i + 1;
                            break;
                        }
                    }
                    Collections.sort(listaCommodities, new MoneySort());
                    while (listaCommodities.size() > 6) {
                        listaCommodities.remove(6);
                    }
                    recuperarListaUsuario(listaCommodities);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarListaUsuario(final List<ListaCommodities> list) {
        DatabaseReference db = ConfiguracaoDatabase.getFirebaseDatabase().child("listaCommodities").child(caminho).child(Base64Handler.codificarBase64(user.getEmail()));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    list.add(dataSnapshot.getValue(ListaCommodities.class));
                }
                plotarGraficos(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
