package com.example.leonardo.infiltrado;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class PapeisSpy extends AppCompatActivity {

    private static int num_jogadores;
    private static int minutos;
    private static int jogador;
    public static int secreto=-1;
    private static String local;
    private static String [] papeisJogador;
    private static Boolean revelado=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papeis_spy);

        if(secreto==-1) {
            num_jogadores = getIntent().getIntExtra("jogadores", 1);
            minutos = getIntent().getIntExtra("minutos", 1);

            Random rand = new Random();
            secreto = 1 + rand.nextInt(num_jogadores);
            jogador = 1;

            String pre_lista[] = getResources().getStringArray(R.array.locais);
            papeisJogador = pre_lista[rand.nextInt(pre_lista.length)].split(";");
            local = papeisJogador[0];
            papeisJogador = Arrays.copyOfRange(papeisJogador, 1, papeisJogador.length);
            shuffleArray(papeisJogador);
        }
        escrevaMensagens();

        //GridLayout bot = (GridLayout)findViewById(R.id.activity_papeis_spy);
        Button bot = (Button)findViewById(R.id.mensagemBot);
        bot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                revelado=true;
                escrevaMensagens();
                return false;
            }
        });
        bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revelado=false;
                escrevaMensagens();
            }
        });
        Button top = (Button)findViewById(R.id.mensagemTop);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(jogador==num_jogadores){
                    Intent intent = new Intent(PapeisSpy.this, ContadorSpy.class);
                    intent.putExtra("minutos", minutos);
                    ContadorSpy.tempo_restante=-1;
                    startActivity(intent);
                    finish();
                }
                jogador++;
                escrevaMensagens();
            }
        });
    }

    private void escrevaMensagens() {
        Button top = (Button)findViewById(R.id.mensagemTop);
        Button bot = (Button)findViewById(R.id.mensagemBot);
        int[] cores = getResources().getIntArray(R.array.cores);
        top.setBackgroundColor(cores[(jogador-1)%num_jogadores]);

        String s= getString(R.string.jogador)+" "+Integer.toString(jogador)+"\n"+getString(R.string.verifique);
        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(0.33f), s.indexOf('\n'), s.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        top.setText(ss1);
        if(revelado && secreto==jogador){
            String text=getString(R.string.voceEspiao);
            SpannableString ss=  new SpannableString(text);
            ss.setSpan(new RelativeSizeSpan(0.4f),0,text.lastIndexOf(' '),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.RED),text.lastIndexOf(' '),text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            bot.setText(ss);
        }
        else if(revelado && secreto!=jogador) {
            String func =papeisJogador[jogador%num_jogadores];

            String text= getString(R.string.lugar) + local + "\n" + getString(R.string.funcao)+func;
            SpannableString ss=  new SpannableString(text);
            ss.setSpan(new RelativeSizeSpan(0.4f), 0, text.indexOf(local),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new RelativeSizeSpan(0.4f), text.indexOf('\n'), text.indexOf(func),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            bot.setText(ss);
        }
        else{
            bot.setText(R.string.pressione);
        }

    }

    static void shuffleArray(String[] ar)
    {
        String a;
        Random rand = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rand.nextInt(i + 1);
            // Simple swap
            a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
