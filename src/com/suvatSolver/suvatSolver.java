/*
 Copyright (C) 2010  Levs Ustinovs

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

To contact the author please email to levi.ustinov@gmail.com.
 */

package com.suvatSolver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class suvatSolver extends Activity implements OnClickListener {
	/* UI stuff: */
	// layout bindings
	private Button btnCalc, btnClear;
	private TextView txtS, txtU, txtV, txtA, txtT;
	// dialog identifier
	private static final int DIALOG_LACKNUMSENTERED_ID = 0;
	private static final int DIALOG_TOOMANYNUMS_ID = 1;
	private static final int DIALOG_BADS_ID = 2;
	private static final int DIALOG_BADU_ID = 3;
	private static final int DIALOG_BADV_ID = 4;
	private static final int DIALOG_BADA_ID = 5;
	private static final int DIALOG_BADT_ID = 6;
	// menu identifiers
	private static final int MENU_ABOUT = 0;

	/* Number formating */
	private java.text.DecimalFormat format = new java.text.DecimalFormat(
			"###.#######");

	/* Debug stuff */
	// for some reason, if the string is retrieved from
	// R (resource) with getString(), then the application throws
	// a RuntimException :-/ and assigned to DBG_NAME
	private String DBG_NAME = "SUVAT Solver";
	private boolean LogOn = false;

	/* SUVAT stuff */
	// Storage suvat variable values:
	private double values[] = new double[5];
	// Storage flags for set and not set suvat variables values:
	private boolean flags[] = new boolean[5];
	// the suvat object (empty initially):
	private Suvat suvat = new Suvat();
	int varCount = 0; // counter for set values

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); // load main UI

		/* retrieve references for the UI controls: */
		btnCalc = (Button) findViewById(R.id.btnCalc);
		btnClear = (Button) findViewById(R.id.btnClear);
		txtS = (TextView) findViewById(R.id.txtS);
		txtU = (TextView) findViewById(R.id.txtU);
		txtV = (TextView) findViewById(R.id.txtV);
		txtA = (TextView) findViewById(R.id.txtA);
		txtT = (TextView) findViewById(R.id.txtT);

		/* initialise the text fields according to suvat object's values: */
		try {
			txtS.setText(suvat.isSet('s') ? format.format(suvat.retrieve('s'))
					: "");
			txtU.setText(suvat.isSet('u') ? format.format(suvat.retrieve('u'))
					: "");
			txtV.setText(suvat.isSet('v') ? format.format(suvat.retrieve('v'))
					: "");
			txtA.setText(suvat.isSet('a') ? format.format(suvat.retrieve('a'))
					: "");
			txtT.setText(suvat.isSet('t') ? format.format(suvat.retrieve('t'))
					: "");
		} catch (Exception e) {
			if (LogOn)
				Log.d(DBG_NAME, "Incorrect suvat char parameter passed!");
		}

		/* Define button listeners */
		btnCalc.setOnClickListener(this);
		btnClear.setOnClickListener(this);
	}

	@Override
	public void onClick(View src) {
		switch (src.getId()) {
		case R.id.btnCalc:
			retrieveSuvat();
			if (LogOn)
				Log.d("SUVAT resolver", "The user has entered " + varCount
						+ " variables.");

			if (varCount >= 4) { // in case it is 4 or 5 variables entered
				showDialog(DIALOG_TOOMANYNUMS_ID);
				if (LogOn)
					Log.d("SUVAT resolver", "More than 3 variables entered!");
				return;
			} else if (varCount < 3) { // less than 3 variables entered
				showDialog(DIALOG_LACKNUMSENTERED_ID);
				if (LogOn)
					Log.d("SUVAT resolver", "Less than 3 variables entered!");
				return;
			} else { // all is well, 3 variables entered
				// reconstruct the suvat particle
				suvat = new Suvat(values, flags);
				// resolve the values
				suvat.resolve();

				// update values
				for (int i = 0; i < 5; i++) {
					try {
						values[i] = suvat.retrieve(Suvat.suvatChar[i]); // retrieve
					} catch (Exception e) {
						if (LogOn)
							Log.d(DBG_NAME, "Check your code!", e);
					}
				}
				// update on screen values
				txtS.setText(format.format(values[0]));
				txtU.setText(format.format(values[1]));
				txtV.setText(format.format(values[2]));
				txtA.setText(format.format(values[3]));
				txtT.setText(format.format(values[4]));
			}
			break;
		case R.id.btnClear:
			// clear the variables in the object
			suvat.clearAll();
			// clear the text in the text boxes
			clearTextBoxes();
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// initialise dialog object
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_LACKNUMSENTERED_ID:
			// create dialog
			builder.setMessage(R.string.lacknumsentered).setCancelable(false)
					.setNeutralButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			return builder.create();
		case DIALOG_TOOMANYNUMS_ID:
			// create dialog
			builder.setMessage(R.string.toomanynums).setCancelable(false)
					.setNeutralButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			return builder.create();
		case DIALOG_BADS_ID:
			// create dialog
			builder.setMessage(
					getString(R.string.invalidVal) + getString(R.string.S)
							+ "!").setCancelable(false).setNeutralButton(
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			return builder.create();
		case DIALOG_BADU_ID:
			// create dialog
			builder.setMessage(
					getString(R.string.invalidVal) + getString(R.string.U)
							+ "!").setCancelable(false).setNeutralButton(
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			return builder.create();
		case DIALOG_BADV_ID:
			// create dialog
			builder.setMessage(
					getString(R.string.invalidVal) + getString(R.string.V)
							+ "!").setCancelable(false).setNeutralButton(
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			return builder.create();
		case DIALOG_BADA_ID:
			// create dialog
			builder.setMessage(
					getString(R.string.invalidVal) + getString(R.string.A)
							+ "!").setCancelable(false).setNeutralButton(
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			return builder.create();
		case DIALOG_BADT_ID:
			// create dialog
			builder.setMessage(
					getString(R.string.invalidVal) + getString(R.string.T)
							+ "!").setCancelable(false).setNeutralButton(
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			return builder.create();
		default:
			return super.onCreateDialog(id); // same as null?
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem = menu.add(0, MENU_ABOUT, 0,
				getString(R.string.about));
		menuItem.setIntent(new Intent(this, activityAbout.class));
		return true;
	}

	private void clearTextBoxes() {
		txtS.setText("");
		txtU.setText("");
		txtV.setText("");
		txtA.setText("");
		txtT.setText("");
	}

	private void retrieveSuvat() {
		//null the variable count
		varCount = 0;
		// build up the two arrays...
		if (!txtS.getText().toString().equals("")) {
			try {
				values[0] = Double.parseDouble(txtS.getText().toString());
				flags[0] = true;
				varCount++;
			} catch (NumberFormatException exc) {
				showDialog(DIALOG_BADS_ID);
				if (LogOn)
					Log.d(DBG_NAME, "Can't parse S: "
							+ txtS.getText().toString());
				return;
			}
		}
		if (!txtU.getText().toString().equals("")) {
			try {
				values[1] = Double.parseDouble(txtU.getText().toString());
				flags[1] = true;
				varCount++;
			} catch (NumberFormatException exc) {
				if (LogOn)
					Log.d(DBG_NAME, "Can't parse U:"
							+ txtU.getText().toString());
				showDialog(DIALOG_BADU_ID);
				return;
			}
		}
		if (!txtV.getText().toString().equals("")) {
			try {
				values[2] = Double.parseDouble(txtV.getText().toString());
				flags[2] = true;
				varCount++;
			} catch (NumberFormatException exc) {
				if (LogOn)
					Log.d(DBG_NAME, "Can't parse V:"
							+ txtV.getText().toString());
				showDialog(DIALOG_BADV_ID);
				return;
			}
		}
		if (!txtA.getText().toString().equals("")) {
			try {
				values[3] = Double.parseDouble(txtA.getText().toString());
				flags[3] = true;
				varCount++;
			} catch (NumberFormatException exc) {
				if (LogOn)
					Log.d("DBG_NAME", "Can't parse A:"
							+ txtA.getText().toString());
				showDialog(DIALOG_BADA_ID);
				return;
			}
		}
		if (!txtT.getText().toString().equals("")) {
			try {
				values[4] = Double.parseDouble(txtT.getText().toString());
				flags[4] = true;
				varCount++;
			} catch (NumberFormatException exc) {
				if (LogOn)
					Log.d(DBG_NAME, "Can't parse T:"
							+ txtT.getText().toString());
				showDialog(DIALOG_BADT_ID);
				return;
			}
		}
	}
}