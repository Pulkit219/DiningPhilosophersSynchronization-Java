package task1;

/**
 * Class Monitor To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor {
	/*
	 * ------------ Data members ------------
	 */

	private int Chopstick[];
	private int philophersArray[];
	private int Totalphilsophers;
	private boolean talking = false;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers) {
		// TODO: set appropriate number of chopsticks based on the # of
		// philosophers
		this.Totalphilsophers = piNumberOfPhilosophers;
		this.philophersArray = new int[piNumberOfPhilosophers];
		this.Chopstick = new int[piNumberOfPhilosophers];

		for (int i = 0; i < piNumberOfPhilosophers; i++) {
			// 0 state means thinking.
			// 1 state is hungry.
			// 2 state is eating.
			// 3 state is talking.
			this.philophersArray[i] = 0;
			// -1 is not used. Other values means it belongs to that
			// philosopher.
			this.Chopstick[i] = -1;
		}
	}

	/*
	 * ------------------------------- User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID) {
		{
			int nthphilopher = piTID - 1;
			this.philophersArray[nthphilopher] = 1; // status means hungry

			// Checking If I have both the
			this.test(nthphilopher);
			while (this.philophersArray[nthphilopher] == 1) {
				// Still hungry so wait.
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID) 
		{
			int nthphilopher = piTID - 1;
			//From eating, go straight to thinking.
			this.philophersArray[nthphilopher] = 0;
			
			//Put down chopsticks.
			this.Chopstick[nthphilopher] = -1;
			this.Chopstick[(nthphilopher + 1) % this.Totalphilsophers] = -1;
			
			for(int i = 1; i < this.Totalphilsophers; i++) {
				this.test((nthphilopher + i) % this.Totalphilsophers);
			}
			
			//Wake up everyone.
			notifyAll();
		}

	/**
	 * Only one philopher at a time is allowed to philosophy (while she is not
	 * eating).
	 */
	public synchronized void requestTalk() {
		{
			if(this.talking == false) {
				this.talking = true;
			}
			else {
				try {
					while(this.talking == true) {
						wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * When one philosopher is done talking stuff, others can feel free to start
	 * talking.
	 */
	public synchronized void endTalk() {
		this.talking = false;
		notifyAll();
	}

	private void test(int nthphilopher) {
		// Try picking up left chopstick
		if (this.philophersArray[nthphilopher] == 1) {
			// Philosopher not hungry. No need to test
			if (this.Chopstick[nthphilopher] == -1) {
				// No one is using this so I pick it up for myself.
				this.Chopstick[nthphilopher] = nthphilopher;
			}

			// Try picking up the right chopstick
			if (this.Chopstick[(nthphilopher + 1) % this.Totalphilsophers] == -1) {
				this.Chopstick[(nthphilopher + 1) % this.Totalphilsophers] = nthphilopher;
			}

			if (this.Chopstick[nthphilopher] == nthphilopher
					&& this.Chopstick[(nthphilopher + 1) % this.Totalphilsophers] == nthphilopher) {
				// I have both the chopsticks. Start eating now.
				this.philophersArray[nthphilopher] = 2;
			}
		}
	}
}

// EOF
