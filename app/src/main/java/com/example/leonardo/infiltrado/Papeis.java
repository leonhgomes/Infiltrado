package com.example.leonardo.infiltrado;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Papeis extends AppCompatActivity {

    private boolean revelado = false;
    private int minutos, jogador,secreto=0,num_jogadores;
    private Button botao;
    private String palavra;
    private String texto_exibido;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        revelado=savedInstanceState.getBoolean("revelado",false);
        jogador= savedInstanceState.getInt("jogador",1);
        secreto= savedInstanceState.getInt("secreto",1);
        texto_exibido = savedInstanceState.getString("texto");
        botao.setText(texto_exibido);
        // Read values from the "savedInstanceState"-object and put them in your textview
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the values you need from your textview into "outState"-object
        outState.putBoolean("revelado",revelado);
        outState.putInt("jogador",jogador);
        outState.putInt("secreto",secreto);
        outState.putString("texto",texto_exibido);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papeis);
        num_jogadores = getIntent().getIntExtra("jogadores",1);
        minutos = getIntent().getIntExtra("minutos",1);
        palavra = getIntent().getStringExtra("palavra");

        Random rand = new Random();
        secreto = 1 + rand.nextInt(num_jogadores);
        jogador = 1;
        botao = (Button) findViewById(R.id.mensagem);
        texto_exibido=getString(R.string.jogador1);
        botao.setText(R.string.jogador1);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fim=false;
                String mens = getString(R.string.jogador) +" "+ Integer.toString(jogador) + " = ";;
                if(!revelado){
                    if(jogador==secreto)
                        mens = mens + "\n\n" + getString(R.string.voce_eh_infiltrado)+"\n\n"+palavra+"\n\n";
                    else
                        mens = mens + "\n\n" + getString(R.string.voce_nao_eh_infiltrado) + "\n\n";
                    if(jogador==num_jogadores)
                        mens = mens +getString(R.string.aperte_tela_iniciar_tempo);
                    else
                        mens = mens +getString(R.string.aperte_tela_passe_aparelho);
                    jogador++;
                }
                else{
                    if(jogador-1==num_jogadores) {
                        Intent intent = new Intent(Papeis.this, Contador.class);
                        intent.putExtra("jogadores", num_jogadores);
                        intent.putExtra("minutos", minutos);
                        intent.putExtra("palavra", palavra);
                        startActivity(intent);
                        finish();
                        fim=true;
                    }
                    else
                        mens = mens + getString(R.string.aperte_tela);

                }
                if(!fim) {
                    texto_exibido=mens;
                    botao.setText(mens);
                }
                revelado= !revelado;

            }
        });

    }
}
