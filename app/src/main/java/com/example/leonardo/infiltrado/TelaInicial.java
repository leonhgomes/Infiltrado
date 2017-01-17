package com.example.leonardo.infiltrado;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Random;

public class TelaInicial extends AppCompatActivity {

    String[] sugestoes;
    int secreto,num_jogadores,minutos;
    SharedPreferences prefs;
    NumberPicker np_jogadores,np_minutos;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        num_jogadores= savedInstanceState.getInt("num_jogadores",4);
        minutos= savedInstanceState.getInt("minutos",5);
        np_minutos.setValue(minutos);
        np_jogadores.setValue(num_jogadores);
        // Read values from the "savedInstanceState"-object and put them in your textview
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the values you need from your textview into "outState"-object
        outState.putInt("num_jogadores",num_jogadores);
        outState.putInt("minutos",minutos);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        //prefs = this.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
          prefs = PreferenceManager.getDefaultSharedPreferences(this);
        num_jogadores =  prefs.getInt("num_jogadores",4);
        minutos = prefs.getInt("minutos",5);


        np_jogadores = (NumberPicker) findViewById(R.id.np1);
        np_jogadores.setMinValue(3);
        np_jogadores.setMaxValue(20);
        np_jogadores.setValue(num_jogadores);


    np_jogadores.setWrapSelectorWheel(true);
        np_jogadores.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                num_jogadores=newVal;
            }
        });

        np_minutos = (NumberPicker) findViewById(R.id.np2);
        np_minutos.setMinValue(1);
        np_minutos.setMaxValue(20);
        np_minutos.setValue(minutos);


        np_minutos.setWrapSelectorWheel(true);
        np_minutos.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                minutos=newVal;
            }
        });


        sugestoes = getResources().getStringArray(R.array.palavras);
        Random rand = new Random();
        secreto =  rand.nextInt(sugestoes.length);

        EditText eddd = (EditText) findViewById(R.id.edit1);

        eddd.setText(sugestoes[secreto]);

        Button novo = (Button) findViewById(R.id.renova_sugestao);
        novo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Random rand = new Random();
                int novo_secreto;
                do{
                    novo_secreto =rand.nextInt(sugestoes.length);
                }while(novo_secreto==secreto);
                secreto=novo_secreto;
                EditText eddd = (EditText) findViewById(R.id.edit1);
                eddd.setText(sugestoes[secreto]);
            }
        });

        Button joga = (Button) findViewById(R.id.joga);
        joga.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(TelaInicial.this, Papeis.class);
            EditText eddd = (EditText) findViewById(R.id.edit1);
            String palavra = eddd.getText().toString();
            intent.putExtra("jogadores", num_jogadores);
            intent.putExtra("minutos", minutos);
            intent.putExtra("palavra", palavra);
            startActivity(intent);

            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("num_jogadores",num_jogadores);
            edit.putInt("minutos",minutos);
            edit.commit();




        }
    });
        Button eng = (Button) findViewById(R.id.english);
        eng.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MyApplication.updateLanguage(getApplicationContext(), "en");
                Intent refresh = new Intent(TelaInicial.this, TelaInicial.class);
                startActivity(refresh);
                finish();
            }
        });

        Button port = (Button) findViewById(R.id.portugues);
        port.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MyApplication.updateLanguage(getApplicationContext(), "pt");
                Intent refresh = new Intent(TelaInicial.this, TelaInicial.class);
                startActivity(refresh);
                finish();
            }
        });


    }
}
