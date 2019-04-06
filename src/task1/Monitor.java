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

	private int chopstick[];
	private state philophersArray[];
	private int totalphilsophers;
	private boolean talking = false;
	enum state {
		  THINKING,
		  HUNGRY,
		  EATING,
		} 
	
  
	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers) {
		// TODO: set appropriate number of chopsticks based on the # of
		// philosophers
		this.totalphilsophers = piNumberOfPhilosophers;
		this.philophersArray = new state[piNumberOfPhilosophers];
		this.chopstick = new int[piNumberOfPhilosophers];

		for (int i = 0; i < piNumberOfPhilosophers; i++) {
			
			this.philophersArray[i] = state.THINKING;
			// -1 is not used. Other values means it belongs to that
			// philosopher.
			this.chopstick[i] = -1;
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
			this.philophersArray[nthphilopher] = state.HUNGRY; // 1 status means hungry

			// Checking If I have both the chopsticks
			this.test(nthphilopher);
			while (this.philophersArray[nthphilopher] == state.HUNGRY) {
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
	 * When a given philosopher's done eating, they put the chopsticks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID) 
		{
			int nthphilopher = piTID - 1;
			//From eating, go straight to thinking.
			this.philophersArray[nthphilopher] = state.THINKING; 
			
			//Put down chopsticks.
			this.chopstick[nthphilopher] = -1;
			this.chopstick[(nthphilopher + 1) % this.totalphilsophers] = -1;
			
			for(int i = 1; i < this.totalphilsophers; i++) {
				//looping through all philosophers except the current one
				this.test((nthphilopher + i) % this.totalphilsophers);
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
		
		// Because if the Philosopher not hungry. No need to test ,1 means hungry
		// if Philosopher is in hungry state
		if (this.philophersArray[nthphilopher] == state.HUNGRY) {
			
			
			if (this.chopstick[nthphilopher] == -1) {
				
				this.chopstick[nthphilopher] = nthphilopher; //flagging true
			}

			
			if (this.chopstick[(nthphilopher + 1) % this.totalphilsophers] == -1) {
				this.chopstick[(nthphilopher + 1) % this.totalphilsophers] = nthphilopher;
			}

			if (this.chopstick[nthphilopher] == nthphilopher
					&& this.chopstick[(nthphilopher + 1) % this.totalphilsophers] == nthphilopher) {
				// I have both the chopsticks. Start eating now, setting status to 2 as eating
				this.philophersArray[nthphilopher] = state.EATING;
			}
		}
	}
}

// EOF
