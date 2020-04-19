package com.example.projetoaziz.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projetoaziz.R;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.MoneySort;
import com.example.projetoaziz.models.Professor;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartsFragment extends Fragment {

    //private static int[] FABINHO_COLORS = {rgb("#bb002f"), rgb("#d50000"), rgb("#ff5722"), rgb("#fbc02d"), rgb("#ffeb3b"), rgb("#00c853"), rgb("#00838f"), rgb("#1976d2"), rgb("#536dfe"), rgb("#303f9f"),};
    private static int[] FABINHO_COLORS = {rgb("#f44336"),
            rgb("#9c27b0"),
            rgb("#3f51b5"),
            rgb("#03a9f4"),
            rgb("#009688"),
            rgb("#8bc34a"),
            rgb("#ffeb3b"),};

    private View v;
    private DatabaseReference db;

    public ChartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_charts, container, false);
        recuperarProfessor(getCurrentUser());

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
    private void plotarGraficos(List<Usuario> list) {
        BarChart mbar = v.findViewById(R.id.barChart);
        BarChart mbar2 = v.findViewById(R.id.barChart2);
        List<Usuario> dezMelhores = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == 6) {
                break;
            } else {
                dezMelhores.add(list.get(i));
            }
        }


        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < dezMelhores.size(); i++) {
            Usuario usuario = dezMelhores.get(i);
            entries.add(new BarEntry(i, usuario.getPatrimonio()));
            entries2.add(new BarEntry(i, usuario.getPatrimonio() / usuario.getPatrimonioAnterior()));
            LegendEntry legendEntry = new LegendEntry();
            legendEntry.label = usuario.getNome();
            legendEntry.formColor = FABINHO_COLORS[i % 6];
            legendEntries.add(legendEntry);
        }
        BarDataSet set = new BarDataSet(entries, "Jogadores");
        BarDataSet set2 = new BarDataSet(entries2, "Jogadores");
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

    private void recuperarListaUsuarios(Professor professor) {

        final List<Usuario> list = new ArrayList<>();
        list.add(professor);
        db = FirebaseDatabase.getInstance().getReference().child("aluno");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario;
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        usuario = dsp.getValue(Usuario.class);
                        list.add(usuario);
                    }
                    Collections.sort(list, new MoneySort());
                    plotarGraficos(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void recuperarProfessor(final FirebaseUser user) {

        DatabaseReference db;
        if ("professor".equals(Objects.requireNonNull(user.getPhotoUrl()).toString())) {
            db = FirebaseDatabase.getInstance().getReference().child("professor").child(Base64Handler.codificarBase64(Objects.requireNonNull(user.getEmail())));
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Professor professor = dataSnapshot.getValue(Professor.class);
                        recuperarListaUsuarios(professor);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {

            db = FirebaseDatabase.getInstance().getReference().child("professor").child(Objects.requireNonNull(user.getDisplayName()));
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Professor professor = dataSnapshot.getValue(Professor.class);
                        recuperarListaUsuarios(professor);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private FirebaseUser getCurrentUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
    }


}
