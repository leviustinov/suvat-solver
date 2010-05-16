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

public class Suvat {
	// suvat char array for looping:
	public static char suvatChar[] = { 's', 'u', 'v', 'a', 't' };
	// suvat variables flags: (first element is 's', second 'u' etc..)
	protected boolean suvatFlags[] = { false, false, false, false, false };
	// suvat variables values (same index principle as suvatFlags)
	protected double suvatVal[] = { 0, 0, 0, 0, 0 };// suvat variables array

	public Suvat() {
		// nothing to do here... all default variable values have been
		// defined...
	}

	public Suvat(double values[], boolean flags[]) {
		// 'value' - the suvat values
		// 'flags' - set/unset flags for the suvat values

		// copy the variable values
		System.arraycopy(values, 0, suvatVal, 0, suvatVal.length);
		// copy the flag values
		System.arraycopy(flags, 0, suvatFlags, 0, suvatFlags.length);
	}

	public boolean set(double var_val, char var_name) {
		/**
		 * Function: set's variables (hence the name! :D) var_name - will take a
		 * variable name var_val - takes a value to be set
		 */
		// checking if var_name is correct, if not return false (error)
		if (findInArray(suvatChar, var_name) == (suvatChar.length + 1))
			return false;
		// otherwise set the appropriate variable
		else {
			suvatVal[findInArray(suvatChar, var_name)] = var_val;
			suvatFlags[findInArray(suvatChar, var_name)] = true;
			return true; // successful
		}
	}

	public void clearAll() {
		// turn off all variables:
		for (int i = 0; i < suvatFlags.length; i++) {
			suvatFlags[i] = false;
		}
	}

	public boolean clear(char var_name) {
		/**
		 * Function: 'clear' clears variables var_name - will take a variable
		 * name such as 's' or 't'. It clears the variables by clearing the flag
		 * representing that variable
		 */
		switch (var_name) {
		case 's':
			suvatFlags[findInArray(suvatChar, 's')] = false;
			break;
		case 'u':
			suvatFlags[findInArray(suvatChar, 'u')] = false;
			break;
		case 'v':
			suvatFlags[findInArray(suvatChar, 'v')] = false;
			break;
		case 'a':
			suvatFlags[findInArray(suvatChar, 'a')] = false;
			break;
		case 't':
			suvatFlags[findInArray(suvatChar, 't')] = false;
			break;
		default:
			return false; // if NOT successful
		}
		return true; // if successful
	}

	public double retrieve(char varName) throws Exception {
		int i = findInArray(suvatChar, varName); // retrieve array index
		// check if var_name is correct or the variables is set
		if (i > suvatChar.length || suvatFlags[i] == false) {
			// invalid char passed
			throw new Exception("SUVAT Char not found!");
		} else {
			// otherwise return the variable:
			return suvatVal[i];
		}
	}

	public boolean isSet(char varName) throws Exception {
		int i = findInArray(suvatChar, varName); // retrieve array index
		// make sure the char exists:
		if (i > suvatChar.length) {
			// invalid char passed
			throw new Exception("SUVAT Char not found!");
		} else {
			// otherwise return the status of that variable:
			return suvatFlags[i];
		}
	}

	public boolean resolve() {
		String unsetStr = "", setStr = ""; // for variable names
		double setVal[] = new double[5]; // for variable values (max 5)

		// adds set variables values to setVal,
		// unset variable names to unsetStr
		// and set variables names to setStr
		for (int i = 0, s = 0; i < suvatFlags.length; i++) {
			if (suvatFlags[i]) {
				setStr += suvatChar[i];
				setVal[s] = suvatVal[i];
				// setVal is only incremented here since it is only used under
				// if(see above) conditions...
				s++;
				// N/B: if s was incremented in the for loops statement, it used
				// to cause a bug with the next
				// for loop, since it left all variables in the array as null,
				// from the first variables
				// to the next (i.g. s to a, was null; 's' was in it's proper
				// place and ;a' wasn't...)
			} else
				unsetStr += suvatChar[i];
		}

		// run through the formulas according to the known(setStr) and
		// unknown(unsetStr) variables
		for (int i = 0; i < unsetStr.length(); i++) {
			switch (unsetStr.charAt(i)) {
			// find which variables needs to be resolved and resolve it
			// according to known variables
			case 's':
				// find index array of variable:
				int s = findInArray(suvatChar, 's');
				if (setStr.equals("uvt"))
					suvatVal[s] = Equations.S.uvt(setVal);
				else if (setStr.equals("uat"))
					suvatVal[s] = Equations.S.uat(setVal);
				else if (setStr.equals("vat"))
					suvatVal[s] = Equations.S.vat(setVal);
				else if (setStr.equals("uva")) {
					suvatVal[s] = Equations.S.uva(setVal);
				}
				suvatFlags[s] = true; // variable has to be set as set...
				break;
			case 'u':
				// find index array of variable:
				int u = findInArray(suvatChar, 'u');
				if (setStr.equals("svt"))
					suvatVal[u] = Equations.U.svt(setVal);
				else if (setStr.equals("sat"))
					suvatVal[u] = Equations.U.sat(setVal);
				else if (setStr.equals("sva"))
					suvatVal[u] = Equations.U.sva(setVal);
				else if (setStr.equals("vat"))
					suvatVal[u] = Equations.U.vat(setVal);
				suvatFlags[u] = true; // variable has to be set as set...
				break;
			case 'v':
				// find index array of variable:
				int v = findInArray(suvatChar, 'v');
				if (setStr.equals("sut"))
					suvatVal[v] = Equations.V.sut(setVal);
				else if (setStr.equals("sat"))
					suvatVal[v] = Equations.V.sat(setVal);
				else if (setStr.equals("sua"))
					suvatVal[v] = Equations.V.sua(setVal);
				else if (setStr.equals("uat"))
					suvatVal[v] = Equations.V.uat(setVal);
				suvatFlags[v] = true; // variable has to be set as set...
				break;
			case 'a':
				// find index array of variable:
				int a = findInArray(suvatChar, 'a');
				if (setStr.equals("sut"))
					suvatVal[a] = Equations.A.sut(setVal);
				else if (setStr.equals("svt"))
					suvatVal[a] = Equations.A.svt(setVal);
				else if (setStr.equals("suv"))
					suvatVal[a] = Equations.A.suv(setVal);
				else if (setStr.equals("uvt"))
					suvatVal[a] = Equations.A.uvt(setVal);
				suvatFlags[a] = true; // variable has to be set as set...
				break;
			case 't':
				// find index array of variable:
				int t = findInArray(suvatChar, 't');
				if (setStr.equals("suv"))
					suvatVal[t] = Equations.T.suv(setVal);
				else if (setStr.equals("sua"))
					suvatVal[t] = Equations.T.sua(setVal);
				else if (setStr.equals("sva"))
					suvatVal[t] = Equations.T.sva(setVal);
				else if (setStr.equals("uva"))
					suvatVal[t] = Equations.T.uva(setVal);
				suvatFlags[t] = true; // variable has to be set as set...
				break;
			default:
				return false; // something went wrong
			}

		}

		return true;
	}

