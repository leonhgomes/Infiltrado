package com.example.leonardo.infiltrado;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

public class TelaInicial extends AppCompatActivity {

    String[] sugestoes;
    boolean infiltrado=true;
    int secreto,num_jogadores,minutos;
    SharedPreferences prefs;
    NumberPicker np_jogadores,np_minutos;


    public void infiltradoSpyfall(){
        if(infiltrado){
            Button btn = (Button) findViewById(R.id.listaLocais);
            btn.setVisibility(View.INVISIBLE);
            btn = (Button) findViewById(R.id.renova_sugestao);
            btn.setVisibility(View.VISIBLE);
            EditText edt = (EditText) findViewById(R.id.edit1);
            edt.setVisibility(View.VISIBLE);
            TextView vi = (TextView) findViewById(R.id.text1);
            vi.setVisibility(View.VISIBLE);
        }
        else{
            Button btn = (Button) findViewById(R.id.listaLocais);
            btn.setVisibility(View.VISIBLE);
            btn = (Button) findViewById(R.id.renova_sugestao);
            btn.setVisibility(View.INVISIBLE);
            EditText edt = (EditText) findViewById(R.id.edit1);
            edt.setVisibility(View.INVISIBLE);
            TextView vi = (TextView) findViewById(R.id.text1);
            vi.setVisibility(View.INVISIBLE);

        }

    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        num_jogadores= savedInstanceState.getInt("num_jogadores",4);
        minutos= savedInstanceState.getInt("minutos",5);
        infiltrado = savedInstanceState.getBoolean("infiltrado",true);

        np_minutos.setValue(minutos);
        np_jogadores.setValue(num_jogadores);
        // Read values from the "savedInstanceState"-object and put them in your textview
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the values you need from your textview into "outState"-object
        outState.putInt("num_jogadores",num_jogadores);
        outState.putInt("minutos",minutos);
        outState.putBoolean("infiltrado",infiltrado);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        num_jogadores =  prefs.getInt("num_jogadores",4);
        minutos = prefs.getInt("minutos",5);
        infiltrado = prefs.getBoolean("infiltrado",true);
        Switch swi = (Switch)findViewById(R.id.infSpy);
        swi.setChecked(!infiltrado);
        np_jogadores = (NumberPicker) findViewById(R.id.np1);
        np_jogadores.setMinValue(3);
        np_jogadores.setMaxValue(20);
        np_jogadores.setValue(num_jogadores);
        infiltradoSpyfall();

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

        final Button local = (Button) findViewById(R.id.listaLocais);
        local.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TelaInicial.this, Locais.class);
                startActivity(intent);


            }
        });

        //Switch swi = (Switch) findViewById(R.id.infSpy);
        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                infiltrado=!isChecked;
                infiltradoSpyfall();

            }
        });



        Button joga = (Button) findViewById(R.id.joga);
        joga.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Intent intent;
            if(infiltrado) {
                intent = new Intent(TelaInicial.this, Papeis.class);
                EditText eddd = (EditText) findViewById(R.id.edit1);
                intent.putExtra("palavra", eddd.getText().toString());
            }
            else{
                PapeisSpy.secreto=-1;
                intent = new Intent(TelaInicial.this,PapeisSpy.class);
            }

            intent.putExtra("jogadores", num_jogadores);
            intent.putExtra("minutos", minutos);

            startActivity(intent);

            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("num_jogadores",num_jogadores);
            edit.putInt("minutos",minutos);
            edit.putBoolean("infiltrado",infiltrado);
            edit.commit();




        }
    });
        Button eng = (Button) findViewById(R.id.english);
        eng.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String lang="en";
                if(prefs.getString("force_local","")=="en")
                    lang="pt";
                MyApplication.updateLanguage(getApplicationContext(), lang);
                Intent refresh = new Intent(TelaInicial.this, TelaInicial.class);
                startActivity(refresh);
                finish();

            }
        });

    }
}
