import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.*;

/*
 * File:	RandomScheduling.java
 * Course: 	21HT - Operating Systems - 1DV512
 * Author: 	Qingqing Dai qd222ab
 * Date: 	2021-12-05
 */

// TODO: put this source code file into a new Java package with meaningful name (e.g., dv512.YourStudentID)!

// You can implement additional fields and methods in code below, but
// you are not allowed to rename or remove any of it!

// Additionally, please remember that you are not allowed to use any third-party libraries

public class RandomScheduling {

	public static class ScheduledProcess {
		int processId;
		int burstTime;
		int arrivalMoment;
		int remainingBurst;

		// The total time the process has waited since its arrival
		int totalWaitingTime;

		// The total CPU time the process has used so far
		// (when equal to burstTime -> the process is complete!)
		int allocatedCpuTime;

		public ScheduledProcess(int processId, int burstTime, int arrivalMoment) {
			this.processId = processId;
			this.burstTime = burstTime;
			this.arrivalMoment = arrivalMoment;
			this.totalWaitingTime = 0;
			this.allocatedCpuTime = 0;
			this.remainingBurst = burstTime;
		}

		public void setArrivalMoment(int moment) {
			this.arrivalMoment = moment;
		}

		// ... add further fields and methods, if necessary
	}

	// Random number generator that must be used for the simulation
	Random rng;
	// a list of processes which are initialized but haven't arrived yet
	List<ScheduledProcess> processes;
	// complete execution time of the simulation
	int executionTimeOfSimulation;
	// time moment - tick
	int tick;

	// ... add further fields and methods, if necessary

	public RandomScheduling(long rngSeed) {
		this.rng = new Random(rngSeed);
		this.processes = new ArrayList<>();
		this.tick = 0;
	}

	public void reset() {
		this.processes.clear();
		this.executionTimeOfSimulation = 0;
		this.tick = 0;
	}

	public void runNewSimulation(final boolean isPreemptive, final int timeQuantum,
			final int numProcesses,
			final int minBurstTime, final int maxBurstTime,
			final int maxArrivalsPerTick, final double probArrival) {
		reset();
		// initialize the processes
		initializeProcesses(numProcesses, minBurstTime, maxBurstTime);

		// a copy of the initial processes
		List<ScheduledProcess> copy = new ArrayList<>(this.processes);
		// a list of completed processes
		List<ScheduledProcess> completedProcesses = new ArrayList<>();
		// a list of processes in the waiting queue
		List<ScheduledProcess> queue = new ArrayList<>();
		// a list that contains at most one process which is currently running
		List<ScheduledProcess> running = new ArrayList<>();

		// run the loop until all processes are completed
		do {
			// add a random process to the queue
			if (!copy.isEmpty()) {
				Collections.shuffle(copy);
				copy.get(0).setArrivalMoment(tick);
				queue.add(copy.get(0));
				copy.remove(0);
			}

			// add no more than 'maxArrivalsPerTick' processes to the queue
			for (int i = 0; i < maxArrivalsPerTick; i++) {
				if (this.rng.nextDouble() <= probArrival && !copy.isEmpty()) {
					Collections.shuffle(copy);
					copy.get(0).setArrivalMoment(tick);
					queue.add(copy.get(0));
					copy.remove(0);
				}
			}

			// preemptive processes
			if (isPreemptive) {
				// take a random process from the queue to run
				Collections.shuffle(queue);
				running.add(queue.get(0));
				queue.remove(0);
				// if the running process has remaining burst less than 1 time quantum
				if (running.get(0).remainingBurst <= timeQuantum) {
					tick += running.get(0).remainingBurst;
					for(ScheduledProcess p: queue) {
						p.totalWaitingTime += running.get(0).remainingBurst;
					}
					completedProcesses.add(running.get(0));
					running.remove(0);
					tick++;
					for(ScheduledProcess p: queue) {
						p.totalWaitingTime++;
					}
					// if the running process has remaining burst more than 1 time quantum
				} else {
					tick += timeQuantum;
					queue.add(running.get(0));
					running.remove(0);
					queue.get(queue.size() - 1).remainingBurst -= timeQuantum;
					tick++;
					for(ScheduledProcess p: queue) {
						p.totalWaitingTime ++;
					}
				}
				// non-preemptive processes
			} else {
				// take a random process from the queue to run
				Collections.shuffle(queue);
				ScheduledProcess p = queue.get(0);
				p.totalWaitingTime = tick - p.arrivalMoment;
				tick += p.burstTime;
				// after the process is completed, move it from the queue to the completed list
				completedProcesses.add(p);
				queue.remove(p);
			}
			tick++; // increment tick
		} while (completedProcesses.size() < numProcesses);
		// assign the current ticks to the execution time of this simulation
		this.executionTimeOfSimulation = tick;
	}

	// initialize the processes with id, burst time and a default arrival moment (-1)
	public void initializeProcesses(final int numProcesses, final int minBurstTime, final int maxBurstTime) {
		int burstTime;
		for (int id = 1; id < numProcesses + 1; id++) {
			burstTime = this.rng.nextInt(maxBurstTime - minBurstTime + 1) + minBurstTime;
			ScheduledProcess process = new ScheduledProcess(id, burstTime, -1);
			this.processes.add(process);
		}

	}

	public void printResults() {
		int waitingTimeForAll = 0;
		System.out.println("Process id   Burst time   Arrival time   Total waiting time");
		for (ScheduledProcess process : processes) {
			System.out.printf("%-13d%-13d%-15d%-4d%n", process.processId, process.burstTime, process.arrivalMoment, process.totalWaitingTime);
			waitingTimeForAll += process.totalWaitingTime;
		}

		System.out.println("\nComplete execution time of the simulation: " + this.executionTimeOfSimulation);
		System.out.println("Average process waiting time: " + (waitingTimeForAll / processes.size()));

	}

	public static void main(String args[]) {
		final long rngSeed = 19940226;

		// Do not modify the code below — instead, complete the implementation
		// of other methods!
		RandomScheduling scheduler = new RandomScheduling(rngSeed);

		final int numSimulations = 5;

		final int numProcesses = 10;
		final int minBurstTime = 2;
		final int maxBurstTime = 10;
		final int maxArrivalsPerTick = 2;
		final double probArrival = 0.75;

		final int timeQuantum = 2;

		boolean[] preemptionOptions = { false, true };

		for (boolean isPreemptive : preemptionOptions) {

			for (int i = 0; i < numSimulations; i++) {
				System.out.println("Running " + ((isPreemptive) ? "preemptive" : "non-preemptive")
						+ " simulation #" + i);

				scheduler.runNewSimulation(
						isPreemptive, timeQuantum,
						numProcesses,
						minBurstTime, maxBurstTime,
						maxArrivalsPerTick, probArrival);

				System.out.println("Simulation results:"
						+ "\n" + "----------------------");
				scheduler.printResults();

				System.out.println("\n");
			}
		}

	}

}