package de.zieren.rot13;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// TODO(jz): Add icons back to menus.
// TODO(jz): Add xhdpi app icon.

// TODO(jz): Eventually fix deprecation issues (when the number of users with
// old versions is sufficiently low; currently it's 28% below API 11).

/**
 * Translate text using the ROT13 cipher.
 *
 * @author jz
 */
public class TranslatorActivity extends Activity {
  /**
   * Maximum text length. This should be enough for most use cases, and still
   * avoid the app from slowing down too much.
   */
  private static final int MAX_TEXT_LENGTH = 4096;

  /** Lookup table for ROT13 translation. */
  private static final char[] LUT;
  /** UI component. */
  private EditText textInput;
  /** UI component. */
  private TextView textOutput;
  /** Used for copy/paste from output or to input, respectively. */
  private ClipboardManager clipboard;

  /** Dialog ID. */
  private static final int DIALOG_ABOUT_ID = 1;
  /** Dialog ID. */
  private static final int DIALOG_HELP_ID = 2;

  // Initialize lookup table.
  static {
    LUT = new char[256];
    for (char c = 0; c < 256; ++c) {
      int i = c;
      if (c >= 'a' && c <= 'z') {
        i += 13;
        if (i > 'z') i -= 26;
      } else if (c >= 'A' && c <= 'Z') {
        i += 13;
        if (i > 'Z') i -= 26;
      }
      LUT[c] = (char) i;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    textInput = (EditText) findViewById(R.id.text_input);
    textOutput = (TextView) findViewById(R.id.text_output);
    clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

    final Button button_copy = (Button) findViewById(R.id.button_copy);
    button_copy.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        clipboard.setText(textOutput.getText());
        Toast.makeText(getApplicationContext(),
                       R.string.translation_copied,
                       Toast.LENGTH_LONG).show();
      }
    });

    final Button buttonPaste = (Button) findViewById(R.id.button_paste);
    buttonPaste.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        textInput.setText(clipboard.getText());
        if (clipboard.getText().length() == 0) {
          Toast.makeText(getApplicationContext(),
                         R.string.clipboard_empty,
                         Toast.LENGTH_LONG).show();
        }
      }
    });

    final Button buttonClear = (Button) findViewById(R.id.button_clear);
    buttonClear.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        textInput.setText("");
        textOutput.setText("");
      }
    });

    final Button button_exit = (Button) findViewById(R.id.button_exit);
    button_exit.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        textInput.setText("");
        textOutput.setText("");
        finish();
      }
    });

    textInput.addTextChangedListener(new TextWatcher() {
      public void afterTextChanged(Editable editable) {
      }
      public void beforeTextChanged(
          CharSequence arg0, int arg1, int arg2, int arg3) {
      }
      public void onTextChanged(
          CharSequence s, int start, int before, int count) {
        final StringBuffer delta = translateROT13(s, start, count);
        StringBuffer translation = new StringBuffer(textOutput.getText());
        translation.replace(start, start + before, delta.toString());
        textOutput.setText(translation);
        // TODO(jz): Minor bug: It seems this doesn't happen on paste.
        textOutput.bringPointIntoView(start +count);
      }
    });
    textInput.setFilters(new InputFilter[] {
        new InputFilter.LengthFilter(MAX_TEXT_LENGTH)
    });
  }

  /** Returns a translation of the interval [start, start+count). */
  private StringBuffer translateROT13(
      CharSequence input, int start, int count) {
    StringBuffer translation = new StringBuffer();
    for (int i = 0; i < count; ++i) {
      // TODO(jz): It is apparently possible to enter chars >= 256. Repro & fix.
      translation.append(LUT[input.charAt(start + i)]);
    }
    return translation;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    Dialog dialog = null;
    switch (id) {
      case DIALOG_ABOUT_ID: {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.about_text)
               .setTitle(R.string.about_title)
               .setIcon(R.drawable.icon)
               .setNeutralButton(R.string.ok,
                 new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                     dismissDialog(DIALOG_ABOUT_ID);
                   }
                 });
        dialog = builder.create();
        break;
      }
      case DIALOG_HELP_ID: {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.help_text)
               .setTitle(R.string.help_title)
               .setNeutralButton(R.string.ok,
                 new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                     dismissDialog(DIALOG_HELP_ID);
                   }
                 });
        dialog = builder.create();
        break;
      }
    }
    assert dialog != null : "invalid dialog ID: " + id;
    return dialog;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.option_about:
      showDialog(DIALOG_ABOUT_ID);
      break;
    case R.id.option_help:
      showDialog(DIALOG_HELP_ID);
      break;
    default:
      assert false : "invalid menu item ID: " + item.getItemId();
    }
    return true;
  }
}
