package erickmaeda.com.br.criptografiashash;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends ActionBarActivity {

    @Bind(R.id.rgChooseMethod)
    RadioGroup rgChooseMethod;
    @Bind(R.id.rbMD5)
    RadioButton rbMD5;
    @Bind(R.id.rbSha1)
    RadioButton rbSha1;
    @Bind(R.id.rbSha256)
    RadioButton rbSha256;
    @Bind(R.id.edtMessageToCriptography)
    EditText edtMessageToCriptography;
    @Bind(R.id.tvMessageEncrypted)
    TextView tvMessageEncrypted;
    private int radioButtonID;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            tvMessageEncrypted.setText("");
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        message = edtMessageToCriptography.getText().toString().trim();

        if (message.equals("")) {
            showAlertDialog(getString(R.string.default_error), getString(R.string.cant_be_null), getString(R.string.default_OK));
            return;
        }

        radioButtonID = rgChooseMethod.getCheckedRadioButtonId();

        if (rbMD5.getId() == radioButtonID) {
            tvMessageEncrypted.append(Html.fromHtml(generateOutput(message, "MD5") + gerarHash(message, "MD5")));
        } else if (rbSha1.getId() == radioButtonID) {
            tvMessageEncrypted.append(Html.fromHtml(generateOutput(message, "SHA-1") + gerarHash(message, "SHA-1")));
        } else if (rbSha256.getId() == radioButtonID) {
            tvMessageEncrypted.append(Html.fromHtml(generateOutput(message, "SHA-256") + gerarHash(message, "SHA-256")));
        } else {
            showAlertDialog(getString(R.string.default_error), getString(R.string.choose_one), getString(R.string.default_OK));
        }
        tvMessageEncrypted.append("\n_________________________________________\n");
    }

    private String generateOutput(String message, String algoritmUsed) {
        return "<b>Algoritmo usado:</b> " + algoritmUsed + "\n<b>Mensagem:</b> " + message + "\n<b>Hash gerada:</b> \n";
    }

    private void showAlertDialog(String title, String body, String neutralButton) {
        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setTitle(title);
        alert.setMessage(body);
        alert.setNeutralButton(neutralButton, null);
        alert.show();
    }

    private String gerarHash(String frase, String algoritmo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(frase.getBytes());
            return getByteToString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private String getByteToString(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (byte aByte : bytes) {
            int parteAlta = ((aByte >> 4) & 0xf) << 4;
            int parteBaixa = aByte & 0xf;
            if (parteAlta == 0) s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }
}
