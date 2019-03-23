package task1;

import common.BaseThread;

/**
 * Class Philosopher. Outlines main subrutines of our virtual philosopher.
 * 
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread {
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating. - Print the fact that a given phil (their TID) has
	 * started eating. - Then sleep() for a random interval. - The print that
	 * they are done eating.
	 */
	public void eat() {
		try {
			System.out.println("Philosopher " + getTID() + "  STARTED EATING");
			// yield();
			sleep((long) (Math.random() * TIME_TO_WASTE));
			// yield();
			System.out.println("Philosopher " + getTID() + " DONE EATING");
		} catch (InterruptedException e) {
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking. - Print the fact that a given phil (their TID) has
	 * started thinking. - Then sleep() for a random interval. - The print that
	 * they are done thinking.
	 */
	public void think() {
		try {
			// ...

			System.out.println("The philosopher " + getTID() + " STARTS THINKING NOW.");
			// yield();
			sleep((long) (Math.random() * TIME_TO_WASTE));
			// yield();
			System.out.println("The philosopher " + getTID() + " STOPS THINKING NOW.");

			// ...
		} catch (InterruptedException e) {
			System.err.println("Philosopher.THINK():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of talking. - Print the fact that a given phil (their TID) has
	 * started talking. - Say something brilliant at random - The print that
	 * they are done talking.
	 */
	public void talk() {
		// ...
		try {
			System.out.println("Philosopher " + getTID() + " STARTS TALKING NOW");
			// yield();
			saySomething();
			sleep((long) (Math.random() * TIME_TO_WASTE));
			// yield();
			System.out.println("Philosopher " + iTID + " STOPS TALKING NOW");
		} catch (InterruptedException e) {
			System.err.println("Philosopher.talking():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
		saySomething();

		// ...
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run() {
		for (int i = 0; i < DiningPhilosophers.DINING_STEPS; i++) {
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();

			/*
			 * TODO: A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			//50% Probability that a philosopher will speak or not
			if (Math.random() >= 0.5) {
				// Some monitor ops down here...
				DiningPhilosophers.soMonitor.requestTalk();
				talk();
				DiningPhilosophers.soMonitor.endTalk();
				// ...
			}

		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random. Feel free to add
	 * your own phrases.
	 */
	public void saySomething() {
		String[] astrPhrases = { "Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
				"You know, true is false and false is true if you think of it",
				"2 + 2 = 5 for extremely large values of 2...", "If thee cannot speak, thee must be silent",
				"My number is " + getTID() + "" };

		System.out.println(
				"Philosopher " + getTID() + " says: " + astrPhrases[(int) (Math.random() * astrPhrases.length)]);
	}
}

// EOF
