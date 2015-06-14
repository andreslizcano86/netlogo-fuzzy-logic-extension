package primitives.rules;

import java.util.List;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

import primitives.implication.Cut;
import sets.general.EmptySet;
import sets.general.FuzzySet;

/**
 * This class implements the min-truncate-rule primitive.
 * 
 * @author Marcos Almendres.
 *
 */
public class MinCutRule extends DefaultReporter {

	/**
	 * This method tells Netlogo the appropriate syntax of the primitive.
	 * Receives a list and a wildcard, returns a wildcard.
	 */
	public Syntax getSyntax() {
		return Syntax.reporterSyntax(
				new int[] { Syntax.ListType(), Syntax.WildcardType() },
				Syntax.WildcardType());
	}

	/**
	 * This method respond to the call from Netlogo and returns the truncated
	 * fuzzy set after applying the rules given.
	 * 
	 * @param arg0
	 *            Arguments from Netlogo call, in this case a list.
	 * @param arg1
	 *            Context of Netlogo when the call was done.
	 * @return A fuzzy set.
	 */
	@Override
	public Object report(Argument[] arg0, Context arg1)
			throws ExtensionException, LogoException {
		FuzzySet conseq = (FuzzySet) arg0[1].get();
		// Checks the format and evaluates all the rules inside the list.
		List<Double> evaluations = SupportRules.variadicRulesChecks(arg0[0]
				.getList());
		double min = Double.POSITIVE_INFINITY;
		for (double d : evaluations) {
			// If one of the evaluation result is not a number, return an empty
			// set.
			if (d == Double.NaN) {
				return new EmptySet();
			}
			// Look for the minimum evaluated value.
			if (d < min) {
				min = d;
			}
		}
		// Truncate the fuzzy set with the minimum value found.
		Cut c = new Cut();
		return c.cutting(conseq, min);
	}

}
