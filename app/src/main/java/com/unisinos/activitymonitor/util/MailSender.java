package com.unisinos.activitymonitor.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.unisinos.activitymonitor.MainActivity;

/**
 * Created by Felipe on 31/08/2015.
 */
public class MailSender {

    private String mailTo;
    private Context context;

    public MailSender(Context context) {
        this.context = context;
    }

    public static MailSender create(MainActivity mainActivity) {
        return new MailSender(mainActivity);
    }

    public MailSender to(String mailTo) {
        this.mailTo = mailTo;
        return this;
    }

    public void send(Uri uri) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent .putExtra(Intent.EXTRA_EMAIL, new String[]{"felipeakeller@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Banco de Dados");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Nenhum aplicativo de email encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}