	private static int findInArray(char array[], char f) {
		// this simply finds a char in the array and returns it's index
		for (int i = 0; i < array.length; i++) {
			if (array[i] == f)
				return i;
		}
		return (array.length + 1); // this should never be reached!
	}

	private static class Equations {
		static class S {
			static double uvt(double uvt[]) {
				return (0.5 * (uvt[0] + uvt[1]) * uvt[2]);
			}

			static double uat(double uat[]) {
				return (uat[0] * uat[2] - 0.5 * uat[1] * uat[2] * uat[2]);
			}

			static double vat(double vat[]) {
				return (vat[0] * vat[2] - 0.5 * vat[1] * vat[2] * vat[2]);
			}

			static double uva(double uva[]) {
				return ((uva[1] * uva[1] - uva[0] * uva[0]) / (2 * uva[2]));
			}
		}

		static class U {
			static double vat(double vat[]) {
				return (vat[0] - vat[1] * vat[2]);
			}

			static double svt(double svt[]) {
				return ((2 * svt[0]) / svt[2] - svt[1]);
			}

			static double sat(double sat[]) {
				return ((sat[0] - 0.5 * sat[1] * sat[2] * sat[2]) / sat[2]);
			}

			static double sva(double sva[]) {
				return (Math.sqrt(sva[1] * sva[1] - 2 * sva[2] * sva[0]));
			}
		}

		static class V {
			static double uat(double uat[]) {
				return (uat[0] + uat[1] * uat[2]);
			}

			static double sut(double sut[]) {
				return ((2 * sut[0]) / sut[2] - sut[1]);
			}

			static double sat(double sat[]) {
				return ((sat[0] + 0.5 * sat[1] * sat[2] * sat[2]) / sat[2]);
			}

			static double sua(double sua[]) {
				return (Math.sqrt(sua[1] * sua[1] + 2 * sua[2] * sua[0]));
			}
		}

		static class A {
			static double uvt(double uvt[]) {
				return ((uvt[1] - uvt[0]) / uvt[2]);
			}

			static double sut(double sut[]) {
				return ((2 * (sut[0] - sut[1] * sut[2])) / (sut[2] * sut[2]));
			}

			static double svt(double svt[]) {
				return ((-2 * (svt[1] * svt[2] + svt[0])) / (svt[2] * svt[2]));
			}

			static double suv(double suv[]) {
				return ((suv[2] * suv[2] - suv[1] * suv[1]) / (2 * suv[0]));
			}
		}

		static class T {
			static double uva(double uva[]) {
				return ((uva[1] - uva[0]) / uva[2]);
			}

			static double suv(double suv[]) {
				return ((2 * suv[0]) / (suv[1] + suv[2]));
			}

			static double sua(double sua[]) {
				// the following will return NaN if sum in sqrt() is negative!
				return ((-sua[1] + Math.sqrt(sua[1] * sua[1] + 2 * sua[2]
						* sua[0])) / sua[2]);
			}

			static double sva(double sva[]) {
				// the following will return NaN if sum in sqrt() is negative!
				return ((-sva[1] + Math.sqrt(sva[1] * sva[1] - 2 * sva[2]
						* sva[0])) / (-sva[2]));
			}

		}
	}

}