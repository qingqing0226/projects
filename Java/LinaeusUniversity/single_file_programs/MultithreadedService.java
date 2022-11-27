import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/*
 * File:	MultithreadedService.java
 * Course: 	21HT - Operating Systems - 1DV512
 * Author: 	Qingqing Dai  qd222ab
 * Date: 	December 2021
 */

// TODO: put this source code file into a new Java package with meaningful name (e.g., dv512.YourStudentID)!

// You can implement additional fields and methods in code below, but
// you are not allowed to rename or remove any of it!

// Additionally, please remember that you are not allowed to use any third-party libraries

public class MultithreadedService {

    // TODO: implement a nested public class titled Task here
    // which must have an integer ID and specified burst time (duration) in milliseconds,
    // see below
    // Add further fields and methods to it, if necessary
    // As the task is being executed for the specified burst time, 
    // it is expected to simply go to sleep every X milliseconds (specified below)

    public class Task implements Callable {
        int id;
        long burstTime;
        long sleepTime;
        long timeLeft;
        long startTime = -1;
        long finishTime = -1;

        public Task(int id, long burstTime, long sleepTime) {
            this.id = id;
            this.burstTime = burstTime;
            this.sleepTime = sleepTime;
            this.timeLeft = burstTime;
        }

        @Override
        public Object call() throws Exception {
            if (this.burstTime == this.timeLeft) {
                this.startTime = System.currentTimeMillis();
            }
            if(this.timeLeft > this.sleepTime) {
                Thread.sleep(this.sleepTime);
                this.timeLeft -= this.sleepTime;
            } else if (this.timeLeft > 0){
                this.finishTime = System.currentTimeMillis() + this.timeLeft;
                Thread.sleep(this.timeLeft);
                this.timeLeft = 0;
            }
            return new long[]{this.id, this.burstTime, this.timeLeft, this.startTime, this.finishTime};

        }
    }

    // Random number generator that must be used for the simulation
	Random rng;
    
    List<long[]> results;
    LocalDateTime simulationStart;
    LocalDateTime simulationEnd;

    // ... add further fields, methods, and even classes, if necessary

	public MultithreadedService (long rngSeed) {
        this.rng = new Random(rngSeed);
    }


	public void reset() {
		this.simulationStart = null;
        this.simulationEnd = null;
        this.results = new ArrayList<>();
    }
    

    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public void runNewSimulation(final long totalSimulationTimeMs,
        final int numThreads, final int numTasks,
        final long minBurstTimeMs, final long maxBurstTimeMs, final long sleepTimeMs){
        reset();

        // TODO:
        // 1. Run the simulation for the specified time, totalSimulationTimeMs
        // 2. While the simulation is running, use a fixed thread pool with numThreads
        // (see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executors.html#newFixedThreadPool(int) )
        // to execute Tasks (implement the respective class, see above!)
        // 3. The total maximum number of tasks is numTasks, 
        // and each task has a burst time (duration) selected randomly
        // between minBurstTimeMs and maxBurstTimeMs (inclusive)
        // 4. The implementation should assign sequential task IDs to the created tasks (0, 1, 2...)
        // and it should assign them to threads in the same sequence (rather any other scheduling approach)
        // 5. When the simulation time is up, it should make sure to stop all of the currently executing
        // and waiting threads!

        // simulations start time:
        this.simulationStart = LocalDateTime.now();
        // simulation end time:
        this.simulationEnd = (new Timestamp(Timestamp.valueOf(simulationStart).getTime() + totalSimulationTimeMs)).toLocalDateTime();
        
        // create tasks:
        List<Callable<long[]>> tasks = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            long burst = rng.nextLong(maxBurstTimeMs - minBurstTimeMs + 1) + minBurstTimeMs;
            Callable<long[]> task = new Task(i, burst, sleepTimeMs);
            tasks.add(task);
        }

        // execute tasks:
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        try {
            List<Future<long[]>> futures = executor.invokeAll(tasks);

            // check if time is up:
            if (LocalDateTime.now().isEqual(simulationEnd) || LocalDateTime.now().isAfter(simulationEnd)) {
                // stop all tasks
                for (Future<long[]> future : futures) {
                    future.cancel(true);
                }
                executor.shutdownNow();
                for(Future<long[]> future : futures) {
                    results.add(future.get());
                }
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 


    }

    /*long nextLong(long n) {
        long bits, val;
        do {
           bits = (this.rng.nextLong() << 1) >>> 1;
           val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
     }*/


    public void printResults() {
        // results: 0-id, 1-burstTime, 2-timeLeft, 3-startTime, 4-finishTime
        System.out.println("Completed tasks:");
        // 1. For each *completed* task, print its ID, burst time (duration),
        // its start time (moment since the start of the simulation), and finish time
        for(long[] result: this.results) {
            if (result[2] == 0) {  // timeLeft = 0 => completed
                System.out.println("ID: " + result[0] + " burstTime: " + result[1] + " startTime: " + result[3] + " finishTime: " + result[4]);
            }
        }
        
        System.out.println("Interrupted tasks:");
        // 2. Afterwards, print the list of tasks IDs for the tasks which were currently
        // executing when the simulation was finished/interrupted
        for (long[] result: this.results) {
            if (result[2] > 0 && result[2] != result[1]) { // timeLeft > 0, timeLeft != burstTime => incompleted
                System.out.println(result[0]);
            }
        }

        System.out.println("Waiting tasks:");
        // 3. Finally, print the list of tasks IDs for the tasks which were waiting for execution,
        // but were never started as the simulation was finished/interrupted
        for (long[] result: this.results) {
            if(result[1] == result[2]) {
                System.out.println(result[0]);
            }
        }
	}




    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public static void main(String args[]) {
		// TODO: replace the seed value below with your birth date, e.g., "20001001"
		final long rngSeed = 19940226;  
				
        // Do not modify the code below — instead, complete the implementation
        // of other methods!
        MultithreadedService service = new MultithreadedService(rngSeed);
        
        final int numSimulations = 3;
        final long totalSimulationTimeMs = 15*1000L; // 15 seconds
        
        final int numThreads = 4;
        final int numTasks = 30;
        final long minBurstTimeMs = 1*1000L; // 1 second  
        final long maxBurstTimeMs = 10*1000L; // 10 seconds
        final long sleepTimeMs = 100L; // 100 ms

        for (int i = 0; i < numSimulations; i++) {
            System.out.println("Running simulation #" + i);

            service.runNewSimulation(totalSimulationTimeMs,
                numThreads, numTasks,
                minBurstTimeMs, maxBurstTimeMs, sleepTimeMs);

            System.out.println("Simulation results:"
					+ "\n" + "----------------------");	
            service.printResults();

            System.out.println("\n");
        }

        System.out.println("----------------------");
        System.out.println("Exiting...");
        
        // If your program has not completed after the message printed above,
        // it means that some threads are not properly stopped! -> this issue will affect the grade
    }
}
