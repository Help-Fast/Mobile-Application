package com.example.helpfastmobile;

import android.content.Context;
import android.content.SharedPreferences;

// Classe para gerenciar a sessão do usuário (ex: salvar e recuperar o token de autenticação).
public class SessionManager {

    private static final String PREF_NAME = "HelpFastSession";
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        // O modo MODE_PRIVATE garante que apenas este app possa ler as preferências.
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Salva o token de autenticação nas SharedPreferences.
    public void saveAuthToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply(); // apply() salva os dados de forma assíncrona.
    }

    // Recupera o token de autenticação salvo.
    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    // Limpa a sessão do usuário (remove o token).
    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.apply();
    }
}
