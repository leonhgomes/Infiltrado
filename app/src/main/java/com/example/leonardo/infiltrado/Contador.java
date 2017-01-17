package com.example.leonardo.infiltrado;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Contador extends AppCompatActivity {
    int minutos;
    Button pausar,reiniciar,fim;
    TextView mensagem;
    TextView mensagem2;
    CountDownTimer contador;
    long tempo_restante;
    boolean pausado=false;
    int etapa=1;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pausado=savedInstanceState.getBoolean("pausado",pausado);
        tempo_restante= savedInstanceState.getLong("tempo_restante",300000);
        etapa= savedInstanceState.getInt("etapa",1);
        contador.cancel();

        if(pausado) {
            contador = new CountDownTimerClass(tempo_restante, 500);
            pausar.setText(R.string.seguir);
        }
        else
            contador = new CountDownTimerClass(tempo_restante, 1000);
        contador.start();

        if(etapa==2){
            mensagem2.setText(R.string.quem_eh_infiltrado);
            fim.setText(R.string.fim);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("pausado",pausado);
        outState.putLong("tempo_restante",tempo_restante);
        outState.putInt("etapa",etapa);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            contador.cancel();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        minutos = getIntent().getIntExtra("minutos",1);
        pausar = (Button)findViewById(R.id.pausar);
        reiniciar = (Button)findViewById(R.id.reiniciar);
        fim = (Button)findViewById(R.id.fim);
        mensagem = (TextView)findViewById(R.id.mensagem);
        mensagem2 = (TextView)findViewById(R.id.mensagem2);
        contador = new CountDownTimerClass(minutos*60000+999, 1000);

        contador.start();

        pausar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(pausado) {
                    contador.cancel();
                    contador = new CountDownTimerClass(tempo_restante, 1000);
                    contador.start();
                    pausar.setText(R.string.pausar);
                }
                else {
                    pausar.setText(R.string.seguir);
                    contador.cancel();
                    contador = new CountDownTimerClass(6000000, 500);
                    contador.start();
                }

                pausado=!pausado;
            }
        });

        reiniciar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(pausado)
                    tempo_restante=minutos*60000+999;
                else {
                    contador.cancel();
                    contador = new CountDownTimerClass(minutos * 60000 + 999, 1000);
                    contador.start();
                }

            }
        });

        fim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            if(etapa==1){
                etapa++;
                pausado=false;
                pausar.setText(R.string.pausar);
                contador.cancel();
                mensagem2.setText(R.string.quem_eh_infiltrado);
                fim.setText(R.string.fim);
                contador = new CountDownTimerClass(minutos*60000+999, 1000);
                contador.start();

            }
            else
                finish();
            }
        });



    }

    public class CountDownTimerClass extends CountDownTimer {

        public CountDownTimerClass(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            boolean piscar=false;
            if(pausado) {
                if (millisUntilFinished % 1000 > 500)
                    piscar = true;
                millisUntilFinished = tempo_restante;
            }
            else
                tempo_restante=millisUntilFinished;
            int min = (int) (millisUntilFinished / 1000);
            min = min /60;
            int seg =  (int) (millisUntilFinished / 1000);
            seg = seg % 60;

            if(!pausado && ( (min==1 && seg==0) || (min==0 && seg==30)) ){
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

            }

            if(piscar)
                mensagem.setText("");
            else
                mensagem.setText(String.format("%02d", min)+":"+String.format("%02d", seg));

        }

        @Override
        public void onFinish() {

            mensagem.setText(R.string.tempo_acabou);

        }
    }
}
