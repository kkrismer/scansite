package edu.mit.scansite.server.scoring;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Tobieh
 */
public abstract class ParallelScorer<T extends ScoringJob> implements Observer {
	protected static final int THREAD_COUNT = 6;
	protected static final long THREAD_SLEEP_TIME_MS = 150;

	private ArrayList<ScoringJob> scoringJobs = new ArrayList<ScoringJob>();
	private boolean scoringIsDone = false;

	public ParallelScorer() {
	}

	public final void doScoring() throws Exception {
		doPrepareJobs();
		ArrayList<ScoringJob> scoringJobsCopy = new ArrayList<ScoringJob>(scoringJobs);
		ExecutorService es = Executors.newCachedThreadPool();
		for (ScoringJob job : scoringJobsCopy) {
			es.execute(job);
		}
		es.shutdown();
		es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		doAfterScoring();
	}

	protected final void addScoringJob(ScoringJob job) {
		job.addObserver(this);
		scoringJobs.add(job);
	}

	private final synchronized boolean isScoringIsDone() {
		return scoringIsDone;
	}

	/**
	 * Is called when the scoring is started. This method is intended to prepare
	 * jobs and add them to the list of jobs.
	 */
	protected abstract void doPrepareJobs();

	/**
	 * This method is called each time a job is finished.
	 * 
	 * @param job
	 *            A finished job.
	 */
	protected abstract void doSaveJobResults(T job);

	/**
	 * This method is called when all jobs are finished.
	 */
	protected abstract void doAfterScoring();

	@Override
	public final synchronized void update(Observable o, Object arg) {
		try {
			@SuppressWarnings("unchecked")
			T job = (T) o;
			if (scoringJobs.contains(job)) {
				doSaveJobResults(job);
				scoringJobs.remove(job);
				if (scoringJobs.isEmpty()) {
					scoringIsDone = true;
				}
			}
		} catch (Exception e) {
		}
	}

}
