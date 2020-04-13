package com.example.projetoaziz.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projetoaziz.R;
import com.example.projetoaziz.helpers.Base64Handler;
import com.example.projetoaziz.helpers.MoneySort;
import com.example.projetoaziz.models.Commodity;
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

    private static int[] FABINHO_COLORS = {
            rgb("#b50037"), //0
            rgb("#d50000"), //1
            rgb("#ff5722"), //2
            rgb("#fbc02d"), //3
            rgb("#ffeb3b"), //4
            rgb("#00c853"), //5
            rgb("#00838f"), //6
            rgb("#1976d2"), //7
            rgb("#2556d1"), //8
            rgb("#303f9f"), //9
    };

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


        return v;
    }

    @SuppressLint("ResourceAsColor")
    private void plotarGraficos(List<Usuario> list) {
        BarChart mbar = v.findViewById(R.id.barChart);


        List<Usuario> dezMelhores = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == 11) {
                break;
            } else {
                dezMelhores.add(list.get(i));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (i == 11) {
                break;
            } else {
                dezMelhores.add(list.get(i));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (i == 11) {
                break;
            } else {
                dezMelhores.add(list.get(i));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (i == 11) {
                break;
            } else {
                dezMelhores.add(list.get(i));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (i == 11) {
                break;
            } else {
                dezMelhores.add(list.get(i));
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < dezMelhores.size(); i++) {
            Usuario usuario = dezMelhores.get(i);
            float patrimonio = usuario.getCreditos();
            for (Commodity commodity : usuario.getListaCommodities()) {
                patrimonio += commodity.getQuantidade() * commodity.getValor();
            }
            entries.add(new BarEntry(i, patrimonio));
            LegendEntry legendEntry = new LegendEntry();
            legendEntry.label = usuario.getNome();
            legendEntry.formColor = FABINHO_COLORS[i % 10];
            legendEntries.add(legendEntry);


        }
        BarDataSet set = new BarDataSet(entries, "Jogadores");
        set.setColors(FABINHO_COLORS);

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
        mbar.animateY(3000);
        mbar.setDrawGridBackground(false);
        mbar.setDrawBorders(false);
        mbar.setFitBars(true);
        mbar.setData(data);
        mbar.invalidate();
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
