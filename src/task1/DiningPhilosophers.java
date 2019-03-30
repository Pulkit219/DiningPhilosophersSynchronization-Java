package task1;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class DiningPhilosophers The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class DiningPhilosophers {
	/*
	 * ------------ Data members ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread while they are socializing
	 * there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * ------- Methods -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv) {
		int number=0;
		int iPhilosophers;
		try {
			/*
			 * TODO: Should be settable from the command line or the default if
			 * no arguments supplied.
			 */

		
			Scanner input = new Scanner(System.in);

			System.out.print("Enter  number of philiosphers BETWEEN 2 AND 11: ");
			try {
				number = input.nextInt();
				if (number < 0) {
					System.err.println(number + " is not a positive integer. Please try again ");
					System.exit(1);
				}

				while (number == 0 || number < 2) { // Checks if input is
													// Integer
					System.out.println("Please provide with valid integer equal 2 or more ");
					number = input.nextInt();

				}

			}

			catch (Exception e) {
				System.err.println("Invalid input, Exiting program. Please try again ");
				System.exit(1);
			}

			if(number>=2){
				iPhilosophers=number;
			}
			else {
				iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
			}
			 

			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for (int j = 0; j < iPhilosophers; j++) {
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}

			System.out.println(iPhilosophers + " philosopher(s) came in for a dinner.");

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for (int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		} catch (InterruptedException e) {
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * 
	 * @param poException
	 *            Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException) {
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
