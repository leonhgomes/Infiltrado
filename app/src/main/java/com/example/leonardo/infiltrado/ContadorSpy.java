package com.example.leonardo.infiltrado;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContadorSpy extends AppCompatActivity {
    private static int minutos;
    private static Button pausar,reiniciar,fim;
    private static TextView mensagem;
    private static TextView mensagem2;
    private static CountDownTimer contador=null;
    public static long tempo_restante=-1;
    private static boolean pausado;

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        contador.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_spy);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        reiniciar = (Button) findViewById(R.id.reiniciar);
        fim = (Button) findViewById(R.id.fim);
        mensagem = (TextView) findViewById(R.id.mensagem);
        mensagem2 = (TextView) findViewById(R.id.mensagem2);
        pausar = (Button) findViewById(R.id.pausar);
        if(tempo_restante==-1) {
            pausado=false;
            minutos = getIntent().getIntExtra("minutos", 1);
            tempo_restante=minutos*60000+999;

        }
        if(contador!=null)
            contador.cancel();
        if(pausado)
            contador = new CountDownTimerClass(tempo_restante, 500);
        else
            contador = new CountDownTimerClass(tempo_restante, 1000);

        contador.start();

        pausar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                contador.cancel();
                if(pausado) {
                    contador = new CountDownTimerClass(tempo_restante, 1000);
                    pausar.setText(R.string.pausar);
                }
                else {
                    pausar.setText(R.string.seguir);
                    contador = new CountDownTimerClass(6000000, 500);
                }
                contador.start();
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
             contador.cancel();
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
                mensagem.setText("     ");
            else
                mensagem.setText(String.format("%02d", min)+":"+String.format("%02d", seg));

        }

        @Override
        public void onFinish() {

            mensagem.setText(R.string.tempo_acabou);

        }
    }
}
